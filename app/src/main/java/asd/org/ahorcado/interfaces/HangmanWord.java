package asd.org.ahorcado.interfaces;

public interface HangmanWord {

    final char MARK = '_';

    boolean containsLetter(char letter);

    void markLetter(char letter);

    boolean isComplete();

    void setMark(int position);

    String getWord();
    void setWord(String word);
}
