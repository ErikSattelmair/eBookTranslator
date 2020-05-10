package ch.erik.ebooktranslator.service.impl;

import ch.erik.ebooktranslator.service.ParameterValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class ParameterValidatorImpl implements ParameterValidator {

    @Override
    public boolean isCoverImageFilePathValid(final String coverImageFilePath) {
        final File file = new File(coverImageFilePath);

        if (!file.exists()) {
            log.error("coverImagePath must not be null!");
            return false;
        }

        if (file.isDirectory()) {
            log.error("coverImagePath must not be a directory!");
            return false;
        }

        final String fileExtension = FilenameUtils.getExtension(coverImageFilePath);
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "bmp":
                return true;
            default: {
                log.error("Wrong file type. Allowed file types are: jpg, jpeg, png, bmp");
                return false;
            }
        }
    }

}
