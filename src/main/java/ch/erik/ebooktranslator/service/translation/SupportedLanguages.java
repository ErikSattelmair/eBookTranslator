package ch.erik.ebooktranslator.service.translation;

import ch.erik.ebooktranslator.model.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum SupportedLanguages {

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

    public static List<Language> getSupportedLanguages() {
        return EnumSet.allOf(SupportedLanguages.class).stream()
                .map(language -> new Language(new Locale(language.languageCode), language.toString().toLowerCase().replace('_', ' ')))
                .collect(Collectors.toList());
    }

}
