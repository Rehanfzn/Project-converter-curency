package com.converter_currency.Controllers;

import java.io.IOException;

import com.converter_currency.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    @SuppressWarnings("unused")
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
