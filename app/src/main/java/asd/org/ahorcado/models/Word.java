/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

public class Word extends AbstractWord {

    private int category;

    public void setMyLetter(char letter) {
        char[] charWord = getOriginalWord().toCharArray();
        for (int i = 0; i < charWord.length; i++) {
            if (letter == charWord[i]) {
                setMark(i);
            }
        }
    }

    public boolean containsLetter(char letter) {
        boolean result = false;
        char[] charWord = getOriginalWord().toCharArray();
        for (int i = 0; i < charWord.length; i++) {
            if (letter == charWord[i]) {
                result = true;
            }
        }
        return result;
    }

    protected void setMark(int position) {
        StringBuilder word = new StringBuilder(getWord());
        word.setCharAt(position, '_');
        setWord(word.toString());
    }

    public void exchangeLetters() {
        char[] wordChars = getWord().toString().toCharArray();
        char[] originalWordChars = getOriginalWord().toCharArray();
        for (int i = 0; i < wordChars.length; i++) {
            if (wordChars[i] != originalWordChars[i]) {
                StringBuilder word = new StringBuilder(getWord());
                word.setCharAt(i, originalWordChars[i]);
                setWord(word.toString());
            } else {
                setMark(i);
            }
        }
    }

    public void inverseLetter(){
        if(!getWord().toString().equals(getOriginalWord().toString())){
            char[] wordChars = getWord().toString().toCharArray();
            char[] originalWordChars = getOriginalWord().toCharArray();
            for(int i = 0; i<wordChars.length;i++){
                if(wordChars[i] == '_'){
                    StringBuilder word = new StringBuilder(getWord());
                    word.setCharAt(i, originalWordChars[i]);
                    setWord(word.toString());
                } else {
                    setMark(i);
                }
            }
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < getWord().toCharArray().length; i++) {
            if (getWord().toCharArray()[i] == '_') {
                return false;
            }
        }
        return true;
    }

}
