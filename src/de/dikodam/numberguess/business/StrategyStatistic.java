package de.dikodam.numberguess.business;

public class StrategyStatistic {

    private IStrategy strategy;
    private boolean finished;
    private int tryCount;

    public StrategyStatistic(IStrategy strategy) {
        this.strategy = strategy;
        finished = false;
        tryCount = 0;
    }

    public IStrategy getStrategy() {
        return strategy;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isNotFinished() {
        return !finished;
    }

    public void setFinished() {
        finished = true;
    }

    public int getTryCount() {
        return tryCount;
    }

    public String getName() {
        return strategy.getName();
    }

    public int guessNumber() {
        tryCount++;
        return strategy.guessNumber();
    }

    public void guessedTooLow() {
        strategy.guessedTooLow();
    }

    public void guessedTooHigh() {
        strategy.guessedTooHigh();
    }

}
