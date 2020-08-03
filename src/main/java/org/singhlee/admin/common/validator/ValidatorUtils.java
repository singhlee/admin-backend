package org.singhlee.admin.common.validator;

import org.singhlee.admin.common.exception.CustomizeException;
import org.singhlee.admin.common.enums.ReturnCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @program: admin-backend
 * @description: 校验工具类
 * @author: singhlee
 * @create: 2020-06-16 14:42
 **/
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @throws CustomizeException 校验不通过，则报CustomizeException异常
     */
    public static void validateEntity(Object object) throws CustomizeException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, Default.class);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new CustomizeException(msg.toString(),ReturnCode.PARAM_FAIL.getCode());
        }
    }
}
