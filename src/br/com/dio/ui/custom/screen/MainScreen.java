package br.com.dio.custom.screen;

import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.custom.button.ResetButton;
import br.com.dio.custom.frame.MainFrame;
import br.com.dio.custom.input.NumberText;
import br.com.dio.custom.panel.MainPanel;
import br.com.dio.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import javax.swing.JPanel;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;


    public MainScreen(Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                List<Space> spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> space,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow){
        if (space == null || space.isEmpty() || space.get(0).isEmpty()){
            throw new IllegalArgumentException("A matriz de espaços está vazia, nula ou mal formatada.");
        }
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
        for (int c = initCol; c <= endCol; c++) {
            spaceSector.add(space.get(c).get(r));
        }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new JButton("Finalizar jogo");

       finishGameButton.addActionListener(e ->{
            if (boardService.gameIsFinished()) {
                showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {

                showMessageDialog(null, "Seu jogo contém alguma incosistência, ajuste e tente novamente");
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
            showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
           var dialogResult = showConfirmDialog(
                   null,
                   "Deseja realamente reiniciar o jogo?",
                   "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
           );
           if (dialogResult == YES_NO_OPTION){
               boardService.reset();
               notifierService.notify(CLEAR_SPACE);
           }
        });
        mainPanel.add(resetButton);
    }
}
