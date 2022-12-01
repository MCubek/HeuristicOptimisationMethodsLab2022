package hr.fer.hmo.algorithm.tabu;

import hr.fer.hmo.algorithm.ISearchAlgorithm;
import hr.fer.hmo.player.Player;
import hr.fer.hmo.squad.ISquadRule;
import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.squad.SquadGenerator;
import hr.fer.hmo.squad.SquadRules;

import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.11.2022.
 */

public class TabuSearchAlgorithm implements ISearchAlgorithm {
    private final ITabuList tabuList;
    private final int maxIterations;

    private final ISquadRule rule = SquadRules.allRules;

    public TabuSearchAlgorithm(ITabuList tabuList, int maxIterations) {
        this.tabuList = tabuList;
        this.maxIterations = maxIterations;

    }

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        if (startingSquad == null)
            startingSquad = SquadGenerator.generateRandomValidSquad(players, new Random(), 11, 4);

        Squad incumbent = startingSquad;
        Squad current = startingSquad;

        for (int i = 0; i < maxIterations; i++) {
            Squad bestNeighbour = null;
            for (var neighbour : current) {
                // Starting clause
                if (bestNeighbour == null) {
                    if (neighbour.checkRule(rule) && tabuList.isAllowed(neighbour))
                        bestNeighbour = neighbour;
                    continue;
                }

                if (neighbour.getScore() > bestNeighbour.getScore()
                    && neighbour.checkRule(rule)
                    && tabuList.isAllowed(neighbour)) {
                    bestNeighbour = neighbour;
                }
            }
            if (bestNeighbour == null) break;

            tabuList.addEntry(current, bestNeighbour);
            current = bestNeighbour;

            System.out.printf("Iteration %d, score :%d%n", i, current.getScore());
            if (current.getScore() > incumbent.getScore()) {
                incumbent = current;
            }
        }
        return incumbent;
    }
}
