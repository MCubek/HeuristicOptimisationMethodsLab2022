package hr.fer.hmo.algorithm.annaeling;

/**
 * @author matejc
 * Created on 05.12.2022.
 */

public interface ITemperatureStrategy {
    void initialiseCurrentTemperature();

    double getAndUpdateTemperature();

    int innerLoopCount();

    double minTemperature();
}
