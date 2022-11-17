package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author matejc
 * Created on 26.10.2022.
 */
public class SquadNeighborhoodSingleIterator implements Iterator<Squad> {

    private final int[] vectorRepresentation;
    private final List<Player> players;

    private int i;
    private int j;

    private int visitedNumber = 0;

    public SquadNeighborhoodSingleIterator(int[] vectorRepresentation, List<Player> players) {
        this.vectorRepresentation = vectorRepresentation;
        this.players = players;

        i = 0;
        j = vectorRepresentation.length;
    }

    @Override
    public boolean hasNext() {
        return visitedNumber != 15 || j < vectorRepresentation.length;
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
