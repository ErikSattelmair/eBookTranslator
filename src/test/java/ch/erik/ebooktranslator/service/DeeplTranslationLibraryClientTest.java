package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.service.impl.DeeplTranslationLibraryClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DeeplTranslationLibraryClientTest.Configuration.class)
public class DeeplTranslationLibraryClientTest {

    @Autowired
    private TranslationLibraryClient translationLibraryClient;

    @Test
    @DisplayName("")
    public void test() throws IOException {
        final String text = """
                Multi-line string literals are processed at compile time. The compiler always proceeds according to the same schema: All line breaks in the character string are first converted into a line feed (\\u000A) independent of the operating system. Sequences explicitly entered with escape characters in the text such as \\n or \\r are excluded from this.
                              
                In the second step, the whitespace owed to the code formatting is removed. It is marked with the position of the concluding three quotation marks. This allows you to place text blocks in the code so that it matches the rest of the code formatting (Listing 3).
                              
                For Scala and Kotlin, multiline literals must either be written left-aligned without whitespace in the source text, or they must be freed from whitespace by string manipulation. Java, on the other hand, is strongly based on Swift’s procedure, a cleanup of the strings at runtime is not necessary.
                              
                Finally, all escape sequences in the text are interpreted and resolved. After compiling, it is no longer possible to find out how a string was defined in the code, whether it was defined as a multi-line string or not.
                """;

        final String result = this.translationLibraryClient.translate(text);
        Assertions.assertEquals("Home", result);
    }

    public static class Configuration {

        @Bean
        public TranslationLibraryClient translationLibraryClient() {
            return new DeeplTranslationLibraryClient();
        }

    }
}
