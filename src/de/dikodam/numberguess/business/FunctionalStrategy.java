package de.dikodam.numberguess.business;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class FunctionalStrategy implements IStrategy {

    private String name;
    private int inclusiveLowerBound;
    private int exclusiveUpperBound;
    private BiFunction<Integer, Integer, Integer> numberGenerator;
    private Function<Integer, Integer> tooLowProcessor;
    private Function<Integer, Integer> tooHighProcessor;


    public static final BiFunction<Integer, Integer, Integer> defaultGenerator = (lowerBound, upperBound) ->
        new Random().nextInt(upperBound) + lowerBound;

    public static final Function<Integer, Integer> defaultBoundProcessor = Function.identity();

    public FunctionalStrategy(String name, int inclusiveLowerBound, int exclusiveUpperBound) {
        this(name, inclusiveLowerBound, exclusiveUpperBound, defaultGenerator, defaultBoundProcessor,
            defaultBoundProcessor);
    }

    public FunctionalStrategy(String name, int inclusiveLowerBound, int exclusiveUpperBound, BiFunction<Integer,
        Integer, Integer> numberGenerator, Function<Integer, Integer> tooLowProcessor, Function<Integer, Integer>
                                  tooHighProcessor) {
        this.name = name;
        this.inclusiveLowerBound = inclusiveLowerBound;
        this.exclusiveUpperBound = exclusiveUpperBound;
        this.withNumberGenerator(numberGenerator)
            .withTooLowProcessor(tooLowProcessor)
            .withTooHighProcessor(tooHighProcessor);
    }

    public FunctionalStrategy withNumberGenerator(BiFunction<Integer, Integer, Integer> numberGenerator) {
        this.numberGenerator = numberGenerator;
        return this;
    }

    public FunctionalStrategy withTooLowProcessor(Function<Integer, Integer> tooLowProcessor) {
        this.tooLowProcessor = tooLowProcessor;
        return this;
    }

    public FunctionalStrategy withTooHighProcessor(Function<Integer, Integer> tooHighProcessor) {
        this.tooHighProcessor = tooHighProcessor;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setRange(int inclusiveLowerBound, int exclusiveUpperBound) {
        this.inclusiveLowerBound = inclusiveLowerBound;
        this.exclusiveUpperBound = exclusiveUpperBound;
    }

    @Override
    public final int guessNumber() {
        return numberGenerator.apply(inclusiveLowerBound, exclusiveUpperBound);
    }

    @Override
    public final void guessedTooLow() {
        inclusiveLowerBound = tooLowProcessor.apply(inclusiveLowerBound);
    }

    @Override
    public final void guessedTooHigh() {
        exclusiveUpperBound = tooHighProcessor.apply(exclusiveUpperBound);
    }
}
