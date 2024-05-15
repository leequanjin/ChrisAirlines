package chrisairlines;

import java.util.HashMap;

public class Booking {

    private HashMap<String, Flight> availableFlights;

    public Booking() {
        this.availableFlights = new HashMap<>();
        availableFlights.put("F001", new Flight("F001", 200.0, "OriginA", "DestinationA", "09:00", "11:00"));
        availableFlights.put("F002", new Flight("F002", 300.0, "OriginB", "DestinationB", "10:00", "12:00"));
        availableFlights.put("F003", new Flight("F003", 400.0, "OriginC", "DestinationC", "11:00", "13:00"));
        availableFlights.put("F004", new Flight("F004", 400.0, "OriginD", "DestinationD", "12:00", "14:00"));
        availableFlights.put("F005", new Flight("F005", 400.0, "OriginE", "DestinationE", "13:00", "15:00"));
    }

    public HashMap<String, Flight> getAvailableFlights() {
        return availableFlights;
    }

    public void setAvailableFlights(HashMap<String, Flight> availableFlights) {
        this.availableFlights = availableFlights;
    }

    public Flight getFlight(String flightCode) {
        return availableFlights.get(flightCode);
    }
}
