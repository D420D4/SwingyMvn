package org.plefevre.View;

import javax.swing.*;
import java.awt.*;

public class TransparentPanel extends JPanel {
    private final Color backgroundColor;

    public TransparentPanel(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2d.dispose();
        super.paintComponent(g);
    }
}
