package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.impl.DeeplTranslationLibraryClient;
import ch.erik.ebooktranslator.service.impl.EBookFileTranslator;
import ch.erik.ebooktranslator.service.impl.GoogleTranslationLibraryClient;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EpubFileReaderTest.Configuration.class)
public class EpubFileReaderTest {

    private static final String DEST_FOLDER_PATH = "/Users/erik/Downloads";

    private static final String EPUB_FILE_PATH = "epub/resource.epub";

    @Autowired
    private EBookTranslator eBookTranslator;

    @Test
    @DisplayName("Test")
    public void testEpubFileReader() throws TranslationException, IOException {
        final byte[] res = this.eBookTranslator.translateEBook(Files.readAllBytes(new ClassPathResource(EPUB_FILE_PATH).getFile().toPath()));
        Files.write(new File(DEST_FOLDER_PATH + "translated_book_" + System.currentTimeMillis() + ".epub").toPath(), res);
    }

    public static class Configuration {

        @Bean
        public EBookTranslator eBookTranslator() {
            return new EBookFileTranslator();
        }

        @Bean
        @Qualifier("mymemory")
        private TranslationLibraryClient translationLibraryClientMyMemory() {
            return new MyMemoryTranslationLibraryClient();
        }

        @Bean
        @Qualifier("deepl")
        private TranslationLibraryClient translationLibraryClientDeepL() {
            return new DeeplTranslationLibraryClient();
        }

        @Bean
        @Qualifier("google")
        private TranslationLibraryClient translationLibraryClientGoogle() {
            return new GoogleTranslationLibraryClient();
        }

    }
}
