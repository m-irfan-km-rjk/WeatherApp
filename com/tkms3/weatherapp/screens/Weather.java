package com.tkms3.weatherapp.screens;

import com.tkms3.api.ApiManager;
import com.tkms3.weatherapp.widgets.*;
import com.google.gson.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Weather extends JPanel{

    private JsonObject raw_weatherData;

    public Weather(ApiManager weatherapi){
        
        setBackground(new Color(0, 128, 128));
        setLayout(null);

        // Create a rounded JTextField
        RoundTextField T1 = new RoundTextField(20, 40, 40); 
        T1.setForeground(Color.BLACK);   
        T1.setBounds(40,20,300,100); 


        // Create a panel for the top area and add the text field
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);  
        topPanel.add(T1); 
        topPanel.setBounds(0,0,420,100);  

        JPanel midPanel = new JPanel();
        midPanel.setOpaque(false);
        midPanel.setLayout(null);
        midPanel.setBounds(0,100,420,520);

        //weather img
        JLabel weatherConditionImg = new JLabel(loadImage("imgs/cloudy.png"));
        weatherConditionImg.setBounds(100,0,250,220);
        midPanel.add(weatherConditionImg);

        //temperature text
        JLabel tempText = new JLabel("10 C");
        tempText.setBounds(150,230,350,50);
        tempText.setFont(new Font("Dialog",Font.BOLD, 48));
        midPanel.add(tempText);

        //weather description

        JLabel weatherDesp = new JLabel("Cloudy");
        weatherDesp.setBounds(170,270,350,35);
        weatherDesp.setFont(new Font("Dialog",Font.PLAIN,28));
        midPanel.add(weatherDesp);

        //humidity Image
        JLabel humidImg = new JLabel(loadImage("imgs/humidity.png"));
        humidImg.setBounds(45,400,74,66);
        midPanel.add(humidImg);

        //humidity text
        JLabel humidText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidText.setBounds(120,400,85,55);
        humidText.setFont(new Font("Dialog",Font.PLAIN,16));
        midPanel.add(humidText);

        //wind speed Image
        JLabel windsImg = new JLabel(loadImage("imgs/windspeed.png"));
        windsImg.setBounds(220,400,75,55);
        midPanel.add(windsImg);
        //wind speed Text
        JLabel windsText = new JLabel("\"<html><b>Windspeed</b> 15km/h</html>\"");
        windsText.setBounds(310,400,85,55);
        windsText.setFont(new Font("Dialog",Font.PLAIN,16));
        midPanel.add(windsText);
        //Unit of temp
        JCheckBox Faheren = new JCheckBox("Fahrenheit",false);
        Faheren.setBounds(170,470,150,30);
        Faheren.setOpaque(false);
        midPanel.add(Faheren);
        // search button
        JButton searchButton = new JButton(loadImage("imgs/search.png"));

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBackground(getBackground());
        searchButton.setBorderPainted(false);
        searchButton.setBounds(375, 20, 20, 20);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = T1.getText();

                // validate input - remove whitespace to ensure non-empty text
                if(userInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }

                // retrieve weather data
                try {
                    raw_weatherData = weatherapi.getWeather(userInput);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println(raw_weatherData);
                //format the data
                JsonObject weather= formatdata(raw_weatherData);
                // update gui
                

                // update weather image
                String weatherCondition = (String) weather.get("weather_condition").getAsString();

                // depending on the condition, we will update the weather image that corresponds with the condition
                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImg.setIcon(loadImage("imgs/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImg.setIcon(loadImage("imgs/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImg.setIcon(loadImage("imgs/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImg.setIcon(loadImage("imgs/snow.png"));
                        break;
                }

                // update temperature text
                double temperature = (double) weather.get("temperature").getAsDouble();
                if (Faheren.isSelected() == false){
                    tempText.setText(temperature + " C");
                }
                else{
                    tempText.setText((((temperature*9)/5)+32) + " F");
                }
                

                // update weather condition text
                weatherDesp.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weather.get("humidity").getAsLong();
                humidText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windspeed = (double) weather.get("windspeed").getAsDouble();
                windsText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");


            }
        });
        topPanel.add(searchButton);
        
         // Add components to the frame using BorderLayout
        add(topPanel);      
        add(midPanel);
        setVisible(true);
    }
    private static JsonObject formatdata(JsonObject raw_weatherData){
        //getting  info from JsonOBj
        JsonObject weatherData = new JsonObject();
        JsonObject hourly = (JsonObject) raw_weatherData.get("hourly");

        JsonArray time = (JsonArray) hourly.get("time");
        int index = indexOfcurrentTime(time);

        JsonArray temparr = (JsonArray) hourly.get("temperature_2m");
        double temperature = (double) temparr.get(index).getAsDouble();

        JsonArray weathercode = (JsonArray) hourly.get("weathercode");
        String weatherCondition = ConvertWeatherCode((long) weathercode.get(index).getAsLong());

        JsonArray relativeHumidity = (JsonArray) hourly.get("relativehumidity_2m");
        long humidity = (long) relativeHumidity.get(index).getAsLong();

        // get windspeed
        JsonArray windspeedData = (JsonArray) hourly.get("windspeed_10m");
        double windspeed = (double) windspeedData.get(index).getAsDouble();

        weatherData.addProperty("temperature", temperature);
        weatherData.addProperty("weather_condition", weatherCondition);
        weatherData.addProperty("humidity", humidity);
        weatherData.addProperty("windspeed", windspeed);
        return weatherData;
    }
    //function to find index of current time
    private static int indexOfcurrentTime(JsonArray timeList){
            LocalDateTime curTime = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
            String formatedTime = curTime.format(formatter);
            int ind = 0;
            for(int i = 0; i < timeList.size(); i++){
                String time = (String) timeList.get(i).getAsString();
                if(time.equalsIgnoreCase(formatedTime)){
                    // return the index
                    ind =i;
                }
            }
            return ind;
    }
    // function to convert weather code into weather conditions
    private static String ConvertWeatherCode(long weathercode){
        String weatherCondition = "";
        if(weathercode == 0L){
            // clear
            weatherCondition = "Clear";
        }else if(weathercode > 0L && weathercode <= 3L){
            // cloudy
            weatherCondition = "Cloudy";
        }else if((weathercode >= 51L && weathercode <= 67L)
                    || (weathercode >= 80L && weathercode <= 99L)){
            // rain
            weatherCondition = "Rain";
        }else if(weathercode >= 71L && weathercode <= 77L){
            // snow
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
    private ImageIcon loadImage(String resourcePath){
        try{
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon so that our component can render it
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }





       
}