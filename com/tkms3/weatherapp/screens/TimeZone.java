package com.tkms3.weatherapp.screens;

import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import com.tkms3.api.ApiManager;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeZone extends JPanel {
    private JButton searchButton;
    private JButton removeButton;
    private final String apiKey = "CFSK56Q5419T";
    private BufferedImage clockImage;
    private BufferedImage searchIconImage;
    private JPanel timeZoneContainer;

    public TimeZone(ApiManager am) {
        setBackground(new Color(0, 128, 128));
        setLayout(new BorderLayout());

        try {
            clockImage = resizeImage(ImageIO.read(new File("imgs/clock.png")), 100, 100);
            searchIconImage = resizeImage(ImageIO.read(new File("imgs/search_icon.png")), 40, 40);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel titleLabel = new JLabel("World Clock", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        searchButton = new JButton(new ImageIcon(searchIconImage));
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setContentAreaFilled(false);
        searchButton.setBackground(new Color(85, 170, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> {
            String location = JOptionPane.showInputDialog("Enter Time Zone (e.g., America/New_York):");
            if (location != null && !location.trim().isEmpty()) {
                fetchLocalTime(am, location);
            }
        });

        removeButton = new JButton("Clear All");
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setBackground(new Color(255, 102, 102));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> {
            timeZoneContainer.removeAll();
            timeZoneContainer.revalidate();
            timeZoneContainer.repaint();
        });

        timeZoneContainer = new JPanel();
        timeZoneContainer.setLayout(new BoxLayout(timeZoneContainer, BoxLayout.Y_AXIS));
        timeZoneContainer.setBackground(new Color(50, 60, 70));

        JScrollPane scrollPane = new JScrollPane(timeZoneContainer);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        scrollPane.getViewport().setBackground(new Color(50, 60, 70));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(0, 128, 128));
        bottomPanel.add(searchButton);
        bottomPanel.add(removeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void fetchLocalTime(ApiManager am, String location) {
        try {
            Gson gson = new Gson();
            TimeResponse timeResponse = gson.fromJson(am.getTimeZone(location), TimeResponse.class);
            String formattedTime = formatDateTime(timeResponse.dateTime);
            addTimeZoneEntry(timeResponse.timeZone, formattedTime, "Offset info");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching time: " + e.getMessage());
        }
    }

    private void addTimeZoneEntry(String location, String time, String offset) {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BorderLayout());
        entryPanel.setPreferredSize(new Dimension(350, 80));
        entryPanel.setBackground(new Color(60, 70, 85));
        entryPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));

        JLabel timeLabel = new JLabel("<html><strong>Location:</strong> " + location + "<br><strong>Time:</strong> " + time + "</html>");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLabel.setForeground(new Color(173, 216, 230));

        entryPanel.add(timeLabel, BorderLayout.CENTER);
        timeZoneContainer.add(entryPanel);
        timeZoneContainer.revalidate();
        timeZoneContainer.repaint();
    }

    private String formatDateTime(String dateTimeStr) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            return dateTime.format(formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing date-time: " + e.getMessage());
            return dateTimeStr;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private static class TimeResponse {
        String dateTime;
        String timeZone;
    }
}
