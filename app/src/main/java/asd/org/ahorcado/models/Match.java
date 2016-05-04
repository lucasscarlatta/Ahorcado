/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Match extends AbstractMatch {

    private User user;
    private Word word;

    public Map<String,String> showOneLetter() {
            Map mapa=new HashMap<>();
            char letter=this.obtainAValidLetter();
            this.execute(letter);
            mapa.put("word",word);
            mapa.put("letter",letter);
            return mapa;
    }

    private char obtainAValidLetter() {
        Random r = new Random();
        String partialWord=this.obtainPartialWord();
        String completedWord=word.getWord();
        char[] wordChars = partialWord.toCharArray();
        char[] originalWordChars = this.word.getWord().toCharArray();
        int positionLetter;
        if (!partialWord.equals(completedWord)) {
            do {
                positionLetter = r.nextInt((wordChars.length - 1) - 0);
            } while (wordChars[positionLetter] != '_');
        } else {
            positionLetter = r.nextInt((wordChars.length - 1) - 0);
        }
        return originalWordChars[positionLetter];
    }

}