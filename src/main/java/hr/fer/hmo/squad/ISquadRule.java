package hr.fer.hmo.squad;

import hr.fer.hmo.player.Player;

import java.util.Collection;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

@FunctionalInterface
public interface ISquadRule {
    boolean validate(Collection<Player> activePlayers, Collection<Player> reservePlayers);
}
