module com.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires com.google.gson;
    requires javafx.media;

    exports com.game.core;
    exports com.game.model;
    exports com.game.controller;
    exports com.game.system;
    exports com.game.map;
    exports com.game.util;
}
