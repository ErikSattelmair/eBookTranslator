package ch.erik.ebooktranslator.service.translation;

import ch.erik.ebooktranslator.model.TranslationRequestModel;

public interface WorkflowEngine {

    boolean startWorkflow(final TranslationRequestModel translationRequestModel);

}
