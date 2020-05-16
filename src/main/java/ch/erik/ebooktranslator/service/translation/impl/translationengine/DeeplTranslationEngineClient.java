package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.model.Language;
import ch.erik.ebooktranslator.service.translation.impl.RandomProxyService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service("deepl")
@Slf4j
public class DeeplTranslationEngineClient extends AbstractWebDriverTranslationEngine {

    //private static final String DEEPL_URL = "https://www.whatsmyip.org/";

    private static final String DEEPL_URL = "https://www.deepl.com/translator";

    public DeeplTranslationEngineClient() {
        super(new RandomProxyService());
    }

    @Override
    protected String getSourceWebElementClass() {
        return "//textarea[@class='lmt__textarea lmt__source_textarea lmt__textarea_base_style']";
    }

    @Override
    protected String getTargetWebElementClass() {
        return "//textarea[@class='lmt__textarea lmt__target_textarea lmt__textarea_base_style']";
    }

    @Override
    protected String getUrl() {
        return DEEPL_URL;
    }

    @Override
    protected int getMaxFragmentSize() {
        return 5_000;
    }

    @Override
    protected String getTranslatedText(final WebElement webElement) {
        return webElement.getAttribute("value");
    }

    @Override
    protected void selectTargetLanguage(final WebDriver browser, final Language targetLanguage) {
        if (!targetLanguage.getId().getLanguage().equalsIgnoreCase(Locale.GERMAN.getLanguage())) {
            final WebElement languageChooser = browser.findElement(By.className("lmt__language_select--target"));
            languageChooser.click();

            final String languageXpath = "//*[@id=\"dl_translator\"]/div[1]/div[4]/div[1]/div[1]/div[1]/div/button[@dl-test=\"translator-lang-option-" + targetLanguage.getId().toString().replace('_', '-') + "\"]";
            final WebElement language = browser.findElement(By.xpath(languageXpath));
            language.click();
        }
    }
}
