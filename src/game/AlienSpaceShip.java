
package game;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author
 */

//r

// TÀU ALIEN
public class AlienSpaceShip extends Alien{
    
    public int heath = 4;  // Điểm số hoặc số lần tấn công cho đối tượng Alien.
    private ArrayList<AlienBullets> bullets = new ArrayList<AlienBullets>();  // Danh sách đạn của đối tượng Alien.
    int typeMove = 0;  // Loại cách di chuyển của đối tượng Alien.
    int typeAlien;  // Loại Alien.
    int count_MoveY;  // Đếm số lần di chuyển theo trục Y.
    double timeLife, timeLifeCount;  // Thời gian sống và thời gian đã trôi qua của đối tượng Alien.
    String trangThaiDiChuyen = "";  // Trạng thái di chuyển của đối tượng Alien.
    private String[] imgFilenames = { // Mảng tên tệp ảnh sẽ được sử dụng cho animation của đối tượng Alien.
        "aliens/alien2-1.png", "aliens/alien2-2.png"};
    private String[] imgFilenames_2 = { // Mảng tên tệp ảnh sẽ được sử dụng cho animation khác của đối tượng Alien.
        "aliens/alien1-1.png", "aliens/alien1-2.png", "aliens/alien1-3.png"};
    private Image[] imgFrames;    // Mảng các hình ảnh sẽ được sử dụng cho animation.
    private int currentFrame = 0; // Số thứ tự của hình ảnh hiện tại trong mảng imgFrames.
    private int imgWidth, imgHeight;    // Chiều rộng và chiều cao của hình ảnh.
    private int xp, yp; // Vị trí hiển thị hình ảnh trong trò chơi.
    private final int INITIAL_X = 1280;  // Vị trí ban đầu theo trục X.
    private final int INITIAL_Y = 720;   // Vị trí ban đầu theo trục Y.

    
    // truyền vào vị trí, loại chuyển động, loại alien
    public AlienSpaceShip(int x, int y, int typeMove, int typeAlien) {
        super(x, y);  // Gọi constructor của lớp cha với tọa độ x và y được truyền vào.

        count_MoveY = y;  // Gán giá trị y cho biến count_MoveY.

        this.typeAlien = typeAlien;  // Gán giá trị typeAlien được truyền vào cho biến instance typeAlien.

        if (typeAlien == 1) {
            loadImages(imgFilenames);  // Nếu typeAlien là 1, tải hình ảnh từ mảng imgFilenames.
        } else {
            loadImages(imgFilenames_2);  // Nếu typeAlien không phải 1, tải hình ảnh từ mảng imgFilenames_2.
        }

        timeLife = System.currentTimeMillis();  // Gán thời gian hiện tại cho biến timeLife.

        super.setW(imgWidth);  // Gọi phương thức setW từ lớp cha để thiết lập chiều rộng của đối tượng.
        super.setH(imgHeight);  // Gọi phương thức setH từ lớp cha để thiết lập chiều cao của đối tượng.

        this.typeMove = typeMove;  // Gán giá trị typeMove được truyền vào cho biến instance typeMove.
    }

