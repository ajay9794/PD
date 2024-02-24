module com.micro.pd {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.micro.pd.controller to javafx.fxml;

    opens com.micro.pd to javafx.fxml;
    exports com.micro.pd;
}