package kata7;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SqliteFlightStore implements FlightStore{
    private final File file;
    private final Connection connection;

    public SqliteFlightStore(File file) throws SQLException {
        this.file = file;
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    @Override
    public Iterable<Flight> flights() {
        return () -> {
            try {
                return createIterator();
            } catch (SQLException ex) {
                return Collections.emptyIterator();
            }
        };
    }

    private Iterator<Flight> createIterator() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM "
                + "flights");
        return new Iterator<Flight>() {
            private Flight nextFlight = nextFlightIn(rs);

            @Override
            public boolean hasNext() {
                return nextFlight != null;
            }

            @Override
            public Flight next() {
                Flight result = nextFlight;
                nextFlight = nextFlightIn(rs);
                return result;
            }
        };
    }
    
    private Flight nextFlightIn(ResultSet rs)  {
        return nextIn(rs) ? readFlightFrom(rs) : null;
    }
    
    private Flight readFlightFrom(ResultSet rs) {
        return new Flight(
                DayOfWeek.of(get(rs,"DAY_OF_WEEK")), 
                time(get(rs,"DEP_TIME")), 
                time(get(rs,"ARR_TIME")),
                get(rs,"DEP_DELAY"),
                get(rs,"ARR_DELAY"),
                get(rs,"AIR_TIME"),
                get(rs,"DISTANCE"),
                get(rs,"CANCELLED") == 1,
                get(rs,"DIVERTED") == 1
        );
    }

    private int get(ResultSet rs, String field) {
        try {
            return rs.getInt(field);
        } catch (SQLException ex) {
            return 0;
        }
    }

    private LocalTime time(int time) {
        return LocalTime.of(time / 100 % 24, time % 100);
    }

    private boolean nextIn(ResultSet rs) {
        try {
            boolean hasNext = rs.next();
            if (hasNext == false) rs.close();
            return hasNext;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    @Override
    public List<Flight> read(String filters) {
        List<Flight> flights = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * " 
                    + "FROM flights " + filters);
            while (rs.next()) {
                flights.add(readFlightFrom(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flights;
    }
}
