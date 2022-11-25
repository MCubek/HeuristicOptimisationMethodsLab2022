package hr.fer.hmo.algorithm.tabu;

import hr.fer.hmo.squad.Squad;

/**
 * @author matejc
 * Created on 25.11.2022.
 */

public interface ITabuList {
    boolean isAllowed(Squad squad);

    void addEntry(Squad previous, Squad next);
}
