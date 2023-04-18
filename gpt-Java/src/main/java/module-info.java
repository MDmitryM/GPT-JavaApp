module com.gptjava.gptjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires spring.context;
    requires com.google.gson;
    requires retrofit2.converter.gson;

    opens com.gptjava.gptjava to javafx.fxml;
    exports com.gptjava.gptjava;

}