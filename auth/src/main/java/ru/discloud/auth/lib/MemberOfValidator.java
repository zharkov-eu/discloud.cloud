package ru.discloud.auth.lib;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;

public class MemberOfValidator implements ConstraintValidator<MemberOf, String> {

    private Map<String, Boolean> lookupMap;

    @Override
    public void initialize(MemberOf constraintAnnotation) {
        this.lookupMap = new HashMap<>();
        for (String string : constraintAnnotation.value().split(",")) {
            this.lookupMap.put(string, true);
        }
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        return lookupMap.get(object) != null;
    }
}
