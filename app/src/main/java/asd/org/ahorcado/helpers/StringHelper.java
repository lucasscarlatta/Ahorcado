/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.helpers;

public class StringHelper {

    private static String WITH_ACCENT = "ÁÉÍÓÚ";
    private static String WITHOUT_ACCENT = "AEIOU";

    public static String clearWord(String input) {
        for (int i = 0; i < WITH_ACCENT.length(); i++)
            input = input.toUpperCase().replace(WITH_ACCENT.charAt(i), WITHOUT_ACCENT.charAt(i));
        return input;
    }

}