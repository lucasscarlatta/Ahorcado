/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Match;
import asd.org.ahorcado.models.Word;

public class GameController {

    public static int NUMBER_LIFE = 6;

    private Map<Word, Integer> wordMap;
    private List<Long> usedWordIdList;
    private AbstractMatch match;

    public GameController() {
        this.wordMap = new HashMap<>();
        this.usedWordIdList = new ArrayList<>();
    }

    private void loadMap(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonWord = (JSONObject) jsonArray.get(i);
                Long id = jsonWord.getLong("id");
                String wordLetter = jsonWord.getString("name");
                int size = wordLetter.length();
                Word word = new Word(id, wordLetter, size, 0);
                wordMap.put(word, 0);
            }
        } catch (Exception e) {
        }
    }

    private Word getWord(JSONArray jsonArray) {
        Word myWord = null;
        if (wordMap.isEmpty()) {
            loadMap(jsonArray);
        }
        for (Word word : wordMap.keySet()) {
            if (wordMap.get(word) == 0) {
                usedWordIdList.add(word.getId());
                wordMap.remove(word);
                myWord = word;
                break;
            }
        }
        return myWord;
    }

    public void newMatch(JSONArray jsonArray) {
        match = new Match();
        match.initialGame(getWord(jsonArray));
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