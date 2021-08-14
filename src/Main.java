import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import java.awt.Color;
import java.awt.Graphics;

import java.util.LinkedList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JPanel implements KeyListener{
    boolean gameActive = true;

    static JFrame board = new JFrame("Asteroids");

    static int boardys = 600; //board x and y size
    static int boardxs = 1000;
    
    int hp = 100, dps = 50, ma = 2, mt = 2; //player values
   
    player p = new player(hp, dps, ma, mt);

    LinkedList<ast> asts = new LinkedList<>();

    public Main(){
        asts.add(new ast(0,0,0,0,3));
        board.setSize(boardxs,boardys);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);
        board.addKeyListener(this);

        board.add(asts.get(0).icon);
        board.add(p.icon);

        board.add(this);
        board.setVisible(true);
        
        while(gameActive){
            tick();
        }
    }

    public void tick(){
        p.move(); //self explanatory
        for (int i = 0, as = asts.size(); i< as; i++){
            
        }
        try {
            Thread.sleep(50); //20 ticks per second
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, boardxs, boardys);

    }

    @Override
    public void keyPressed(KeyEvent arg0){
        //System.out.println(arg0.getKeyCode());
        if(arg0.getKeyCode()==38){
            p.isBoosting = true;
        }
        if(arg0.getKeyCode() == 39){
            p.isTurning = 1;
        }
        if(arg0.getKeyCode() == 37){
            p.isTurning = 2;
        }
    }
    @Override
    public void keyReleased(KeyEvent arg0){
        if(arg0.getKeyCode()==38){
            p.isBoosting = false;
        }
        if(p.isTurning != 0 && (arg0.getKeyCode() == 37 || arg0.getKeyCode() == 39)){
            p.isTurning = 0;
        }
    }
    @Override
    public void keyTyped(KeyEvent arg0){

    }

    public static void main(String[] args) throws Exception{
        Main m = new Main(); 
    }
}
