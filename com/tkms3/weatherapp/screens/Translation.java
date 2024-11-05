package com.tkms3.weatherapp.screens;

import javax.swing.*;

import com.tkms3.api.ApiManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Translation extends JPanel {

    private JTextArea inputTextArea;
    private JComboBox<String> fromLanguageBox;
    private JComboBox<String> toLanguageBox;
    private JTextArea outputTextArea;
    private JButton translateButton;
    private ApiManager am;

    public Translation(ApiManager am) {
        this.am = am;
        setBackground(new Color(0,128,128));

        // Create the main panel
        setLayout(new GridBagLayout());

        // Initialize components
        inputTextArea = new JTextArea(5, 30);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        fromLanguageBox = new JComboBox<>(new String[]{"English", "Spanish", "French", "German", "Chinese"});
        toLanguageBox = new JComboBox<>(new String[]{"English", "Spanish", "French", "German", "Chinese"});

        outputTextArea = new JTextArea(5, 30);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false); // Make output text area non-editable

        translateButton = new JButton("Translate");

        // Set up layout within the main panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // From language label and combo box
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("From Language:"), gbc);

        gbc.gridx = 1;
        add(fromLanguageBox, gbc);

        // To language label and combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("To Language:"), gbc);

        gbc.gridx = 1;
        add(toLanguageBox, gbc);

        // Input text area
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("Text to Translate:"), gbc);

        gbc.gridy = 3;
        add(new JScrollPane(inputTextArea), gbc);

        // Translate button
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(translateButton, gbc);

        // Output text area
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(new JLabel("Translated Text:"), gbc);

        gbc.gridy = 6;
        add(new JScrollPane(outputTextArea), gbc);

        // Action for translate button
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateText();
            }
        });
    }

    private void translateText() {
        String fromLanguage = (String) fromLanguageBox.getSelectedItem();
        String toLanguage = (String) toLanguageBox.getSelectedItem();
        String inputText = inputTextArea.getText();

        // Check if input text is empty
        if (inputText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to translate.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if the selected languages are the same
        if (fromLanguage.equals(toLanguage)) {
            JOptionPane.showMessageDialog(this, "From and To languages cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String translatedText = am.getTranslation(inputText,fromLanguage,toLanguage);
        outputTextArea.setText(translatedText);
    }
}
