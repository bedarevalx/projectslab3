package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class GanttChart extends JFrame {
    public GanttChart() throws IOException {
        // Create dataset
        IntervalCategoryDataset dataset = getCategoryDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(
                "Диаграмма Ганта", // Chart title
                "", // X-Axis Label
                "Время", // Y-Axis Label
                dataset, false, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        ChartPanel panel = new ChartPanel(chart);
        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("SS"));
        setContentPane(panel);
    }
    private IntervalCategoryDataset getCategoryDataset() throws IOException {
        File dataDiagramFile = new File("E:\\Projects Idea\\projectslab3\\src\\com\\company\\dataDiagramFile.txt");
        TaskSeries series1 = new TaskSeries("Estimated Date");
        FileReader fr = new FileReader(dataDiagramFile);
        Scanner sc = new Scanner(fr);
        while (sc.hasNext()) {
            int eventI = sc.nextInt();
            int eventJ = sc.nextInt();
            int workTime = sc.nextInt();
            int earlyTime = sc.nextInt();
            series1.add(new dataWork(eventI + "-" + eventJ, earlyTime, earlyTime + workTime));

        }
        fr.close();
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series1);
        return dataset;
    }
}
