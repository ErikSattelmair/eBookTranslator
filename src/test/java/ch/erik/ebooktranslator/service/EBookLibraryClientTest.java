package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.service.translation.EBookLibraryClient;
import ch.erik.ebooktranslator.service.translation.impl.EBookLibraryClientImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EBookLibraryClientTest.Configuration.class)
public class EBookLibraryClientTest {

    @Autowired
    private ch.erik.ebooktranslator.service.translation.EBookLibraryClient EBookLibraryClient;

    @Test
    @DisplayName("")
    public void test() throws IOException {
        this.EBookLibraryClient.downloadEBook();
    }

    public static class Configuration {

        @Bean
        public EBookLibraryClient eBookLibraryClient() {
            return new EBookLibraryClientImpl();
        }

    }
}
