/**
 * @author: maxu1
 * @date: 2019/1/27 11:55
 */

package com.xupt.seckill.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @author maxu
 */
@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    public ValidationResult validator(Object bean) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> validate = validator.validate(bean);
        validate.forEach(validates -> {
            String message = validates.getMessage();
            String property = validates.getPropertyPath().toString();
            result.getErrorsMsgMap().put(property, message);
        });

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}

