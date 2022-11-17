package hr.fer.hmo.algorithm;

import hr.fer.hmo.player.Player;
import hr.fer.hmo.squad.Squad;

import java.util.List;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

public interface ISearchAlgorithm {
    Squad search(List<Player> players, Squad startingSquad);
}
