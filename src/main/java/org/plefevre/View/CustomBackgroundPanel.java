package org.plefevre.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

public class CustomBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public CustomBackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            int panelWidth = getWidth() + 8;
            int panelHeight = getHeight() + 8;
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            double scaleX = (double) panelWidth / imgWidth;
            double scaleY = (double) panelHeight / imgHeight;
            double scale = Math.max(scaleX, scaleY);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);
            int x = (panelWidth -4 - newWidth) / 2;
            int y = (panelHeight -4- newHeight) / 2;


            AffineTransform transform = new AffineTransform();
            transform.translate(x, y);
            transform.scale(scale, scale);

            g2d.drawImage(backgroundImage, transform, this);
            g2d.dispose();
        }    }
}

