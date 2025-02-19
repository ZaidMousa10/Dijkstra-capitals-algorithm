package com.example.algo_proj3;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Map extends Scene {

    private ComboBox<String> sourceComboBox;
    private ComboBox<String> targetComboBox;
    private ComboBox<String> filterComboBox;
    private Label pathLabel;
    private Label distanceLabel;
    private Label costLabel;
    private Label timeLabel;
    private TextArea pathArea;
    private TextField distanceField;
    private TextField costField;
    private TextField timeField;
    private ImageView mapView;
    private Graph graph;

    public Map(Stage stage, Scene previousScene, File file) throws IOException {
        super(new BorderPane(), 1200, 600);

        BorderPane root = (BorderPane) this.getRoot();

        // Set background image
        Image backgroundImage = new Image("file:src/main/resources/com/example/algo_proj3/world.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
        );
        root.setBackground(new Background(background));

        // Create GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Map pane inside the ScrollPane
        Pane mapPane = new Pane();
        mapView = new ImageView(new Image("file:src/main/resources/com/example/algo_proj3/Equirectangular-projection.jpg"));
        mapView.setPreserveRatio(true);
        mapView.setFitWidth(1200);
        mapView.setFitHeight(600);
        mapPane.getChildren().add(mapView);
        mapPane.setPrefWidth(1200);
        mapPane.setPrefHeight(600);

        // Add ScrollPane to GridPane
        gridPane.add(mapPane, 0, 0);

        // Control panel
        VBox controlPanel = new VBox();
        controlPanel.setAlignment(Pos.CENTER_LEFT);

        sourceComboBox = new ComboBox<>();
        sourceComboBox.setPromptText("Select Source");
        targetComboBox = new ComboBox<>();
        targetComboBox.setPromptText("Select Target");
        filterComboBox = new ComboBox<>();
        filterComboBox.setPromptText("Filter");
        filterComboBox.getItems().addAll("Shortest Distance", "Lowest Cost", "Shortest Time");
        filterComboBox.setValue("Shortest Distance");

        pathLabel = new Label("Path: ");
        pathLabel.setTextFill(Color.BLUE);
        pathLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        pathArea = new TextArea();
        pathArea.setPrefHeight(200);
        pathArea.setPrefWidth(200);
        pathArea.setEditable(false);

        distanceLabel = new Label("Distance: ");
        distanceLabel.setTextFill(Color.GREEN);
        distanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        distanceField = new TextField();
        distanceField.setEditable(false);

        costLabel = new Label("Cost: ");
        costLabel.setTextFill(Color.RED);
        costLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        costField = new TextField();
        costField.setEditable(false);

        timeLabel = new Label("Time: ");
        timeLabel.setTextFill(Color.ORANGE);
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        timeField = new TextField();
        timeField.setEditable(false);

        Button runButton = new Button("Run");
        runButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        // Run button logic
        runButton.setOnAction(e -> {
            Pane pane = mapPane;
            ObservableList<javafx.scene.Node> children = pane.getChildren();
            children.removeIf(n -> n instanceof Line);

            String source = sourceComboBox.getValue();
            String target = targetComboBox.getValue();
            String filter = filterComboBox.getValue();

            if (source == null || target == null || filter == null) {
                showAlert("Error", "Please select source, target, and filter.", Alert.AlertType.ERROR);
                return;
            }

            Vertex sourceVertex = graph.getVertix(source);
            Vertex targetVertex = graph.getVertix(target);

            if (sourceVertex == null || targetVertex == null) {
                showAlert("Error", "Invalid source or destination.", Alert.AlertType.ERROR);
                return;
            }

            if (sourceVertex.equals(targetVertex)) {
                pathArea.setText("The source and destination are the same");
                distanceField.setText("0 km");
                timeField.setText("0 hours");
                costField.setText("$0");
                return;
            }

            TableEntry result = null;

            // Use the selected filter to choose the appropriate Dijkstra method
            if ("Shortest Distance".equals(filter)) {
                result = graph.distanceDijkstra(sourceVertex, targetVertex);
            } else if ("Lowest Cost".equals(filter)) {
                result = graph.costDijkstra(sourceVertex, targetVertex);
            } else if ("Shortest Time".equals(filter)) {
                result = graph.timeDijkstra(sourceVertex, targetVertex);
            } else {
                showAlert("Error", "Invalid filter selected.", Alert.AlertType.ERROR);
                return;
            }


            if (result == null || result.getRoute() == null || result.getRoute().isEmpty()) {
                showAlert("No Path Found", "No path exists between the selected capitals.", Alert.AlertType.INFORMATION);
                clearOutputFields();
                return;
            }

            double totalDistance = result.getDistance();
            double totalCost = result.getCost();
            double totalTime = result.getTime();

            if (totalDistance <= 0 || totalCost <= 0 || totalTime <= 0) {
                showAlert("No Valid Path Found", "No valid path exists between the selected capitals.", Alert.AlertType.INFORMATION);
                clearOutputFields();
                return;
            }

            StringBuilder path = new StringBuilder();
            Node currentNode = result.getRoute().getFront().getNext();
            while (currentNode != null) {
                Edge edge = currentNode.getElement();
                path.append(edge.getSource().getCapital().getCapitalName())
                        .append(" -> ")
                        .append(edge.getDestination().getCapital().getCapitalName())
                        .append(": ")
                        .append(String.format("%.2f km, $%.2f, %.2f hours",
                                (double) edge.getDistance(),
                                (double) edge.getCost(),
                                (double) edge.getTime()))
                        .append("\n");

                Line line = new Line(
                        edge.getSource().getCapital().getX(),
                        edge.getSource().getCapital().getY(),
                        edge.getDestination().getCapital().getX(),
                        edge.getDestination().getCapital().getY()
                );
                line.setStroke(Color.BLUE);
                line.setStrokeWidth(2);
                pane.getChildren().add(line);

                currentNode = currentNode.getNext();
            }

            pathArea.setText(path.toString());
            distanceField.setText(String.format("%.2f km", totalDistance));
            costField.setText(String.format("$%.2f", totalCost));
            timeField.setText(String.format("%.2f Min", totalTime));
        });


        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14;");
        backButton.setOnAction(event -> stage.setScene(previousScene));

        Label sourceLabel = new Label("Source:");
        sourceLabel.setTextFill(Color.DARKBLUE);
        sourceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label targetLabel = new Label("Target:");
        targetLabel.setTextFill(Color.DARKGREEN);
        targetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label filterLabel = new Label("Filter:");
        filterLabel.setTextFill(Color.DARKORANGE);
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Add to control panel
        controlPanel.getChildren().addAll(
                sourceLabel, sourceComboBox,
                targetLabel, targetComboBox,
                filterLabel, filterComboBox,
                runButton, pathLabel, pathArea,
                distanceLabel, distanceField,
                costLabel, costField,
                timeLabel, timeField,
                backButton
        );

        VBox vBox = new VBox();
        vBox.getChildren().add(controlPanel);
        root.setCenter(gridPane);
        root.setRight(vBox);

        populateCapitalsAndEdges(file, mapPane);
    }

    private void populateCapitalsAndEdges(File file, Pane mapPane) throws IOException {
        graph = new Graph(100);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            if (firstLine == null || firstLine.trim().isEmpty()) {
                throw new IOException("Invalid file format: Missing number of capitals and edges.");
            }

            String[] metadata = firstLine.split(",");
            int numberOfCapitals = Integer.parseInt(metadata[0].trim());
            int numberOfEdges = Integer.parseInt(metadata[1].trim());

            // Read vertices
            for (int i = 0; i < numberOfCapitals; i++) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String capital = parts[0].trim();
                    double latitude = Double.parseDouble(parts[1].trim());
                    double longitude = Double.parseDouble(parts[2].trim());

                    sourceComboBox.getItems().add(capital);
                    targetComboBox.getItems().add(capital);

                    double x = convertLongitudeToX(longitude, mapView);
                    double y = convertLatitudeToY(latitude, mapView);

                    Capitals capitalObj = new Capitals(capital, latitude, longitude, x, y);
                    Vertex vertex = new Vertex(capitalObj);
                    graph.addVertex(vertex);

                    // Create pin
                    ImageView pin = new ImageView(new Image("file:src/main/resources/com/example/algo_proj3/airport (1).png"));
                    pin.setFitWidth(15);
                    pin.setFitHeight(15);
                    pin.setLayoutX(x - 7.5);
                    pin.setLayoutY(y - 15);

                    // Create label for the capital
                    Label capitalLabel = new Label(capital);
                    capitalLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-padding: 2px;");
                    capitalLabel.setLayoutX(x - (capital.length() * 3)); // Center the label
                    capitalLabel.setLayoutY(y + 5); // Place below the pin

                    // Add pin and label to the map pane
                    mapPane.getChildren().addAll(pin, capitalLabel);

                    // Shared click handler
                    Runnable selectCapital = () -> {
                        if (sourceComboBox.getValue() == null) {
                            sourceComboBox.setValue(vertex.getCapital().getCapitalName());
                        } else if (targetComboBox.getValue() == null) {
                            targetComboBox.setValue(vertex.getCapital().getCapitalName());
                        }
                    };

                    // Handle pin click
                    pin.setOnMouseClicked(e -> selectCapital.run());

                    // Handle label click
                    capitalLabel.setOnMouseClicked(e -> selectCapital.run());
                }
            }

            // Read edges
            for (int i = 0; i < numberOfEdges; i++) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String source = parts[0].trim();
                    String destination = parts[1].trim();
                    int cost = Integer.parseInt(parts[2].trim());
                    int time = Integer.parseInt(parts[3].trim());

                    Vertex sourceVertex = graph.getVertix(source);
                    Vertex destinationVertex = graph.getVertix(destination);

                    if (sourceVertex == null || destinationVertex == null) {
                        continue; // Skip this edge
                    }

                    int distance = getDistance(sourceVertex, destinationVertex);

                    Edge edge = new Edge(sourceVertex, destinationVertex, time, cost, distance);
                    sourceVertex.addEdge(edge);
                }
            }
        }
    }

    private void clearOutputFields() {
        pathArea.setText("No path found");
        distanceField.setText("0 km");
        timeField.setText("0 hours");
        costField.setText("$0");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private static int getDistance(Vertex sourceVertex, Vertex destinationVertex) {
        double srcLatitude = sourceVertex.getCapital().getLatitude();
        double destLatitude = destinationVertex.getCapital().getLatitude();
        double srcLongitude = sourceVertex.getCapital().getLongitude();
        double destLongitude = destinationVertex.getCapital().getLongitude();

        double radSrcLatitude = Math.toRadians(srcLatitude);
        double radDestLatitude = Math.toRadians(destLatitude);
        double radSrcLongitude = Math.toRadians(srcLongitude);
        double radDestLongitude = Math.toRadians(destLongitude);

        double diffBetweenLat = radDestLatitude - radSrcLatitude;
        double diffBetweenLong = radDestLongitude - radSrcLongitude;

        double res = Math.pow(Math.sin(diffBetweenLat / 2), 2) + Math.pow(Math.sin(diffBetweenLong / 2), 2) *
                Math.cos(radSrcLatitude) * Math.cos(radDestLatitude);
        double earthRad = 6378;
        double c = 2 * Math.asin(Math.sqrt(res));

        return (int) (earthRad * c);
    }

    private double convertLongitudeToX(double longitude, ImageView mapView) {
        return (longitude + 180) / 360 * mapView.getFitWidth();
    }

    private double convertLatitudeToY(double latitude, ImageView mapView) {
        return (1 - (90 + latitude) / 180) * mapView.getFitHeight();
    }
}
