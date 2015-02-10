/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3010_lab4_sinegraph;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Berube
 */
public class CS3010_Lab4_SineGraph extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        SineGraph graph = new SineGraph();
        SineGraphControlPanel controlPanel = new SineGraphControlPanel(graph);
        Scene scene = new Scene(root, 800, 600);
        
        root.getChildren().add(controlPanel);
        root.getChildren().add(graph);
        root.setSpacing(0);
        root.setPadding(new Insets(0, 0, 0, 0));
        root.setFillHeight(true);
        
        graph.minHeight(0);
        graph.minWidth(0);
        graph.prefHeightProperty().bind(root.heightProperty());
        graph.prefWidthProperty().bind(root.widthProperty());

        primaryStage.setTitle("SineGraph");
        primaryStage.setScene(scene);
        primaryStage.minHeightProperty().bind(controlPanel.minHeightProperty());
        primaryStage.minWidthProperty().bind(controlPanel.widthProperty().multiply(4));
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
