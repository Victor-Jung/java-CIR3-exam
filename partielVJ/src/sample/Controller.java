package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    @FXML
    public LineChart tempChart;
    @FXML
    public LineChart sizeChart;
    @FXML
    public BarChart hist;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        BufferedReader reader;
        BufferedReader readerTemp;

        // Prepare containers
        XYChart.Series<Double, Double> seriesSize = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> seriesTempGIS = new XYChart.Series<Double, Double>();
        XYChart.Series<Double, Double> seriesTempGCAG = new XYChart.Series<Double, Double>();

        XYChart.Series<String, Double> barSerie = new XYChart.Series<String, Double>();
        barSerie.setName("Mean of temp by decade");

        seriesSize.setName("Glacier size");
        seriesTempGIS.setName("Temperature");

        try{

            reader = new BufferedReader(new FileReader("src/sample/glaciers.txt"));
            readerTemp = new BufferedReader(new FileReader("src/sample/temperature.txt"));

            String line = reader.readLine();
            line = reader.readLine(); // Skip the first line
            String line2 = readerTemp.readLine();
            line2 = readerTemp.readLine(); // Skip the first line

            int i = 0; // line counter
            int j = 0; // month counter
            double sum = 0; // For the mean calculus
            String year = "2016"; // For the decade display

            while (line != null) {

                String[] values = line.split(","); // extract the line's data with comma as separator
                seriesSize.getData().add(new XYChart.Data(Double.parseDouble(values[0]), Double.parseDouble(values[1])));

                line = reader.readLine();
            }

            while (line2 != null) {

                String[] valuesTemp = line2.split(","); // extract the line's data with comma as separator
                String[] date = valuesTemp[1].split("-"); // extract the date data with dash as separator


                if(i % 2 == 1){
                    seriesTempGIS.getData().add(new XYChart.Data(Double.parseDouble(date[0])+Double.parseDouble(date[1])/12, Double.parseDouble(valuesTemp[2])));
                }
                else{
                    j++;
                    seriesTempGCAG.getData().add(new XYChart.Data(Double.parseDouble(date[0])+Double.parseDouble(date[1])/12, Double.parseDouble(valuesTemp[2])));
                }
                i++;

                sum += Double.parseDouble(valuesTemp[2]);

                if(j % 120 == 0){

                    sum = sum/(20*12);

                    barSerie.getData().add(new XYChart.Data(year, sum));

                    year = Double.toString(Double.parseDouble(year) - 10);
                    sum = 0;
                }

                line2 = readerTemp.readLine();
            }
        }
        catch(Exception e) {
            System.out.print("error in reader");
        }

        sizeChart.getData().add(seriesSize);
        tempChart.getData().add(seriesTempGIS);
        tempChart.getData().add(seriesTempGCAG);
        hist.getData().add(barSerie);
    }
}
