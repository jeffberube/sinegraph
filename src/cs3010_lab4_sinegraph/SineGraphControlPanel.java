/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3010_lab4_sinegraph;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Jeff Berube
 */
public class SineGraphControlPanel extends VBox {
    
    SineGraph graph;
    
    FramedPanel colorSchemePanel;
    FramedPanel sinePanel;
    FramedPanel modePanel;
    FramedPanel animationPanel;
    
    NumberSpinner amplitudeControl;
    NumberSpinner periodControl;
    NumberSpinner phaseShiftControl;
    NumberSpinner verticalShiftControl;
    
    Button playButton;
    
    public SineGraphControlPanel(SineGraph graph) {
        super();

        this.graph = graph;
        
        setStyle("-fx-background-color: #222222;");
        setMinWidth(220);
        setMaxWidth(220);
        setPadding(new Insets(8, 8, 8, 8));
        setSpacing(16);
        
        createSinePanel();
        createModePanel();
        createColorSchemePanel();
        createAnimationPanel();
        
        minHeightProperty().bind(colorSchemePanel.heightProperty().add(sinePanel.heightProperty()).add(modePanel.heightProperty()).add(animationPanel.heightProperty()).add(160));
        
    }
    
    private void createSinePanel() {
        sinePanel = new FramedPanel("Sine Parameters");
        sinePanel.setBackgroundColor(Color.TRANSPARENT);
        sinePanel.setContent(createSinePanelContent());
        this.getChildren().add(sinePanel);
    }
    
    private BorderPane createSinePanelContent() {
        BorderPane content = new BorderPane();
        VBox sineControls = createSineControls();
        VBox sineLabels = createSineLabels();
        
        sineLabels.spacingProperty().bind(sineControls.heightProperty().subtract(sineControls.heightProperty()).divide(4).add(24));
        sineLabels.setTranslateY(4);
        
        content.prefWidthProperty().bind(this.widthProperty().subtract(28));
        content.setRight(sineControls);
        content.setLeft(sineLabels);
        content.setTranslateY(16);
        
        return content;
    }
    
    private VBox createSineLabels() {
        VBox sineLabels = new VBox();
        
        sineLabels.setTranslateX(12);
        sineLabels.getChildren().add(createLabel("Amplitude"));
        sineLabels.getChildren().add(createLabel("Period"));
        sineLabels.getChildren().add(createLabel("Phase Shift"));
        sineLabels.getChildren().add(createLabel("Vertical Shift"));
                
        return sineLabels;
    }
    
    
    
    private VBox createSineControls() {
        VBox sineControls = new VBox();
        
        amplitudeControl = createAmplitudeControl();
        periodControl = createPeriodControl();
        phaseShiftControl = createPhaseShiftControl();
        verticalShiftControl = createVerticalShiftControl();
        
        sineControls.setSpacing(16);
        sineControls.getChildren().add(amplitudeControl);
        sineControls.getChildren().add(periodControl);
        sineControls.getChildren().add(phaseShiftControl);
        sineControls.getChildren().add(verticalShiftControl);
        
        return sineControls;
    }
    
    private NumberSpinner createAmplitudeControl() {
        NumberSpinner amplitudeControl = new NumberSpinner(0.0, 5.0);
        
        amplitudeControl.setValue(graph.getAmplitude());
        amplitudeControl.setAlignment(Pos.CENTER_RIGHT);
        amplitudeControl.setStepWidth(0.1);
        amplitudeControl.setMaxWidth(64);
        amplitudeControl.setPrefWidth(64);
        amplitudeControl.setMinWidth(64);
        amplitudeControl.valueProperty().bindBidirectional(graph.amplitudeProperty());
        
        return amplitudeControl;
    }

