package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.Squad;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 26.10.2022.
 */

@RequiredArgsConstructor
public class GreedyConstructionAlgorithm implements ISearchAlgorithm {
    private final double alpha;
    private final Random random;

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        return null;
    }
}
