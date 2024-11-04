package com.tkms3.weatherapp.widgets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Navbar extends JPanel {
    JButton w, c, d;

    public Navbar(CardLayout cardLayout, JPanel cardPanel) {
        super();
        this.setBackground(new Color(0, 108, 108));
        this.setLayout(new GridLayout(1, 3));  // Adjust layout for your buttons

        // Load icons (assuming these paths are correct)
        ImageIcon weatherIcon = loadImage("imgs/weather_icon.jpg");
        ImageIcon detailsIcon = loadImage("imgs/details_icon.jpg");
        ImageIcon currencyIcon = loadImage("imgs/currency_icon.jpg");

        // Create and configure buttons
        w = createButton(weatherIcon);
        c = createButton(currencyIcon);
        d = createButton(detailsIcon);

        // Weather button action - show the "Weather" card
        w.addActionListener(e -> cardLayout.show(cardPanel, "Weather"));

        // Currency Conversion button action - show the "CurrencyConversion" card
        c.addActionListener(e -> cardLayout.show(cardPanel, "CurrencyConversion"));

        // Place Details button action - show the "PlaceDetails" card
        d.addActionListener(e -> cardLayout.show(cardPanel, "PlaceDetails"));

        // Add buttons to Navbar
        this.add(w);
        this.add(c);
        this.add(d);
    }

    // Method to create and style a button
    private JButton createButton(ImageIcon icon) {
        JButton button = new JButton();
        button.setIcon(icon);
        button.setBackground(getBackground());
        button.setBorderPainted(false);
        return button;
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource: " + resourcePath);
        return null;
    }
}