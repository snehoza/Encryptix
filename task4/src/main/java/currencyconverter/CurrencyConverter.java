package currencyconverter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the base currency code (e.g., USD): ");
        String baseCurrency = s.nextLine().toUpperCase();

        System.out.print("Enter the target currency code (e.g., EUR): ");
        String targetCurrency = s.nextLine().toUpperCase();

        System.out.print("Enter the amount to convert:");
        double amount = s.nextDouble();

        try {
            double convertedAmount = convertCurrency(baseCurrency, targetCurrency, amount);
            System.out.printf("\n%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
        } catch (IOException e) {
            System.out.println("Error fetching data from API: " + e.getMessage());
        }
        s.close();
    }

    private static double convertCurrency(String baseCurrency, String targetCurrency, double amount)
            throws IOException {
        URL url = new URL(API_URL + baseCurrency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            JsonObject ratesObject = gson.fromJson(response.toString(), JsonObject.class).getAsJsonObject("rates");
            double exchangeRate = ratesObject.get(targetCurrency).getAsDouble();

            return amount * exchangeRate;
        }
    }
}
