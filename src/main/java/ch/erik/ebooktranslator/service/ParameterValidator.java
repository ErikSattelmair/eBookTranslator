package ch.erik.ebooktranslator.service;

public interface ParameterValidator {

    boolean isCoverImageFilePathValid(final String coverImageFilePath);

    boolean isEbookFilePathValid(final boolean useEbookFilePath, final String ebookFilePath);

}
