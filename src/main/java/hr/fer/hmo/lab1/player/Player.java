package hr.fer.hmo.lab1.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Player {
    private int id;
    private PlayerPosition position;
    private String name;
    private String club;
    private int points;
    private double price;
}
