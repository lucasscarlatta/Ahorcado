/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import asd.org.ahorcado.exceptions.MatchLostException;

public abstract class AbstractMatch {

    private AbstractGuess abstractGuess;
    private int life;
    private User user;
    private boolean result;

    public AbstractGuess getAbstractGuess() {
        return abstractGuess;
    }

    public void setAbstractGuess(AbstractGuess abstractGuess) {
        this.abstractGuess = abstractGuess;
    }

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

    public String execute(CharSequence letter) {
        return this.abstractGuess.processWord(letter);
    }

    public boolean afterExecute(CharSequence letter) throws MatchLostException {
        if (!this.abstractGuess.guessLetter(letter)) {
            --life;
            if (life == 0) {
                throw new MatchLostException();
            }
        }
        return this.abstractGuess.isComplete();
    }

}
