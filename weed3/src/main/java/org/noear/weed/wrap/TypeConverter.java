package org.noear.weed.wrap;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TypeConverter {
    public Object convert(Object val, Class<?> target){
        if (val instanceof Number) {
            Number number = (Number) val;

            if (Long.class == target || Long.TYPE == target) {
                return number.longValue();
            }

            if (Integer.class == target || Integer.TYPE == target) {
                return number.intValue();
            }

            if (Short.class == target || Short.TYPE == target) {
                return number.shortValue();
            }

            if (Double.class == target || Double.TYPE == target) {
                return number.doubleValue();
            }

            if (Float.class == target || Float.TYPE == target) {
                return number.floatValue();
            }

            if (Boolean.class == target || Boolean.TYPE == target) {
                return number.intValue() > 0;
            }

            if(Date.class == target){
                return new Date(number.longValue());
            }
        }

        if(target == java.util.Date.class) {
            if (val instanceof String) {
                return Timestamp.valueOf((String) val);
            }
        }

        if (target == LocalDateTime.class) {
            if (val instanceof java.sql.Timestamp) {
                return ((Timestamp) val).toLocalDateTime();
            }

            if (val instanceof String) {
                return LocalDateTime.parse((String) val);
            }
        }

        if (target == LocalDate.class) {
            if (val instanceof java.sql.Date) {
                return ((Date) val).toLocalDate();
            }

            if (val instanceof java.sql.Timestamp) {
                return ((Timestamp) val).toLocalDateTime().toLocalDate();
            }

            if (val instanceof String) {
                return LocalDate.parse((String) val);
            }
        }

        if (target == LocalTime.class) {
            if (val instanceof java.sql.Time) {
                return ((Time) val).toLocalTime();
            }

            if (val instanceof String) {
                return LocalTime.parse((String) val);
            }
        }

        if (target == Boolean.TYPE) {
            if (val instanceof Boolean) {
                return val;
            }

            if (val instanceof Number) {
                return ((Number) val).byteValue() > 0;
            }
        }

        return val;
    }
}
