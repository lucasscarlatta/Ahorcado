/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import asd.org.ahorcado.exceptions.LostLifeException;
import asd.org.ahorcado.helpers.StringHelper;
import asd.org.ahorcado.interfaces.HangmanWord;

public class Guesser extends AbstractGuess {

    public Guesser(HangmanWord originalWord) {
        this.originalWord = originalWord;
        this.word = new Word(originalWord.getWord());
        this.word.setBlankWord();
    }

    public void exchangeLetters(char letter) throws LostLifeException {
        boolean guessed = false;
        String originalWordString = this.originalWord.getWord();
        char[] clearWord = StringHelper.clearWord(originalWordString).toCharArray();
        for (int i = 0; i < originalWordString.length(); i++) {
            if (letter == clearWord[i]) {
                guessed = true;
                this.word.showLetter(i, originalWordString.charAt(i));
            }
        }
        if (!guessed) {
            throw new LostLifeException();
        }
    }

}