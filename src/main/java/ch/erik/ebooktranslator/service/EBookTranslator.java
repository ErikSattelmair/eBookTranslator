package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;

public interface EBookTranslator {

    byte[] translateEBook(final byte[] source, final boolean useProxy) throws TranslationException;

}
