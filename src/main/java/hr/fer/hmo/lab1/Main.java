package hr.fer.hmo.lab1;

import hr.fer.hmo.lab1.algorithm.GraspSearchAlgorithm;
import hr.fer.hmo.lab1.algorithm.GreedyLocalSearchAlgorithm;
import hr.fer.hmo.lab1.algorithm.ISearchAlgorithm;
import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.player.PlayerPosition;
import hr.fer.hmo.lab1.squad.Squad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Requires path of file and algorithm number as only arguments");

        ISearchAlgorithm algorithm = switch (args[1]) {
            case "1" -> new GreedyLocalSearchAlgorithm();
            case "2" -> new GraspSearchAlgorithm(new GreedyLocalSearchAlgorithm());
            default -> throw new IllegalStateException("Unexpected algorithm: " + args[1]);
        };

        Path file = Path.of(args[0]);

        try {
            List<Player> players = loadPlayers(file);

            Squad test = Squad.generateRandomSquad(players, new Random(), 11, 4);

            Squad solution = algorithm.search(players, null);

            System.out.println(solution);
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<Player> loadPlayers(Path file) throws IOException {
        List<Player> players = new ArrayList<>(700);
        try (var reader = Files.newBufferedReader(file, StandardCharsets.ISO_8859_1)) {
            String line = reader.readLine();
            while (line != null && ! line.isEmpty()) {
                var split = line.split(",");

                Player player = new Player(Integer.parseInt(split[0]),
                        PlayerPosition.get(split[1]),
                        split[2],
                        split[3],
                        Integer.parseInt(split[4]),
                        Double.parseDouble(split[5]));

                players.add(player);

                line = reader.readLine();
            }
        }
        return players;
    }
}