package hr.fer.hmo.squad;

import hr.fer.hmo.player.Player;

import java.util.Set;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public interface ISquadRule {
    boolean validate(Set<Player> activePlayers, Set<Player> reservePlayers);
}
