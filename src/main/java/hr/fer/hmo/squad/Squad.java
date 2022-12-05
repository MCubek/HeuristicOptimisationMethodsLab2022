package hr.fer.hmo.squad;

import hr.fer.hmo.player.Player;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public class Squad implements Iterable<Squad> {
    private final Collection<Player> activePlayers;
    private final Collection<Player> reservePlayers;

    private final List<Player> players;

    private final int[] vectorRepresentation;

    public Squad(List<Player> players) {
        this.players = players;
        this.activePlayers = new LinkedList<>();
        this.reservePlayers = new LinkedList<>();

        this.vectorRepresentation = new int[players.size()];
    }

    Squad(int[] vectorRepresentation, List<Player> players) {
        this.players = players;
        this.activePlayers = getActivePlayersFromVector(vectorRepresentation);
        this.reservePlayers = getReservePlayersFromVector(vectorRepresentation);

        this.vectorRepresentation = vectorRepresentation;
    }

    private Collection<Player> getActivePlayersFromVector(int[] vectorRepresentation) {
        Collection<Player> activePlayersSet = new LinkedList<>();

        for (int i = 0; i < vectorRepresentation.length; i++) {
            if (vectorRepresentation[i] == 1) {
                activePlayersSet.add(players.get(i));
            }
        }
        return activePlayersSet;
    }

    private Collection<Player> getReservePlayersFromVector(int[] vectorRepresentation) {
        Collection<Player> reservePlayersSet = new LinkedList<>();

        for (int i = 0; i < vectorRepresentation.length; i++) {
            if (vectorRepresentation[i] == 2) {
                reservePlayersSet.add(players.get(i));
            }
        }
        return reservePlayersSet;
    }

    public void addActivePlayer(Player player) {
        this.vectorRepresentation[player.getId() - 1] = 1;
        activePlayers.add(player);
    }

    public void addReservePlayer(Player player) {
        this.vectorRepresentation[player.getId() - 1] = 2;
        reservePlayers.add(player);
    }

    public boolean isPlayerInSquad(Player player) {
        return activePlayers.contains(player) || reservePlayers.contains(player);
    }

    public int[] getVectorRepresentation() {
        return vectorRepresentation;
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

    public Map<String, Long> clubCounts() {
        return Stream.concat(activePlayers.stream(), reservePlayers.stream())
                .collect(Collectors.groupingBy(Player::getClub, Collectors.counting()));
    }

    @Override
    public String toString() {
        String active = activePlayers.stream()
                .map(Player::getId)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String reserve = reservePlayers.stream()
                .map(Player::getId)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return "%s%n%s%n".formatted(active, reserve);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Squad squad = (Squad) o;

        return Arrays.equals(vectorRepresentation, squad.vectorRepresentation);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vectorRepresentation);
    }

    @Override
    public Iterator<Squad> iterator() {
        return new SquadNeighborhoodIterator(vectorRepresentation, players);
    }

    public List<Squad> getNeighboursListWithPredicate(Predicate<Squad> predicate) {
        List<Squad> list = new ArrayList<>();
        for (var neighbour : this) {
            if (predicate.test(neighbour))
                list.add(neighbour);
        }
        return list;
    }
}
