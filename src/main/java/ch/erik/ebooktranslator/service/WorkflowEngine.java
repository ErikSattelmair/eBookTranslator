package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;

public interface WorkflowEngine {

    void startWorkflow(final String coverImageFilePath, final boolean useProxy) throws Exception, TranslationException;

}
