package asd.org.ahorcado.models;


import asd.org.ahorcado.interfaces.HangmanWord;

public class Guesser extends AbstractGuess {

    public Guesser() {
        this.originalWord = new Word("Activity");
        this.word = new Word("Activity");
    }

    public Guesser(HangmanWord originalWord, HangmanWord word) {
        this.originalWord = originalWord;
        this.word = word;
    }

    public void exchangeLetters() {
        char[] wordChars = word.getWord().toCharArray();
        char[] originalWordChars = this.originalWord.getWord().toCharArray();
        for (int i = 0; i < wordChars.length; i++) {
            if (wordChars[i] != originalWordChars[i]) {
                StringBuilder word = new StringBuilder(this.word.getWord());
                word.setCharAt(i, originalWordChars[i]);
                this.word.setWord(word.toString());
            } else {
                this.word.setMark(i);
            }
        }
    }

    public void inverseLetter() {
        if(!this.word.getWord().equals(this.originalWord.getWord())){
            char[] wordChars = this.word.getWord().toCharArray();
            char[] originalWordChars = this.originalWord.getWord().toCharArray();
            for(int i = 0; i<wordChars.length;i++){
                if(wordChars[i] == '_'){
                    StringBuilder word = new StringBuilder(this.word.getWord());
                    word.setCharAt(i, originalWordChars[i]);
                    this.word.setWord(word.toString());
                } else {
                    this.word.setMark(i);
                }
            }
        }
    }

    public String getNewWord() {
        StringBuilder sbWord = new StringBuilder();
        int size = this.word.getWord().length();
        for (int i = 0; i < size; i++){
            sbWord.append("_");
        }
        return sbWord.toString();
    }

}
