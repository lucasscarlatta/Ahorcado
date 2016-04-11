package asd.org.ahorcado.models;

import asd.org.ahorcado.exceptions.ForbiddenCharacterException;
import asd.org.ahorcado.interfaces.HangmanWord;

public abstract class AbstractGuess {

    protected HangmanWord originalWord;
    protected HangmanWord word;

    public abstract void inverseLetter();
    public abstract void exchangeLetters();

    public String processWord(char letter) {
        inverseLetter();
        this.word.markLetter(letter);
        exchangeLetters();
        return this.word.getWord();
    }

    public boolean guessLetter(char letter) throws Exception {
        if(Character.isLetter(letter)) {
            return this.word.containsLetter(letter);
        } else {
            throw new ForbiddenCharacterException();
        }
    }

    public boolean isComplete() {
        return this.word.isComplete();
    }
}
