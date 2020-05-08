package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;
import nl.siegmann.epublib.domain.Book;

public interface EBookTranslator {

    byte[] translateEBook(final byte[] source) throws TranslationException;

}
