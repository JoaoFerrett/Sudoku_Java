package ui.custom.screen;

import models.Space;
import service.BoardService;
import service.NotifierService;
import ui.custom.button.CheckGameStatus;
import ui.custom.button.FinishGame;
import ui.custom.button.ResetButton;
import ui.custom.frame.MainFrame;
import ui.custom.input.NumberText;
import ui.custom.panel.MainPanel;
import ui.custom.panel.SudokuSector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.*;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static service.EventEnum.CLEAR_SPACE;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig){

        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension , mainPanel);
        for(int r = 0; r < 9; r+=3){
            var endRow = r+2;
            for(int c = 0; c < 9; c+=3){
                var endCol = c+2;
                var spaces = getSpacesFromSpector(boardService.getSpaces(), c, r, endCol, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(generateSection(spaces));
            }
        }

        addResetButton(mainPanel);
        addFinishButton(mainPanel);
        addCheckGameStatusButton(mainPanel);

        mainFrame.revalidate();
        mainFrame.repaint();

    }

    private List<Space> getSpacesFromSpector(List<List<Space>> spaces, final int initCol, final int initRow,
                                                                        final int endCol, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for(int r = initRow; r <= endRow; r++){
            for(int c = initCol; c <= endCol; c++){
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }


    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscriber(CLEAR_SPACE, t));
        return new SudokuSector(fields);
     }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatus(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "The game has not started yet";
                case INCOMPLETE -> "The game is incomplete";
                case COMPLETE -> "The game is completed";
            };
            message += hasErrors ? " and have errors!" : " and have no errors";
            showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }


    private void addFinishButton(JPanel mainPanel) {
        finishGameButton = new FinishGame(e ->{
            if(boardService.gameIsFinished()){
                showMessageDialog(null, "You finished the game!");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            }else{
                var message = "Your game has errors, try changes!";
                showMessageDialog(null, message);
            }
        });
        mainPanel.add(finishGameButton);
    }


    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
            var dialogResult = showConfirmDialog(
                    null,
                    "Are you really sure that you want to clear the game?",
                    "Clean Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE

            );
            if (dialogResult == 0){
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }


}
