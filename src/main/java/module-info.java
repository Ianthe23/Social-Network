module org.example.lab6networkfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.lab6networkfx to javafx.fxml;
    exports org.example.lab6networkfx;
}