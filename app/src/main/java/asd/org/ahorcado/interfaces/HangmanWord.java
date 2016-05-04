/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.interfaces;

public interface HangmanWord {

    final char MARK = '_';

    final char LETTER_I = 'I';

    boolean containsLetter(char letter);

    void showLetter(int position, char letter);

    boolean isComplete();

    void setMark(int position);

    String getWord();

    void setWord(String word);

    void setBlankWord();
}
