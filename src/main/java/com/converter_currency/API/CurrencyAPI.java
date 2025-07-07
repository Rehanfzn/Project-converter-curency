package com.converter_currency.API;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class CurrencyAPI {

    @SuppressWarnings("deprecation")
    public static double getExchangeRate(String base, String target) {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/2e9a80d70020185654cd4c4a/latest/" + base;
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonElement root = JsonParser.parseReader(new InputStreamReader(request.getInputStream()));
            JsonObject jsonobj = root.getAsJsonObject();
            if (jsonobj.get("result").getAsString().equals("error")) {
                throw new RuntimeException("API error: " + jsonobj.get("error-type").getAsString());
            }
            JsonObject rates = jsonobj.getAsJsonObject("conversion_rates");
            return rates.get(target).getAsDouble();
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new RuntimeException("Gagal ambil kurs mata uang dari API", e);
        }
    }
}
