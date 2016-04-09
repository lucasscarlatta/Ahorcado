/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

public class Word extends AbstractWord {

    private int category;

    public Word(int category) {
        super();
        this.category = category;
    }

    public boolean containsLetter(CharSequence letter) {
        boolean result = false;
        char[] charWord = getWord().toCharArray();
        for (int i = 0; i < charWord.length; i++) {
            if (letter.equals(charWord[i])) {
                setMark(i);
                result = true;
            }
        }
        return result;
    }

    protected void setMark(int position) {
        getWord().toCharArray()[position] = '_';
    }

    public void exchangeLetters() {
        char[] wordChars = getWord().toCharArray();
        char[] originalWordChars = getOriginalWord().toCharArray();
        for (int i = 0; i < wordChars.length; i++) {
            if (wordChars[i] != originalWordChars[i]) {
                wordChars[i] = originalWordChars[i];
            } else {
                setMark(i);
            }
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < getWord().toCharArray().length; i++) {
            if (getWord().toCharArray()[i] != '_') {
                return false;
            }
        }
        return true;
    }

}
