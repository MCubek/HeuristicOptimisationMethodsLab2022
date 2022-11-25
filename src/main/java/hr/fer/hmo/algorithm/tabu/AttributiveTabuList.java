package hr.fer.hmo.algorithm.tabu;

import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.squad.SquadPlayerChangeElement;

import java.util.*;

/**
 * @author matejc
 * Created on 25.11.2022.
 */

public class AttributiveTabuList implements ITabuList {

    private final int tabuTenure;
    private final Deque<List<SquadPlayerChangeElement>> tabuList;

    public AttributiveTabuList(int tabuTenure) {
        this.tabuTenure = tabuTenure;

        this.tabuList = new LinkedList<>();
    }

    @Override
    public boolean isAllowed(Squad squad) {
        var changes = tabuList.stream()
                .flatMap(Collection::stream)
                .toList();

        return ! changes.stream()
                .map(change -> {
                    var vector = squad.getVectorRepresentation();
                    var value = vector[change.index()];

                    return value == change.previousPosition();
                })
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    @Override
    public void addEntry(Squad previous, Squad next) {
        if (tabuList.size() == tabuTenure) {
            tabuList.removeLast();
        }


        tabuList.push(getChangedIndexes(previous, next));
    }

    private List<SquadPlayerChangeElement> getChangedIndexes(Squad previous, Squad next) {
        List<SquadPlayerChangeElement> changes = new ArrayList<>();

        var previousVector = previous.getVectorRepresentation();
        var currentVector = next.getVectorRepresentation();

        for (int i = 0; i < previousVector.length; i++) {
            int previousValue = previousVector[i];
            int currentValue = currentVector[i];

            if (previousValue != currentValue)
                changes.add(new SquadPlayerChangeElement(i, previousValue, currentValue));
        }

        return changes;
    }
}