    private NumberSpinner createPeriodControl() {
        NumberSpinner periodControl = new NumberSpinner(0.0, 10.0);
        
        periodControl.setValue(graph.getAmplitude());
        periodControl.setAlignment(Pos.CENTER_RIGHT);
        periodControl.setStepWidth(0.1);
        periodControl.setMaxWidth(64);
        periodControl.setPrefWidth(64);
        periodControl.setMinWidth(64);
        periodControl.valueProperty().bindBidirectional(graph.periodProperty());
        
        return periodControl;
    }
    
    private NumberSpinner createPhaseShiftControl() {
        NumberSpinner phaseShiftControl = new NumberSpinner(-Math.PI, Math.PI);
        
        phaseShiftControl.setValue(graph.getAmplitude());
        phaseShiftControl.setAlignment(Pos.CENTER_RIGHT);
        phaseShiftControl.setStepWidth(0.1);
        phaseShiftControl.setMaxWidth(64);
        phaseShiftControl.setPrefWidth(64);
        phaseShiftControl.setMinWidth(64);
        phaseShiftControl.valueProperty().bindBidirectional(graph.phaseShiftProperty());
        
        return phaseShiftControl;
    }
    
    private NumberSpinner createVerticalShiftControl() {
        NumberSpinner verticalShiftControl = new NumberSpinner(-5.0, 5.0);
        
        verticalShiftControl.setValue(graph.getAmplitude());
        verticalShiftControl.setAlignment(Pos.CENTER_RIGHT);
        verticalShiftControl.setStepWidth(0.1);
        verticalShiftControl.setMaxWidth(64);
        verticalShiftControl.setPrefWidth(64);
        verticalShiftControl.setMinWidth(64);
        verticalShiftControl.valueProperty().bindBidirectional(graph.verticalShiftProperty());
        
        return verticalShiftControl;
    }
    
    private void createModePanel() {
        modePanel = new FramedPanel("Mode");
        modePanel.setBackgroundColor(Color.TRANSPARENT);
        modePanel.setContent(createModePanelContent());
        this.getChildren().add(modePanel);
    }
    
    private VBox createModePanelContent() {
        VBox content = new VBox();
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton radiansRadio = createRadiansRadio(modeGroup);
        RadioButton degreesRadio = createDegreesRadio(modeGroup);
        
        content.setSpacing(16);
        content.setTranslateX(12);
        content.setTranslateY(16);

        content.getChildren().add(radiansRadio);
        content.getChildren().add(degreesRadio);
        
        return content;
    }
    
    private RadioButton createRadiansRadio(ToggleGroup group) {
        RadioButton radiansRadio = new RadioButton("Radians");
        
        radiansRadio.setToggleGroup(group);
        radiansRadio.setTextFill(Color.WHITE);
        radiansRadio.setSelected(true);
        
        radiansRadio.selectedProperty().addListener(e->{
            if (radiansRadio.isSelected()) graph.setMode(SineGraph.Mode.RADIANS);
        });
        
        return radiansRadio;
    }
    
    private RadioButton createDegreesRadio(ToggleGroup group) {
        RadioButton degreesRadio = new RadioButton("Degrees");
        
        degreesRadio.setToggleGroup(group);
        degreesRadio.setTextFill(Color.WHITE);
        
        degreesRadio.selectedProperty().addListener(e->{
            if (degreesRadio.isSelected()) graph.setMode(SineGraph.Mode.DEGREES);
        });
        
        return degreesRadio;
    }
    
    private void createColorSchemePanel() {
        colorSchemePanel = new FramedPanel("Colors");
        colorSchemePanel.setBackgroundColor(Color.TRANSPARENT);
        colorSchemePanel.setContent(createColorSchemePanelContent());
        this.getChildren().add(colorSchemePanel);
    }
    
    private BorderPane createColorSchemePanelContent() {
        BorderPane content = new BorderPane();
        VBox colorControls = createColorControls();
        VBox colorLabels = createColorLabels();
        
        colorLabels.spacingProperty().bind(colorControls.heightProperty().subtract(colorControls.heightProperty()).divide(4).add(24));
        colorLabels.setTranslateY(4);
        
        content.prefWidthProperty().bind(this.widthProperty().subtract(28));
        content.setRight(colorControls);
        content.setLeft(colorLabels);
        content.setTranslateY(16);
        
        return content;
    }
    
