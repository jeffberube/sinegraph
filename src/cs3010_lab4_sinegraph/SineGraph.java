/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3010_lab4_sinegraph;

import java.util.Iterator;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jeff Berube
 */
public class SineGraph extends ScrollPane {
    
    public enum Mode { RADIANS, DEGREES, NUMERIC; }
    
    private final StackPane container = new StackPane();
    private Rectangle background;
    private final Pane grid = new Pane();
    private Line xAxis;
    private Line yAxis;
    private Pane sine;
    
    private SimpleDoubleProperty[] sinePoints;
    private final int totalPoints = 500;
    
    private final SimpleObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> gridColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> axesColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> sineColor = new SimpleObjectProperty<>();
    
    private final SimpleObjectProperty<Mode> xAxisMode = new SimpleObjectProperty(Mode.RADIANS);

    String[] labelsInRadians = new String[]{"-\u03C0", "-3\u03C0/4", "-\u03C0/2", "-\u03C0/4", "0     ", "\u03C0/4", "\u03C0/2", "3\u03C0/4", "\u03C0"};
    String[] labelsInDegrees = new String[]{"-180\u00B0", "-135\u00B0", "-90\u00B0", "-45\u00B0", "0     ", "45\u00B0", "90\u00B0", "135\u00B0", "180\u00B0"};
    
    private final SimpleStringProperty[] xAxisTickLabels = new SimpleStringProperty[labelsInRadians.length];
    
    private final double maxX = Math.PI;
    private final int maxY = 5;
    private final double minX = -Math.PI;
    private final int minY = -5;
    
    private final double verticalLineCount = 9;
    private final double horizontalLineCount = (maxY * 2) + 1;
    
    private SimpleDoubleProperty amplitude;
    private SimpleDoubleProperty period;
    private SimpleDoubleProperty phaseShift;
    private SimpleDoubleProperty verticalShift;
    
    public SineGraph(double amplitude, double period, double phaseShift, double verticalShift) {
        super();
        
        this.amplitude = new SimpleDoubleProperty(this, "amplitude", amplitude);
        this.period = new SimpleDoubleProperty(this, "period", period);
        this.phaseShift = new SimpleDoubleProperty(this, "phaseShift", phaseShift);
        this.verticalShift = new SimpleDoubleProperty(this, "verticalShift", verticalShift); 

        init();
        generateTickLabels();
        createBackground();
        createGrid();
        createXYaxis();
        createLabels();
        createSine();
        
        xAxisMode.addListener(e->{
            updateTickLabels();
        });
    }
    
    public SineGraph() {
        this(1.0, 1.0, 0.0, 0.0);
    } 
    
