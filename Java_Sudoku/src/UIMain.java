import service.SudokuGenerator;
import ui.custom.screen.MainScreen;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UIMain {

    public static void main(String[] args){

        SudokuGenerator generator = new SudokuGenerator();

        generator.generateFullBoard();
        generator.removeNumbers(40);

        String[] data = generator.formatConverse();

        final var gameConfig = Stream.of(data).collect(
                Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                )
        );

        var mainScreen = new MainScreen(gameConfig);
        mainScreen.buildMainScreen();

    }

}