module com.micro.pd {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
                        
    opens com.micro.pd to javafx.fxml;
    exports com.micro.pd;
}