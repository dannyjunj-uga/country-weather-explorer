package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the current weather data object in the OpenWeatherMap One Call API response.
 * Includes temperature, feels-like temperature, humidity, wind speed, and weather conditions.
 */
public class Current {
    public float temp;
    @SerializedName("feels_like")
    public float feelsLike;
    public int humidity;
    @SerializedName("wind_speed")
    public float windSpeed;
    public Weather[] weather;
}
