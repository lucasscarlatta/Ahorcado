package asd.org.ahorcado.models;

public abstract class AbstractGuess {

    private AbstractWord abstractWord;

    public AbstractWord getAbstractWord() {
        return abstractWord;
    }

    public void setAbstractWord(AbstractWord abstractWord) {
        this.abstractWord = abstractWord;
    }

    public String processWord(CharSequence letter) {
        if(this.abstractWord.containsLetter(letter)){
            this.abstractWord.exchangeLetters();
        }
        return this.abstractWord.getWord();
    }

    public boolean guessLetter(CharSequence letter){
        return this.abstractWord.containsLetter(letter);
    }

    public boolean isComplete() {
        return this.abstractWord.isComplete();
    }
}
