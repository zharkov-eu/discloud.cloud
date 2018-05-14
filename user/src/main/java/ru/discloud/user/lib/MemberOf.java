package ru.discloud.user.lib;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = MemberOfValidator.class)
@Documented
public @interface MemberOf {

    String message() default "{ru.discloud.auth.lib.MemberOf}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String value();

    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MemberOf[] value();
    }
}
