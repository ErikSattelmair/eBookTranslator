package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.model.Language;
import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import ch.erik.ebooktranslator.service.translation.EBookSaveService;
import ch.erik.ebooktranslator.service.translation.EBookTranslator;
import ch.erik.ebooktranslator.service.translation.TranslationLibraryClient;
import ch.erik.ebooktranslator.service.translation.impl.EBookFileTranslator;
import ch.erik.ebooktranslator.service.translation.impl.EBookToFileSaveService;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.DeeplTranslationEngineClient;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.GoogleTranslationEngineClient;
import ch.erik.ebooktranslator.service.translation.impl.translationengine.MyMemoryTranslationEngineClient;
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
import java.util.Locale;

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
        final TranslationParameterHolder translationParameterHolder = TranslationParameterHolder.builder()
                .ebook(Files.readAllBytes(new ClassPathResource(EPUB_FILE_PATH).getFile().toPath()))
                .useProxy(false)
                .coverImageFilePath("")
                .targetLanguage(new Language(Locale.ENGLISH, "English"))
                .build();
        final byte[] res = this.eBookTranslator.translateEBook(translationParameterHolder);
        this.eBookSaveService.saveBook(res);
    }

    public static class Configuration {

        @Bean
        public EBookTranslator eBookTranslator() {
            return new EBookFileTranslator(new DeeplTranslationEngineClient());
        }

        @Bean
        @Qualifier("mymemory")
        public TranslationLibraryClient translationLibraryClientMyMemory() {
            return new MyMemoryTranslationEngineClient();
        }

        @Bean
        @Qualifier("deepl")
        public TranslationLibraryClient translationLibraryClientDeepL() {
            return new DeeplTranslationEngineClient();
        }

        @Bean
        @Qualifier("google")
        public TranslationLibraryClient translationLibraryClientGoogle() {
            return new GoogleTranslationEngineClient();
        }

        @Bean
        public EBookSaveService eBookSaveService() {
            return new EBookToFileSaveService();
        }

    }
}
