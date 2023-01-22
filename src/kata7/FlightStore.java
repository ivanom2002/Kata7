package kata7;

import java.util.List;

interface FlightStore {
    Iterable<Flight> flights();
    public List<Flight> read(String filters);
}
