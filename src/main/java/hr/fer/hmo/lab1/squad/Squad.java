package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

@Getter
public class Squad {
    private final Set<Player> activePlayers;
    private final Set<Player> reservePlayers;
    private final int activePlayersCount;
    private final int reservePlayersCount;

    public Squad(int activePlayers, int reservePlayers) {
        this.activePlayers = new HashSet<>(activePlayers);
        this.reservePlayers = new HashSet<>(reservePlayers);
        this.activePlayersCount = activePlayers;
        this.reservePlayersCount = reservePlayers;
    }

    public boolean addActivePlayer(Player player) {
        if (activePlayers.size() < activePlayersCount)
            return activePlayers.add(player);
        else return false;

    }

    public boolean addReservePlayer(Player player) {
        if (reservePlayers.size() < reservePlayersCount)
            return reservePlayers.add(player);
        else return false;
    }

    public boolean removeActivePlayer(Player player) {
        return activePlayers.remove(player);
    }

    public boolean removeReservePlayer(Player player) {
        return reservePlayers.remove(player);
    }

    public double getCost() {
        return Stream.concat(activePlayers.stream(), reservePlayers.stream())
                .mapToDouble(Player::getPrice)
                .sum();
    }

    public int getScore() {
        return activePlayers.stream()
                .mapToInt(Player::getPoints)
                .sum();
    }

    public boolean checkRule(ISquadRule rule) {
        return rule.validate(activePlayers, reservePlayers);
    }

    @Override
    public String toString() {
        String active = activePlayers.stream()
                .map(Player::getId)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        String reserve = reservePlayers.stream()
                .map(Player::getId)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        return "%s%n%s%n".formatted(active, reserve);
    }
}
