package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.impl.EBookFileTranslator;
import ch.erik.ebooktranslator.service.impl.MyMemoryTranslationLibraryClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EpubFileReaderTest.Configuration.class)
public class EpubFileReaderTest {

    private static final String EPUB_FILE_PATH = "epub/resource1.epub";

    @Autowired
    private EBookTranslator eBookTranslator;

    @Test
    @DisplayName("Test")
    public void testEpubFileReader() throws TranslationException, IOException {
        this.eBookTranslator.translateEBook(Files.readAllBytes(new ClassPathResource(EPUB_FILE_PATH).getFile().toPath()));
    }

    public static class Configuration {

        @Bean
        public EBookTranslator eBookTranslator() {
            return new EBookFileTranslator();
        }

        @Bean
        @Qualifier("mymemory")
        private TranslationLibraryClient translationLibraryClient() {
            return new MyMemoryTranslationLibraryClient();
        }

    }
}
