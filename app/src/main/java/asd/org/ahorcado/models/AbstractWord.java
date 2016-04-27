/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

public abstract class AbstractWord {

    private String word;
    private String originalWord;
    private int size;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    protected abstract boolean containsLetter(char letter);

    protected abstract void setMark(int position);

    protected abstract void exchangeLetters();


    public abstract boolean isComplete();
}
