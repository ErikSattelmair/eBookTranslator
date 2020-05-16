package ch.erik.ebooktranslator.service.translation;

import ch.erik.ebooktranslator.model.TranslationParameterHolder;
import nl.siegmann.epublib.domain.Resource;

import java.io.IOException;
import java.util.List;

public interface TranslationLibraryClient {

    List<Resource> translate(final List<Resource> textResources, final TranslationParameterHolder translationParameterHolder) throws IOException;

}