    // điều khiển di chuyển của tàu
    // Tùy thuộc vào giá trị của typeMove, phi thuyền có thể di chuyển lên xuống hoặc tự động bắn đạn
    public void move() {

        if (typeMove == 0) {
            // nếu vị trí trục y của phi thuyền lớp cha 
            /*Dòng này gán giá trị 0 cho thuộc tính y của đối tượng hiện tại (phi thuyền) bằng cách sử dụng super. Điều này có nghĩa rằng nó đặt lại tọa độ y của phi thuyền về 0. Trong ngữ cảnh này, y thường đại diện cho tọa độ của đối tượng trên trục tung của không gian đồ họa.*/
            if (super.y > 720) {
                super.y = 0;
            }

            if (super.x < 0) {
                super.x = INITIAL_X;
            }

            super.y += 3;
        }

        if (typeMove == 1) {
            if (super.y > 720) {
                super.y = 0;
            }

            if (super.x < 0) {
                super.x = INITIAL_X;
            }

            super.y += 2;
            //super.x += 1;
        }
        if (typeMove == 3) {
            /* kiểm tra xem giá trị của timeLifeCount có lớn hơn 2000 mili giây (2 giây) hay không. Nếu điều kiện này đúng, nghĩa là đã đủ 2 giây kể từ lần cuối cùng bắn đạn (hoặc lần đầu tiên bắn đạn), thì phương thức fire() được gọi để tạo ra một viên đạn mới và đưa nó vào danh sách đạn. 
            Sau đó, timeLife được cập nhật để ghi lại thời điểm mới nhất mà đạn đã được bắn.*/
            timeLifeCount = System.currentTimeMillis() - timeLife;

            if (timeLifeCount > 2000) {
                fire();
                timeLife = System.currentTimeMillis();

            }

            // Nếu trangThaiDiChuyen là "RIGHT", tức là phi thuyền đang di chuyển sang phải, thì giảm giá trị x của phi thuyền đi 3 đơn vị.
            //Ngược lại, nếu trangThaiDiChuyen không phải là "RIGHT", thì tăng giá trị x của phi thuyền lên 3 đơn vị. 
            //Điều này tạo hiệu ứng di chuyển ngang của phi thuyền.
            if (trangThaiDiChuyen == "RIGHT") {
                super.x -= 3;
            } else {
                super.x += 3;
            }
            /*nghĩa là phi thuyền đã đi xa đủ để đổi hướng di chuyển sang trái.
            Trong trường hợp này, trangThaiDiChuyen được đặt thành "LEFT" để thay đổi hướng di chuyển.*/
            if (super.x > INITIAL_X - 50) {
                trangThaiDiChuyen = "RIGHT";
            }
            // ghĩa là phi thuyền đã đi ra khỏi phía bên trái màn hình. 
            //Trong trường hợp này, trangThaiDiChuyen được đặt thành "RIGHT" để thay đổi hướng di chuyển.
            if (super.x < 0) {
                trangThaiDiChuyen = "LEFT";
            }
            // kiểm tra xem y của phi thuyền trừ đi count_MoveY có nhỏ hơn 200 hay không. 
            //Nếu điều kiện này đúng, nghĩa là phi thuyền chưa đi đủ xa từ vị trí ban đầu theo trục tung, 
            //thì tăng giá trị y của phi thuyền lên 2 đơn vị và tăng giá trị count_MoveY thêm 1.
            //Điều này làm cho phi thuyền có một sự thay đổi độ cao trong quá trình di chuyển.
            if (super.y - count_MoveY < 200) {
                super.y += 2;
                count_MoveY++;
            }

        }

    }

    // hàm kiểu cấu trúc mảng --> trả về một mảng chứa các ảnh  -->mảng này sẽ được ghi đè tất cả các ảnh trong phương thức  loadImages
    //  Các phương thức này trả về mảng chứa các khung hình (ảnh) 
    public Image[] getImgf() {
        return imgFrames;
    }

    //số khung hình hiện tại của phi thuyền ngoài hành tinh.
    public int getCrrentF() {
        return currentFrame;
    }

    // Phương thức này đọc và tải các hình ảnh từ tệp và lưu chúng trong mảng imgFrames. Hình ảnh được load qua URL sử dụng 
    private void loadImages(String[] imgFileNames) {
        int numFrames = imgFileNames.length;// số lượng khung hình = số lượng hình ảnh
        imgFrames = new Image[numFrames];  // aimgFileNamesllocate the array , mảng hình ảnh được định nghĩa là mảng với số phần tử bằng số lượng hình ảnh 
        URL imgUrl = null;// tạm thời cho đường dẫn ảnh là NULL
        // chạy 
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

    // getBullets() trả về danh sách đạn
    public ArrayList getBullets() {
        return bullets;
    }

    //fire() thêm một viên đạn mới vào danh sách khi phi thuyền bắn
    private void fire() {
        bullets.add(new AlienBullets(super.x - 3, super.y - 3));
    }
    
}
