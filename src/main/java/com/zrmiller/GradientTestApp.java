package com.zrmiller;

import com.zrmiller.core.colors.Gradient;
import com.zrmiller.gui.misc.GradientPanel;

import javax.swing.*;
import java.awt.*;

public class GradientTestApp {

    public static void main(String[] args) {
        Gradient testGradient = new Gradient();
        testGradient.addKey(0.5f, Color.GREEN);
        testGradient.addKey(0.1f, Color.RED);
        testGradient.addKey(0.8f, Color.BLUE);
        testGradient.addKey(0.6f, Color.YELLOW);

        Gradient heatGradient = new Gradient();
        heatGradient.addKey(0f, new Color(0, 0, 0));
        heatGradient.addKey(0.25f, new Color(17, 59, 229));
        heatGradient.addKey(0.5f, new Color(198, 21, 211));
        heatGradient.addKey(0.75f, new Color(220, 10, 49));
        heatGradient.addKey(1f, new Color(231, 220, 13));

        SwingUtilities.invokeLater(() -> {
            // Frame
            JFrame frame = new JFrame("Gradient Test");
            frame.setSize(800, 400);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // Gradient Panel
            GradientPanel panel = new GradientPanel();
            panel.setGradient(heatGradient);
            frame.add(panel);

            // Show frame
            frame.setVisible(true);
        });

    }

}
