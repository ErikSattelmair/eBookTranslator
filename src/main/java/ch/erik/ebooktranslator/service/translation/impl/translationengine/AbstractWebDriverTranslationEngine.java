package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.model.Language;
import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import ch.erik.ebooktranslator.service.translation.ProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractWebDriverTranslationEngine extends AbstractTranslationEngineClient {

    private static final String DELIMITER = "\n\n";

    private final ProxyService proxyService;

    @Override
    public List<Resource> translate(final List<Resource> resources, final TranslationParameterHolder translationParameterHolder) throws IOException {
        final WebDriver browser = createBrowser(translationParameterHolder.isUseProxy());
        browser.get(this.getUrl());

        selectTargetLanguage(browser, translationParameterHolder.getTargetLanguage());

        final WebElement source = browser.findElement(By.xpath(getSourceWebElementClass()));
        source.click();

        for (final Resource resource : resources) {
            if (resource.getMediaType().getName().equals(MediaType.APPLICATION_XHTML_XML_VALUE)) {
                final byte[] resourceContent = resource.getData();
                final Document document = Jsoup.parse(new String(resourceContent));
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);

                document.title(translateText(browser, document.title()));

                final Elements elements = document.select(HTML_TAGS_CONTAINING_TEXT);
                final List<TextNode> textNodes = elements.textNodes();

                final List<String> textsToTranslate = new ArrayList<>();
                final StringBuilder stringBuilder = new StringBuilder();

                for (final TextNode textNode : textNodes) {
                    final String textNodeText = textNode.text();
                    if (stringBuilder.length() + textNodeText.length() + DELIMITER.length() > getMaxFragmentSize()) {
                        textsToTranslate.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                    stringBuilder.append(textNodeText).append(DELIMITER);
                }

                textsToTranslate.add(stringBuilder.toString());

                final int numberOfLettersToProcessInTotal = textsToTranslate.stream().map(String::length).mapToInt(Integer::intValue).sum();
                log.info("Numbers of letters to process in total: {}", numberOfLettersToProcessInTotal);
                log.info("Numbers of textNodes to process: {}", textNodes.size());

                final int numberOfTextNodesLetters = textNodes.stream().map(textNode -> textNode.text().length()).mapToInt(Integer::intValue).sum();
                log.info("Numbers of textNodes letters to process: {}", numberOfTextNodesLetters);
                log.info("Numbers of texts to translate: {}", textsToTranslate.size());
                log.info(StringUtils.repeat("-", 20) + "\n\n");

                if (numberOfLettersToProcessInTotal > 0) {
                    final List<String> translatedTexts = textsToTranslate.stream()
                            .map(text -> translateText(browser, text))
                            .collect(Collectors.toList());

                    final String[] translatedJoinedTextsParts = String.join(DELIMITER, translatedTexts).split(DELIMITER);

                    for (int i = 0; i < translatedJoinedTextsParts.length; i++) {
                        final TextNode textNode = textNodes.get(i);
                        textNode.text(translatedJoinedTextsParts[i]);
                    }
                }

                resource.setData(document.html().getBytes());
            }
        }

        browser.close();

        return resources;
    }

    private WebDriver createBrowser(final boolean useProxy) throws IOException {
        System.setProperty("webdriver.chrome.driver", new ClassPathResource("driver/chromedriver").getFile().getPath());

        final ChromeOptions chromeOptions = new ChromeOptions();

        if (useProxy) {
            final Proxy proxy = new Proxy();
            final String httpProxyAddress = this.proxyService.getProxy(false);
            final String httpsProxyAddress = this.proxyService.getProxy(true);
            proxy.setHttpProxy(httpProxyAddress);
            proxy.setSslProxy(httpsProxyAddress);

            log.info("Using http proxy {}", httpProxyAddress);
            log.info("Using https proxy {}", httpsProxyAddress);
            chromeOptions.setCapability("proxy", proxy);
        }

        return new ChromeDriver(chromeOptions);
    }

    private String translateText(final WebDriver browser, final String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        final int maxRetries = 100;
        int retries = 1;

        while (retries <= maxRetries) {
            try {
                final WebElement sourceTextArea = browser.findElement(By.xpath(getSourceWebElementClass()));
                sourceTextArea.clear();

                final WebDriverWait wait = new WebDriverWait(browser, 3);
                wait.until(valueLoadedCondition());
                sourceTextArea.sendKeys(text);

                wait.until(valueLoadedCondition());

                return getTranslatedText(browser.findElement(By.xpath(getTargetWebElementClass())));
            } catch (Exception e) {
                log.error("Could not perform request. Reason {}", e.getMessage());
                log.error("Retries left: {}", maxRetries - retries);
                retries++;
            }
        }

        throw new RuntimeException("Could no read translation");
    }

    private ExpectedCondition<Boolean> valueLoadedCondition() {
        return driver -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.warn("Could not put thread to sleep");
            }

            return true;
        };
    }

    protected abstract String getUrl();

    protected abstract int getMaxFragmentSize();

    protected abstract String getSourceWebElementClass();

    protected abstract String getTargetWebElementClass();

    protected abstract String getTranslatedText(final WebElement webElement);

    protected abstract void selectTargetLanguage(final WebDriver browser, final Language language);
}
