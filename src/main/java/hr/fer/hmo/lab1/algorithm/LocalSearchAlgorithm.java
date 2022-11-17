package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.ISquadRule;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.squad.SquadGenerator;
import hr.fer.hmo.lab1.squad.SquadRules;

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

            var squadScore = squad.getScore();

            List<Squad> betterNeighbours = squad.getNeighboursList()
                    .parallelStream()
                    .flatMap(neighbour -> neighbour.getNeighboursList().parallelStream())
                    .filter(neighbour -> neighbour.getScore() > squadScore && neighbour.checkRule(rule))
                    .toList();

            if (betterNeighbours.isEmpty()) break;

            squad = betterNeighbours.get(random.nextInt(betterNeighbours.size()));
            System.out.printf("Local Search iter %d, score %d.%n", i + 1, squad.getScore());

        }
        return squad;
    }
}
