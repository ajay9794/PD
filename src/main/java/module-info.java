module com.micro.pd {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;

    // SQLite JDBC
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // OkHttp
    requires okhttp3;


    // JSON Simple
    requires org.json;

    //materialFX
    requires MaterialFX;
    requires VirtualizedFX;

    requires org.apache.commons.lang3;
    requires lombok;

    opens com.micro.pd.controller to javafx.fxml;

    opens com.micro.pd to javafx.fxml;
    exports com.micro.pd;
}