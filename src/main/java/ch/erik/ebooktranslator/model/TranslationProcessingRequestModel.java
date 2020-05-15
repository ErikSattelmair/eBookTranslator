package ch.erik.ebooktranslator.model;

import ch.erik.ebooktranslator.validation.EbookFilePath;
import lombok.Data;

@Data
public class TranslationProcessingRequestModel {

    @EbookFilePath
    private String originalFilePath;

    @EbookFilePath
    private String translationFilePath;

}
