package com.tkms3.weatherapp.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;

public class RoundTextField extends JTextField {

    private int arcWidth;  // Width of the corner arc
    private int arcHeight; // Height of the corner arc

    public RoundTextField(int columns, int arcWidth, int arcHeight) {
        super("", columns); // Start with an empty text field
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smoother edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint the rounded rectangle background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
        
        // Paint the text field's content
        super.paintComponent(g2);
        
        g2.dispose();  // Clean up resources
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Paint the rounded rectangle border
        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
        
        g2.dispose();  // Clean up resources
    }
    
    @Override
    public void setBorder(Border border) {
        // Override to remove the default border, if any
        super.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Optional padding
    }
}