package com.converter_currency.Controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.converter_currency.API.CurrencyAPI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> countryComboBoxResult;

    @FXML
    private TextField fieldUser;

    @FXML
      private Label clockLabel;
    
    private void updateClock() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    clockLabel.setText(LocalDateTime.now().format(formatter));
}

    @FXML
    private TextField fieldHasil;
    ObservableList<String> Countries = FXCollections.observableArrayList("AED", "AUD", "CAD", "CNY", "EUR", "HKD", "IDR", "JPY", "KGS", "QAR", "SGD", "USD");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countryComboBox.setItems(Countries);
        countryComboBoxResult.setItems(Countries);
        countryComboBox.getSelectionModel().select("USD");
        countryComboBoxResult.getSelectionModel().select("IDR");
        // Event saat ComboBox diganti
        countryComboBox.setOnAction(e -> {
            applyCurrencyFormat(fieldUser, getLocaleFromCurrency(countryComboBox.getValue()));
            convertCurrency();
        });
        countryComboBoxResult.setOnAction(e -> convertCurrency());
        // Event saat user ngetik
        fieldUser.setOnKeyReleased(e -> convertCurrency());
        applyCurrencyFormat(fieldUser, getLocaleFromCurrency(countryComboBox.getValue()));

       //real time jam
       Timeline timeline = new Timeline(
       new KeyFrame(Duration.ZERO, e -> updateClock()),
       new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void convertCurrency() {
        String base = getSelectedComboBoxString();
        String target = getSelectedResultComboBoxString();
        String numericOnly = fieldUser.getText().replaceAll("[^\\d.]", "");
        try {
            double input = Double.parseDouble(numericOnly);

            // Panggil API eksternal
            double rate = CurrencyAPI.getExchangeRate(base, target);
            double result = input * rate;

            fieldHasil.setText(String.format("%.2f", result));
        } catch (NumberFormatException ex) {
            fieldHasil.setText(""); // Kosongin kalau input nggak valid
        } catch (RuntimeException ex) {
            fieldHasil.setText("Gagal konek");
            System.err.println("Error saat ambil kurs: " + ex.getMessage());
        }
    }

    public String getSelectedComboBoxString() {
        return countryComboBox.getSelectionModel().getSelectedItem();
    }

    public String getSelectedResultComboBoxString() {
        return countryComboBoxResult.getSelectionModel().getSelectedItem();
    }

    private void applyCurrencyFormat(TextField field, Locale locale) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Hanya izinkan angka
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[\\d,]*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        field.setTextFormatter(formatter);

        // Format teks jadi mata uang saat berubah
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            String digits = newVal.replaceAll("[^\\d]", "");
            if (!digits.isEmpty()) {
                try {
                    long value = Long.parseLong(digits);
                    String formatted = currencyFormat.format(value);
                    if (!formatted.equals(newVal)) {
                        field.setText(formatted);
                        field.positionCaret(formatted.length());
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });
    }

private Locale getLocaleFromCurrency(String currencyCode) {
switch (currencyCode) {
  case "AED":
     return new Locale("ar", "AE");
    case "AUD":
      return new Locale("en", "AU");
    case "CAD":
      return new Locale("en", "CA");
    case "CNY":
      return Locale.CHINA;
    case "EGP":
      return new Locale("ar", "EG");
    case "EUR":
      return Locale.GERMANY;
    case "GIP":
      return new Locale("en", "GI");
    case "HKD":
      return new Locale("zh", "HK");
    case "IDR":
      return new Locale("id", "ID");
    case "JPY":
      return Locale.JAPAN;
    case "KGS":
      return new Locale("ru", "KG");
    case "KWD":
      return new Locale("ar", "KW");
    case "QAR":
      return new Locale("ar", "QA");
    case "SGD":
       return new Locale("en", "SG");
    case "USD":
          return Locale.US;
         default:
         return Locale.US;
        }
    }

}
