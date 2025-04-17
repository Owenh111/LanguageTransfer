module com.example.javafxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.javafxdemo to javafx.fxml;
    exports com.example.javafxdemo;
    exports com.example.javafxdemo.Exercises;
    opens com.example.javafxdemo.Exercises to javafx.fxml;

    //opens com.example.javafxdemo.Controller to javafx.fxml;
}