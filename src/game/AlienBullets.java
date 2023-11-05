
package game;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author
 */
//r

// ĐẠN CỦA ALIEN
public class AlienBullets extends Bullets{
    // TỐC ĐỘ
    final int MISSILE_SPEED = 5;
    final int BOARD_HEIGHT = 800;
    private int imgWidth, imgHeight;    // width and height of the image
    Image Bullets;
    
    public AlienBullets(int x, int y){
        super(x, y);
        loadBullets("bullets/bullets 1.png");
        super.setW(imgWidth);
        super.setH(imgHeight);
    }
    
    // CẬP ẢNH - VỊ TRÍ ĐẠN
    private void loadBullets(String name){
        URL imgUrl = null;
        imgUrl = getClass().getClassLoader().getResource(name);
        if (imgUrl == null) {
            System.err.println("Couldn't find file: ");
        } else {
            try {
                Bullets = ImageIO.read(imgUrl);  // load image via URL
            } catch (IOException ex) {
            }
        }
        imgWidth = Bullets.getWidth(null);
        imgHeight = Bullets.getHeight(null);
    }
    
    public Image getImage(){
        return Bullets;
    }
    
    // ĐẠN DI CHUYỂN 
    public void move() {
        // TỐC ĐỘ
        super.y += MISSILE_SPEED;
        // KHI ĐỘ CAO > 800 THÌ K HIỂN THỊ
        if (y > BOARD_HEIGHT) {
            vis = false;
        }
    }
}
