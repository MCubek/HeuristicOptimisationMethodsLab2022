package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

@Getter
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

    private Squad(int[] vectorRepresentation, List<Player> players) {
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
            if (vectorRepresentation[i] == 0) {
                reservePlayersSet.add(players.get(i));
            }
        }
        return reservePlayersSet;
    }

    public boolean addActivePlayer(Player player) {
        if (activePlayers.size() < activePlayersCount) {
            this.vectorRepresentation[player.getId()] = 1;
            return activePlayers.add(player);
        } else return false;

    }

    public boolean addReservePlayer(Player player) {
        if (reservePlayers.size() < reservePlayersCount) {
            this.vectorRepresentation[player.getId()] = 2;
            return reservePlayers.add(player);
        } else return false;
    }

    public boolean removeActivePlayer(Player player) {
        this.vectorRepresentation[player.getId()] = 0;
        return activePlayers.remove(player);
    }

    public boolean removeReservePlayer(Player player) {
        this.vectorRepresentation[player.getId()] = 0;
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

    @Override
    public Iterator<Squad> iterator() {
        return new SquadNeighborhoodIterator(vectorRepresentation, players);
    }

    public static class SquadNeighborhoodIterator implements Iterator<Squad> {

        private final int[] vectorRepresentation;
        private final List<Player> players;

        private int i;
        private int j;

        private int visitedNumber = 0;

        public SquadNeighborhoodIterator(int[] vectorRepresentation, List<Player> players) {
            this.vectorRepresentation = vectorRepresentation;
            this.players = players;

            i = 0;
            j = vectorRepresentation.length;
        }

        @Override
        public boolean hasNext() {
            return visitedNumber != 15 && j < vectorRepresentation.length;
        }

        @Override
        public Squad next() {
            if (! hasNext()) throw new NoSuchElementException();

            if (j >= vectorRepresentation.length) {
                while (vectorRepresentation[i] == 0) {
                    i++;
                }
                visitedNumber++;
                j = 0;
            }

            try {
                while (i == j || vectorRepresentation[j] != 0) {
                    j++;
                }
            } catch (IndexOutOfBoundsException e) {
                return new Squad(vectorRepresentation, players);
            }

            int[] newVector = Arrays.copyOf(vectorRepresentation, vectorRepresentation.length);
            newVector[j] = vectorRepresentation[i];
            newVector[i] = 0;

            j++;
            return new Squad(newVector, players);
        }
    }

    public static Squad generateRandomSquad(List<Player> players, Random random, int activePlayersCount, int reservePlayersCount) {
        Squad squad = new Squad(activePlayersCount, reservePlayersCount, players);

        var pickedPlayers = random.ints(0, players.size() - 1)
                .distinct()
                .limit((long) activePlayersCount + reservePlayersCount)
                .mapToObj(players::get)
                .toList();

        var activePlayers = random.ints(0, pickedPlayers.size() - 1)
                .distinct()
                .limit(activePlayersCount)
                .mapToObj(pickedPlayers::get)
                .toList();

        var reservePlayers = new ArrayList<>(pickedPlayers);
        reservePlayers.removeAll(activePlayers);


        activePlayers.forEach(squad::addActivePlayer);
        reservePlayers.forEach(squad::addReservePlayer);

        return squad;
    }
}
