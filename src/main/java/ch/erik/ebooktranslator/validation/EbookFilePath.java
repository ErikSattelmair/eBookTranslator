package ch.erik.ebooktranslator.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EbookFilePathValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EbookFilePath {
    String message() default "Invalid file path";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
