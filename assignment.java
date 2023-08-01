package first;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherForecast {
    private static final String API_KEY = "YOUR_OPENWEATHERMAP_API_KEY";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=51.5074&lon=-0.1278&exclude=current,minutely,daily&appid=" + API_KEY;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                if (choice == 0) {
                    System.out.println("Exiting the program.");
                    break;
                } else if (choice >= 1 && choice <= 3) {
                    getAndPrintData(scanner, choice);
                } else {
                    System.out.println("Invalid choice! Please try again.");
                }
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
    }

    private static void getAndPrintData(Scanner scanner, int choice) {
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = scanner.next();
        JSONObject forecastData;
        try {
            forecastData = new JSONObject(new URL("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22").openStream());
            JSONArray hourlyData = forecastData.getJSONArray("list");

            for (int i = 0; i < hourlyData.length(); i++) {
                JSONObject hourlyEntry = hourlyData.getJSONObject(i);
                String dt = getDateStringFromUnixTimestamp(hourlyEntry.getLong("dt"));

                if (dt.startsWith(date)) {
                    switch (choice) {
                        case 1:
                            System.out.println("Temperature on " + dt + ": " + hourlyEntry.getDouble("temp") + " °C");
                            break;
                        case 2:
                            System.out.println("Wind Speed on " + dt + ": " + hourlyEntry.getDouble("wind_speed") + " m/s");
                            break;
                        case 3:
                            System.out.println("Pressure on " + dt + ": " + hourlyEntry.getDouble("pressure") + " hPa");
                            break;
                    }
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("No data available for the provided date.");
    }

    private static String getDateStringFromUnixTimestamp(long timestamp) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp * 1000));
    }
}
