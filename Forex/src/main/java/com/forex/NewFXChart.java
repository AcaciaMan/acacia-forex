/*
 * Copyright 2017 Acacia Man
 * The program is distributed under the terms of the GNU General Public License
 * 
 * This file is part of acacia-forex.
 *
 * acacia-forex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * acacia-forex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with acacia-forex.  If not, see <http://www.gnu.org/licenses/>.
 */ 
package com.forex;

import com.forex.data.CurrencyPair;
import com.forex.data.Rate;
import java.math.BigDecimal;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class NewFXChart {
    
    static CurrencyPair cp;

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Swing and JavaFX");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(900, 600);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }
    
    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }
    
    private static Scene createScene() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        
        yAxis.setForceZeroInRange(false);
        //xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<>(xAxis, yAxis);
        
        lineChart.setTitle(cp.getPeriodEnum().toString());
        lineChart.setCreateSymbols(false);

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName(cp.toString());

        XYChart.Series lsSeries = new XYChart.Series();
        lsSeries.setName(cp.getLs().percents.toString());

        //populating the series with data
        // series.getData().add(new XYChart.Data(1, 23));
        int i = 0;
        for(Rate r: cp.getRates()) {
            series.getData().add(new XYChart.Data(i, r.rate));
            lsSeries.getData().add(new XYChart.Data(i, cp.getLs().a.add(cp.getLs().b.multiply(new BigDecimal(i)))       ));
            i++;
        }
        
        
        
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        lineChart.getData().add(lsSeries);
        
        return (scene);
    }
    
    public static void invoke(CurrencyPair cpShow) {
        
        cp = cpShow;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }
}
