package hr.fer.hmo.lab1;

import hr.fer.hmo.lab1.algorithm.GraspSearchAlgorithm;
import hr.fer.hmo.lab1.algorithm.GreedyConstructionAlgorithm;
import hr.fer.hmo.lab1.algorithm.ISearchAlgorithm;
import hr.fer.hmo.lab1.algorithm.LocalSearchAlgorithm;
import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.util.LoadUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Lab1Main {

    private static final double ALPHA = 0.8;
    private static final double BETA = 1.69;
    private static final int MAX_ITERATIONS_LOCAL_SEARCH = 10_000;
    private static final int MAX_ITERATIONS_GRASP = 10;

    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Requires path of file and algorithm number as only arguments");

        Random random = new Random();
        ISearchAlgorithm localSearchAlgorithm = new LocalSearchAlgorithm(MAX_ITERATIONS_LOCAL_SEARCH, random);
        ISearchAlgorithm greedyAlgorithm = new GreedyConstructionAlgorithm(1, BETA, random);
        ISearchAlgorithm constructionAlgorithm = new GreedyConstructionAlgorithm(ALPHA, BETA, random);

        ISearchAlgorithm algorithm = switch (args[1]) {
            case "1" -> greedyAlgorithm;
            case "2" -> new GraspSearchAlgorithm(localSearchAlgorithm, constructionAlgorithm, MAX_ITERATIONS_GRASP);
            default -> throw new IllegalStateException("Unexpected algorithm: " + args[1]);
        };

        Path file = Path.of(args[0]);

        try {
            List<Player> players = LoadUtil.loadPlayers(file);

            Squad solution = algorithm.search(players, null);

            System.out.printf("Score = %d, cost = %.1f%n", solution.getScore(), solution.getCost());

            System.out.println(solution);
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}