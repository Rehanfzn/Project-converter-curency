module com.converter_currency {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.converter_currency to javafx.fxml;
    opens  com.converter_currency.Controllers to javafx.fxml;
    exports com.converter_currency;
}
