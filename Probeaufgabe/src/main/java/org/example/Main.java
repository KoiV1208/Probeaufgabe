package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stage;
    @Override
    public void start(Stage stage) {
        Parent content;
        try {


            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/main.fxml"));
            content = viewLoader.load();

            stage = new Stage();
            stage.setTitle("Probeaufgabe");
            stage.setScene(new Scene(content, 1000, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Application started");
    }
     public void closeApp()
        {
        if(stage != null)
            stage.close();
        }
    public static void main(String[] args) {
        launch();
    }
}
