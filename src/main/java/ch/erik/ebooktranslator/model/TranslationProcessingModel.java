package ch.erik.ebooktranslator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.siegmann.epublib.domain.Book;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TranslationProcessingModel {

    @NotNull
    final Book original;

    @NotNull
    final Book translation;

}
