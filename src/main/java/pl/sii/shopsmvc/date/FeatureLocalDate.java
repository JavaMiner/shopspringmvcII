package pl.sii.shopsmvc.date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FeatureLocalDate.FeatureValidator.class)
@Documented
public @interface FeatureLocalDate {
    String message() default "{javax.validation.constraints.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class FeatureValidator implements ConstraintValidator<FeatureLocalDate, LocalDate> {
       public void initialize(FeatureLocalDate constraint) {
       }

       public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
          return localDate == null || localDate.isBefore(LocalDate.now());
       }
    }
}
