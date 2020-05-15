package ch.erik.ebooktranslator.controller;

import ch.erik.ebooktranslator.model.TranslationRequestModel;
import ch.erik.ebooktranslator.service.translation.SupportedLanguages;
import ch.erik.ebooktranslator.service.translation.WorkflowEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class TranslationController {

    @Autowired
    private WorkflowEngine workflowEngine;

    @GetMapping("/")
    public String showMainPage(final Model model) {
        model.addAttribute("translationRequestModel", new TranslationRequestModel());
        model.addAttribute("targetLanguages", SupportedLanguages.getSupportedLanguages());
        return "index";
    }

    @PostMapping("/")
    public String showMainPage(@Valid final TranslationRequestModel translationRequestModel,
                               final BindingResult bindingResult,
                               final Model model) {
        if (bindingResult.hasErrors()) {
            final List<ObjectError> errors = bindingResult.getAllErrors();

            for (final ObjectError error : errors) {
                final String[] errorCodes = error.getCodes();

                if (errorCodes != null) {
                    final String relevantErrorCode = errorCodes[errorCodes.length - 1];

                    if (relevantErrorCode.equalsIgnoreCase("coverImageFilePath")) {
                        model.addAttribute("coverImageFilePathInvalid", true);
                    }

                    if (relevantErrorCode.equalsIgnoreCase("ebookFilePath")) {
                        model.addAttribute("ebookFilePathInvalid", true);
                    }
                }
            }
        } else {
            final boolean translationSuccessful = this.workflowEngine.startWorkflow(translationRequestModel);
            model.addAttribute("translationSuccessful", translationSuccessful);
        }

        model.addAttribute("targetLanguages", SupportedLanguages.getSupportedLanguages());
        return "index";
    }

}
