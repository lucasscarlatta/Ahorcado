/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.controller;

import org.json.JSONException;
import org.json.JSONObject;

import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Match;
import asd.org.ahorcado.models.Word;

public class GameController {

    public static int NUMBER_LIFE = 6;

    private AbstractMatch match;

    public GameController() {
    }

    private Word getWord(Object object) {
        Word word = null;
        try {
            Long id = ((JSONObject) object).getLong("id");
            String wordLetter = ((JSONObject) object).getString("name");
            int size = wordLetter.length();
            word = new Word(id, wordLetter, size, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return word;
    }

    public void newMatch(Object object) {
        match = new Match();
        match.initialGame(getWord(object));
        match.setLife(NUMBER_LIFE);
    }

    public void execute(char letter) {
        match.execute(letter);
    }

    public String obtainPartialWord() {
        return match.obtainPartialWord();
    }

    public boolean isComplete() {
        return match.isComplete();
    }

    public int getRemainingLives() throws MatchLostException {
        if (match.getLife() == 0) {
            throw new MatchLostException();
        } else {
            return match.getLife();
        }
    }

    public String originalWord() {
        return match.getOriginalWord();
    }

    public int getCoins() {
        return match.getCoin();
    }

    public char showOneLetter() {
        return this.match.showOneLetter();
    }

}