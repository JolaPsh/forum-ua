package forum.test.ua.util.validators;

import forum.test.ua.util.exceptions.BadRequestException;

/**
 * Created by Joanna Pakosh, 05.2020
 */

public class FieldValidator {

    public static void notEmpty(String value, String field) {
        if (value == null) {
            throw new BadRequestException("Required field " + field + " cannot be empty");
        }
    }

    public static void atLeastOneParameter(String[] params)  {
        int count = 0;
        for (String param: params) {
            if (param != null) {
                count++;
            }
        }
        if (count == 0) {
            throw new BadRequestException("At least one parameter is required");
        }
    }
}
