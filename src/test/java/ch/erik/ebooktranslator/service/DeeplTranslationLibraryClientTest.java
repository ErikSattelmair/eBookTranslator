package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.service.impl.DeeplTranslationLibraryClient;
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

        Assertions.assertTrue(this.translationLibraryClient.translate(textResources));
    }

    public static class Configuration {

        @Bean
        public TranslationLibraryClient translationLibraryClient() {
            return new DeeplTranslationLibraryClient();
        }

    }
}
