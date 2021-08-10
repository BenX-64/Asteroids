import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

public class Main{
    boolean gameActive = true;
    JFrame board = new JFrame("Asteroids");
    int boardsize = 600;
    
    

    public Main(){
        board.setSize(boardsize,boardsize);
        board.setVisible(true);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void tick(){

    }

    public static void main(String[] args){
        Main m = new Main();
    }
}
