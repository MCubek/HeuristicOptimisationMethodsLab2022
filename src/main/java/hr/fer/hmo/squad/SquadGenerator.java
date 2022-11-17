package hr.fer.hmo.squad;

import hr.fer.hmo.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 26.10.2022.
 */

public class SquadGenerator {

    private SquadGenerator() {

    }

    public static Squad generateRandomValidSquad(List<Player> players, Random random, int activePlayersCount, int reservePlayersCount) {
        Squad squad;
        do {
            squad = new Squad(activePlayersCount, reservePlayersCount, players);

            var pickedPlayers = random.ints(0, players.size() - 1)
                    .distinct()
                    .limit((long) activePlayersCount + reservePlayersCount)
                    .mapToObj(players::get)
                    .toList();

            var activePlayers = random.ints(0, pickedPlayers.size() - 1)
                    .distinct()
                    .limit(activePlayersCount)
                    .mapToObj(pickedPlayers::get)
                    .toList();

            var reservePlayers = new ArrayList<>(pickedPlayers);
            reservePlayers.removeAll(activePlayers);


            activePlayers.forEach(squad::addActivePlayer);
            reservePlayers.forEach(squad::addReservePlayer);
        } while (! squad.checkRule(SquadRules.allRules));

        return squad;
    }
}
