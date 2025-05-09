# ForecastFit
//test codes
private static void generateOutfitRecommendation(String json) {
        JsonObject forecastData = JsonParser.parseString(json).getAsJsonObject();
        JsonArray daily = forecastData.getAsJsonObject("timelines").getAsJsonArray("daily");
        JsonObject today = daily.get(0).getAsJsonObject().getAsJsonObject("values");
        
        double temperatureAvg = today.get("temperatureAvg").getAsDouble(); 
        double temperature = today.get("temperatureMax").getAsDouble();
        StringBuilder recommendation = new StringBuilder();
        recommendation.append("Current (avg) temp: ").append(temperatureAvg).append("°F\n");
        recommendation.append("Today's high: ").append(temperatureMax).append("°F\n");

        if (temperature < 32) {
            recommendation.append("Bundle up! Wear thermal layers, a heavy coat, gloves, and a hat.");
        } else if (temperature < 50) {
            recommendation.append("It's chilly. Wear a jacket and maybe a scarf.");
        } else if (temperature < 70) {
            recommendation.append("Mild weather. A hoodie or light sweater works well.");
        } else if (temperature < 85) {
            recommendation.append("Warm day! T-shirts and shorts should be fine.");
        } else {
            recommendation.append("Hot! Wear breathable fabrics and stay hydrated.");
        }
    
        System.out.println(recommendation.toString());
    }