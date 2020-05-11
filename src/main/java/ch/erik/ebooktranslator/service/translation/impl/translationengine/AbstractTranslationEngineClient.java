package ch.erik.ebooktranslator.service.translation.impl.translationengine;

import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTranslationEngineClient implements TranslationLibraryClient {

    protected static final String HTML_TAGS_CONTAINING_TEXT = "p, span, a, b, i, strong, em, br, h1, h2, h3, h4, h5, h6, " +
            "blockquote, q, code, pre, li, dt, dd, mark, ins, del, sup, sub, small";

    @AllArgsConstructor
    public enum SUPPORTED_LANGUAGES {
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
