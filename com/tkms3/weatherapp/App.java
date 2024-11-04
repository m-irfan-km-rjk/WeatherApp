package com.tkms3.weatherapp;

import com.tkms3.api.ApiManager;
import com.tkms3.weatherapp.screens.CurrencyConversion;
import com.tkms3.weatherapp.screens.PlaceDetails;
import com.tkms3.weatherapp.screens.Weather;
import com.tkms3.weatherapp.widgets.Navbar;

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
        win.setResizable(false);

        // Set up the CardLayout and JPanel container for the cards
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Create instances of each screen and add them to cardPanel
        PlaceDetails pd = new PlaceDetails(a);
        Weather wp = new Weather(a);
        CurrencyConversion c = new CurrencyConversion(a);

        // Add each panel to cardPanel with a unique name
        cardPanel.add(wp, "Weather");
        cardPanel.add(pd, "PlaceDetails");
        cardPanel.add(c, "CurrencyConversion");

        // Navbar with references to each screen and cardLayout for switching
        Navbar nb = new Navbar(cardLayout, cardPanel);

        // Add cardPanel and navbar to the main frame
        win.add(cardPanel, BorderLayout.CENTER);
        win.add(nb, BorderLayout.SOUTH);

        win.setVisible(true);
    }
}
