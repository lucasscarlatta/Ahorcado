package asd.org.ahorcado.controller;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import asd.org.ahorcado.models.Word;

/**
 * Created by Huber on 20/04/2016.
 */
public class GameController {
    Context context;
    Map<Word, Integer> wordMap;

    public GameController(Context context) {
        this.wordMap = new HashMap<>();
        this.context = context;
    }
}
