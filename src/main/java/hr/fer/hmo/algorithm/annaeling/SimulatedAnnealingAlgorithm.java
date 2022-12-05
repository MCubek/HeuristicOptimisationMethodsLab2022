package hr.fer.hmo.algorithm.annaeling;

import hr.fer.hmo.algorithm.ISearchAlgorithm;
import hr.fer.hmo.player.Player;
import hr.fer.hmo.squad.ISquadRule;
import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.squad.SquadRules;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.11.2022.
 */

public class SimulatedAnnealingAlgorithm implements ISearchAlgorithm {

    private final ITemperatureStrategy temperatureStrategy;
    private final Random random;

    private static final ISquadRule rule = SquadRules.allRules;

    public SimulatedAnnealingAlgorithm(ITemperatureStrategy temperatureStrategy, Random random) {
        this.temperatureStrategy = temperatureStrategy;
        this.random = random;
    }

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        Squad current = Objects.requireNonNull(startingSquad);

        temperatureStrategy.initialiseCurrentTemperature();

        double temperature = Double.MAX_VALUE;

        for (int i = 0; temperature > temperatureStrategy.minTemperature(); i++) {
            temperature = temperatureStrategy.getAndUpdateTemperature();
            System.out.printf("Temperature: %.4f%n", temperature);

            for (int j = 0; j < temperatureStrategy.innerLoopCount(); j++) {

                Squad neighbour = getRandomValidNeighbour(current);

                current = shouldSwitchWithNeighbour(current, neighbour, temperature) ? neighbour : current;
            }
            System.out.printf("Iteration: %d, score: %d%n", i, current.getScore());
            System.gc();
        }
        return current;
    }

    private boolean shouldSwitchWithNeighbour(Squad current, Squad neighbour, double temperature) {
        int currentFitness = current.getScore();
        int neighbourFitness = neighbour.getScore();

        double deltaE = (1.0 * neighbourFitness - currentFitness) * - 1;

        if (deltaE <= 0) return true;

        return random.nextDouble() <= Math.exp(- deltaE / temperature);
    }

    private Squad getRandomValidNeighbour(Squad current) {
        var neighbourhood = current.getNeighboursListWithPredicate(squad -> squad.checkRule(rule));
        return neighbourhood.get(random.nextInt(neighbourhood.size()));
    }
}
