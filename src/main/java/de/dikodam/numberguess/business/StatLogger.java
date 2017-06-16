package de.dikodam.numberguess.business;

import java.util.*;
import java.util.stream.Collectors;

public class StatLogger {

    private static StatLogger instance;

    private Map<Integer, List<StrategyStatistic>> strategyStatistics;
    private int runCounter;


    private StatLogger() {
        reset();
    }

    public static StatLogger getInstance() {
        if (instance == null) {
            instance = new StatLogger();
        }
        return instance;
    }

    /**
     * resets the StatLogger by reinstanciating its fields
     *
     * @return returns the resetted StatLoggerInstance
     */
    public StatLogger reset() {
        strategyStatistics = new HashMap<>();
        runCounter = 0;
        return this;
    }

    public void addStrategyStats(Collection<StrategyStatistic> strategyWrappers) {
        runCounter++;
        strategyStatistics.put(runCounter, new ArrayList<>(strategyWrappers));
    }

    /**
     * @return returns a string containg a report of the simulation of all
     */
    public String getBigStatistics() {
        StringBuilder sb = new StringBuilder();
        strategyStatistics.forEach((run, stratStat) -> sb.append(singleRunReport(run, stratStat)));
        return sb.toString();
    }

    private String singleRunReport(Integer run, List<StrategyStatistic> stratStats) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Run : %d\n", run));
        stratStats.forEach(strategyStat -> {
            sb.append(String.format("Strategy: %s\n", strategyStat.getName()));
            sb.append(String.format("TryCount: %d\n", strategyStat.getTryCount()));
        });
        sb.append("##########\n");
        return sb.toString();
    }

    public String getSummaryStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Runs: %d\n", runCounter));

        // TODO make the following possible
//        sb.append(String.format("LowerBound: %d", runCounter));
//        sb.append(String.format("UpperBound: %d", runCounter));

        Map<String, List<StrategyStatistic>> strategyStatisticsGroupedByNames =
            strategyStatistics.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.groupingBy(StrategyStatistic::getName));

        for (Map.Entry<String, List<StrategyStatistic>> entry : strategyStatisticsGroupedByNames.entrySet()) {
            sb.append(String.format("Strategy: %s\n", entry.getKey()));
            OptionalDouble averageTries = entry.getValue()
                .stream()
                .mapToInt(StrategyStatistic::getTryCount)
                .average();
            sb.append(String.format("avg. tries: %,.2f\n", averageTries.orElse(-1)));
        }

        // TODO compute (VAR or SD), maybe extrema

        return sb.toString();
    }
}
