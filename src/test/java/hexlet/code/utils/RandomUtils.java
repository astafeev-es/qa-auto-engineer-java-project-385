package hexlet.code.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtils {
    public static String randomString() {
        return RandomStringUtils.secure().nextAlphanumeric(4, 8);
    }
}
