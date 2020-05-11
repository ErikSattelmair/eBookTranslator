package ch.erik.ebooktranslator.service.translation;

import ch.erik.ebooktranslator.exception.TranslationException;

public interface EBookTranslator {

    byte[] translateEBook(final byte[] source, final String coverImageFilePath, final boolean useProxy) throws TranslationException;

}
