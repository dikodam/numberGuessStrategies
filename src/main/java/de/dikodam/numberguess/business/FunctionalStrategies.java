package de.dikodam.numberguess.business;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionalStrategies {


    private FunctionalStrategies() {
    }

    public static final BiFunction<Integer, Integer, Integer> randomNumberGenerator = (lowerBound, upperBound) ->
            new Random().nextInt(upperBound) + lowerBound;

    public static final Function<Integer, Integer> voidBoundProcessor = Function.identity();

    public static final Function<Integer, Integer> restrictingTooLowProcessor = (lastGuess) -> lastGuess + 1;

    public static final Function<Integer, Integer> restrictingTooHighProcessor = Function.identity();

    public static final BiFunction<Integer, Integer, Integer> targettingGenerator = (incLower, exclUpper) ->
            (exclUpper - incLower) / 2 + incLower;

    public static FunctionalStrategy getDefaultNoobStrategy(int inclusiveLowerBound, int exclusiveUpperBound) {
        return new FunctionalStrategy("defaultRandom", inclusiveLowerBound, exclusiveUpperBound)
                .withNumberGenerator(randomNumberGenerator)
                .withTooLowProcessor(voidBoundProcessor)
                .withTooHighProcessor(voidBoundProcessor);
    }

    public static FunctionalStrategy getRestrictingRandomStrategy(int inclusiveLowerBound, int exclusiveUpperBound) {
        return new FunctionalStrategy("restrictingRandom", inclusiveLowerBound, exclusiveUpperBound)
                .withNumberGenerator(randomNumberGenerator)
                .withTooLowProcessor(restrictingTooLowProcessor)
                .withTooHighProcessor(restrictingTooHighProcessor);
    }

    public static FunctionalStrategy getRestrictingTargettingStrategy(int inclusiveLowerBound, int exclusiveUpperBound) {
        return new FunctionalStrategy("restrictingTargetting", inclusiveLowerBound, exclusiveUpperBound)
                .withNumberGenerator(targettingGenerator)
                .withTooLowProcessor(restrictingTooLowProcessor)
                .withTooHighProcessor(restrictingTooHighProcessor);
    }
}
