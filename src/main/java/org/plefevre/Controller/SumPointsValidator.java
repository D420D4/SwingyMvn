package org.plefevre.Controller;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.plefevre.Model.Hero;

public class SumPointsValidator implements ConstraintValidator<ValidSum, Hero> {

    @Override
    public boolean isValid(Hero obj, ConstraintValidatorContext context) {
        if (obj == null) return true;

        int a = obj.getDefensePoint();
        int b = obj.getAttackPoint();
        int c = obj.getHit_point();

        return a >= 0 && b >= 0 && c >= 0 && (a + b + c == 5);
    }
}


