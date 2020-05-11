package ch.erik.ebooktranslator.service.translation;

import nl.siegmann.epublib.domain.Resource;

import java.io.IOException;
import java.util.List;

public interface TranslationLibraryClient {

    boolean translate(final List<Resource> textResources, final boolean useProxy) throws IOException;

}
