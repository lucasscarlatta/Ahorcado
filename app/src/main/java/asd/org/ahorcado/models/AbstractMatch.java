/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import java.util.Map;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.exceptions.LostLifeException;

public abstract class AbstractMatch {

    protected AbstractGuess guesser;

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

    public void execute(char letter) {
        try {
            this.guesser.processWord(letter);
        } catch (LostLifeException e) {
            --life;
        }
    }

    public abstract Map<String,String> showOneLetter();

    /*public String getNewWord() {
        return guesser.getNewWord();
    }*/

    public void initialGame(Word word) {
        guesser = new Guesser(word);
    }

    public String obtainPartialWord() {
        return guesser.word.getWord();
    }

    public boolean isComplete() {
        return guesser.word.isComplete();
    }

}