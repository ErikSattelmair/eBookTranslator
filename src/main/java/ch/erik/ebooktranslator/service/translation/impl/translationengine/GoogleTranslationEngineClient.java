package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.model.Language;
import ch.erik.ebooktranslator.service.translation.impl.RandomProxyService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service("google")
@Slf4j
public class GoogleTranslationEngineClient extends AbstractWebDriverTranslationEngine {

    private static final String GOOGLE_TRANSLATOR_URL = "https://translate.google.com/";

    public GoogleTranslationEngineClient() {
        super(new RandomProxyService());
    }

    @Override
    protected String getSourceWebElementClass() {
        return "//textarea[@id='source']";
    }

    @Override
    protected String getTargetWebElementClass() {
        return "//span[@class='tlid-translation translation']";
    }

    @Override
    protected String getUrl() {
        return GOOGLE_TRANSLATOR_URL;
    }

    @Override
    protected int getMaxFragmentSize() {
        return 5_000;
    }

    @Override
    protected String getTranslatedText(final WebElement webElement) {
        return webElement.getText();
    }

    @Override
    protected void selectTargetLanguage(final WebDriver browser, final Language targetLanguage) {
        if (!targetLanguage.getId().getLanguage().equals(Locale.GERMAN.getLanguage())) {
            final WebElement languageChooser = browser.findElement(By.className("tlid-open-target-language-list"));
            languageChooser.click();

            final WebElement language = browser.findElements(By.className("language_list_item_wrapper-" + targetLanguage.getId().getLanguage())).get(2);
            language.click();
        }
    }
}
