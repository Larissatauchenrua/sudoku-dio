package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        List<String> positions = Stream.of(args)
                .collect(Collectors.toList());

        int option = -1;
        while (true){
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removerNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }
    }

    private static void startGame(List<String> positionsList) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        Map<String, String> positions = positionsList.stream()
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                        ));

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++){
                var key = "%s,%s".formatted(i, j);
                var positionConfig = positions.get(key);

                if (positionConfig == null){
                    spaces.get(i).add(new Space(0, false));
                    continue;
                }
                int expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
    }

    private static void inputNumber() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a linha em que o número foi inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a coluna em que o número foi inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o nnúmero que vai entra na posição [%s,%s]\n", row, col);
        var value = runUntilGetValidNumber(1, 9);
        if (!board.changeValue(row, col, value)){
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", row, col);
        }
    }

    private static void removerNumber() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a linha em que o número foi inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a coluna em que o número foi inserido");
        var col = runUntilGetValidNumber(0, 8);
        if (!board.clearValue(row, col)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", row, col);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        String[] args = new String[81];
        int argsPos = 0;

        for (int i = 0; i < BOARD_LIMIT; i++){
            var rowList = board.getSpaces().get(i);
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var space = rowList.get(j);
                if (isNull(space.getActual()) || space.getActual() ==0) {
                    args[argsPos++] = "  ";
                } else if (space.isFixed()) {
                    args[argsPos++] = String.format("%2s", "*" + space.getActual());
                } else {
                    args[argsPos++] = String.format("%2s", " " + space.getActual().toString());
                }
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf(BoardTemplate.BOARD_TEMPLATE + '\n', (Object[]) args);
    }

    private static void showGameStatus() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
        if(board.hasErros()){
            System.out.println("O jogo contém erros");
        }else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void clearGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.next();
        }
        if(confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }

    private static void finishGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinished()){
            System.out.println("Parabéns você concluiu o jogo");
            showCurrentGame();
            board = null;
        }else if (board.hasErros()) {
            System.out.println("Seu jogo contém erros, verifique seu board e ajuste-o");
        }else {
            System.out.println("Você ainda precisa preencher algum espaço");
        }
    }


    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

}
