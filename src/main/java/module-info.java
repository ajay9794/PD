module com.micro.pd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    // SQLite JDBC
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // OkHttp3
//    requires static okhttp3; /** building executable Jar */
    requires  okhttp3;
    requires org.slf4j;

    // JSON Simple
    requires org.json;

    //materialFX
    requires MaterialFX;
    requires VirtualizedFX;

    requires org.apache.commons.lang3;

    opens com.micro.pd.controller to javafx.fxml;

    opens com.micro.pd to javafx.fxml;
    exports com.micro.pd;
}