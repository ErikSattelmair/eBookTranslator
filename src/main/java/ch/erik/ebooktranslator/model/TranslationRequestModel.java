package ch.erik.ebooktranslator.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class TranslationRequestModel {

    @NotEmpty
    @NotBlank
    private String coverImageFilePath;

    private boolean useProxy;

    private boolean useBokLibrary;

    private boolean useDeepl;

    private boolean useGoogle = true;

    private boolean useMyMemory;

    private boolean useEbookFileFromDisk;

    private String ebookFilePath;

}
