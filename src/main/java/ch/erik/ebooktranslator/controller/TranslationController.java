package ch.erik.ebooktranslator.controller;

import ch.erik.ebooktranslator.model.TranslationProcessingModel;
import ch.erik.ebooktranslator.model.TranslationRequestModel;
import ch.erik.ebooktranslator.service.translation.WorkflowEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class TranslationController {

    @Autowired
    private WorkflowEngine workflowEngine;

    @GetMapping("/")
    public String showMainPage(final Model model) {
        model.addAttribute("translationRequestModel", new TranslationRequestModel());
        return "index";
    }

    @PostMapping("/")
    public String showMainPage(@Valid final TranslationRequestModel translationRequestModel,
                               final BindingResult bindingResult,
                               final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("requestInvalid", true);
            return "index";
        }

        final boolean translationSuccessful = this.workflowEngine.startWorkflow(translationRequestModel);
        if (!translationSuccessful) {
            model.addAttribute("translationSuccessful", false);
            return "index";
        }

        model.addAttribute("translationSuccessful", true);

        return "index";
    }

    @GetMapping("/processing-tool")
    public String showTranslationProcessingPage(final Model model) {
        model.addAttribute("translationProcessingModel", new TranslationProcessingModel());
        return "processing-tool";
    }

}
