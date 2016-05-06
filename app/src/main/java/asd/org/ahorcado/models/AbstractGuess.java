/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import java.util.Map;

import java.util.HashMap;

import asd.org.ahorcado.exceptions.ForbiddenCharacterException;
import asd.org.ahorcado.exceptions.LostLifeException;
import asd.org.ahorcado.interfaces.HangmanWord;

public abstract class AbstractGuess {

    protected HangmanWord originalWord;
    protected HangmanWord word;

    public abstract void exchangeLetters(char letter) throws LostLifeException;

    public void processWord(char letter) throws LostLifeException {
        exchangeLetters(letter);
    }

    public boolean guessLetter(char letter) throws Exception {
        if (Character.isLetter(letter)) {
            return this.word.containsLetter(letter);
        } else {
            throw new ForbiddenCharacterException();
        }
    }

    public abstract char showAValidLetter();
    public abstract char obtainAValidLetter();


}

