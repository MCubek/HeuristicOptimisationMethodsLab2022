package hr.fer.hmo;

import hr.fer.hmo.algorithm.GreedyConstructionAlgorithm;
import hr.fer.hmo.algorithm.ISearchAlgorithm;
import hr.fer.hmo.algorithm.SimulatedAnnealingAlgorithm;
import hr.fer.hmo.algorithm.tabu.AttributiveTabuList;
import hr.fer.hmo.algorithm.tabu.ITabuList;
import hr.fer.hmo.algorithm.tabu.TabuSearchAlgorithm;
import hr.fer.hmo.player.Player;
import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.util.LoadUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Lab2Main {

    private static final double ALPHA = 0.2;
    private static final double BETA = 1.69;

    private static final int TABU_TENURE = 10;
    private static final int MAX_ITER = 100;

    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Requires path of file and algorithm number as only arguments");

        Random random = new Random();
        ISearchAlgorithm greedyAlgorithm = new GreedyConstructionAlgorithm(ALPHA, BETA, random);

        ITabuList tabuList = new AttributiveTabuList(TABU_TENURE);

        ISearchAlgorithm algorithm = switch (args[1]) {
            case "1" -> new TabuSearchAlgorithm(tabuList, MAX_ITER);
            case "2" -> new SimulatedAnnealingAlgorithm();
            default -> throw new IllegalStateException("Unexpected algorithm: " + args[1]);
        };

        Path file = Path.of(args[0]);

        try {
            List<Player> players = LoadUtil.loadPlayers(file);

            Squad startingSquad = greedyAlgorithm.search(players, null);

            Squad solution = algorithm.search(players, startingSquad);

            System.out.printf("Score = %d, cost = %.1f%n", solution.getScore(), solution.getCost());

            System.out.println(solution);
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}