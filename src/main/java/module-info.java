module org.example.lab6networkfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.lab6networkfx to javafx.fxml;
    exports org.example.lab6networkfx;
}