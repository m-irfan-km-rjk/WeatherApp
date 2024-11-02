package com.tkms3.weatherapp;

import com.tkms3.api.ApiManager;
import com.tkms3.weatherapp.screens.PlaceDetails;
import com.tkms3.weatherapp.screens.Weather;

import javax.swing.*;
import java.awt.*;

public class App {

    public static void main(String[] args) {
        // Initialize ApiManager
        ApiManager a = new ApiManager();
        
        // Create and set up the JFrame
        JFrame win = new JFrame("Weather App");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setBounds(50, 50, 420, 840);
        win.setBackground(Color.BLUE);
        //win.setResizable(false);
        
        // Create an instance of PlaceDetails JPanel
        PlaceDetails pd = new PlaceDetails(a);
        Weather wp = new Weather(a);

        win.add(wp, BorderLayout.CENTER);

        win.setVisible(true);
    }
}
