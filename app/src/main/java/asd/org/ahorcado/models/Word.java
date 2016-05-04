/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;


import java.util.Random;

import asd.org.ahorcado.interfaces.HangmanWord;


public class Word implements HangmanWord {

    private Long id;
    private String word;
    private int category;
    private int size;

    public Word(String word) {
        this.word = word;
        this.size = word.length();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word.toUpperCase();
    }

    public String getWord() {
        return this.word;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void showLetter(int position, char letter) {
        StringBuilder sb = new StringBuilder(this.word);
        sb.setCharAt(position, letter);
        this.word = sb.toString();
    }

    public boolean containsLetter(char letter) {
        boolean result = false;
        char[] charWord = this.word.toCharArray();
        for (int i = 0; i < size; i++) {
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
        for (int i = 0; i < size; i++) {
            if (getWord().toCharArray()[i] == '_') {
                return false;
            }
        }
        return true;
    }

    public void setBlankWord() {
        StringBuilder sbWord = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sbWord.append(HangmanWord.MARK);
        }
        this.word = sbWord.toString();
    }

}