    private VBox createColorLabels() {
        VBox colorLabels = new VBox();
        
        colorLabels.setTranslateX(12);
        colorLabels.getChildren().add(createLabel("Background"));
        colorLabels.getChildren().add(createLabel("Grid"));
        colorLabels.getChildren().add(createLabel("Axes"));
        colorLabels.getChildren().add(createLabel("Sine"));
                
        return colorLabels;
    }
    
    private VBox createColorControls() {
        VBox colorControls = new VBox();
        
        //colorControls.setTranslateX(-12);
        colorControls.setSpacing(16);
        colorControls.getChildren().add(createBackgroundColorPicker());
        colorControls.getChildren().add(createGridColorPicker());
        colorControls.getChildren().add(createAxesColorPicker());
        colorControls.getChildren().add(createSineColorPicker());
        
        return colorControls;
    }
    
    private ColorPicker createBackgroundColorPicker() {
        ColorPicker backgroundColorPicker = new ColorPicker(graph.backgroundColorProperty().get());
        
        backgroundColorPicker.setStyle("-fx-color-label-visible: false ;");
        
        backgroundColorPicker.setOnAction(e->{
            graph.setBackgroundColor(backgroundColorPicker.getValue());
        });
        
        return backgroundColorPicker;
    }
    
    private ColorPicker createGridColorPicker() {
        ColorPicker gridColorPicker = new ColorPicker(graph.gridColorProperty().get());
        
        gridColorPicker.setStyle("-fx-color-label-visible: false ;");
        
        gridColorPicker.setOnAction(e->{
            graph.setGridColor(gridColorPicker.getValue());
        });
        
        return gridColorPicker;
    }
    
    private ColorPicker createAxesColorPicker() {
        ColorPicker axesColorPicker = new ColorPicker(graph.axesColorProperty().get());
        
        axesColorPicker.setStyle("-fx-color-label-visible: false ;");
        
        axesColorPicker.setOnAction(e->{
            graph.setAxesColor(axesColorPicker.getValue());
        });
        
        return axesColorPicker;
    }
    
    private ColorPicker createSineColorPicker() {
        ColorPicker sineColorPicker = new ColorPicker(graph.sineColorProperty().get());
        
        sineColorPicker.setStyle("-fx-color-label-visible: false ;");
        
        sineColorPicker.setOnAction(e->{
            graph.setSineColor(sineColorPicker.getValue());
        });
        
        return sineColorPicker;
    }
    
    private void createAnimationPanel() {
        animationPanel = new FramedPanel("Animate");
        animationPanel.setBackgroundColor(Color.TRANSPARENT);
        animationPanel.setContent(createAnimationPanelContent());
        this.getChildren().add(animationPanel);
    }
    
    private BorderPane createAnimationPanelContent() {
        BorderPane content = new BorderPane();
        
        content.setLeft(createPlayButton());
        
        return content;
    }
    
    private Button createPlayButton() {
        playButton = new Button();
        Polygon rightArrow = new Polygon();
        
        rightArrow.getPoints().addAll(new Double[] {0.5, 0.5, 10.5, 6.5, 0.5, 12.5});
        rightArrow.setFill(Color.web("#222222"));
        rightArrow.setTranslateX(1);
        rightArrow.setTranslateY(1);
        
        playButton.setStyle("-fx-background-radius: 10em;"
                            + "-fx-min-width: 32px;"
                            + "-fx-min-height: 32px;"
                            + "-fx-max-width: 32px;"
                            + "-fx-max-height: 32px;"
        );
        
        playButton.setGraphic(rightArrow);
        playButton.setTranslateX(12);
        playButton.setTranslateY(16);
     
        playButton.setOnAction(e->animateSine());

        return playButton;
    }
    
