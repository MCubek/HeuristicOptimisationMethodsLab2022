package hr.fer.hmo;

import hr.fer.hmo.algorithm.GreedyConstructionAlgorithm;
import hr.fer.hmo.algorithm.ISearchAlgorithm;
import hr.fer.hmo.algorithm.SimulatedAnnealingAlgorithm;
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

    private static final double ALPHA = 0.2;
    private static final double BETA = 1.69;

    private static final int MAX_ITER = 100;

    /**
     * @param args First argument: Input file
     *             Second argument: 1 = TABU, 2 = Simulated Annealing
     *             Third argument: 1 = Greedy Initial, 2 = Random Initial
     *             Fourth argument: Tabu tenure
     *             Fifth argument: 1 = Explicit tabu list, 2 = Attributive tabu list
     */
    public static void main(String[] args) {
        if (args.length != 5)
            throw new IllegalArgumentException("Requires 4 arguments");

        Random random = new Random();
        ISearchAlgorithm greedyAlgorithm = new GreedyConstructionAlgorithm(ALPHA, BETA, random);

        int tabuTenure = Integer.parseInt(args[3]);

        ITabuList tabuList = switch (args[4]) {
            case "1" -> new AttributiveTabuList(tabuTenure);
            case "2" -> new ExplicitTabuList(tabuTenure);
            default -> throw new IllegalArgumentException("Unexpected tabu list: " + args[4]);
        };

        ISearchAlgorithm algorithm = switch (args[1]) {
            case "1" -> new TabuSearchAlgorithm(tabuList, MAX_ITER);
            case "2" -> new SimulatedAnnealingAlgorithm();
            default -> throw new IllegalArgumentException("Unexpected algorithm: " + args[1]);
        };

        Path file = Path.of(args[0]);


        try {
            List<Player> players = LoadUtil.loadPlayers(file);

            Squad startingSquad = switch (args[2]) {
                case "1" -> SquadGenerator.generateRandomValidSquad(players, random, 11, 4);
                case "2" -> greedyAlgorithm.search(players, null);
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