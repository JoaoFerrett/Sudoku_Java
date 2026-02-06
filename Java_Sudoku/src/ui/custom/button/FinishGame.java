package ui.custom.button;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FinishGame extends JButton {

    public FinishGame(final ActionListener actionListener){
        this.setText("Finish Game");
        this.addActionListener(actionListener);
    }

}