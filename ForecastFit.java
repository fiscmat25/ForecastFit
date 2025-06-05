import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ForecastFit {

    private static final String WEATHER_API_KEY = "fl9QNs82oVelaHd0lgmX3AiFWWWiiuCs";
    private static final String GEOCODING_API_KEY = "8c069a4b476d47f5a6fd99bf3f21b852";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi there! I can help you choose an outfit based on the weather.");
        System.out.print("Would you like your outfit recommendation for today? (yes/no): ");

        String userInput = scanner.nextLine().trim().toLowerCase();
        if (userInput.equals("yes")) {
            System.out.print("Enter your city and state or address (e.g., 'Issaquah, WA'): ");
            String locationInput = scanner.nextLine().trim();

            String coordinates = getCoordinatesFromLocation(locationInput);
            if (coordinates == null) {
                System.out.println("Unable to convert location to coordinates.");
                return;
            }

            String forecastJson = getWeatherForecast(coordinates);
            if (forecastJson != null) {
                generateOutfitRecommendation(forecastJson);
            } else {
                System.out.println("Sorry, I couldn't fetch the weather forecast.");
            }
        } else {
            System.out.println("Okay, come back anytime you need fashion advice!");
        }
        scanner.close();
    }

    private static String getCoordinatesFromLocation(String location) {
        try {
            String encodedLocation = URLEncoder.encode(location, "UTF-8");
            String geocodeUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + encodedLocation + "&key=" + GEOCODING_API_KEY;

            URL url = new URL(geocodeUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonArray results = json.getAsJsonArray("results");
            if (results.size() == 0) return null;

            JsonObject geometry = results.get(0).getAsJsonObject().getAsJsonObject("geometry");
            double lat = geometry.get("lat").getAsDouble();
            double lng = geometry.get("lng").getAsDouble();

            return lat + "," + lng;
        } catch (Exception e) {
            System.out.println("Geocoding error:");
            e.printStackTrace();
            return null;
        }
    }

    private static String getWeatherForecast(String location) {
        try {
            String fullUrl = "https://api.tomorrow.io/v4/weather/forecast?location=" + location + "&timesteps=1d&units=imperial&apikey=" + WEATHER_API_KEY;

            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            System.out.println("Error fetching weather:");
            e.printStackTrace();
            return null;
        }
    }

    private static void generateOutfitRecommendation(String json) {
        JsonObject forecastData = JsonParser.parseString(json).getAsJsonObject();
        JsonArray daily = forecastData.getAsJsonObject("timelines").getAsJsonArray("daily");
        JsonObject today = daily.get(0).getAsJsonObject().getAsJsonObject("values");

        double temperatureAvg = today.get("temperatureAvg").getAsDouble();
        double temperatureMax = today.get("temperatureMax").getAsDouble();

        System.out.println("\nWeather Summary:");
        System.out.println("Average Temperature: " + temperatureAvg + "°F");
        System.out.println("High Temperature: " + temperatureMax + "°F");

        System.out.print("Outfit Recommendation: ");
        if (temperatureMax < 32) {
            System.out.println("Bundle up! Wear thermal layers, a heavy coat, gloves, and a hat.");
        } else if (temperatureMax < 50) {
            System.out.println("It's chilly. Wear a jacket and maybe a scarf.");
        } else if (temperatureMax < 70) {
            System.out.println("Mild weather. A hoodie or light sweater works well.");
        } else if (temperatureMax < 85) {
            System.out.println("Warm day! T-shirts and shorts should be fine.");
        } else {
            System.out.println("Hot! Wear breathable fabrics and stay hydrated.");
        }
    }
}
