package ch.erik.ebooktranslator.model;

import lombok.Data;
import nl.siegmann.epublib.domain.Book;

import javax.validation.constraints.NotNull;

@Data
public class TranslationProcessingModel {

    @NotNull
    private Book original;

    @NotNull
    private Book translation;

}
