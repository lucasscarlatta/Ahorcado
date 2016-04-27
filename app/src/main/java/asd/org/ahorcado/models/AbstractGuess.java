package asd.org.ahorcado.models;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGuess {

    private Word word = new Word();
    
    public String processWord(char letter) {
        this.word.inverseLetter();
        this.word.setMyLetter(letter);
        this.word.exchangeLetters();
        return this.word.getWord();
    }

    public boolean guessLetter(char letter) {
        return this.word.containsLetter(letter);
    }

    public boolean isComplete() {
        return this.word.isComplete();
    }

    public String getNewWord() {
        //TODO Huber;
        word.setOriginalWord("ACTIVITY");
        word.setWord("ACTIVITY");
        word.setSize(8);
        StringBuilder sbWord = new StringBuilder();
        for (int i = 0; i < word.getSize(); i++){
            sbWord.append("_");
        }
        return sbWord.toString();
    }

    public Map<String,String> showOneLetter(){
        Map mapa=new HashMap<>();
        char letter=this.word.obtainAValidLetter();
        String word=processWord(letter);
        mapa.put("word",word);
        mapa.put("letter",letter);
        return mapa;
    }
}
