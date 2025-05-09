//this is a program
// Required libraries: Gson (https://github.com/google/gson)
// You can add Gson to your classpath or use Maven/Gradle for dependency management

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ForecastFit {

    private static final String API_KEY = "fl9QNs82oVelaHd0lgmX3AiFWWWiiuCs";
    private static final String BASE_URL = "https://api.tomorrow.io/v4/weather/forecast?location=42.3478,-71.0466&timesteps=1d&units=imperial&apikey=";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi there! I can help you choose an outfit based on the weather.");
        System.out.print("Would you like your outfit recommendation for today? (yes/no): ");

        String userInput = scanner.nextLine().trim().toLowerCase();
        if (userInput.equals("yes")) {
            String forecastJson = getWeatherForecast();
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

    private static String getWeatherForecast() {
        try {
            URL url = new URL(BASE_URL + API_KEY);
            System.out.println("Using API URL: " + BASE_URL + API_KEY);
            System.out.println("Fetching weather data...");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("✅ Raw JSON response:");
            System.out.println(content.toString());

            return response.toString();
        } catch (Exception e) {
            System.out.println("❌ Error fetching weather:");
            e.printStackTrace();
            return null;
        }
    }

    private static void generateOutfitRecommendation(String json) {
        JsonObject forecastData = JsonParser.parseString(json).getAsJsonObject();
        JsonArray timelines = forecastData.getAsJsonObject("data").getAsJsonArray("timelines");
        JsonArray intervals = timelines.get(0).getAsJsonObject().getAsJsonArray("intervals");
        JsonObject today = intervals.get(0).getAsJsonObject().getAsJsonObject("values");

        double temperature = today.get("temperatureMax").getAsDouble();
        StringBuilder recommendation = new StringBuilder("🌡 Today's high is " + temperature + "°F. ");

        if (temperature < 32) {
            recommendation.append("Bundle up! Wear thermal layers, a heavy coat, gloves, and a hat. 🧣🧤");
        } else if (temperature < 50) {
            recommendation.append("It's chilly. Wear a jacket and maybe a scarf. 🧥");
        } else if (temperature < 70) {
            recommendation.append("Mild weather. A hoodie or light sweater works well. 👕");
        } else if (temperature < 85) {
            recommendation.append("Warm day! T-shirts and shorts should be fine. 😎");
        } else {
            recommendation.append("Hot! Wear breathable fabrics and stay hydrated. 🩳🧢");
        }

        System.out.println(recommendation.toString());
    }
}
