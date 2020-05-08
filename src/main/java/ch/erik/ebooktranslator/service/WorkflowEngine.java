package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.exception.TranslationException;

public interface WorkflowEngine {

    void startWorkflow() throws Exception, TranslationException;

}