    private void init() {
        backgroundColor.set(Color.WHITE);
        gridColor.set(Color.LIGHTGRAY);
        axesColor.set(Color.BLACK);
        sineColor.set(Color.RED);
        
        setContent(container);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true);
        setFitToWidth(true);
        setTranslateX(-1);
        setTranslateY(-1);
        setPadding(new Insets(0, 0, 0, 0));
    }
    
    private void generateTickLabels() {
        for (int i = 0; i < xAxisTickLabels.length; i++)
            xAxisTickLabels[i] = new SimpleStringProperty(labelsInRadians[i]);
    }
    
    private void updateTickLabels() {
        for (int i = 0; i < xAxisTickLabels.length; i++)
            xAxisTickLabels[i].set(xAxisMode.get() == Mode.RADIANS ? labelsInRadians[i] : labelsInDegrees[i]);
    }
    
    private void createBackground() {
        background = new Rectangle(-1.5, -1.5, -0.5, -0.5);
        background.widthProperty().bind(this.widthProperty());
        background.heightProperty().bind(this.heightProperty());
        background.fillProperty().bind(this.backgroundColor);
        container.getChildren().add(background);
    }
    
    private void createGrid() {
        container.getChildren().add(grid);
        drawGrid();      
    }
    
    private void redrawGrid() {
        clearGrid();
        drawGrid();
    }
    
    private void clearGrid() {
        Iterator gridLines = grid.getChildren().iterator();
        
        while (gridLines.hasNext()) {
            gridLines.next();
            gridLines.remove();
        }   
    }
    
    private void drawGrid() {

        for (int i = 0; i < verticalLineCount; i++) {          
            Line verticalLine = new Line(0.5, 0.5, 0.5, 0.5);
            verticalLine.setStroke(gridColor.get());
            verticalLine.strokeProperty().bind(gridColor);
            verticalLine.startXProperty().bind(snapTo(this.widthProperty().multiply(0.9).divide(verticalLineCount - 1).multiply(i).add(this.widthProperty().multiply(0.05))));
            verticalLine.endXProperty().bind(snapTo(this.widthProperty().multiply(0.9).divide(verticalLineCount - 1).multiply(i).add(this.widthProperty().multiply(0.05))));
            verticalLine.endYProperty().bind(this.heightProperty());
            grid.getChildren().add(verticalLine);
        }

        for (int i = 0; i < horizontalLineCount; i++) {          
            Line horizontalLine = new Line(0.5, 0.5, 0.5, 0.5);
            horizontalLine.setStroke(gridColor.get());
            horizontalLine.strokeProperty().bind(this.gridColor);
            horizontalLine.startYProperty().bind(snapTo(this.heightProperty().multiply(0.9).divide(horizontalLineCount - 1).multiply(i).add(this.heightProperty().multiply(0.05))));
            horizontalLine.endYProperty().bind(snapTo(this.heightProperty().multiply(0.9).divide(horizontalLineCount - 1).multiply(i).add(this.heightProperty().multiply(0.05))));
            horizontalLine.endXProperty().bind(this.widthProperty()); 
            grid.getChildren().add(horizontalLine);
        }
        
    }
    
    private void createXYaxis() {
        createXaxis();
        createYaxis();
    }
    
    private void createXaxis() {
        xAxis = new Line(0.5, 0.5, 0.5, 0.5);
        xAxis.setStroke(axesColor.get());
        xAxis.strokeProperty().bind(axesColor);
        xAxis.startYProperty().bind(snapTo(this.heightProperty().divide(2)));
        xAxis.endYProperty().bind(snapTo(this.heightProperty().divide(2)));
        xAxis.endXProperty().bind(this.widthProperty());
        container.getChildren().add(xAxis);
    }
    
    private void createYaxis() {
        yAxis = new Line(0.5, 0.5, 0.5, 0.5);
        yAxis.setStroke(axesColor.get());
        yAxis.strokeProperty().bind(axesColor);
        yAxis.startXProperty().bind(snapTo(this.widthProperty().divide(2)));
        yAxis.endXProperty().bind(snapTo(this.widthProperty().divide(2)));
        yAxis.endYProperty().bind(this.heightProperty());
        container.getChildren().add(yAxis);
    }
    
    private void createLabels() {
        createXaxisTickLabels();
        createYaxisTickLabels();
    }
    
    private void createXaxisTickLabels() {
        for (int i = 0; i < xAxisTickLabels.length; i++) {
            Label tickLabel = new Label(xAxisTickLabels[i].get());
            tickLabel.setTranslateY(10);
            tickLabel.translateXProperty().bind(snapTo(this.widthProperty().multiply(0.9).divide(verticalLineCount - 1).multiply(i).add(this.widthProperty().multiply(0.05)).subtract(this.widthProperty().multiply(0.5))));
            tickLabel.textProperty().bind(xAxisTickLabels[i]);
            tickLabel.textFillProperty().bind(axesColor);
            
            container.getChildren().add(tickLabel);
        }
    }
    
    private void createYaxisTickLabels() {
        for (int i = 0; i <= maxY - minY; i++) {
            
            if (maxY - i == 0) continue;        // Skip 0 on Y axis
            
            Label tickLabel = new Label(maxY - i > 0 ? " " + Integer.toString(maxY - i) : Integer.toString(maxY - i));
            tickLabel.setTranslateX(-10);
            tickLabel.setAlignment(Pos.CENTER_RIGHT);
            tickLabel.translateYProperty().bind(snapTo(this.heightProperty().multiply(0.9).divide(horizontalLineCount - 1).multiply(i).add(this.heightProperty().multiply(0.05)).subtract(this.heightProperty().multiply(0.5))));
            tickLabel.textFillProperty().bind(axesColor);
            
            container.getChildren().add(tickLabel);
        }
    }
    
    private void createSine() {
        sine = new Pane();
        sine.translateXProperty().bind(this.widthProperty().multiply(0.05));
        sine.translateYProperty().bind(this.heightProperty().divide(2));
 
        calculateSinePoints();
        drawSine();

        container.getChildren().add(sine);
    }
    
    private void drawSine() {
        for (int i = 0; i < sinePoints.length - 1; i++)
            drawSineSegment(i, i + 1);
    }
    
    private void drawSineSegment(int startIndex, int endIndex) {
        Line sineSegment = new Line(0, 0, 0, 0);
        
        sineSegment.setStroke(sineColor.get());
        sineSegment.strokeProperty().bind(sineColor);
        sineSegment.startXProperty().bind(this.widthProperty().multiply(0.9).divide(totalPoints - 1).multiply(startIndex));
        sineSegment.startYProperty().bind(sinePoints[startIndex]);
        sineSegment.endXProperty().bind(this.widthProperty().multiply(0.9).divide(totalPoints - 1).multiply(endIndex));
        sineSegment.endYProperty().bind(sinePoints[endIndex]);
        
        sine.getChildren().add(sineSegment);
    }
    
    private void calculateSinePoints() {
        double stride = (Math.PI * 2) / (totalPoints - 1);
        sinePoints = new SimpleDoubleProperty[totalPoints];
        
        for (int i = 0; i < totalPoints; i++) {
            SimpleDoubleProperty y = new SimpleDoubleProperty(0);
            y.bind(this.heightProperty().multiply(0.9).divide(maxY * 2)
                    .multiply(amplitude.multiply(sin(period.multiply((i * stride) - Math.PI).add(phaseShift))).add(verticalShift).negate()));
            sinePoints[i] = y;
        }
    }

    public DoubleBinding sin(ObservableNumberValue other) {
        return new DoubleBinding() { 
            {
                super.bind(other);
            }
            
            @Override
            protected double computeValue() {
                return Math.sin(other.doubleValue());
            }
        }; 
    }
    
    public DoubleBinding snapTo(ObservableNumberValue other) {
        return new DoubleBinding() {
            {
                super.bind(other);
            }
            
            @Override
            protected double computeValue() {
                return ((int) other.doubleValue()) + 0.5;
            }
        };
    }
    
    public void redraw() {
        redrawGrid();
    }
    
    public SimpleObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor.get();
    }
    
    public void setBackgroundColor(Color color) {
        backgroundColor.set(color);
    }
   
    public SimpleObjectProperty<Color> gridColorProperty() {
        return gridColor;
    }
    
    public Color getGridColor() {
        return gridColor.get();
    }
    
    public void setGridColor(Color color) {
        gridColor.set(color);
    }
    
    public SimpleObjectProperty<Color> axesColorProperty() {
        return axesColor;
    }
    
    public Color getAxeColor() {
        return axesColor.get();
    }
    
    public void setAxesColor(Color color) {
        axesColor.set(color);
    }

    public SimpleObjectProperty<Color> sineColorProperty() {
        return sineColor;
    }
    
    public Color getSineColor() {
        return sineColor.get();
    }
    
    public void setSineColor(Color color) {
        sineColor.set(color);
    }
    
    public Mode getMode() {
        return xAxisMode.get();
    }
    
    public void setMode(Mode mode) {
        xAxisMode.set(mode);
    }
    
    public double getAmplitude() {
        return amplitude.get();
    }
    
    public void setAmplitude(double a) {
        if (a > 0.0) amplitude.set(a);
    }
    
    public SimpleDoubleProperty amplitudeProperty() {
        return amplitude;
    }
    
    public double getA() {
        return getAmplitude();
    }
    
    public void setA(double a) {
        setAmplitude(a);  
    }
    
    public SimpleDoubleProperty aProperty() {
        return amplitudeProperty();
    }
    
    public double getPeriod() {
        return period.get();
    }
    
    public void setPeriod(double p) {
        if (p > 0.0) period.set(p);
    }
    
    public SimpleDoubleProperty periodProperty() {
        return period;
    }
    
    public double getB() {
        return getPeriod();
    }
    
    public void setB(double b) {
        setPeriod(b);
    }
    
    public SimpleDoubleProperty bProperty() {
        return periodProperty();
    }
    
    public double getPhaseShift() {
        return phaseShift.get();
    }
    
    public void setPhaseShift(double ps) {
        phaseShift.set(ps);
    }
    
    public SimpleDoubleProperty phaseShiftProperty() {
        return phaseShift;
    }
    
    public double getC() {
        return getPhaseShift();
    }
    
    public void setC(double c) {
        setPhaseShift(c);
    }
    
    public SimpleDoubleProperty cProperty() {
        return phaseShiftProperty();
    }
    
    public double getVerticalShift() {
        return verticalShift.get();
    }
    
    public void setVerticalShift(double vs) {
        verticalShift.set(vs);
    }
    
    public SimpleDoubleProperty verticalShiftProperty() {
        return verticalShift;
    }
    
    public double getD() {
        return getVerticalShift();
    }
    
    public void setD(double d) {
        setVerticalShift(d);
    }
    
    public SimpleDoubleProperty dProperty() {
        return verticalShiftProperty();
    }
    
}
