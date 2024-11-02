package com.tkms3.weatherapp.screens;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import com.tkms3.api.ApiManager;
import com.tkms3.weatherapp.widgets.RoundTextField;

public class PlaceDetails extends JPanel {

    public PlaceDetails(ApiManager apm) {
        setLayout(new BorderLayout());

        JPanel search = new JPanel(new GridBagLayout());
        JEditorPane pn = new JEditorPane("text/html", "<body style='background-color: #40E0D0; font-size:20px'><center><b>Enter the place name</b></center></body>");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        JLabel slabel = new JLabel("Place");
        slabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        search.add(slabel, gbc);

        RoundTextField input = new RoundTextField(20, 10, 10);
        gbc.gridx = 1; // Second column
        gbc.weightx = 1.0; // Allow the text field to grow
        search.add(input, gbc);

        JButton sbutton = new JButton("Get Details");
        sbutton.setPreferredSize(new Dimension(100, 30));
        sbutton.addActionListener(e -> {
            String place = input.getText();
            input.setText("");
            if(place.equals("")) {
                ;
            } else {
                pn.setText("<body style='background-color: #40E0D0; font-size:20px'><center><b>Loading...</b></center></body>");
                try {
                    String content = apm.getPlaceDetails(place)
                            .replace("`", "")
                            .replaceFirst("html", "");
        
                    pn.setText(content);
                    System.out.println(content);
                } catch (Exception ex) {
                    pn.setText("<p>Error fetching details. Please try again.</p>");
                    ex.printStackTrace();
                }
            }
        });
        gbc.gridx = 2; // Third column
        gbc.weightx = 0; // Do not allow the button to grow
        search.add(sbutton, gbc);
        
        add(search, BorderLayout.NORTH);

        pn.setEditable(false);

        pn.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(pn);
        add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }
}
