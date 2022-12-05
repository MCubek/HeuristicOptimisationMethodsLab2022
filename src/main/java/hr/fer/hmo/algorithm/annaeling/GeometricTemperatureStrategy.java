package hr.fer.hmo.algorithm.annaeling;

/**
 * @author matejc
 * Created on 05.12.2022.
 */

public class GeometricTemperatureStrategy implements ITemperatureStrategy{
    private final int innerLoopCount;

    private final double initialTemperature;

    private final double minTemperature;
    private final double alpha;
    private double currentTemperature;

    public GeometricTemperatureStrategy(int innerLoopCount, double initialTemperature, double minTemperature, double alpha) {
        this.innerLoopCount = innerLoopCount;
        this.initialTemperature = initialTemperature;
        this.minTemperature = minTemperature;
        this.alpha = alpha;

        initialiseCurrentTemperature();
    }

    @Override
    public double minTemperature() {
        return minTemperature;
    }

    @Override
    public void initialiseCurrentTemperature() {
        currentTemperature = initialTemperature;
    }


    @Override
    public int innerLoopCount() {
        return innerLoopCount;
    }

    @Override
    public double getAndUpdateTemperature() {
        double returnTemperature = currentTemperature;
        currentTemperature *= alpha;
        return returnTemperature;
    }
}
