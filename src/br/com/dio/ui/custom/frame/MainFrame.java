package br.com.dio.custom.frame;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(final Dimension dimension, JPanel Panel){
        super("Sudoko");
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(Panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
