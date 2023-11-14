module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.model to javafx.base;
    opens org.example.controller to javafx.fxml;

    exports org.example;
    exports org.example.model;
    exports org.example.repository;
}
