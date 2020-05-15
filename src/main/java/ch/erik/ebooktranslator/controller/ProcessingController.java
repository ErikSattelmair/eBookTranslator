package ch.erik.ebooktranslator.controller;

import ch.erik.ebooktranslator.model.TranslationProcessingModel;
import ch.erik.ebooktranslator.model.TranslationProcessingRequestModel;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@Slf4j
public class ProcessingController {

    @GetMapping("/processing-tool")
    public String showTranslationProcessingRequestPage(final Model model) {
        model.addAttribute("translationProcessingRequestModel", new TranslationProcessingRequestModel());
        return "processing-tool";
    }

    @PostMapping("/processing-tool")
    public String showTranslationProcessingPage(@Valid final TranslationProcessingRequestModel translationProcessingRequestModel,
                                                final BindingResult bindingResult,
                                                final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("filePathsIncorrect", true);
        } else {
            try (final FileInputStream originalInputStream = new FileInputStream(new File(translationProcessingRequestModel.getOriginalFilePath()));
                 final FileInputStream translationInputStream = new FileInputStream(new File(translationProcessingRequestModel.getTranslationFilePath()))) {

                final EpubReader epubReader = new EpubReader();
                final Book original = epubReader.readEpub(originalInputStream);
                final Book translation = epubReader.readEpub(translationInputStream);

                // TODO: Check if E-Books are the same

                model.addAttribute("translationProcessingModel", new TranslationProcessingModel(original, translation));
            } catch (IOException e) {
                log.error("Could not read at least one e-book file!");
                model.addAttribute("processingError", true);
            }
        }

        return "processing-tool";
    }

}
