package edu.sru.cpsc.webshopping.secure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
//@Constraint(validatedBy = UniqueLoginValidator.class)
@Target({ ElementType.ANNOTATION_TYPE,ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
	public @interface UniqueLogin {
		String message() default "{}";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	    

}

