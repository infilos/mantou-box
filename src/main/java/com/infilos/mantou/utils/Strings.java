package com.infilos.mantou.utils;

import com.google.common.net.UrlEscapers;

public final class Strings {
    private Strings() {
    }

    public static String trim(String input) {
        return input.replaceAll("\"", "");
    }

    public static String encodeUrl(String url) {
        return UrlEscapers.urlFragmentEscaper().escape(url);
    }
}

