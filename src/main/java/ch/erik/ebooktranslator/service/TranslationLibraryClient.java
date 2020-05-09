package ch.erik.ebooktranslator.service;

import nl.siegmann.epublib.domain.Resource;

import java.io.IOException;
import java.util.List;

public interface TranslationLibraryClient {

    boolean translate(final List<Resource> textResources) throws IOException;

}
