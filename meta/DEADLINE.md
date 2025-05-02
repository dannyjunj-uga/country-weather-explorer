# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

Country & Weather Explorer is a JavaFX application that allows users to select
any country from a scrollable list and view both general information and live
weather conditions for that country.

Upon selecting a country and clicking "Get Info," the app displays:
- The capital city, population, currency, and flag of the selected country.
- The current temperature, humidity, wind speed, weather condition, and icon
  for the capital's location.

API Integration:
This app uses two RESTful JSON APIs in a meaningful way:

1. **REST Countries API** is used to fetch country-specific information,
   including the capital city and geographic coordinates.
2. **OpenWeatherMap One Call API** uses the latitude and longitude of the
   selected country's capital to retrieve current weather data.

**GitHub Repository:**
https://github.com/dannyjunj-uga/cs1302-api-app

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://restcountries.com/v3.1/name/Germany?fullText=true
```

> Returns a detailed JSON object containing country metadata, including capital,
> population, flag URL, and lat/lng. No API key required.

### API 2

```
https://api.openweathermap.org/data/3.0/onecall?lat=52.52&lon=13.405&exclude=minutely,hourly,daily,alerts&appid=f215fe56b58b9e106899d6a4bd02acdd
```

> Requires a subscription-based API key that charges a small fee for any call
> over the free limit of 1000 calls per day ('f215fe56b58b9e106899d6a4bd02acdd').
> Lat/Lon is retrieved from REST Countries API. Response includes current
> temperature, feels-like, humidity, wind, and weather icon.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

One exciting thing I learned during this project was how to chain together two APIs
where the second depends on data returned by the first. I also improved my
understanding of JavaFX layout management, especially VBox/HBox alignment and
dynamic UI updates with threads.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I could start over, I would plan the visual layout and component hierarchy more
carefully from the beginning. Initially I placed all UI code in the `start` method,
which made it messy. Breaking it into smaller helper methods significantly improved
organization and readability, although I think creating a custom component would have
been more intuitive.
