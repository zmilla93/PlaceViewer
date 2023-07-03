package com.zrmiller.gui.misc;

import com.zrmiller.core.colors.Gradient;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that draws a custom color gradient.
 * Used for debugging.
 */
public class GradientPanel extends JPanel {

    private Gradient gradient;

    public void setGradient(Gradient gradient) {
        this.gradient = gradient;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gradient == null || gradient.getKeyCount() < 1) return;
        for (int i = 0; i < getWidth(); i++) {
            float f = i / (float) getWidth();
            Color color = gradient.resolveColor(f);
            g.setColor(color);
            g.fillRect(i, 0, 1, getHeight());
        }
    }

}
