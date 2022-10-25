package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.ISquadRule;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.squad.SquadRules;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

public class GreedyLocalSearchAlgorithm implements ISearchAlgorithm {

    public static final double P = 0.3;
    public static final Random random = new Random();
    public static final int MAX_ITERATIONS = 1_000;

    public static final ISquadRule rule = SquadRules.allRules;


    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        Squad squad = startingSquad;

        HashSet<Squad> visited = new HashSet<>();

        if (squad == null) squad = Squad.generateRandomValidSquad(players, random, 11, 4);

        visited.add(squad);

        for (int i = 0; i < MAX_ITERATIONS; i++) {

            Squad previousSquad = squad;

            for (var neighbour : squad) {
                if (neighbour.checkRule(rule) && ! visited.contains(neighbour) &&
                    (random.nextFloat() < P || neighbour.getScore() > squad.getScore())) {
                    squad = neighbour;
                    break;
                }
            }
            System.out.println(squad.getScore());
            visited.add(squad);

            if (previousSquad.equals(squad)) break;

        }
        return squad;
    }
}
