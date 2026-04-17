package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    private static final int size = 9;
    private int[][] board = new int[size][size];
    private Random random = new Random();


    //Verifies if the generated number is valid or not
    private boolean isValid(int row, int col, int num) {
        int i, j;
        for (i = 0; i < size; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        //Because it's integer division and multiplication, it helps to scan needed boxes
        int boxRow = (row / 3 ) * 3;
        int boxCol = (col / 3) * 3;

        //Scans the positions to verify if it's valid
        for(i = 0; i < 3; i++){
            for(j = 0; j < 3; j++){
                if(board[boxRow + i][boxCol + j] == num) return false;
            }
        }
        return true;
    }

    //Scrambles the numbers
    private List<Integer> getShuffledNumbers(){
        List<Integer> nums = new ArrayList<>();
        int i;
        for(i = 1; i<= 9; i++) nums.add(i);
        Collections.shuffle(nums);
        return nums;
    }


    private boolean fillBoard(int row, int col){

        if(row == size) return true;

        int nextRow = (col == size -1) ? row + 1 : row;
        int nextCol = (col + 1) % size;

        List<Integer> numbers = getShuffledNumbers();

        for(int num : numbers){
            if(isValid(row, col, num)){
                board[row][col] = num;

                if(fillBoard(nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }


    //Randomly remove numbers, then player can insert it himself
    //int attempts controls the difficulty level (the bigger, hardest)
    public void removeNumbers(int attempts){
        while(attempts > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                attempts--;
            }
        }
    }


    //Converts the whole board (matrix) into String format
    //(because I was using args to generate a game, and, for now, I don't wanna change the whole source structure)
    public String[] formatConverse(){
        List<String> result = new ArrayList<>();

        int x, y;
        for(x = 0; x < 9; x++){
            for(y = 0; y < 9; y++){
                int value = board[x][y];
                boolean fixed = value != 0;

                result.add(x + "," + y + ";" + value + "," + fixed);
            }
        }
        return result.toArray(new String[0]);
    }


    public int[][] generateFullBoard() {
        fillBoard(0 ,0);
        return board;
    }


}
