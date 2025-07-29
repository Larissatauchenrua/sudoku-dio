
package br.com.dio;

import br.com.dio.ui.custom.screen.StartScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartScreen().setVisible(true);
        });
    }
}