package hr.fer.hmo;

import hr.fer.hmo.algorithm.GreedyConstructionAlgorithm;
import hr.fer.hmo.algorithm.ISearchAlgorithm;
import hr.fer.hmo.algorithm.annaeling.SimulatedAnnealingAlgorithm;
import hr.fer.hmo.algorithm.annaeling.GeometricTemperatureStrategy;
import hr.fer.hmo.algorithm.annaeling.ITemperatureStrategy;
import hr.fer.hmo.algorithm.annaeling.LinearTemperatureStrategy;
import hr.fer.hmo.algorithm.tabu.AttributiveTabuList;
import hr.fer.hmo.algorithm.tabu.ExplicitTabuList;
import hr.fer.hmo.algorithm.tabu.ITabuList;
import hr.fer.hmo.algorithm.tabu.TabuSearchAlgorithm;
import hr.fer.hmo.player.Player;
import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.squad.SquadGenerator;
import hr.fer.hmo.util.LoadUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Lab2Main {

    private static final double GREEDY_ALPHA = 0.0;
    private static final double GREEDY_BETA = 1.69;

    private static final int TABU_MAX_ITER = 200;

    private static final double SIMULATED_ANNEALING_GEOMETRIC_ALPHA = 0.85;
    private static final double SIMULATED_ANNEALING_LINEAR_BETA = 5;
    private static final int SIMULATED_ANNEALING_ITER_INNER = 50;
    private static final double SIMULATED_ANNEALING_MIN_TEMPERATURE = 1E-5;


    /**
     * @param args First argument: Input file
     *             Second argument: 1 = TABU, 2 = Simulated Annealing
     *             Third argument: 1 = Greedy Initial, 2 = Random Initial
     *             Fourth argument: Tabu tenure or Initial Temperature
     *             Fifth argument: 1 = Explicit tabu list, 2 = Attributive tabu list
     *             : 1 LinearTemperatureStrategy, 2 GeometricTemperatureStrategy
     */
    public static void main(String[] args) {
        if (args.length != 5)
            throw new IllegalArgumentException("Requires 4 arguments");

        Random random = new Random();
        ISearchAlgorithm greedyAlgorithm = new GreedyConstructionAlgorithm(GREEDY_ALPHA, GREEDY_BETA, random);

        int tabuTenure = Integer.parseInt(args[3]);
        int initialTemperature = Integer.parseInt(args[3]);

        ITabuList tabuList = null;
        ITemperatureStrategy temperatureStrategy = null;

        if (args[1].equals("1")) {
            tabuList = switch (args[4]) {
                case "1" -> new ExplicitTabuList(tabuTenure);
                case "2" -> new AttributiveTabuList(tabuTenure);
                default -> throw new IllegalArgumentException("Unexpected tabu list: " + args[4]);
            };
        } else if (args[1].equals("2")) {
            temperatureStrategy = switch (args[4]) {
                case "1" -> new LinearTemperatureStrategy(SIMULATED_ANNEALING_ITER_INNER,
                        initialTemperature,
                        SIMULATED_ANNEALING_MIN_TEMPERATURE,
                        SIMULATED_ANNEALING_LINEAR_BETA);
                case "2" -> new GeometricTemperatureStrategy(SIMULATED_ANNEALING_ITER_INNER,
                        initialTemperature,
                        SIMULATED_ANNEALING_MIN_TEMPERATURE,
                        SIMULATED_ANNEALING_GEOMETRIC_ALPHA);
                default -> throw new IllegalArgumentException("Unexpected temperature strategy: " + args[4]);
            };
        }

        ISearchAlgorithm algorithm = switch (args[1]) {
            case "1" -> new TabuSearchAlgorithm(tabuList, TABU_MAX_ITER);
            case "2" -> new SimulatedAnnealingAlgorithm(temperatureStrategy, random);
            default -> throw new IllegalArgumentException("Unexpected algorithm: " + args[1]);
        };

        Path file = Path.of(args[0]);


        try {
            List<Player> players = LoadUtil.loadPlayers(file);

            Squad startingSquad = switch (args[2]) {
                case "1" -> greedyAlgorithm.search(players, null);
                case "2" -> SquadGenerator.generateRandomValidSquad(players, random, 11, 4);
                default -> throw new IllegalArgumentException("Unexpected starting squad algorithm: " + args[2]);
            };


            Squad solution = algorithm.search(players, startingSquad);

            System.out.printf("Score = %d" +
                              "%nCost = %.1f%n", solution.getScore(), solution.getCost());

            System.out.println(solution);
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}