package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.Squad;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

@RequiredArgsConstructor
public class GraspSearchAlgorithm implements ISearchAlgorithm {
    private final ISearchAlgorithm localSearchAlgorithm;

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {
        return null;
    }
}
