package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.Squad;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

@RequiredArgsConstructor
public class GraspSearchAlgorithm implements ISearchAlgorithm {
    private final ISearchAlgorithm localSearchAlgorithm;
    private final ISearchAlgorithm constructionAlgorithm;
    private final int MAX_ITERATIONS;
    private final Random random;

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {

        Squad bestSquad = null;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Squad constructedSquad = constructionAlgorithm.search(players, null);
            Squad localOptimumSquad = localSearchAlgorithm.search(players, constructedSquad);

            if (bestSquad == null || localOptimumSquad.getScore() > bestSquad.getScore()) {
                bestSquad = localOptimumSquad;
            }
        }
        return bestSquad;
    }
}
