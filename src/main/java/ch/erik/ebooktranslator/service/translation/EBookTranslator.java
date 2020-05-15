package ch.erik.ebooktranslator.service.translation;

import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.model.TranslationParameterHolder;

public interface EBookTranslator {

    byte[] translateEBook(final TranslationParameterHolder translationParameterSet) throws TranslationException;

}
