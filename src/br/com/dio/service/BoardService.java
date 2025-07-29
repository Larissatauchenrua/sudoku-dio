package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {

    private final static int BOARD_LIMIT = 9;

    private final Board board;

    public BoardService(final Map<String, String> gameConfig) {
       this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces(){
        return board.getSpaces();
    }

    public void reset(){
        board.reset();
    }

    public boolean hasErros(){
        return board.hasErros();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameIsFinished(){
        return board.gameIsFinished();
    }

    private List<List<Space>> initBoard(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());

            for (int j = 0; j < BOARD_LIMIT; j++) {
                var key = "%s,%s".formatted(i, j);
                var positionConfig = gameConfig.get(key);

                if (positionConfig == null) {
                    spaces.get(i).add(new Space(0, false));
                    continue;
                }
                String[] parts = positionConfig.split(":");
                int expected = Integer.parseInt(parts[0]);
                boolean fixed = Boolean.parseBoolean(parts[1]);

                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        return spaces;
    }
}


