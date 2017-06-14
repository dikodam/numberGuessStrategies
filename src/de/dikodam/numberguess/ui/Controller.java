package de.dikodam.numberguess.ui;

import de.dikodam.numberguess.business.Simulator;
import de.dikodam.numberguess.business.StatLogger;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Optional;

public class Controller {

    @FXML
    public TextField inputRunCount;
    @FXML
    public TextArea outputArea;
    @FXML
    public TextField inputUpperBound;
    @FXML
    public TextField inputLowerBound;

    private StatLogger statLogger;
    private Simulator simulator;
    private static int DEFAULT_LOWER_BOUND = 0;
    private static int DEFAULT_UPPER_BOUND = 100;


    public void initialize() {
        statLogger = StatLogger.getInstance();
    }

    public void processGo() {
        simulator = new Simulator(getLowerBound().orElse(DEFAULT_LOWER_BOUND), getUpperBound().orElse
            (DEFAULT_UPPER_BOUND));
        simulator.simulate(getRunCount().orElse(100))
            .thenRun(() -> outputArea.setText(statLogger.getSummaryStatistics()));
    }

    private Optional<Integer> getLowerBound() {
        try {
            return Optional.of(Integer.parseInt(inputLowerBound.getText()));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

    private Optional<Integer> getUpperBound() {
        int lowerBound = getLowerBound().orElse(DEFAULT_LOWER_BOUND);
        try {
            Integer upperBound = Integer.parseInt(inputUpperBound.getText());
            if (upperBound < lowerBound) {
                return Optional.of(lowerBound);
            } else {
                return Optional.of(upperBound);
            }
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

    private Optional<Integer> getRunCount() {
        try {
            int runCount = Integer.parseInt(inputRunCount.getText());
            if (runCount >= 1) {
                return Optional.of(runCount);
            } else {
                return Optional.empty();
            }
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }
}
