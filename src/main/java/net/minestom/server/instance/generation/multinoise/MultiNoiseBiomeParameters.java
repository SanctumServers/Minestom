package net.minestom.server.instance.generation.multinoise;

import lombok.Getter;

@Getter
public final class MultiNoiseBiomeParameters {
    private double temperature;
    private double humidity;
    private double altitude;
    private double weirdness;
    private double offset;

    public MultiNoiseBiomeParameters() {

    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getWeirdness() {
        return weirdness;
    }

    public void setWeirdness(double weirdness) {
        this.weirdness = weirdness;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
