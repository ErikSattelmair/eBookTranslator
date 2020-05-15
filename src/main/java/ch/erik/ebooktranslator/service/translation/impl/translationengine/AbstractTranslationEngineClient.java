package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTranslationEngineClient implements TranslationLibraryClient {

    protected static final String HTML_TAGS_CONTAINING_TEXT = "p, span, a, b, i, strong, em, br, h1, h2, h3, h4, h5, h6, " +
            "blockquote, q, code, pre, li, dt, dd, mark, ins, del, sup, sub, small";

}
