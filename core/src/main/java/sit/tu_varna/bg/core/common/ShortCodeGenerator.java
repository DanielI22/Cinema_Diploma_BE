package sit.tu_varna.bg.core.common;

import org.apache.commons.lang3.RandomStringUtils;

public class ShortCodeGenerator {

    private static final int SHORT_CODE_LENGTH = 5;

    public static String generateShortCode() {
        return RandomStringUtils.randomNumeric(SHORT_CODE_LENGTH);
    }
}