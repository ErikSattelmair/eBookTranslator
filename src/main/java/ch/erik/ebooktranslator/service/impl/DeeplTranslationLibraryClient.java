package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.TranslationLibraryClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class DeeplTranslationLibraryClient implements TranslationLibraryClient {

    private static final String DEEPL_URL = "https://www.deepl.com/translator";

    @Override
    public String translate(String sourceText) throws IOException {
        System.setProperty("webdriver.chrome.driver", new ClassPathResource("driver/chromedriver").getFile().getPath());

        final WebDriver browser = new ChromeDriver();
        browser.get(DEEPL_URL);

        final WebElement languageChooser = browser.findElement(By.className("lmt__language_select--target"));
        languageChooser.click();

        final WebDriverWait wait = new WebDriverWait(browser, 5);
        final String languageXpath = "//*[@id=\"dl_translator\"]/div[1]/div[4]/div[1]/div[1]/div[1]/div/button[@dl-test=\"translator-lang-option-" + SUPPORTED_LANGUAGES.PORTUGUESE_BRAZILIAN.getLanguageCode() + "\"]";
        final WebElement language = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(languageXpath)));
        language.click();

        final WebElement sourceTextArea = browser.findElement(By.className("lmt__source_textarea"));
        sourceTextArea.sendKeys(sourceText);

        wait.until((ExpectedCondition<Boolean>) driver -> driver.findElement(By.className("lmt__target_textarea")).getAttribute("value").length() != 0);

        final String translatedText = browser.findElement(By.className("lmt__target_textarea")).getAttribute("value");

        browser.close();

        return translatedText;
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
