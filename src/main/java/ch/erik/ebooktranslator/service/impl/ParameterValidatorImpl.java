package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.ParameterValidator;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ParameterValidatorImpl implements ParameterValidator {

    @Override
    public boolean isCoverImageFilePathValid(final String coverImageFilePath) {
        return isFileValid(coverImageFilePath, Lists.newArrayList("jpg", "jpeg", "png", "bmp"));
    }

    @Override
    public boolean isEbookFilePathValid(final boolean useEbookFilePath, final String ebookFilePath) {
        if (useEbookFilePath) {
            return isFileValid(ebookFilePath, Lists.newArrayList("ebup"));
        }

        return true;
    }

    private boolean isFileValid(final String filePath, final List<String> validFileExtensions) {
        final File file = new File(filePath);

        if (!file.exists()) {
            log.error("filePath must not be null!");
            return false;
        }

        if (file.isDirectory()) {
            log.error("filePath must not be a directory!");
            return false;
        }

        final String fileExtension = StringUtils.defaultString(FilenameUtils.getExtension(filePath));
        return validFileExtensions.contains(fileExtension);
    }

}
