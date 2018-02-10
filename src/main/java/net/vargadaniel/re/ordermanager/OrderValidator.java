package net.vargadaniel.re.ordermanager;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
	
	private ValidatorFactory validatorFactory;
	
	@PostConstruct
	protected void initValidatorFactory() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
	}
	
	public <T> void validate(T object) {
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> validationResuts = validator.validate(object);
		if (!validationResuts.isEmpty()) {
			throw new ConstraintViolationException(validationResuts);
		}		
	}	

}
