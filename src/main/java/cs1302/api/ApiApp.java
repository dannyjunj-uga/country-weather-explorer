package cs1302.api;

import javafx.scene.Node;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A JavaFX application that allows users to select a country and view
 * its basic information and current weather conditions. The app fetches
 * data from the REST Countries API and the OpenWeatherMap API, and displays
 * it in a clean, structured GUI with country and weather details.
 */
public class ApiApp extends Application {

    private static final HttpClient HTTPCLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private static final String WEATHER_API_KEY = "f215fe56b58b9e106899d6a4bd02acdd";

    private ListView<String> countryList;
    private ObservableList<String> countryNames = FXCollections.observableArrayList();
    private Label capitalLabel, populationLabel, currencyLabel, tempLabel, feelsLikeLabel,
        humidityLabel, windLabel, conditionLabel, weatherHeader, countryHeader;
    private ImageView flagImageView, weatherIcon;
    private ProgressIndicator loadingSpinner;

    /**
     * Initializes the JavaFX application. Builds and arranges all UI components including
     * the country selector, loading spinner, and information panels. Sets up the stage
     * and scene, and triggers loading of country data.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene is set.
     */
    @Override
    public void start(Stage stage) {
        Label title = new Label("🌍 Country & Weather Explorer 🌍");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox root = new VBox(15, title, createControls(),
            new HBox(20, createCountryBox(), createWeatherBox()));
        root.setStyle("-fx-padding: 20px; -fx-background-color: #1e1e1e;");
        root.setAlignment(Pos.TOP_CENTER);

        setLabelTextWhite(
            capitalLabel, populationLabel, currencyLabel,
            tempLabel, feelsLikeLabel, humidityLabel, windLabel, conditionLabel
        );

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Country & Weather Explorer");
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();

        loadCountryNames();
    }

    /**
     * Creates and returns a VBox containing the country selection ListView,
     * a fetch button, and a loading spinner. This component handles user interaction
     * for selecting a country and requesting information.
     *
     * @return a VBox containing the country selection UI and controls
     */
    private Node createControls() {
        countryList = new ListView<>(countryNames);
        countryList.setPrefHeight(200);
        countryList.setPrefWidth(250);

        Button fetchButton = new Button("Get Info");
        fetchButton.setOnAction(e -> {
            String selected = countryList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                fetchAndDisplay(selected);
            }
        });

        loadingSpinner = new ProgressIndicator();
        loadingSpinner.setVisible(false);
        loadingSpinner.setPrefSize(50, 50);

        HBox spinnerBox = new HBox(loadingSpinner);
        spinnerBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(10, countryList, fetchButton, spinnerBox);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Creates and returns a VBox containing the country information UI elements,
     * including labels for capital, population, currency, and a flag image.
     * The header label is stored for dynamic updates.
     *
     * @return a VBox displaying country information
     */
    private Node createCountryBox() {
        countryHeader = new Label("Country Info");
        countryHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        countryHeader.setWrapText(true);

        Region spacer = new Region();
        spacer.setMinHeight(15);

        capitalLabel = new Label("Capital: ");
        populationLabel = new Label("Population: ");
        currencyLabel = new Label("Currency: ");
        flagImageView = new ImageView();
        flagImageView.setFitWidth(150);
        flagImageView.setFitHeight(100);
        flagImageView.setPreserveRatio(true);

        HBox flagWrapper = new HBox(flagImageView);
        flagWrapper.setAlignment(Pos.CENTER);

        VBox box = new VBox(5, countryHeader, capitalLabel,
            populationLabel, currencyLabel, spacer, flagWrapper);
        box.setStyle("-fx-padding: 10; -fx-background-color: #2c2c2c; -fx-border-color: #444;");
        box.setAlignment(Pos.TOP_LEFT);
        box.setPrefSize(250, 300);
        return box;
    }

    /**
     * Creates and returns a VBox containing the weather information UI elements,
     * including labels for temperature, humidity, wind, and condition, as well as
     * an icon image. The header label is stored for dynamic updates if needed.
     *
     * @return a VBox displaying current weather conditions
     */
    private Node createWeatherBox() {
        weatherHeader = new Label("Weather Info");
        weatherHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        weatherHeader.setWrapText(true);

        tempLabel = new Label("Temp: ");
        feelsLikeLabel = new Label("Feels Like: ");
        humidityLabel = new Label("Humidity: ");
        windLabel = new Label("Wind: ");
        conditionLabel = new Label("Condition: ");
        weatherIcon = new ImageView();
        weatherIcon.setFitWidth(100);
        weatherIcon.setFitHeight(100);
        weatherIcon.setPreserveRatio(true);

        HBox weatherIconWrapper = new HBox(weatherIcon);
        weatherIconWrapper.setAlignment(Pos.CENTER);

        VBox box = new VBox(5, weatherHeader, tempLabel, feelsLikeLabel, humidityLabel, windLabel,
            conditionLabel, weatherIconWrapper);
        box.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-border-color: #555;");
        box.setAlignment(Pos.TOP_LEFT);
        box.setPrefSize(250, 300);
        return box;
    }

