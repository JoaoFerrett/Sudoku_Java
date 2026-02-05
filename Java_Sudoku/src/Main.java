import models.Board;
import models.Space;
import util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import static util.BoardTemplate.BOARD_TEMPLATE;

public class Main{

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        final var positions = Stream.of(args).collect(
                Collectors.toMap(
                        k -> k.split(";")[0], v -> v.split(";")[1]
                )
        );

        var option = -1;
        while(true){
            IO.println("Select one option:");
            IO.println("1 - Start new game");
            IO.println("2 - Put new number");
            IO.println("3 - Remove a number");
            IO.println("4 - Look at current game");
            IO.println("5 - Verify game status");
            IO.println("6 - Clean game");
            IO.println("7 - End game");
            IO.println("8 - Exit");

            option = scanner.nextInt();

            switch (option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> verifyStatus();
                case 6 -> cleanGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> IO.println("Invalid option");
            }


        }
    }

    private static boolean verifyIfGameStarted(){
        if (isNull(board)) {
            System.out.println("The game has not started yet");
            return true;
        }else
            return false;
    }

    private static void startGame(Map<String, String> positions) {
        if (!isNull(board)) {
            System.out.println("There is a started game already!");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++){
            spaces.add(new ArrayList<>());
            for(int j = 0; j < BOARD_LIMIT; j++){
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("The game is ready to get started");
    }



    private static void inputNumber() {
        if(verifyIfGameStarted()){
            return;
        }

        System.out.println("Inform a row that number will be insert into:");
        var row = runTillGetValidNum(0, 8);
        System.out.println("Inform a column that number will be insert into");
        var col = runTillGetValidNum(0, 8);
        System.out.println("Inform a number that will be inputed into position: [" + row + ", " + col + "]");
        var value = runTillGetValidNum(1, 9);
        if(!board.changeValue(row, col, value)){
            System.out.println("The position [" + row + ", " + col + "] has an fixed value");
        }
    }

    private static void removeNumber() {
        if(verifyIfGameStarted()){
            return;
        }

        System.out.println("Inform a row that number will be insert into:");
        var row = runTillGetValidNum(0, 8);
        System.out.println("Inform a column that number will be insert into");
        var col = runTillGetValidNum(0, 8);
        System.out.println("Inform a number that will be inputed into position: [" + row + ", " + col + "]");
        if(!board.clearValue(row, col)){
            System.out.println("The position [" + row + ", " + col + "] has an fixed value");
        }
    }

    private static void showCurrentGame() {
        if(verifyIfGameStarted()){
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for(int i = 0; i < BOARD_LIMIT; i++){
            for(var col: board.getSpaces()){
                args[argPos++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void verifyStatus() {
        if(verifyIfGameStarted()){
            return;
        }

        System.out.println(board.getStatus().getLabel());
        if(board.hasErrors()){
            System.out.println("Game contains errors");
        }else{
            System.out.println("The Game has no errors");
        }
    }

    private static void cleanGame() {
        if(verifyIfGameStarted()){
            return;
        }

        System.out.println("Are you sure?");
        var confirm = scanner.next();
        while(!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("no")){
            System.out.println("yes / no");
            confirm = scanner.next();
        }
        if(confirm.equalsIgnoreCase("yes")){
            board.reset();
        }
    }

    private static void finishGame() {
        if(verifyIfGameStarted()){
            return;
        }

        if(board.gameIsFinished()){
            System.out.println("Nice, you did it!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Your game has errors yet!");
        }else{
            System.out.println("You still have to fill in a space!");
        }
    }



    private static int runTillGetValidNum(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.println("Inform a number between " + min + " and " + max);
            current = scanner.nextInt();
        }
        return current;
    }



}