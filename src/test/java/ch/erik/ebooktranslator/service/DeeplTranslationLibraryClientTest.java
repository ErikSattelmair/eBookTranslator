package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.model.Language;
import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.DeeplTranslationEngineClient;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DeeplTranslationLibraryClientTest.Configuration.class)
public class DeeplTranslationLibraryClientTest {

    @Autowired
    private TranslationLibraryClient translationLibraryClient;

    @Test
    @DisplayName("")
    public void test() throws IOException {
        final EpubReader epubReader = new EpubReader();
        final Book book = epubReader.readEpub(new FileInputStream(new ClassPathResource("").getFile()));
        final List<Resource> textResources = book.getContents().stream().filter(resource -> resource.getMediaType().getName().equals("application/xhtml+xml")).collect(Collectors.toList());
        final TranslationParameterHolder translationParameterHolder = TranslationParameterHolder.builder()
                .useProxy(false)
                .targetLanguage(new Language(Locale.ENGLISH, "English"))
                .build();

        Assertions.assertFalse(this.translationLibraryClient.translate(textResources, translationParameterHolder).isEmpty());
    }

    public static class Configuration {

        @Bean
        public TranslationLibraryClient translationLibraryClient() {
            return new DeeplTranslationEngineClient();
        }

    }
}
