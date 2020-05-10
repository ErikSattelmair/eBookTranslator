package ch.erik.ebooktranslator.service;

import ch.erik.ebooktranslator.model.TranslationRequestModel;

public interface WorkflowEngine {

    boolean startWorkflow(final TranslationRequestModel translationRequestModel);

}
