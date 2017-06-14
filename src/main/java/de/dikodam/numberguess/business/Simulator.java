package de.dikodam.numberguess.business;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Simulator {

    private int inclusiveLowerBound;
    private int exclusiveUpperBound;
    private int numberToGuess;
    private List<StrategyStatistic> strategyWrappers;
    private StatLogger statLogger;
    private Random random;

    public Simulator(int inclusiveLowerBound, int exclusiveUpperBound) {
        this.inclusiveLowerBound = inclusiveLowerBound;
        this.exclusiveUpperBound = exclusiveUpperBound;
        strategyWrappers = new ArrayList<>();
        statLogger = StatLogger.getInstance().reset();
        random = new Random();

        // add a default strategy
        this.addStrategy(FunctionalStrategies.getDefaultNoobStrategy(inclusiveLowerBound, exclusiveUpperBound));
        this.addStrategy(FunctionalStrategies.getRestrictingRandomStrategy(inclusiveLowerBound, exclusiveUpperBound));
        this.addStrategy(FunctionalStrategies.getRestrictingTargettingStrategy(inclusiveLowerBound, exclusiveUpperBound));
    }

    public int getInclusiveLowerBound() {
        return inclusiveLowerBound;
    }

    public int getExclusiveUpperBound() {
        return exclusiveUpperBound;
    }

    public void addStrategy(IStrategy strategy) {
        strategyWrappers.add(new StrategyStatistic(strategy));
    }

    public CompletableFuture<?> simulate(int runs) {
        CompletableFuture<Integer> cf = CompletableFuture.completedFuture(0);
        // TODO wrap running strategies in CFs
        for (int i = 0; i < runs; i++) {
            numberToGuess = random.nextInt(exclusiveUpperBound) + inclusiveLowerBound;
            doOneRun();
        }
        return cf;
    }

    private void doOneRun() {
        while (notFinishedStrategiesExists(strategyWrappers)) {
            strategyWrappers
                .stream()
                .filter(StrategyStatistic::isNotFinished)
                .forEach(this::doOneStep);
        }
        statLogger.addStrategyStats(strategyWrappers);
        prepareForNewRun();
    }

    private boolean notFinishedStrategiesExists(List<StrategyStatistic> strategyWrappers) {
        return strategyWrappers
            .stream()
            .anyMatch(StrategyStatistic::isNotFinished);
    }

    private void doOneStep(StrategyStatistic strategyWrapper) {
        int guessedNumber = strategyWrapper.guessNumber();
        if (guessedNumber == numberToGuess) {
            strategyWrapper.setFinished();
        } else if (guessedNumber < numberToGuess) {
            strategyWrapper.guessedTooLow();
        } else {
            strategyWrapper.guessedTooHigh();
        }
    }

    private void prepareForNewRun() {
        strategyWrappers = strategyWrappers
            .stream()
            .map((strategyWrapper) -> new StrategyStatistic(strategyWrapper.getStrategy()))
            .collect(Collectors.toList());
    }
}
