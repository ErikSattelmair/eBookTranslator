package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import ch.erik.ebooktranslator.service.translation.ProxyService;
import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import ch.erik.ebooktranslator.service.translation.UserAgent;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

@Service("mymemory")
@Slf4j
public class MyMemoryTranslationEngineClient extends AbstractTranslationEngineClient implements TranslationLibraryClient {

    private static final String MY_MEMORY_URL = "https://api.mymemory.translated.net/get";

    // private static final String MY_MEMORY_URL = "https://www.whatsmyip.org";

    @Autowired
    private ProxyService proxyService;

    @Override
    public List<Resource> translate(final List<Resource> resources, final TranslationParameterHolder translationParameterHolder) throws IOException {
        for (final Resource resource : resources) {
            if (resource.getMediaType().getName().equals("application/xhtml+xml")) {
                final byte[] resourceContent = resource.getData();
                final Document document = Jsoup.parse(new String(resourceContent));
                final boolean useProxy = translationParameterHolder.isUseProxy();

                document.title(translateText(document.title(), useProxy));

                final Elements elements = document.select(HTML_TAGS_CONTAINING_TEXT);
                final List<TextNode> textNodes = elements.textNodes();

                textNodes.stream().parallel()
                        .filter(textNode -> StringUtils.isNotBlank(textNode.getWholeText()))
                        .forEach(textNode -> translateText(textNode.text(), useProxy));
            }
        }

        return resources;
    }

    private String translateText(final String text, final boolean usProxy) {
        final UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(MY_MEMORY_URL)
                .queryParam("q", text)
                .queryParam("langpair", "en|it")
                .build();

        log.debug("API endpoit: {}", uriComponents.toUriString());

        final int maxRetries = 20;
        int retries = 0;

        while (retries <= maxRetries) {
            try {
                final ResponseEntity<String> response = createProxiedRestTemplate(usProxy).exchange(uriComponents.toUriString(), HttpMethod.GET, createHttpEntity(), String.class);
                final String responseBody = response.getBody();
                if (StringUtils.isNotBlank(responseBody)) {
                    return JsonPath.read(responseBody, "$.responseData.translatedText");
                }
            } catch (Exception e) {
                log.error("Could not perform request. Reason {}", e.getMessage());
                log.debug("Retries left: {}", maxRetries - retries);
                retries++;
            }
        }

        throw new RuntimeException("Response is null");
    }

    private RestTemplate createProxiedRestTemplate(final boolean usProxy) {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        if (usProxy) {
            final String[] randomProxyParts = this.proxyService.getProxy(true).split(":");
            final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(randomProxyParts[0], Integer.parseInt(randomProxyParts[1])));
            requestFactory.setProxy(proxy);

            log.info("Using proxy {}", proxy.address());
        }

        return new RestTemplate(requestFactory);
    }

    private HttpEntity<String> createHttpEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, UserAgent.getRandomUserAgent().getValue());

        return new HttpEntity<>(headers);
    }

}
