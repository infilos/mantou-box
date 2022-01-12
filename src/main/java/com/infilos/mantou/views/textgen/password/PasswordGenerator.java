package com.infilos.mantou.views.textgen.password;

import java.security.SecureRandom;
import java.util.*;

public class PasswordGenerator {
    static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER_CHARS = LOWER_CHARS.toUpperCase();
    static final String DIGIT_CHARS = "0123456789";
    static final String SYMBOL_CHARS = "_!@#$%^&*?";

    public static List<String> generate(Integer expectCount,
                                        Integer expectLength,
                                        boolean isUseLower,
                                        boolean isUseUpper,
                                        boolean isUseDigits,
                                        boolean isUseSymbols,
                                        String symbolCanditates) {
        StringBuilder builder = new StringBuilder();
        if (isUseLower) {
            builder.append(LOWER_CHARS);
        }
        if (isUseUpper) {
            builder.append(UPPER_CHARS);
        }
        if (isUseDigits) {
            builder.append(DIGIT_CHARS);
        }
        if (isUseSymbols) {
            builder.append(symbolCanditates);
        }

        String candidates = builder.toString();
        int candidateLength = candidates.length();

        if (candidates.isBlank()) {
            return List.of();   // no option selected
        }

        List<String> passwords = new ArrayList<>();

        for (int i = 0; i < expectCount; i++) {
            StringBuilder password = new StringBuilder(expectLength);
            Random random = new SecureRandom();

            for (int idx = 0; idx < expectLength; idx++) {
                password.append(candidates.charAt(random.nextInt(candidateLength)));
            }

            passwords.add(password.toString());
        }

        return passwords;
    }
}
