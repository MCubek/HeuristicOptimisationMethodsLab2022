package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.ISquadRule;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.squad.SquadGenerator;
import hr.fer.hmo.lab1.squad.SquadRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.10.2022.
 */


public record LocalSearchAlgorithm(int MAX_ITERATIONS, Random random) implements ISearchAlgorithm {

    public static final ISquadRule rule = SquadRules.allRules;


    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        Squad squad = startingSquad;

        if (squad == null) squad = SquadGenerator.generateRandomValidSquad(players, random, 11, 4);

        for (int i = 0; i < MAX_ITERATIONS; i++) {

            List<Squad> neighborhood = new ArrayList<>();
            for (var neighbour : squad) {
                if (neighbour.checkRule(rule) &&
                    (neighbour.getScore() > squad.getScore())) {
                    neighborhood.add(neighbour);
                }
            }

            if (neighborhood.isEmpty()) break;

            squad = neighborhood.get(random.nextInt(neighborhood.size()));

        }
        return squad;
    }
}
