import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.geom.Area;

public class shot {
    double x;
    double y;
    int size = 20;
    private double mv = 50; //max velocity
    private double vx;
    private int vy;
    int dps;

    JLabel icon;
    String iconPath; 
    ImageIcon image;
    Area hitbox;


    public shot(int x, int y, int dps, int deg){
        iconPath = new File("").getAbsolutePath() + "/assets/shot.png";
        image = new ImageIcon(iconPath);
        this.icon = new JLabel(image);
        icon.setBounds(x,y,size,size);
        hitbox = new Area(icon.getBounds());

        this.x = x;
        this.y = y;
        this.dps = dps;

        this.vx = -(int)(mv*Math.sin(Math.toRadians(deg)));
        this.vy = -(int)(mv*Math.cos(Math.toRadians(deg)));
    }
    public void move(){
        x+=vx;
        y+=vy;
        icon.setBounds((int)x,(int)y,size,size);
        hitbox = new Area(icon.getBounds());

    }
    public void shadowcheck(){ //checks ahead if the shot's path intersects with any asteroids
        //for(int i = 0; i< mv)
    }
}
