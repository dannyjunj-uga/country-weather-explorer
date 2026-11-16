# Country & Weather Explorer

A JavaFX desktop application that combines country information with real-time weather data to provide users with an interactive way to explore countries around the world.

## Overview

**Country & Weather Explorer** allows users to select a country from a scrollable list and retrieve detailed information about that country along with current weather conditions in its capital city.

The application demonstrates how multiple RESTful APIs can be connected together in a meaningful way. The **REST Countries API** provides information about the selected country, including its capital and geographic coordinates. Those coordinates are then used to query the **OpenWeatherMap API**, allowing the application to retrieve current weather conditions for the country's capital.

This API-to-API connection allows information from one service to directly determine what data is requested from another service.

## Features

- 🌎 Browse and select countries from a scrollable country list
- 🏛️ Display the selected country's capital city
- 👥 Display population information
- 💰 Display the country's currency
- 🏳️ Display the country's flag
- 🌡️ Display current temperature and "feels like" temperature
- 💧 Display current humidity
- 💨 Display wind speed
- ☁️ Display current weather conditions
- 🌤️ Display a corresponding weather icon
- ⏳ Loading spinner while API requests are processed
- 🎨 Modern dark-themed JavaFX interface
- 📱 Organized information panels for country and weather data

## API Integration

The application uses two RESTful JSON APIs.

### REST Countries API

The REST Countries API provides information about the selected country.

Example request:

```text
https://restcountries.com/v3.1/name/United%20States?fullText=true
```

The application uses information from the response such as:

- Country name
- Capital city
- Population
- Currency
- Geographic coordinates
- Flag image URL

### OpenWeatherMap API

The OpenWeatherMap API provides current weather information based on the geographic coordinates obtained from the REST Countries API.

Example request:

```text
https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=minutely,hourly,daily,alerts&appid=YOUR_API_KEY
```

The application uses the response to display:

- Temperature
- Feels-like temperature
- Humidity
- Wind speed
- Weather condition
- Weather icon

### How the APIs Work Together

The two APIs are connected through the country's geographic information:

```text
User selects country
        ↓
REST Countries API
        ↓
Country information + latitude/longitude
        ↓
OpenWeatherMap API
        ↓
Current weather for that location
        ↓
JavaFX interface
```

For example, when a user selects a country, the application first retrieves that country's latitude and longitude from the REST Countries API. Those coordinates are then passed into the OpenWeatherMap request to retrieve weather information for the corresponding location.

## Technologies

- **Java**
- **JavaFX**
- **Maven**
- **Gson**
- **Java HTTP Client**
- **RESTful APIs**
- **JSON**
- **Git/GitHub**

## Architecture

The application uses separate Java classes to represent and parse JSON responses from the APIs. Gson converts the JSON responses into Java objects, allowing the application to access API data through standard Java fields and objects.

API requests are performed on background threads so that network operations do not block the JavaFX application thread. Once the responses are processed, `Platform.runLater()` is used to safely update the JavaFX user interface.

## User Interface

The application uses a dark-themed interface with separate information panels for country and weather data. The country selection area allows users to browse available countries, while the information panels dynamically update after a successful API request.

A loading spinner provides visual feedback while information is being retrieved from the APIs.

## Project Goals

This project was developed to demonstrate practical skills in:

- REST API integration
- JSON parsing
- Object-oriented programming
- JavaFX application development
- Multithreaded programming
- UI/UX design
- Error handling
- Working with external data sources
- Connecting multiple APIs to create a meaningful application

## Future Improvements

Potential future improvements include:

- Adding multi-day weather forecasts
- Displaying additional geographic information
- Adding sunrise and sunset times
- Supporting multiple weather units
- Adding search and filtering capabilities
- Displaying weather alerts when available
- Adding interactive maps
- Improving accessibility and responsive layout behavior

## Author

**Daniel Jungwirth**

Computer Science Student  
University of Georgia
