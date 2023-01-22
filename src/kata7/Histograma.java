package kata7;

import java.io.File;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

class Histograma {

    private final String dimension, filters, binSize;
    private final FlightStore flights;
    public Map<String, Integer> map = new HashMap<>();
    
    public Histograma(String dimension, String filters, String binSize) throws SQLException {
        this.flights = new SqliteFlightStore(new File("flights.db"));
        this.dimension = dimension;
        this.filters = filters;
        this.binSize = binSize;
    }
    
    public List<Flight> read() {
        return flights.read(filters());
    }
    
    public String filters() {
        if (filters == null) return filters;
        String s = "WHERE " + filters;
        s = s.replace(":", " == ");
        s = s.replace("-", " AND ");
        return s;
    }
    
    public Integer get(String key) {
        return this.map.get(key);
    } 
    
    public void increment(String key) {
        this.map.put(key, this.map.containsKey(key) ? this.map.get(key) + 1 : 1);
    }

    @Override
    public String toString() {
        String string = "Histograma{" + "dimension " + dimension + ", filters " 
                + filters + ", binSize " + binSize + ", map " + map + '}';
        string = string.replace("=", " ");
        return string;
    }

    void applyKeyFormat() {
        int value;
        String newKey;
        List<String> keys = new ArrayList<>();
        
        for (String key: map.keySet()) {
            keys.add(key);
        }
        
        for (String key : keys) {
            value = map.get(key);
            map.remove(key);
            if ("CANCELLED".equals(dimension) || "DIVERTED".equals(dimension)) {
                map.put(key == "1" ? "true" : "false", value);
            }
            
            else if ("DAY_OF_WEEK".equals(dimension)) {
                newKey = DayOfWeek.of((Integer.valueOf(key) * Integer.valueOf(binSize)) + 1).toString();
                map.put(newKey, value);
            } 
            
            else if ("DEP_TIME".equals(dimension) || "ARR_TIME".equals(dimension)) {
                newKey = LocalTime.of((Integer.valueOf(key) * Integer.valueOf(binSize)) % 24,
                        0).toString();
                map.put(newKey, value);
            } 
            
            else {
                newKey = String.valueOf(Integer.valueOf(key) * Integer.valueOf(binSize));
                map.put(newKey, value);
            }
        }
    }
}