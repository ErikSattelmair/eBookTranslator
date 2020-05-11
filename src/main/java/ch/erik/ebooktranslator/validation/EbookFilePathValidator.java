package ch.erik.ebooktranslator.validation;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EbookFilePathValidator implements ConstraintValidator<EbookFilePath, String> {

    @Override
    public boolean isValid(final String payload, final ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(payload) || ValidationUtils.isFileValid(payload, Lists.newArrayList("eubp"));
    }
}
