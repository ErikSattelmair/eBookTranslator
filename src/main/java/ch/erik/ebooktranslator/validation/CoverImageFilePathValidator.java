package ch.erik.ebooktranslator.validation;

import com.google.common.collect.Lists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CoverImageFilePathValidator implements ConstraintValidator<CoverImageFilePath, String> {

    @Override
    public boolean isValid(final String payload, final ConstraintValidatorContext constraintValidatorContext) {
        return ValidationUtils.isFileValid(payload, Lists.newArrayList("jpg", "jpewg", "png", "bmp"));
    }
}
