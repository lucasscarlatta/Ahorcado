/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import asd.org.ahorcado.interfaces.HangmanWord;

public class Word implements HangmanWord {

    private String word;
    private int category;

    public Word(String word) { this.word = word; }

    public void setWord(String word) { this.word = word.toUpperCase(); }

    public String getWord() { return this.word; }


    public void markLetter(char letter) {
        char[] charWord = this.word.toCharArray();
        for (int i = 0; i < charWord.length; i++) {
            if (letter == charWord[i]) {
                setMark(i);
            }
        }
    }

    public boolean containsLetter(char letter) {
        boolean result = false;
        char[] charWord = this.word.toCharArray();
        for (int i = 0; i < charWord.length; i++) {
            if (letter == charWord[i]) {
                result = true;
            }
        }
        return result;
    }

    public void setMark(int position) {
        StringBuilder word = new StringBuilder(this.word);
        word.setCharAt(position, HangmanWord.MARK);
        this.word = (word.toString());
    }

    public boolean isComplete() {
        for (int i = 0; i < this.word.toCharArray().length; i++) {
            if (this.word.toCharArray()[i] != HangmanWord.MARK) {
                return false;
            }
        }
        return true;
    }
}
