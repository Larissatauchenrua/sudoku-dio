package br.com.dio;

import br.com.dio.ui.custom.screen.MainScreen;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UIMain {

    public static void main(String[] args) {
        final Map<String, String> gameConfig = Stream.of(args)
                .map(s -> s.split(":"))
                .filter(s -> s.length == 2)
                .collect(Collectors.toMap(
                s -> s[0],
                s -> s[1]
        ));
        var mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();
    }
}
