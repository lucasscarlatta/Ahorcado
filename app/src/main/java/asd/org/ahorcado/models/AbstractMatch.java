/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import asd.org.ahorcado.exceptions.LostLifeException;

public abstract class AbstractMatch {

    protected AbstractGuess guesser;

    public static String MATCH_ID = "matchId";
    public static String IS_ACTIVE = "isActive";

    private int life;
    private User user;
    private boolean result;
    private  int coin = 2;

    public int getCoin(){return coin;}

    public void setCoin(int coin){this.coin=coin;}

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

    public boolean execute(char letter) {
        boolean result = true;
        try {
            this.guesser.processWord(letter);
        } catch (LostLifeException e) {
            --life;
            result = false;
        } finally {
            return result;
        }
    }

    public char showOneLetter(){
        setCoin(this.coin -1);
        return this.guesser.showAValidLetter();
    }

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

    public String getOriginalWord() {
        return guesser.originalWord.getWord();
    }
}