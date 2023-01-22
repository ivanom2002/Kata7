package kata7;

import java.io.File;
import java.sql.SQLException;
import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.Set;

public class Kata7 {

    public static void main(String[] args) throws SQLException {
        port(4567);

        get("/histogram", (request, response) -> {
            String dimension = request.queryParams("dimension");
            String filters = request.queryParams("filters");
            String binSize = request.queryParams("binSize");
            if (binSize == null) binSize = "1";

            Histograma histograma = new Histograma(dimension, filters, binSize);
            for (Flight flight : histograma.read()) {
                histograma.increment(flight.get(dimension, Integer.valueOf(binSize)));
            }
            
            histograma.applyKeyFormat();
            
            Gson gson = new Gson();
            String json = gson.toJson(histograma.toString());

            response.type("application/json");
            return json;
        });
    }
}
