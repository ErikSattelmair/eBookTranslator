package ch.erik.ebooktranslator.service;

import java.io.IOException;

public interface TranslationLibraryClient {

    String translate(final String sourceText) throws IOException;

}
