package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.impl.*;
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

    private static final String DEST_FOLDER_PATH = "/Users/erik/Downloads";

    private static final String EPUB_FILE_PATH = "epub/resource.epub";

    @Autowired
    private EBookTranslator eBookTranslator;

    @Autowired
    private EBookSaveService eBookSaveService;

    @Test
    @DisplayName("Test")
    public void testEpubFileReader() throws TranslationException, IOException {
        final byte[] res = this.eBookTranslator.translateEBook(Files.readAllBytes(new ClassPathResource(EPUB_FILE_PATH).getFile().toPath()), "", false);
        this.eBookSaveService.saveBook(res);
    }

    public static class Configuration {

        @Bean
        public EBookTranslator eBookTranslator() {
            return new EBookFileTranslator();
        }

        @Bean
        @Qualifier("mymemory")
        public TranslationLibraryClient translationLibraryClientMyMemory() {
            return new MyMemoryTranslationLibraryClient();
        }

        @Bean
        @Qualifier("deepl")
        public TranslationLibraryClient translationLibraryClientDeepL() {
            return new DeeplTranslationLibraryClient();
        }

        @Bean
        @Qualifier("google")
        public TranslationLibraryClient translationLibraryClientGoogle() {
            return new GoogleTranslationLibraryClient();
        }

        @Bean
        public EBookSaveService eBookSaveService() {
            return new EBookToFileSaveService();
        }

    }
}
