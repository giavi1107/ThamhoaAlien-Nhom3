
package game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author
 */

//r
@SuppressWarnings("serial")
public class PhiThuyen extends InSpace{
    
    int heath = 20;
    
    // HỈNH ẢNH PHI THUYỀN KHI DI CHUYỂN - LÊN - XUỐNG - TRÁI - PHẢI
    private String[] imgFilenames = {
         "phithuyen/pt1.png", "phithuyen/pt2.png"};
    
    private String[] imgFilenames_left = {
         "phithuyen/pt3.png", "phithuyen/pt4.png"};
    
    private String[] imgFilenames_right = {
         "phithuyen/pt5.png", "phithuyen/pt6.png"};
    
    private String[] imgFilenames_down = {
         "phithuyen/pt7.png"};
    
    private String[] imgFilenames_forward = {
         "phithuyen/pt8.png", "phithuyen/pt9.png"};
    
    
    private Image[] imgFrames;    // array of Images to be animated
    private int currentFrame = 0; // current frame number
    private int imgWidth, imgHeight;    // width and height of the image
    private int xp, yp; // plate with animation
    private ArrayList<SpaceShipBullets> bullets= new ArrayList<SpaceShipBullets>();

    // Phương thức constructor sau đây là một phần của lớp PhiThuyen và được sử dụng để khởi tạo một đối tượng phi thuyền khi nó được tạo.
    public PhiThuyen(int x, int y) {
        // gọi constructor của lớp cơ sở (InSpace) và truyền tọa độ ban đầu (x, y) của phi thuyền cho lớp cơ sở. Điều này làm cho phi thuyền được tạo với tọa độ ban đầu (x, y) và khởi tạo các thuộc tính chung với lớp cơ sở
        super(x, y);
        loadImages(imgFilenames);
        // super.setW(30) và super.setH(30): Đây là các dòng để đặt chiều rộng và chiều cao của phi thuyền bằng 30 điểm ảnh.
        super.setW(30);
        super.setH(30);
    }

    /*Biến xp và yp được đặt thông qua phương thức keyPressed(KeyEvent e) khi người chơi nhấn các phím mũi tên hoặc các phím điều hướng. Khi người chơi nhấn các phím, giá trị của xp và yp được cập nhật, và sau đó phương thức move() được gọi để di chuyển phi thuyền dựa trên các giá trị này.*/
    // giúp phi thuyền chiến đấu di chuyển 
    // Phương thức move() trong lớp PhiThuyen được sử dụng để cập nhật vị trí của phi thuyền dựa trên giá trị của biến xp (di chuyển theo trục x) và yp (di chuyển theo trục y).
    public void move() {
        super.x += xp;
        super.y += yp;
    }
    
    public Image[] getImgf(){
        return imgFrames;
    }
    
    public int getCrrentF(){
        return currentFrame;
    }
    
    // KHI NHẢ PHÍM
    /*Phương thức keyReleased(KeyEvent e) trong lớp PhiThuyen được sử dụng để xử lý sự kiện khi người chơi thả các phím sau khi đã nhấn chúng*/
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        loadImages(imgFilenames);

        if (key == KeyEvent.VK_LEFT) {
            xp = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            xp = 0;
        }

        if (key == KeyEvent.VK_UP) {
            yp = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            yp = 0;
        }
    }

    // KHI NHẤN PHÍM
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // Lấy mã phím mà người chơi đã nhấn.

        if (key == KeyEvent.VK_SPACE) {
            fire(); // Nếu người chơi nhấn phím SPACE, thực hiện hành động bắn đạn (gọi phương thức fire() để tạo đạn).
        }

        if (key == KeyEvent.VK_LEFT) {
            xp = -2; // Nếu người chơi nhấn phím mũi tên trái, đặt giá trị của xp thành -2 để di chuyển phi thuyền sang trái.
            loadImages(imgFilenames_left); // Tải hình ảnh phi thuyền hướng trái để hiển thị phi thuyền hướng trái trên màn hình.
        }

        if (key == KeyEvent.VK_RIGHT) {
            xp = 2; // Nếu người chơi nhấn phím mũi tên phải, đặt giá trị của xp thành 2 để di chuyển phi thuyền sang phải.
            loadImages(imgFilenames_right); // Tải hình ảnh phi thuyền hướng phải để hiển thị phi thuyền hướng phải trên màn hình.
        }

        if (key == KeyEvent.VK_UP) {
            yp = -2; // Nếu người chơi nhấn phím mũi tên lên, đặt giá trị của yp thành -2 để di chuyển phi thuyền lên.
            loadImages(imgFilenames_forward); // Tải hình ảnh phi thuyền hướng lên để hiển thị phi thuyền hướng lên trên màn hình.
        }

        if (key == KeyEvent.VK_DOWN) {
            yp = 2; // Nếu người chơi nhấn phím mũi tên xuống, đặt giá trị của yp thành 2 để di chuyển phi thuyền xuống.
            loadImages(imgFilenames_down); // Tải hình ảnh phi thuyền hướng xuống để hiển thị phi thuyền hướng xuống trên màn hình.
        }
    }
    
    // CẬP NHẬT TRẠNG THÁI HÌNH ẢNH KHI DI CHUYỂN
    /* trong lớp PhiThuyen được sử dụng để tải các hình ảnh từ tệp và lưu chúng trong mảng imgFrames để sử dụng trong việc hiển thị phi thuyền trên màn hình*/
    private void loadImages(String[] imgFileNames) {
        int numFrames = imgFileNames.length;
        imgFrames = new Image[numFrames];  // allocate the array
        URL imgUrl = null;
        for (int i = 0; i < numFrames; ++i) {
            imgUrl = getClass().getClassLoader().getResource(imgFileNames[i]);
            if (imgUrl == null) {
                System.err.println("Couldn't find file: " + imgFileNames[i]);
            } else {
                try {
                    imgFrames[i] = ImageIO.read(imgUrl);  // load image via URL
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        imgWidth = imgFrames[0].getWidth(null);
        imgHeight = imgFrames[0].getHeight(null);
    }

    // hai phương thức liên quan đến việc bắn đạn và quản lý đạn:
    public ArrayList getBullets() {
        return bullets;
    }

    private void fire() {
        bullets.add(new SpaceShipBullets(super.x - 3, super.y - 3));
    }
}
