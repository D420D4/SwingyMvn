package org.plefevre.Controller;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SumPointsValidator.class)
@Target({ ElementType.TYPE }) // Applique cette contrainte sur une classe
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSum {
    String message() default "Sum of points must be 5 and all points must be positive";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
