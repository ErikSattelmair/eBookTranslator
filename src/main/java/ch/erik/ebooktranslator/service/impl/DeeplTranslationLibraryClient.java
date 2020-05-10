package ch.erik.ebooktranslator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("deepl")
@Slf4j
public class DeeplTranslationLibraryClient extends AbstractWebDriverTranslationLibrary {

    //private static final String DEEPL_URL = "https://www.whatsmyip.org/";

    private static final String DEEPL_URL = "https://www.deepl.com/translator";

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
}
