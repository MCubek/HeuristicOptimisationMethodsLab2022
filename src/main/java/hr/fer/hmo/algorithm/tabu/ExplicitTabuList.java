package hr.fer.hmo.algorithm.tabu;

import hr.fer.hmo.squad.Squad;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author matejc
 * Created on 25.11.2022.
 */

public class ExplicitTabuList implements ITabuList {

    private final int tabuTenure;
    private final Deque<Squad> tabuList;

    public ExplicitTabuList(int tabuTenure) {
        this.tabuTenure = tabuTenure;

        this.tabuList = new LinkedList<>();
    }

    @Override
    public boolean isAllowed(Squad squad) {
        for (var element : tabuList) {
            if (element.equals(squad)) return false;
        }
        return true;
    }

    @Override
    public void addEntry(Squad previous, Squad next) {
        if (tabuList.size() == tabuTenure) tabuList.removeLast();

        tabuList.push(next);
    }
}
