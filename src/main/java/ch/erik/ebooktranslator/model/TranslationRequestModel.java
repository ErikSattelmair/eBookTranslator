package ch.erik.ebooktranslator.model;

import ch.erik.ebooktranslator.validation.CoverImageFilePath;
import ch.erik.ebooktranslator.validation.EbookFilePath;
import lombok.Data;

@Data
public class TranslationRequestModel {

    @CoverImageFilePath
    private String coverImageFilePath;

    private boolean useProxy;

    private boolean useBokLibrary;

    private boolean useDeepl;

    private boolean useGoogle = true;

    private boolean useMyMemory;

    private boolean useEbookFileFromDisk;

    @EbookFilePath
    private String ebookFilePath;

}
