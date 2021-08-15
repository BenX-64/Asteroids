import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
public class ast {
    private int maxhp; //self explanatory
    int hp;
    private int x, y; 
    private int vx, vy;
    int ax, ay;
    int mass;
    int size;

    JLabel icon;
    String iconPath1, iconPath2, iconPath3, iconPath3b;

    public ast(int x, int y, int vx, int vy, int mass){
        int size1 = 50;
        int size2 = 75;
        int size3 = 100;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;

        if(mass == 1){
            size = size1;
        }else if(mass == 2){
            size = size2;
        }else{
            size = size3;
        }
        int size = 100; //L and w of asteroids of different masses

        String iconPath1;
        String iconPath2 = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid2.png";
        String iconPath3 = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3.png";
        String iconPath3b = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3b.png";
        ImageIcon image = new ImageIcon(iconPath3b);
        icon = new JLabel(image);
        icon.setBounds(x,y,size,size);
        

    }
    public void move(){
        x+=vx;
        y+=vy;
        if(size == 3){
            icon.setBounds(x,y,size,size);
        }
    }
    public boolean intersects(JLabel icon){
        // TODO Auto-generated method stub
        return false;
    }
    

}
