package de.dikodam.numberguess.business;

public interface IStrategy {

    String getName();

    void setRange(int inclusiveLowerBound, int exclusiveUpperBound);

    int guessNumber();

    void guessedTooLow();

    void guessedTooHigh();
}