    /**
     * Applies a white text color style to all provided labels.
     * This is used to ensure text remains visible on a dark background.
     *
     * @param labels the labels to apply the white text style to.
     */
    private void setLabelTextWhite(Label... labels) {
        for (Label label : labels) {
            label.setStyle("-fx-text-fill: white;");
        }
    }

    /**
     * Loads the list of countries by sending a request to the REST Countries API.
     * Populates the ListView with country names that have capital cities.
     * Runs on a background thread and updates the UI on the JavaFX Application Thread.
     */
    private void loadCountryNames() {
        new Thread(() -> {
            try {
                String url = "https://restcountries.com/v3.1/all";
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
                HttpResponse<String> response = HTTPCLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());
                Country[] countries = GSON.fromJson(response.body(), Country[].class);

                Platform.runLater(() -> {
                    for (Country country : countries) {
                        if (country.capital != null && country.capital.length > 0) {
                            countryNames.add(country.name.common);
                        }
                    }
                    countryNames.sort(String::compareToIgnoreCase);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Fetches country and weather information for the selected country. Uses the
     * REST Countries API to obtain metadata such as capital, population, currency,
     * and flag. Then queries the OpenWeatherMap API using the country’s latitude
     * and longitude to get current weather conditions.
     *
     * Updates the user interface with the retrieved information and handles errors gracefully.
     *
     * @param countryName the name of the country selected by the user.
     */
    private void fetchAndDisplay(String countryName) {
        Platform.runLater(() -> loadingSpinner.setVisible(true));
        new Thread(() -> {
            try {
                String encodedName = URLEncoder.encode(countryName,
                    StandardCharsets.UTF_8).replace("+", "%20");
                String url = "https://restcountries.com/v3.1/name/" +
                    encodedName + "?fullText=true";
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = HTTPCLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new IOException("Failed to fetch country: HTTP" + response.statusCode());
                }
                Country[] countries = GSON.fromJson(response.body(), Country[].class);
                Country country = countries[0];
                String capital = country.capital[0];
                String flagUrl = country.flags.png;
                String currency = country.currencies.keySet().iterator().next();
                double lat = country.latlng[0];
                double lon = country.latlng[1];
                String weatherUrl = String.format(
                    "https://api.openweathermap.org/data/3.0/onecall?" +
                    "lat=%.4f&lon=%.4f&exclude=minutely,hourly,daily,alerts&appid=%s",
                    lat, lon, WEATHER_API_KEY);
                HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .build();
                HttpResponse<String> weatherResponse = HTTPCLIENT.send(weatherRequest,
                    HttpResponse.BodyHandlers.ofString());
                WeatherOneCallResponse weather = GSON.fromJson(weatherResponse.body(),
                    WeatherOneCallResponse.class);
                Platform.runLater(() -> {
                    countryHeader.setText(countryName);
                    capitalLabel.setText("Capital: " + capital);
                    populationLabel.setText("Population: " + country.population);
                    currencyLabel.setText("Currency: " + currency + "\n");
                    flagImageView.setImage(new Image(flagUrl));
                    int tempC = Math.round((weather.current.temp - 273.15f));
                    int feelsC = Math.round((weather.current.feelsLike - 273.15f));
                    weatherHeader.setText(capital + "'s Current Weather");
                    tempLabel.setText("Temp: " + tempC + "°C");
                    feelsLikeLabel.setText("Feels Like: " + feelsC + "°C");
                    humidityLabel.setText("Humidity: " + weather.current.humidity + "%");
                    windLabel.setText("Wind: " + weather.current.windSpeed + " m/s");
                    conditionLabel.setText("Condition: " + weather.current.weather[0].description);
                    weatherIcon.setImage(new Image("https://openweathermap.org/img/wn/" +
                        weather.current.weather[0].icon + "@2x.png"));
                    loadingSpinner.setVisible(false);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loadingSpinner.setVisible(false);
                    showError("Could not load data.");
                });
            }
        }).start();
    }

    /**
     * Displays an error alert dialog with the given message. Used to inform the
     * user of issues such as failed network requests or missing data.
     *
     * @param message the error message to display in the alert dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
