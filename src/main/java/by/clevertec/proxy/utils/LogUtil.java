package by.clevertec.proxy.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtil {

    public static <T> String getErrorMessageToLog(String methodName, T classObject) {
        return String.format("Exception in the %s inside the %s\n", methodName, classObject.getClass().getName());
    }
}
