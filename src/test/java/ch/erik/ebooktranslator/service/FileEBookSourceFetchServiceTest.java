package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.service.impl.FileEBookSourceFetchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FileEBookSourceFetchServiceTest.Configuration.class)
public class FileEBookSourceFetchServiceTest {

    private static final String DOWNLOAD_FOLDER_PATH = "/Users/erik/Downloads";

    @Autowired
    private EBookSourceFetchService eBookSourceFetchService;

    @Test
    @DisplayName("")
    public void test() throws Exception {
        final byte[] ebookContent = this.eBookSourceFetchService.getEbookSource(DOWNLOAD_FOLDER_PATH);
        Assertions.assertNotNull(ebookContent);
        Assertions.assertTrue(ebookContent.length > 0);
    }

    public static class Configuration {

        @Bean
        public EBookSourceFetchService eBookSourceFetchService() {
            return new FileEBookSourceFetchService();
        }

    }
}
