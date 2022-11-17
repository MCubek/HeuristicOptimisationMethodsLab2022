package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public class Squad implements Iterable<Squad> {
    private final Set<Player> activePlayers;
    private final Set<Player> reservePlayers;
    private final int activePlayersCount;
    private final int reservePlayersCount;
    private final List<Player> players;

    private final int[] vectorRepresentation;

    public Squad(int activePlayers, int reservePlayers, List<Player> players) {
        this.players = players;
        this.activePlayers = new HashSet<>(activePlayers);
        this.reservePlayers = new HashSet<>(reservePlayers);
        this.activePlayersCount = activePlayers;
        this.reservePlayersCount = reservePlayers;

        this.vectorRepresentation = new int[players.size()];
    }

    Squad(int[] vectorRepresentation, List<Player> players) {
        this.players = players;
        this.activePlayers = getActivePlayersFromVector(vectorRepresentation);
        this.reservePlayers = getReservePlayersFromVector(vectorRepresentation);
        this.activePlayersCount = activePlayers.size();
        this.reservePlayersCount = reservePlayers.size();

        this.vectorRepresentation = vectorRepresentation;
    }

    private Set<Player> getActivePlayersFromVector(int[] vectorRepresentation) {
        Set<Player> activePlayersSet = new HashSet<>();

        for (int i = 0; i < vectorRepresentation.length; i++) {
            if (vectorRepresentation[i] == 1) {
                activePlayersSet.add(players.get(i));
            }
        }
        return activePlayersSet;
    }

    private Set<Player> getReservePlayersFromVector(int[] vectorRepresentation) {
        Set<Player> reservePlayersSet = new HashSet<>();

        for (int i = 0; i < vectorRepresentation.length; i++) {
            if (vectorRepresentation[i] == 2) {
                reservePlayersSet.add(players.get(i));
            }
        }
        return reservePlayersSet;
    }

    public void addActivePlayer(Player player) {
        if (activePlayers.size() < activePlayersCount) {
            this.vectorRepresentation[player.getId() - 1] = 1;
            activePlayers.add(player);
        }

    }

    public void addReservePlayer(Player player) {
        if (reservePlayers.size() < reservePlayersCount) {
            this.vectorRepresentation[player.getId() - 1] = 2;
            reservePlayers.add(player);
        }
    }

    public boolean isPlayerInSquad(Player player) {
        return activePlayers.contains(player) || reservePlayers.contains(player);
    }

    int[] getVectorRepresentation() {
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
        return new SquadNeighborhoodSingleIterator(vectorRepresentation, players);
    }

    public List<Squad> getNeighboursList() {
        List<Squad> list = new ArrayList<>();
        for (var neighbour : this) {
            list.add(neighbour);
        }
        return list;
    }

}
