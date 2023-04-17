module com.gptjava.gptjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gptjava.gptjava to javafx.fxml;
    exports com.gptjava.gptjava;
}