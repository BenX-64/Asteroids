import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Area;

import java.util.ArrayList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JLayeredPane implements KeyListener{
    static boolean gameActive = true;

    static JFrame board = new JFrame("Asteroids");

    final int tickrate = 20; //ticks per second (changing this does nothing)

    static int boardys = 600; //board x and y size
    static int boardxs = 1000;
    
    int hp = 100, dps = 50, ma = 2, mt = 2; //player values
   
    player p = new player(hp, dps, ma, mt, false);
    player pr = new player(hp, dps, ma, mt, true);

    ArrayList<ast> astlist = new ArrayList<>();
    int maxasts = 15; //sets limit for max # of asteroids on board at once

    String cst = "Speed: " + String.format("%.2f",p.cs*20);
    JLabel cs = new JLabel(cst);

    String hps = "HP: " + 100*p.hp/p.maxhp + "%";
    JLabel hpi = new JLabel(hps);
    public Main(){

        board.addKeyListener(this);
        board.setSize(boardxs,boardys);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);
        board.getContentPane().setBackground(Color.black);

        setLayer(board.add(p.icon),1);
        setLayer(board.add(pr.icon),0);

        cs.setForeground(Color.white); //current speed
        cs.setBounds(p.psize, boardys-70, boardxs/2, 20);
        cs.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        setLayer(board.add(cs),0);
        
        hpi.setForeground(Color.white); //hp %
        hpi.setBounds(10,pr.y-20,boardxs, 20);
        hpi.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        setLayer(board.add(hpi),0);

        board.add(this);
        board.setVisible(true);
        setLayer(this, 3);

        while(gameActive){
            tick(); //self explanatory
        }
    }

    public void tick(){
        cst = "Speed: "+ String.format("%.2f", p.cs*20);
        cs.setText(cst);

        updatehpi();

        pr.move();
        p.move(); //self explanatory

        for(int i = 0; i< astlist.size(); i++){
            if(!astlist.get(i).icon.isVisible()){
                continue;
            }
            astlist.get(i).move();
            if(astlist.get(i).hitbox.intersects(p.hitbox.getBounds())){
                board.remove(astlist.get(i).icon);
                astlist.remove(i);
                if(i>0){
                    i--;
                }
                p.dmg(astlist.get(i).dmg);
                updatehpi();
                if(p.hp<=0){
                    p.hp = 0;
                    updatehpi();
                    endGame();
                }
                
            }
        }
        if(astlist.size()<=maxasts){
            spawn(); //sometimes spawns new asteroids
        }
        try {
            Thread.sleep(50); //20 ticks per second
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void spawn(){
        int spawndice = (int)(Math.random()*100+1);
        if(spawndice <= 5){
            int massdice = (int)(Math.random()*100+1);
            if(massdice > 90){
                astlist.add(new ast(3));
            }else if(massdice > 60){
                astlist.add(new ast(3));
            }else{
                astlist.add(new ast(3));
            }

            //band-aid fix for a bug that makes 
            //the newest asteroid teleport to the middle when thrusting is true
            astlist.get(astlist.size()-1).icon.setVisible(false); 
            setLayer(board.add(astlist.get(astlist.size()-1).icon), 2);
            if(astlist.size()>1){
                astlist.get(astlist.size()-2).icon.setVisible(true);
            }
        }
    }
    public void paint(Graphics g){
        super.paintComponent(g);

        /*
        g.setColor(Color.black);
        g.fillRect(0, 0, boardxs, boardys);
        */      
    }
    private void endGame(){
        gameActive = false;
    }
    public void resetGame(){
        while(astlist.size()>0){
            astlist.remove(0);
        }
        p.reset();
        pr.reset();

    }
    public void updatehpi(){
        hps = "HP: " + 100*p.hp/p.maxhp + "%";
        if(100*p.hp/p.maxhp<=25){
            hpi.setForeground(Color.red);
        }
        hpi.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        hpi.setText(hps);
    }

    @Override
    public void keyPressed(KeyEvent arg0){
       // System.out.println(arg0.getKeyCode());
        if(gameActive){
            if(arg0.getKeyCode()==38){
                p.thrustOn();
            }
            if(arg0.getKeyCode() == 39){
                p.isTurning = 1;
                pr.isTurning = 1;
            }
            if(arg0.getKeyCode() == 37){
                p.isTurning = 2;
                pr.isTurning = 2;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent arg0){
        if(gameActive){
            if(arg0.getKeyCode()==38){
                p.thrustOff();
            }
            if(p.isTurning != 0 && pr.isTurning!=0 && (arg0.getKeyCode() == 37 || arg0.getKeyCode() == 39)){
                p.isTurning = 0;
                pr.isTurning = 0;
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent arg0){

    }

    public static void main(String[] args) throws Exception{
        Main m = new Main(); 
    }
}
