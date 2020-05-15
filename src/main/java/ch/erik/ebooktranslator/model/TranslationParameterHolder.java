package ch.erik.ebooktranslator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationParameterHolder {

    private byte[] ebook;

    private String coverImageFilePath;

    private boolean useProxy;

    private Language targetLanguage;

}
