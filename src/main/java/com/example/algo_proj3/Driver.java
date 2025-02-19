package com.example.algo_proj3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Driver extends Application {

    File file; // To hold the reference to the selected file

    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane(); // Main layout pane

        // Set the background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("/com/example/algo_proj3/world.jpg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                )
        );
        borderPane.setBackground(new Background(backgroundImage));

        // Logo at the center
        Image logoImage = new Image(getClass().getResource("/com/example/algo_proj3/globe.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        borderPane.setCenter(logoView);

        // Load button
        Button loadButton = new Button("Load The Capitals File");
        designButton(loadButton);

        HBox optionsBox = new HBox(10, loadButton);
        optionsBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(optionsBox); // Set the HBox with buttons at the bottom of the border pane

        Scene scene = new Scene(borderPane, 1200, 600);
        borderPane.setPadding(new Insets(15, 15, 15, 15)); // Padding around the border pane

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser");

        // Event handler for compressing files
        loadButton.setOnAction(e -> {
            ExtensionFilter filterTXT = new ExtensionFilter("Text files", "*txt");
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().add(filterTXT);

            file = fileChooser.showOpenDialog(stage);

            try {
                if (file.length() == 0)
                    throw new IOException();
                Map map = new Map(stage, scene, file);
                stage.setScene(map);
                stage.setMaximized(true);
            } catch (Exception e2) {
                e2.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File is empty, try another valid file");
                alert.showAndWait();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Dijkstra Maps");
        stage.show();
    }

    private void designButton(Button button) {
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2980b9, #6dd5fa);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #ffffff;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;"
        );

        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #6dd5fa, #2980b9);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10px 20px;" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 20;"
            );
            button.setCursor(Cursor.HAND); // Change cursor to hand
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #2980b9, #6dd5fa);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10px 20px;" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 20;"
            );
            button.setCursor(Cursor.DEFAULT); // Reset cursor to default
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
