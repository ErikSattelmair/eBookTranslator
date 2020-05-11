package ch.erik.ebooktranslator.validation;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isFileValid(final String filePath, final List<String> validFileExtensions) {
        final File file = new File(filePath);

        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        final String fileExtension = StringUtils.defaultString(FilenameUtils.getExtension(filePath));
        return validFileExtensions.contains(fileExtension);
    }

}
