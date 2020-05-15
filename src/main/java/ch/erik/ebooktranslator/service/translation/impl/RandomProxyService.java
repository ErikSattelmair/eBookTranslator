package ch.erik.ebooktranslator.service.translation.impl;

import ch.erik.ebooktranslator.service.translation.ProxyService;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class RandomProxyService implements ProxyService {

    private static final String PROXY_API_URL = "http://pubproxy.com/api/proxy";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getProxy(final boolean https) {
        final ResponseEntity<String> response = this.restTemplate.getForEntity(createUri(https), String.class);
        final String proxy = extractProxyFromResponse(response);
        return proxy;
    }

    private String createUri(final boolean https) {
        return UriComponentsBuilder.fromHttpUrl(PROXY_API_URL)
                .queryParam("speed", 30)
                .queryParam("limit", 1)
                .queryParam("google", true)
                .queryParam("user_agent", true)
                .queryParam("https", https)
                .build().toUriString();
    }

    private String extractProxyFromResponse(final ResponseEntity<String> response) {
        final String responseBody = response.getBody();
        return JsonPath.read(responseBody, "$.data.[0].ipPort");
    }

}
