package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("google")
@Slf4j
public class GoogleTranslationEngineClient extends AbstractWebDriverTranslationEngine {

    private static final String GOOGLE_TRANSLATOR_URL = "https://translate.google.com/";

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
}
