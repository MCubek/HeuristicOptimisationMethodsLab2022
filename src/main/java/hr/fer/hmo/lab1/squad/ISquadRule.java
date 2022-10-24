package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;

import java.util.Set;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public interface ISquadRule {
    boolean validate(Set<Player> activePlayers, Set<Player> reservePlayers);
}
