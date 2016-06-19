/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.controller;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asd.org.ahorcado.activities.VersusActivity;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.helpers.UserAdapter;
import asd.org.ahorcado.interfaces.HangmanWord;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Match;
import asd.org.ahorcado.models.Word;
import asd.org.ahorcado.utils.MySharedPreference;

public class GameController {

    public static int NUMBER_LIFE = 6;

    private AbstractMatch match;

    public GameController() {
    }

    public Word getWord(Object object) {
        Word word = null;
        try {
            Long id = ((JSONObject) object).getLong("id");
            String wordLetter = ((JSONObject) object).getString("word");
            int size = ((JSONObject) object).getInt("size");
            word = new Word(id, wordLetter, size, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return word;
    }

    public void newMatch(Word word) {
        match = new Match();
        match.initialGame(word);
        match.setLife(NUMBER_LIFE);
    }

    public boolean execute(char letter) {
        return match.execute(letter);
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

    public List<Map<String, String>> getUserMap(JSONArray jsonArray) {
        List<Map<String, String>> userList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, String> userMap = new HashMap<>();
                JSONObject jsonUser = (JSONObject) jsonArray.get(i);
                String token = jsonUser.getString(MySharedPreference.TOKEN_TO_SERVER);
                if (token.compareTo(FirebaseInstanceId.getInstance().getToken()) != 0) {
                    String id = jsonUser.getString("id");
                    String name = jsonUser.getString("userId");
                    userMap.put(UserAdapter.COLUMN_ID, id);
                    userMap.put(UserAdapter.COLUMN_NAME, name);
                    userList.add(userMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public int countGuessedLetters(String partialWord) {
        String guessed = partialWord.replaceAll(String.valueOf(HangmanWord.MARK), "");
        return guessed.length();
    }

    public void getChallenger(JSONObject jsonObject, int opponentUser, String opponentName, Context context) {
        try {
            Intent intent = new Intent(context, VersusActivity.class);
            intent.putExtra(UserAdapter.COLUMN_ID, opponentUser);
            intent.putExtra(UserAdapter.COLUMN_NAME, opponentName);
            intent.putExtra(AbstractMatch.IS_ACTIVE, jsonObject.getBoolean(AbstractMatch.IS_ACTIVE));
            Long id = jsonObject.getLong(AbstractMatch.MATCH_ID);
            if (id != null) {
                String wordText = jsonObject.getString("wordText");
                Long wordId = jsonObject.getLong("wordId");
                int size = jsonObject.getInt("wordSize");
                intent.putExtra("word", new Word(wordId, wordText, size, 0));
            } else {
                id = -1L;
            }
            intent.putExtra(AbstractMatch.MATCH_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}