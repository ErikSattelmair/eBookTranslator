package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.ProxyHelper;
import ch.erik.ebooktranslator.service.TranslationLibraryClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DeeplTranslationLibraryClient extends AbstractTranslationLibraryClient implements TranslationLibraryClient {

    //private static final String DEEPL_URL = "https://www.whatsmyip.org/";

    private static final String DEEPL_URL = "https://www.deepl.com/translator";
    
    @Override
    public boolean translate(final List<Resource> textResources) throws IOException {
        final WebDriver browser = createBrowser();
        browser.get(DEEPL_URL);

        final WebElement languageChooser = browser.findElement(By.className("lmt__language_select--target"));
        languageChooser.click();

        final String languageXpath = "//*[@id=\"dl_translator\"]/div[1]/div[4]/div[1]/div[1]/div[1]/div/button[@dl-test=\"translator-lang-option-" + SUPPORTED_LANGUAGES.PORTUGUESE_BRAZILIAN.getLanguageCode() + "\"]";
        final WebElement language = browser.findElement(By.xpath(languageXpath));
        language.click();

        for (final Resource textResource : textResources) {
            final byte[] resourceContent = textResource.getData();
            final Document document = Jsoup.parse(new String(resourceContent));

            document.title(translateText(browser, document.title()));

            final Elements elements = document.select(HTML_TAGS_CONTAINING_TEXT);
            final List<TextNode> textNodes = elements.textNodes();

            textNodes.stream().parallel()
                    .filter(textNode -> StringUtils.isNotBlank(textNode.getWholeText()))
                    .forEach(textNode -> translateText(browser, textNode.text()));
        }

        browser.close();

        return true;
    }

    private WebDriver createBrowser() throws IOException {
        System.setProperty("webdriver.chrome.driver", new ClassPathResource("driver/chromedriver").getFile().getPath());

        // Set proxy
        final Proxy proxy = new Proxy();
        final String proxyAddress = ProxyHelper.getRandomProxy();
        proxy.setHttpProxy(proxyAddress);
        proxy.setSslProxy(proxyAddress);
        proxy.setFtpProxy(proxyAddress);

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("proxy", proxy);

        return new ChromeDriver(chromeOptions);
    }

    private String translateText(final WebDriver browser, final String text) {
        final WebElement sourceTextArea = browser.findElement(By.className("lmt__source_textarea"));
        sourceTextArea.sendKeys(text);

        final WebDriverWait wait = new WebDriverWait(browser, 5);
        wait.until(valueLoadedCondition());

        final String translation = browser.findElement(By.className("lmt__target_textarea")).getAttribute("value");
        sourceTextArea.clear();

        return translation;

    }

    private ExpectedCondition<Boolean> valueLoadedCondition() {
        return driver -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        };
    }

    @AllArgsConstructor
    private enum SUPPORTED_LANGUAGES {
        GERMAN("de-DE"),
        ENGLISH("en-EN"),
        FRENCH("fr-FR"),
        SPANISH("es-ES"),
        PORTUGUESE("pt-PT"),
        PORTUGUESE_BRAZILIAN("pt-BR"),
        ITALIAN("it-IT"),
        DUTCH("nl-NL"),
        POLISH("pl-PL"),
        RUSSIAN("ru-RU"),
        JAPANESE("ja-JA"),
        CHINESE_SIMPLE("zh-ZH");

        @Getter
        private final String languageCode;
    }
}
