package com.tkms3.weatherapp.screens;

import com.tkms3.api.ApiManager;
import com.tkms3.weatherapp.widgets.RoundTextField;
import com.google.gson.JsonObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrencyConversion extends JPanel {

    private RoundTextField inputAmountField;
    private JComboBox<String> fromCurrencyBox;
    private JComboBox<String> toCurrencyBox;
    private RoundTextField outputAmountField;
    private JButton convertButton;
    private JsonObject currencyData;

    public CurrencyConversion(ApiManager am) {
        currencyData = am.getCurrencyData();
        initComponents();
    }

    private void initComponents() {
        setBackground(new Color(0, 128, 128)); // Background color
        setLayout(new GridBagLayout()); // Use GridBagLayout for better control over layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components
        gbc.weightx = 1.0; // Allow components to expand

        // Create rounded text fields and buttons
        inputAmountField = new RoundTextField(20, 40, 40);
        inputAmountField.setForeground(Color.BLACK);
        inputAmountField.setPreferredSize(new Dimension(300, 50));

        fromCurrencyBox = new JComboBox<>();
        toCurrencyBox = new JComboBox<>();
        outputAmountField = new RoundTextField(20, 40, 40);
        outputAmountField.setEditable(false); // Make output field non-editable
        outputAmountField.setForeground(Color.BLACK);
        outputAmountField.setPreferredSize(new Dimension(300, 50));

        convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(64, 224, 208)); // Button color
        convertButton.setForeground(Color.BLACK);
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        Font comboBoxFont = new Font("Arial", Font.PLAIN, 18); // Change size as needed
        fromCurrencyBox.setFont(comboBoxFont);
        toCurrencyBox.setFont(comboBoxFont);

        // Initialize combo boxes with currency options
        String[] currArray = currencyData.getAsJsonObject("data").keySet().toArray(new String[0]);
        fromCurrencyBox.setModel(new DefaultComboBoxModel<>(currArray));
        toCurrencyBox.setModel(new DefaultComboBoxModel<>(currArray));

        // Add components to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(fromCurrencyBox, gbc);

        gbc.gridy = 1;
        add(inputAmountField, gbc);

        gbc.gridy = 2;
        add(toCurrencyBox, gbc);

        gbc.gridy = 3;
        add(outputAmountField, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        add(convertButton, gbc);
    }

    private void convertCurrency() {
        try {
            String fromCurrency = (String) fromCurrencyBox.getSelectedItem();
            String toCurrency = (String) toCurrencyBox.getSelectedItem();
            double amount = Double.parseDouble(inputAmountField.getText());

            // Check if the selected currencies are the same
            if (fromCurrency.equals(toCurrency)) {
                JOptionPane.showMessageDialog(this, "From and To currencies cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double conversionRate = getConversionRate(fromCurrency, toCurrency);
            double convertedAmount = amount * conversionRate;
            outputAmountField.setText(String.format("%.2f", convertedAmount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getConversionRate(String fromCurrency, String toCurrency) {
        double fromRate = currencyData.getAsJsonObject("data").get(fromCurrency).getAsDouble();
        double toRate = currencyData.getAsJsonObject("data").get(toCurrency).getAsDouble();
    
        return toRate / fromRate;
    }
}
