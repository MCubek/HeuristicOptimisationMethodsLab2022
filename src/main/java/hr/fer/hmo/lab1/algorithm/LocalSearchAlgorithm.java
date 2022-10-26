package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.ISquadRule;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.squad.SquadGenerator;
import hr.fer.hmo.lab1.squad.SquadRules;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

@RequiredArgsConstructor
public class LocalSearchAlgorithm implements ISearchAlgorithm {

    public static final double P = 0.01;
    public final int MAX_ITERATIONS;
    public final Random random;

    public static final ISquadRule rule = SquadRules.allRules;


    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        Squad squad = startingSquad;

        if (squad == null) squad = SquadGenerator.generateRandomValidSquad(players, random, 11, 4);

        for (int i = 0; i < MAX_ITERATIONS; i++) {

            List<Squad> neighborhood = new ArrayList<>();
            for (var neighbour : squad) {
                if (neighbour.checkRule(rule) &&
                    (random.nextFloat() < P || neighbour.getScore() > squad.getScore())) {
                    neighborhood.add(neighbour);
                }
            }
            System.out.println(squad.getScore());

            if (neighborhood.size() > 1) break;

            squad = neighborhood.get(random.nextInt(0, neighborhood.size() - 1));

        }
        return squad;
    }
}
