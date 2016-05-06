/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asd.org.ahorcado.dal.dao.WordDAO;
import asd.org.ahorcado.exceptions.LostLifeException;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Match;
import asd.org.ahorcado.models.Word;

public class GameController {

    public static int NUMBER_LIFE = 6;

    private Context context;
    private Map<Word, Integer> wordMap;
    private List<Long> usedWordIdList;
    private AbstractMatch match;

    public GameController(Context context) {
        this.wordMap = new HashMap<>();
        this.context = context;
        this.usedWordIdList = new ArrayList<>();
    }

    private void loadMap() {
        WordDAO wordDAO = new WordDAO(context);
        wordMap = wordDAO.getWords(usedWordIdList);
    }

    private Word getWord() {
        Word myWord = null;
        if (wordMap.isEmpty()) {
            loadMap();
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

    public void newMatch() {
        match = new Match();
        match.initialGame(getWord());
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

    public int getRemainingLives() throws LostLifeException {
        if (match.getLife() != 0){
            return match.getLife();
        } else {
            throw new LostLifeException();
        }
    }

    public int getCoins(){
        return match.getCoin();
    }

    public char showOneLetter(){
       return this.match.showOneLetter();
    }
}