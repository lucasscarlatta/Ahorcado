/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import java.util.Map;

import asd.org.ahorcado.exceptions.MatchLostException;

public abstract class AbstractMatch {

    private AbstractGuess guessWord = new GuessWord();
    private int life;
    private User user;
    private boolean result;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String execute(char letter) {
        return this.guessWord.processWord(letter);
    }

    public boolean afterExecute(char letter) throws MatchLostException {
        if (!this.guessWord.guessLetter(letter)) {
            --life;
            if (life == 0) {
                throw new MatchLostException();
            }
        }
        return this.guessWord.isComplete();
    }

    public Map<String,String> showOneLetter() {
        return this.guessWord.showOneLetter();
    }

    public String getNewWord() {
        return guessWord.getNewWord();
    }

    public void initialGame(){
        setLife(6);
    }
}
