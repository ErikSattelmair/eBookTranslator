package ch.erik.ebooktranslator;

import ch.erik.ebooktranslator.configuration.ApplicationConfiguration;
import ch.erik.ebooktranslator.exception.TranslationException;
import ch.erik.ebooktranslator.service.WorkflowEngine;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Application {

    public static void main(String[] args) throws Exception, TranslationException {
        final AbstractApplicationContext abstractApplicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        final WorkflowEngine workflowEngine = abstractApplicationContext.getBean(WorkflowEngine.class);

        final String coverImageFilePath = args[0];
        final boolean useProxy = Boolean.TRUE.equals(Boolean.valueOf(args[1]));

        workflowEngine.startWorkflow(coverImageFilePath, useProxy);

        abstractApplicationContext.close();
    }

}
