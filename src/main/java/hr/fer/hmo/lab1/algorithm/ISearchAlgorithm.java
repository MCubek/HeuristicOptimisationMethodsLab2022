package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.squad.Squad;

import java.util.List;

/**
 * @author matejc
 * Created on 25.10.2022.
 */

public interface ISearchAlgorithm {
    Squad search(List<Player> players, Squad startingSquad);
}
