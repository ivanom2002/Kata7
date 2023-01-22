package kata7;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Flight {
    private final DayOfWeek dayOfWeek;
    private final LocalTime departureTime;
    private final LocalTime arrivalTime;
    private final int departureDelay;
    private final int arrivalDelay;
    private final int duration;
    private final int distance;
    private final boolean cancelled;
    private final boolean diverted;

    public Flight(DayOfWeek dayOfWeek, LocalTime departureTime, LocalTime arrivalTime, 
            int departureDelay, int arrivalDelay, int duration, int distance, 
            boolean cancelled, boolean diverted) {
        this.dayOfWeek = dayOfWeek;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureDelay = departureDelay;
        this.arrivalDelay = arrivalDelay;
        this.duration = duration;
        this.distance = distance;
        this.cancelled = cancelled;
        this.diverted = diverted;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureDelay() {
        return departureDelay;
    }

    public int getArrivalDelay() {
        return arrivalDelay;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDiverted() {
        return diverted;
    }

    @Override
    public String toString() {
        return "Flight{" + "dayOfWeek=" + dayOfWeek + ", departureTime=" + 
                departureTime + ", arrivalTime=" + arrivalTime + ", departureDelay=" +
                departureDelay + ", arrivalDelay=" + arrivalDelay + ", duration=" + duration +
                ", distance=" + distance + ", cancelled=" + cancelled + ", diverted=" + 
                diverted + '}';
    }

    public String get(String dimension, int binSize) {
        
        int clave = 0;
        
        switch(dimension) {
            case "DAY_OF_WEEK":
                clave = getDayOfWeek().ordinal() / binSize;
                return String.valueOf(clave);
            case "DEP_TIME":
                clave = getDepartureTime().getHour() / binSize;
                return String.valueOf(clave);
            case "DEP_DELAY":
                clave = getDepartureDelay() / binSize;
                return String.valueOf(clave);
            case "ARR_TIME":
                clave = getArrivalTime().getHour() / binSize;
                return String.valueOf(clave);
            case "ARR_DELAY":
                clave = getArrivalDelay() / binSize;
                return String.valueOf(clave);
            case "CANCELLED":
                clave = isCancelled() ? 1 : 0;
                return String.valueOf(clave);
            case "DIVERTED":
                clave = isDiverted() ? 1 : 0;
                return String.valueOf(clave);
            case "AIR_TIME":
                clave = getDuration() / binSize;
                return String.valueOf(clave);
            case "DISTANCE":
                clave = getDistance() / binSize;
                return String.valueOf(clave);
            default:
                System.out.println("Dimensión no válida");
                break;
        }
        return String.valueOf(clave);
    }
}
