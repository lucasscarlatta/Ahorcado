/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public char showAValidLetter() {
        char letter=this.obtainAValidLetter();
        try {
            this.exchangeLetters(letter);
        }catch(LostLifeException e){
            //we don't have to do anything 'cause the letter is valid
        }
        return letter;
    }

    public char obtainAValidLetter() {
        Random r = new Random();
        String partialWord=this.word.getWord();
        String completedWord=this.originalWord.getWord();
        char[] wordChars = partialWord.toCharArray();
        char[] originalWordChars = completedWord.toCharArray();
        int positionLetter;
        if (!partialWord.equals(completedWord)) {
            do {
                positionLetter = r.nextInt((wordChars.length - 1) - 0);
            } while (wordChars[positionLetter] != '_');
        } else {
            positionLetter = r.nextInt((wordChars.length - 1) - 0);
        }
        return originalWordChars[positionLetter];
    }

}