package cs1302.api;

import java.util.Map;

/**
 * Represents a country as returned by the REST Countries API.
 * Contains metadata including the country's name, capital, population,
 * currencies, geographic coordinates, and flag information.
 */
public class Country {
    public Name name;
    public String[] capital;
    public long population;
    public Map<String, Object> currencies;
    public double[] latlng;
    public Flags flags;
}