    private Label createLabel(String name) {
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        return label;
    }
    
    private void animateSine() {  
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        
        playButton.setDisable(true);

        amplitudeControl.setEditable(false);
        periodControl.setEditable(false);
        phaseShiftControl.setEditable(false);
        verticalShiftControl.setEditable(false);
        
        amplitudeControl.setDisable(true);
        periodControl.setDisable(true);
        phaseShiftControl.setDisable(true);
        verticalShiftControl.setDisable(true);
        
        amplitudeControl.setValue(0);
        //graph.setAmplitude(0.01);
        graph.setPeriod(1);
        
        final KeyValue kvAmplitude = new KeyValue(graph.amplitudeProperty(), 5.0, Interpolator.EASE_BOTH);
        final KeyFrame kfAmplitude = new KeyFrame(Duration.millis(5000), kvAmplitude);
        
        final KeyValue kvPeriod = new KeyValue(graph.periodProperty(), 10.0, Interpolator.EASE_BOTH);
        final KeyFrame kfPeriod = new KeyFrame(Duration.millis(5000), kvPeriod);
        
        timeline.getKeyFrames().add(kfAmplitude);
        timeline.getKeyFrames().add(kfPeriod);
        
        timeline.setOnFinished(e->{
            graph.setAmplitude(1.0);
            graph.setPeriod(1.0);
            
            amplitudeControl.setEditable(true);
            periodControl.setEditable(true);
            phaseShiftControl.setEditable(true);
            verticalShiftControl.setEditable(true);
            
            amplitudeControl.setDisable(false);
            periodControl.setDisable(false);
            phaseShiftControl.setDisable(false);
            verticalShiftControl.setDisable(false);
            
            playButton.setDisable(false);
            
        });
        
        timeline.play();
        
    }
    
    private class FramedPanel extends Pane {
        
        private final Pane content = new StackPane();
        private final Rectangle frame = new Rectangle(0.5, 0.5, 0.5, 0.5);
        private Label caption;
        
        private final SimpleObjectProperty<Color> captionColor = new SimpleObjectProperty<>(Color.WHITE);
        private final SimpleObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(Color.TRANSPARENT);
        
        public FramedPanel(String caption) { 
            super();

            createFrame();
            createContent();
            createLabel(caption);

            setStyle("-fx-background-color: #222222");
        }
        
        private void createFrame() {
            frame.setStroke(Color.LIGHTGRAY);
            frame.setStrokeWidth(1);
            frame.setFill(getBackgroundColor());
            frame.fillProperty().bind(backgroundColor);
            frame.widthProperty().bind(this.widthProperty().subtract(4));
            frame.heightProperty().bind(content.heightProperty());
            frame.setTranslateY(9);
            frame.setTranslateX(1);
            frame.setSmooth(false);
            
            this.getChildren().add(frame);
        }
        
        private void createLabel(String string) {
            caption = new Label(" " + string + "  ");
            caption.setTranslateX(8.5);
            caption.setTextFill(Color.WHITE);
            caption.setStyle("-fx-background-color: #222222;");
            this.getChildren().add(caption);
        
        }
        
        private void createContent() {
            content.setPadding(new Insets(8, 0, 24, 0));
            //content.prefHeightProperty().bind(this.heightProperty());
            this.getChildren().add(content);
        }
        
        private void setContent(Node content) {
            this.content.getChildren().add(content);
        }
        
        public String getCaption() {
            return this.caption.getText();
        }
                
        public void setCaption(String caption) {
            this.caption.setText(caption);
        }
        
        public Color getCaptionColor() {
            return captionColor.get();
        }
        
        public void setCaptionColor(Color color) {
            captionColor.set(color);
        }
    
        public Color getBackgroundColor() {
            return backgroundColor.get();
        }
        
        public void setBackgroundColor(Color color) {
            backgroundColor.set(color);
        }
        
    }
    
}
