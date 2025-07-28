package br.com.dio.ui.custom.screen;

import br.com.dio.model.GameStatusEnum;
import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;


    public MainScreen(Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void addFinishGameButton(JPanel mainPanel) {
        JButton finishGameButton = new JButton("Finalizar jogo");

       finishGameButton.addActionListener(e ->{
            if (boardService.gameIsFinished()) {
                JOptionPane.showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {

                JOptionPane.showMessageDialog(null, "Seu jogo contém alguma incosistência, ajuste e tente novamente");
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new JButton("Checar Status");

        checkGameStatusButton. addActionListener(e ->{
            boolean hasErros = boardService.hasErros();
            GameStatusEnum gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case COMPLETE -> "O jogo foi completo";
                case INCOMPLETE -> "O jogo está imcompleto";
                case NON_STARTED -> "O jogo não foi iniciado";
            };
            message += hasErros ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        JButton resetButton = new ResetButton(e ->{
           var dialogResult = JOptionPane.showConfirmDialog(
                   null,
                   "Deseja realamente reiniciar o jogo?",
                   "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
           );
           if (dialogResult == 0){
               boardService.reset();
           }
        });
        mainPanel.add(resetButton);
    }
}
