/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.helpers;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringHelper {

    public static String clearWord(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\P{ASCII}");
        return pattern.matcher(normalized).replaceAll("");
    }

}