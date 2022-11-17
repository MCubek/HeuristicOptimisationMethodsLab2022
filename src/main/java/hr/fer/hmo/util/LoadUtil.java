package hr.fer.hmo.util;

import hr.fer.hmo.player.Player;
import hr.fer.hmo.player.PlayerPosition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author matejc
 * Created on 17.11.2022.
 */

public class LoadUtil {
    private LoadUtil() {
    }
    public static List<Player> loadPlayers(Path file) throws IOException {
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
