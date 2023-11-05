
package game;

/**
 *
 * @author
 */
// import java.awt.event: cung cấp lớp và giao diện để xử lí sự kiện trong java
// import javax.sound.midi.MidiSystem: cung cấp các lớp và phương thức để thực hiện và điều khiển âm nhạc MIDI


import java.awt.*;// cung cấp cá lớp và phương thức để tạo giao diện đồ họa cơ bản trong java
import java.awt.event.KeyAdapter;// xử lí sự kiện từ bản phím
import java.awt.event.KeyEvent;// xử lí sự kiện từ bản phím
import java.io.File;// xác thực và tương tác với các tệp và thư mục, bao gồm kiểm tra sự tồn tại, tạo mới, đọc, viết và xóa chúng.
import java.io.IOException;// ngoại lệ trong quá trình thực thi
import java.net.URL;// cho phép tạo, xác thực, tương tác với các địa chỉ URL(tài nguyên internet) hệ thống tệp cục bộ
import java.util.ArrayList;//  Java Collections Framework dùng lưu trữ danh sách đối tượng
import java.util.ConcurrentModificationException;
import java.util.Random;// tạo số ngẫu nhiên
import javax.imageio.ImageIO;// cung cấp các lớp để đọc và ghi hình ảnh (để đọc hình ảnh từ tệp hình ảnh)
//import javax.sound.midi.InvalidMidiDataException;// xử lí ngoại lệ khi thao tác với MIDI
//import javax.sound.midi.MidiSystem;// truy cập vào các tài nguyên trong MIDI hệ thống 
//import javax.sound.midi.MidiUnavailableException;// xử lí ngoại lệ khi 
//import javax.sound.midi.Sequence;//tài nguyên MIDI không thể sử dụng hoặc không khả dụng
//import javax.sound.midi.Sequencer;//  điều khiển các khía cạnh của phát lại như tốc độ và âm lượng của MIDI
import javax.swing.*;// cung cấp các thành phần để xây dựng đồ họa giao diện trong java
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

//r
public class GameBanAlien extends JPanel {
    // TIÊU ĐỀ GAME
    JFrame CuaSoGameBanAlien;
    public String title = "Phi thuyền không gian - màn 1";
    // TRẠNG THÁI TRÒ CHƠI
    boolean inGame = true;
    
    // ẢNH KẾT QUẢ THẮNG / THUA
    Image Effect;
    private String[] imgFileNames = {
         "effect/defeat.png", "effect/victory.png"};
    
    // HÌNH ẢNH HIỆU ỨNG
    private Image[] imgFrames;    // array of Images to be animated
    
    // NGƯỜI CHƠI
    PhiThuyen pt;
    // QUÁI VẬT
    Boss boss;
    
    // KHUNG HÌNH MỖI GIÂY
    private int frameRate = 30;    // frame rate in frames per second
    // SỐ KHUNG HÌNH HIỆN TẠI
    private int currentFrame = 0; // current frame number
    private int currentFrameAlien = 0; // current frame numberr
    private int currentFrameBoss = 0; // current frame numberr
    private int currentFrameExp = 0; // current frame numberr
    
    // DANH SÁCH
    ArrayList<AlienSpaceShip> ListAlien ; // tạo một biến ListAlien danh sách lưu trữ đối tượng AlienSpaceShip
    
    // LUỒNG XỬ LÝ HIỆU ỨNG
    Exp exp  = new Exp();// tạo đối tượng mới Exp(của class Exp) 
    Thread animationExp;//biến thuộc kiểu Thread--> dùng thục hiện đa luồng--> để xử lí các hoạt động liên quan đến hiệu ứng và animation
    Thread BOSS;//biến thuộc kiểu Thread
    
    // CÀI ĐẶT HÌNH NỀN
    Image backGround;
    int xbg, ybg = 0, ybg_loop = ybg - 810;
    double map_px = 0;
    String Gaming = "normal";
   
    // PHÁT NHẠC
//    private Sequencer backgroundMusic;
    private Clip backgroundMusic;
    
    public GameBanAlien() {
        // GỌI HÀM KHỞI TẠO
        initGame();
    }
    
    // KHỞI TẠO TRÒ CHƠI
    void initGame(){
        // VỊ TRÍ PHI THUYỀN với tọa độ 380, 600
        pt = new PhiThuyen(380, 600);
        
        // GỌI HÀM KHỞI TẠO
        initAliens(1, 1);
        
        // MÀU NỀN
        setBackground(Color.BLACK);
        // SỰ KIỆN PHÍM
        addKeyListener(new TAdapter());
        // JPanel
        setFocusable(true);
        
        // HÌNH NỀN
        loadBG("effect/bg_lv1_2.jpg");
        
        // ÂM THANH NỀN
        loadBackgroundMusic("src/sound/GameBanAlien.wav");
        
        // CẬP NHẬT HÌNH NỀN - HIỆU ỨNG
        // KHỞI TẠO LUỒNG
        // tạo đối tượng tên animationBackground theo kiểu thread
        // đối  tượng được định nghĩa bên trong khối mã lệnh
        // mục tiêu cuối cùng là cập nhật hình nên liên tục và tạo ra hiệu ứng chuyển động trong trò chơi 
        Thread animationBackground = new Thread () {
            @Override // Ghi đè phương thức run() trong lớp Thread để định nghĩa các tác vụ trong luồng
            public void run() {
                while (true) {
                    // KIỂM TRA TRÒ CHƠI ĐANG CHẠY? - NẾU K THOÁT KHỎI THREAD/ LUỒNG
                    // kiểm tra chương trình game còn chạy không
                    // nếu hiện tại inGame là False(ban đầu là true)--> sẽ kết thúc
                    if(!inGame){
                        return;
                    }
                    // DI CHUYỂN NỀN - tạo di chuyển dọc cho hình nền
                    ybg++;
                    ybg_loop++;
                    map_px ++;
                    
                    // tạo hiệu ứng vô tận lên và xuống
                    if(ybg >= 720){
                        ybg = -810;
                    }
                    
                    if(ybg_loop >= 720){
                        ybg_loop = ybg - 810;
                    }
                    
                    // TẠO THÊM QUÁI VẬT KHÁC LOẠI
                    // nếu mà đối tượng map_px đạt ngưỡng 600 thì gọi phương thức initAliens
                    if(map_px == 600) initAliens(0, 1);
                    if(map_px == 1200) initAliens(3, 2);
                    
                    // CẬP NHẬT NỀN VÀ QUÁT VẬT
                    repaint();  // Refresh the display,  phương thức của lớp JComponent 
                    // kiểm soát tốc độ khung hình--> đảm bảo tốc độ khung hình không vượt quá số khung hình hiển thị trong giây
                    try {
                        // TẠM DỪNG ĐỂ KIỂM SOÁT TỐC ĐỘ CẬP NHẬT
                        Thread.sleep(300 / frameRate); //300 CHIA CHO SỐ KHUNG HÌNH MỖI GIÂY -trì hoãn nhường cho các chủ đề khác
                    } catch (InterruptedException ex) { } // xử lí luồng bị gián đoạn trong trạng thái ngủ hoặc bị gián đoạn bởi sự kiện bên ngoài
                }
            }
        };
        
        // BẮT ĐẦU LUỒNG TẠO HIỆU ỨNG
        // khởi động chạy song song với luồng chính(màn hình game sẽ chạy song song với luồng chính)
        animationBackground.start();  // start the thread to run animation
        
        // ÂM THANH
        
        
        // HIỆU ỨNG CHO PHI THUYỀN - ĐẠN - Gaming 
        // luồng điều khiển tàu
        Thread animationSpaceShip = new Thread () {
            @Override
            public void run() {
                while (true) {
                    // KIỂM TRA TRÒ CHƠI ĐANG CHẠY? - NẾU K THOÁT KHỎI THREAD/ LUỒNG 
                    if(!inGame){
                        return;
                    }
                    
                    // CẬP NHẬT VỊ TRÍ CỦA PHI THUYỀN
                    update();   // để khung hình được lặp lại cũng như hiển thị các khung hình liên tiếp
                    
                    // CẬP NHẬT VỊ TRÍ CỦA ĐẠN 
                    updateBullets();  //để thường xuyên cập nhật trạng thái viên đạn
                    
                    // DI CHUYỂN PHI THUYỀN
                    pt.move();
                    
                    // CẬP HÌNH ẢNH PHI THUYỀN - ĐẠN BẮN
                    repaint();  // làm mới và vẽ lại giao diện để người chơi thấy được sự thay đổi 
                    try {
                        // TẠM DỪNG ĐỂ KIỂM SOÁT TỐC ĐỘ CẬP NHẬT
                        Thread.sleep(300 / frameRate); // để dảm bảo tốc độ khung hình nhỏ hơn số khung hình xuất hiện
                    } catch (InterruptedException ex) { } // xử lí ngoại lệ cho khung hình
                }
            }
        };
        
        // BẮT ĐẦU LUỒNG TẠO HIỆU ỨNG - //luồng điều khiển tàu chạy song song với luồng chính
        animationSpaceShip.start();  // start the thread to run animation
        
        // HIỆU ỨNG CHO THUYỀN ALIEN
        // luồng điều khiển tàu vũ trụ ngoài hành tinh
        Thread animationAlienSpaceShip = new Thread () {
            @Override
            public void run() {
                while (true) {
                    // KIỂM TRA TRÒ CHƠI ĐANG CHẠY? - NẾU K THOÁT KHỎI THREAD/ LUỒNG
                    if(!inGame){
                        return;
                    }
                    
                    // CẬP NHẬT VỊ TRÍ ALIEN 
                    updateAlien();   // update the position and image- dùng để cập nhật lại trạng thái khung hình người ngoài hành tinh
                    //Alien1.move();
                    
                    // CẬP HÌNH ẢNH ALIEN
                    repaint();  // Refresh the display - vẽ lại chính đối tượng khung hình
                    try {
                        // TẠM DỪNG ĐỂ KIỂM SOÁT TỐC ĐỘ CẬP NHẬT - // không cho tốc độ khung hình vượt quá số lượng khung hình
                        Thread.sleep(300 / frameRate); // delay and yield to other threads
                    } catch (InterruptedException ex) { }
                }
            }
        };
        // BẮT ĐẦU LUỒNG TẠO HIỆU ỨNG - // cho các người ngoài hành tinh chạy song song chung với luồng chính
        animationAlienSpaceShip.start();  // start the thread to run animation
        
        // thiết lập luồng Boss
        BOSS = new Thread () {
            @Override
            public void run() {
                while (true) {
                    // CẬP NHẬT VỊ TRÍ BOSS
                    updateBoss();   // update the position and image - cập nhật trạng thái của Boss và viên đạn của Boss
                    // CẬP HÌNH ẢNH BOSS
                    repaint();  // Refresh the display - Vẽ lại khung hình vừa mới cập nhật
                    try {
                        // TẠM DỪNG ĐỂ KIỂM SOÁT TỐC ĐỘ CẬP NHẬT - // không cho tốc độ khung hình lớn hơn số lượng khung hình
                        Thread.sleep(300 / frameRate); // delay and yield to other threads
                    } catch (InterruptedException ex) { }
                }
            }
        };
        
        
        // HIỆU ỨNG NỔ- SÁT THƯƠNG ĐẠN BẮN
        //animationExp thiết lập luồng 
        animationExp = new Thread () {
            @Override
            public void run() {
                while (true) {
                    updateExp();   // update the position and image - để cập nhật lại khung hình hiệu ứng
                    repaint();  // Refresh the display - vẽ lại khung hình hiện tại
                    try {
                        // TẠM DỪNG ĐỂ KIỂM SOÁT TỐC ĐỘ CẬP NHẬT
                        Thread.sleep(300 / 5); // delay and yield to other threads
                    } catch (InterruptedException ex) { }
                }
            }
        };
        // thiết lập đối tượng luồng hiệu ứng chạy song song với luồng chính
        animationExp.start();
        
    }

//    private void loadBackgroundMusic(String midiFilePath) {
//        try {
//            File midiFile = new File(midiFilePath);
//            Sequence sequence = MidiSystem.getSequence(midiFile);
//
//            backgroundMusic = MidiSystem.getSequencer();
//            backgroundMusic.open();
//            backgroundMusic.setSequence(sequence);
//            backgroundMusic.setLoopCount(Sequencer.LOOP_CONTINUOUSLY); // Loop the music
//            backgroundMusic.start();
//        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public void stopBackgroundMusic() {
//        if (backgroundMusic != null && backgroundMusic.isRunning()) {
//            backgroundMusic.stop();
//            backgroundMusic.close();
//        }
//    }
    private void loadBackgroundMusic(String wavFilePath) {
        try {
            // Tạo một AudioInputStream từ tệp WAV
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(wavFilePath));

            // Lấy một Clip
            backgroundMusic = AudioSystem.getClip();

            // Mở Clip và đặt nó bằng AudioInputStream
            backgroundMusic.open(audioInputStream);

            // Lặp vô hạn
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
    
    // XUẤT HIỆN BOSS
    private void startBoss() {
        // VỊ TRÍ BOSS BAN ĐÀU
        boss = new Boss(600, -500);
        // TRẠNG THÁI BOSS XUẤT HIỆN
        Gaming = "boss";
        // BẮT ĐẦU LUỒNG CỦA BOSS
        BOSS.start();  // start the thread to run animation
    }
    
    
    
    // KHỞI TẠO ALIEN
    public void initAliens(int pos, int typeAlien) {
        
        // DANH SÁCH CHỨA - //tạo mảng ListAlien là nơi lưu trữ các đối tượng người ngoài hành tinh
        ListAlien = new ArrayList<>();
        
        // SỐ NGẪU NHIÊN
        Random rand = new Random();
        // VỊ TRÍ
        int alien_x, alien_y; // khởi tạo biến kiểu int cho người ngoài hành tinh
//        for (int[] p : pos) {
//            ListAlien.add(new AlienSpaceShip(p[0], p[1]));
//        }

        // LOẠI 1
        if(typeAlien == 1){
            // TẠO 40 ALIEN
            for (int xx = 0; xx < 40 ; xx ++) {
                // VỊ TRÍ X, Y
                // tạo tọa độ ngẫu nhiên cho người ngoài hành tinh nằm trong khoảng từ x= 1 --> 1200 và y= -300 --> -1
                alien_x = rand.nextInt(1200) + 1;
                alien_y = 0 - rand.nextInt(300) + (1);
                // THÊM VÀO DANH SÁCH
                // thêm đối tượng AlienSpaceShip với các thông tin và tọa độ khác nhau 
                ListAlien.add(new AlienSpaceShip( alien_x, alien_y, pos, 1));
            }
        }
        // NẾU LÀ LOẠI KHÁC
        else{
            // TẠO 40 CON - LOẠI 2
            for (int xx = 0; xx < 40 ; xx ++) {
                // VỊ TRÍ X, Y
                alien_x = rand.nextInt(1200) + 1;
                alien_y = 0 - rand.nextInt(300) + (1);
                // THÊM VÀO DANH SÁCH
                ListAlien.add(new AlienSpaceShip(alien_x, alien_y, pos , 2));
            }
        }
        
 
    }
 
    // CẬP NHẬT SỐ KHUNG HÌNH
    // hàm hiển thị các khung hình liên tiếp
    public void update() {
        ++currentFrame;    // display next frame, dùng di chuyển các khung hình tiếp theo trong chuỗi các khung hình 
        // kiểm tra xem currentFrame có vượt qua số lượng khung hình tối đa không
        if (currentFrame >= pt.getImgf().length) {
            //nếu vượt quá thiết lập khung hình =0 để bắt đầu lại tạo hiệu ứng lặp
            currentFrame = 0;
        }
    }
    
    // CẬP NHẬT VỊ TRÍ BOSS
    public void updateBoss() {
        // kiểm tra Boss có đang hiển thị trên màn hình không? - NẾU K THÌ KẾT THÚC - K CẦN CẬP NHẬT
        if(!boss.isVisible()){
            return;
        }
        
        // TẠO HIỆU ỨNG
        // tăng giá trị biến khung hình Boss
        ++currentFrameBoss;    // display next frame
        
        // VƯỢT QUÁ SỐ KHUNG HÌNH CÓ SẴN
        // nếu giá trị khung hình Boss vượt qua số lượng khung hình Boss cho phép
        if (currentFrameBoss >= boss.getImgf().length) {
            // cập nhật lại giá trị khung hình Boss là 0 để trò chơi tạo hiệu ứng lặp lại
            currentFrameBoss = 0;
        }
        
        // DI CHUYỂN BOSS
        // cho đối tượng Boss di chuyển (biến boss thuộc class Boss)
        boss.move();
        
        // ĐẠN CỦA BOSS - //tạo danh sách bullets_boss để lấy phương thức danh sách các viên đạn của Boss
        ArrayList bullets_boss = boss.getBullets();
        
        // duyệt kích thước danh sách viên đạn của Boss
        for (int i = 0; i < bullets_boss.size(); i++) {
            //tạo biến m là đối tượng thuộc class BossBullets
            // lấy viên đạn cụ thể từ danh sách bullets_boss dựa vào chỉ mục i
            BossBullets m = (BossBullets) bullets_boss.get(i); // BossBullets là lớp đại diện cho viên đạn của Boss.

            if (m.isVisible()) {
                // nếu viên đạn m hiển thị trên màn hình thì di chuyển 
                m.move();
            } else {
                // loại bỏ viên đạn khỏi danh sách
                bullets_boss.remove(i);
            }
        }
    } 

    // CẬP NHẬT VỊ TRÍ NỔ KHI BẮN
    public void updateExp() {
        // biến giá tri khung hình(int)
        // biến được gán = giá trị đối tượng exp của class Exp(được lấy bởi phương thức getCF())
        currentFrameExp = exp.getCF();
    } 
    
    // CẬP NHẬT ALIEN
    // hàm cập nhật lại trạng thái Ngoài hành tinh
    public void updateAlien() {
        // kiểm tra xem danh sách biến cục bộ ListAlien có rỗng không
        if (ListAlien.isEmpty()) {
            // nếu rỗng thì không cập nhật
            return;
        }
        // tăng giá trị biến khung hình số nguyên 
        ++currentFrameAlien;    // display next frame
        // nếu biến vượt quá số lượng khung hình tối đa trong độ dài danh sách ListAlien
        if (currentFrameAlien >= ListAlien.get(0).getImgf().length) {
            // sẽ thiết lập lại biến để bắt đầu cho khung hình đầu tiên
            currentFrameAlien = 0;
        }

        // duyệt qua tất cả phần tử trong danh sách ListAlien
        for (int i = 0; i < ListAlien.size(); i++) {
//            System.out.println("enter_1");
            // tạo biến a là kiểu dữ liệu AlienSpaceShip(thuyền người ngoài hành tinh)
            AlienSpaceShip a = ListAlien.get(i); // biến a là phần tử trong danh sách  ListAlien theo chỉ mục i

            // kiểm tra xem đối tượng có nằm trong màn hình và dưới tọa độ 710 không
            if (a.isVisible() && a.getY() < 710) {
                // nếu có thì di chuyển 
                a.move();
            } else {
                // không bao giờ chạy vào
                // kiểm tra biến currentFrameExp có >= 13 không
                if (currentFrameExp >= 13) {
                    // nếu có thì loại bỏ khỏi danh sách ListAlien
//                    System.out.println("enter");
                    ListAlien.remove(i);
                }
            }
            // tạo danh sách đạn người ngoài hành tinh và lấy phương thức đạn của class AlienSpaceShip
            ArrayList bullets_Alien = a.getBullets();
            // duyệt qua tất cả có tất cả phần tử trong danh sách bullets_Alien
            for (int j = 0; j < bullets_Alien.size(); j++) {
                // tạo viên đạn m thuộc class AlienBullets ở vị trí j trong danh sách bullets_Alien
                AlienBullets m = (AlienBullets) bullets_Alien.get(j);
                // nếu m còn xuất hiện trong màn hình thì di chuyển
                if (m.isVisible()) {
                    m.move();
                } else {
                    // nếu không xóa viên đoạn ra khỏi danh sách 
                    bullets_Alien.remove(j);
                }
            }

        }
    }
    
    // CẬP NHẬT ĐẠN
    // cập nhật trạng thái đối tượng viên đạn trong trò chơi
    private void updateBullets() {
        // tạo  danh sách các viên đạn từ biến cục bộ từ lớp phi thuyền
        // Phương thức trả về danh sách viên đạn
        ArrayList bullets = pt.getBullets();

        // duyệt qua hết tất cả các viên đạn trong danh sách
        // SpaceShipBullets: cái này là class(đối tượng)
        System.out.println("update");
        for (int i = 0; i < bullets.size(); i++) {
            // lấy ra một viên đạn cụ thể từ danh sách bullets theo chỉ mục i 

            SpaceShipBullets m = (SpaceShipBullets) bullets.get(i);
            // kiểm tra viên đạn được lấy ra có đang hiển thị trên màn hình không
            if (m.isVisible()) {
                // nếu có thì gọi phương thức di chuyển 
                m.move();
            } else {
                // nếu viên đạn không còn hiển thị ra khỏi màn hình hoặc va chạm vào mục tiêu
                // thì loại bỏ viên đạn ra khỏi danh sách bullets
                bullets.remove(i);
            }
        }

    }

    // SỰ KIỆN PHÍM ĐIỀU KHIỂN PHI THUYỀN
    // KeyAdapter của awt trong java
    private class TAdapter extends KeyAdapter {
        // SỰ KIỆN KHI THẢ PHÍM
        @Override
        public void keyReleased(KeyEvent e) {
            pt.keyReleased(e);
        }
        // SỰ KIỆN KHI NHẤN PHÍM
        @Override
        public void keyPressed(KeyEvent e) {
            pt.keyPressed(e);
        }
    }
 
    
    /** Custom painting codes on this JPanel */
    
    // VẼ PHẦN TỬ - HÌNH ẢNH TRONG GIAO DIỆN
    // phương thức paintComponent dùng để vẽ lại nội dung khi gọi repaint()
    // khi repaint được gọi lên nó sẽ vẽ những gì có trong phương thức paintComponent
    @Override
    public void paintComponent(Graphics g) throws ConcurrentModificationException, ArrayIndexOutOfBoundsException{
        // VẼ NỀN
        //Dòng này gọi phương thức paintComponent của lớp cha (JPanel) để vẽ nền và các thành phần giao diện khác.
        super.paintComponent(g);  // paint background
  
        // tạo đối tượng g2d là kiểu Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        
        // KIỂM TRA VA CHẠM GIỮA CÁC ĐỐI TƯỢNG
        checkCollisions();
        
        // VẼ HÌNH NỀN
        // dùng để vẽ hình nền
        //vẽ hình nền tại vị trí (5, ybg)
        g.drawImage(backGround, 5, ybg, null);
        //vẽ hình nền tại vị trí (0, ybg_loop)
        g.drawImage(backGround, 0, (ybg_loop), null);
        //Biến ybg và ybg_loop có thể được sử dụng để tạo hiệu ứng di chuyển hình nền trong trò chơi.
        //Bằng cách thay đổi giá trị của chúng trong mỗi khung hình,
        //bạn có thể tạo ra sự cảm giác chuyển động của hình nền trong trò chơi

        // VẼ ĐẠN CỦA TÀU K GIAN trên màn hình
        ArrayList bullets = pt.getBullets();//  danh sách các viên đạn từ tàu vũ trụ (pt)
        // duyệt qua phần tử trong danh sách các viên đạn
        for (int bullets_run = 0; bullets_run < bullets.size(); bullets_run++) {

            // 
            Object m1 = bullets.get(bullets_run);//  // lấy ra đối tượng cụ thể dựa vào chỉ muc bullets_run

            SpaceShipBullets m = (SpaceShipBullets) m1;// Dòng này ép kiểu đối tượng m1 thành kiểu SpaceShipBullets
            g.drawImage(m.getImage(), m.getX() + 5, m.getY(), this);//Dòng này vẽ hình ảnh của viên đạn m lên màn hình tại vị trí (m.getX() + 5, m.getY())
        }
        
        // VẼ TÀU K GIAN
        // Dòng này vẽ hình ảnh tàu vũ trụ (pt) tại vị trí (pt.getX(), pt.getY()) trên màn hình. 
        //Hình ảnh được lấy từ mảng imgf theo chỉ số currentFrame, 
        //có nghĩa là chọn hình ảnh hiện tại của tàu vũ trụ.
       
        g.drawImage(pt.getImgf()[currentFrame], pt.getX(), pt.getY(), this);
        
        /*Nếu tàu đã bị tiêu diệt
        ) và nếu không còn hiển thị, nó vẽ một hình ảnh hiệu ứng nổ tại vị trí (pt.getX() - 10, pt.getY() - 10
        ) trên màn hình.Hình ảnh hiệu ứng nổ được lấy từ mảng imgf của đối tượng exp dựa vào chỉ số currentFrameExp.*/
        if(!pt.isVisible()){
            g.drawImage(exp.getImgf()[currentFrameExp], pt.getX() - 10, pt.getY() -10, this);
        }
        
        // VẼ THÔNG TIN TRẠNG THÁI - // thiết lập màu vẽ cho đối tượng Graphics2D là màu trắng
        g2d.setColor(Color.WHITE);
        
        // MÁU HIỆN TẠI
        // tính toán phần trăm sức khỏe của tàu vũ trụ. 
        int persentHeath = Math.round((float) ((float)pt.heath / (float)20 * (float)100));
        // Dòng này vẽ thông tin sức khỏe trên màn hình
        g2d.drawString("Sinh lực : "+persentHeath+" % ", 20, 45);
        // Game road
        
        // XUẤT HIỆN BOSS
        // kiểm tra xem danh sách tàu vũ trụ địch (ListAlien) có rỗng không và biến Gaming có giá trị là "prepa_boss
        if(ListAlien.size() == 0 && Gaming == "prepa_boss"){
            // GỌI HÀM CHO BOSS XUẤT HIỆN
            startBoss();
        }
        
        
        
        // VẼ ALIEN 1
        //duyệt qua danh sách tàu vũ trụ địch trong ListAlien.
        for (int ListAlien_run = 0; ListAlien_run < ListAlien.size(); ListAlien_run ++) {
            
            // tạo biến a kiểu AlienSpaceShip từ danh sách ListAlien tại vị trí ListAlien_run.
            AlienSpaceShip a = ListAlien.get(ListAlien_run);
            // nghĩa là tàu vũ trụ địch thuộc loại 2 
            if(a.typeAlien == 2){
                //Thiết lập màu vẽ cho đối tượng Graphics2D là màu trắng.
                g2d.setColor(Color.WHITE);
                // Vẽ thông tin về số tàu vũ trụ địch còn lại 
                g2d.drawString("Tàu địch còn lại : "+ListAlien.size()+"", 20, 30);
                //: Nếu có tàu vũ trụ địch loại 2 xuất hiện, 
                //biến Gaming được thiết lập thành "prepa_boss" để chuẩn bị cho trận đấu với "boss" 
                Gaming = "prepa_boss";
            }
                     //Vẽ hình ảnh của tàu vũ trụ địch a lên màn hình tại vị trí (a.getX(), a.getY())
            g.drawImage(a.getImgf()[currentFrameAlien], a.getX(), a.getY(), this);
            //  Lấy danh sách các viên đạn của tàu vũ trụ địch a.
            ArrayList bullets_Alien = a.getBullets();
            // duyệt qua danh sách viên đạn của tàu vũ trụ địch
            for (int bullets_run = 0; bullets_run < bullets_Alien.size(); bullets_run ++) {
            //Dòng này lấy ra một đối tượng từ danh sách viên đạn bullets_Alien tại vị trí bullets_run.
                Object m1 = bullets_Alien.get(bullets_run);
                AlienBullets m = (AlienBullets) m1;//thực hiện việc ép kiểu (type casting) từ một đối tượng m1 sang đối tượng kiểu AlienBullets. Điều này đòi hỏi rằng m1 phải thực sự là một thể hiện của lớp AlienBullets, nếu không sẽ xảy ra lỗi kiểu
                g.drawImage(m.getImage(), m.getX() + 5,m.getY(), this);// vẽ hình ảnh của viên đạn m lên màn hình tại vị trí (m.getX() + 5, m.getY())
            }
            
            /*Dòng mã này kiểm tra xem tàu vũ trụ địch có tồn tại (được đánh dấu là không hiển thị) hay không, và nếu nó không hiển thị (tức là đã bị tiêu diệt), thì nó vẽ một hiệu ứng nổ (exp.getImgf()[currentFrameExp]) 
            tại vị trí của tàu vũ trụ địch đã bị tiêu diệt.*/
            if(!a.isVisible()){
                g.drawImage(exp.getImgf()[currentFrameExp], a.getX() - 10, a.getY() -10, this);
            }
        }
        
        // TRẠNG THÁI CÓ BOSS
        if(Gaming == "boss"){
            // VỄ BOSS
            // vẽ hình ảnh của tàu vũ trụ boss tại vị trí (boss.getX(), boss.getY()). Dựa vào currentFrame, nó sử dụng một khung hình cụ thể để vẽ
            g.drawImage(boss.getImgf()[currentFrame], boss.getX(), boss.getY(), this);
            
            // TÀU BOSS K HIỂN THỊ
            //Nếu tàu vũ trụ boss không hiển thị (tức là đã bị tiêu diệt), thì nó vẽ một loạt hiệu ứng nổ tại các vị trí khác nhau
            if(!boss.isVisible()){
                g.drawImage(exp.getImgf()[currentFrameExp], boss.getX() - 10, boss.getY() + 100, this);
                g.drawImage(exp.getImgf()[currentFrameExp], boss.getX() + 100, boss.getY() + 150, this);
                g.drawImage(exp.getImgf()[currentFrameExp], boss.getX() + 120, boss.getY() -10, this);
                g.drawImage(exp.getImgf()[currentFrameExp], boss.getX() + 200, boss.getY() +130, this);
            }
            
            // VẼ ĐẠN TÀU BOSS
            // Lấy danh sách đạn của tàu vũ trụ boss. 
            //Đây là một danh sách chứa các đối tượng đạn (có thể là đạn bắn ra từ tàu vũ trụ boss).
            ArrayList bullets_boss = boss.getBullets();
            //chạy qua danh sách đạn của bos
            for (int bullets_run = 0; bullets_run < bullets_boss.size(); bullets_run++) {

                Object m1 = bullets_boss.get(bullets_run);

                BossBullets m = (BossBullets) m1;
                g.drawImage(m.getImage(), m.getX() + 5, m.getY(), this);
            }
            // nếu tàu vũ trụ boss không còn hiển thị 
            if (!boss.isVisible()) {
                CotChuyenNeuThang cotchuyen = new CotChuyenNeuThang();
                JFrame frame = new JFrame("Cốt chuyện");
                ImageIcon img = new ImageIcon("src/effect/Icon.png");
                frame.setIconImage(img.getImage());
                frame.setSize(1280, 720);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(cotchuyen);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                cotchuyen.CuaSoCotChuyenNeuThang=frame;
                CuaSoGameBanAlien.dispose();
                stopBackgroundMusic();
            }

            // Vẽ lượng máu còn lại của boss
            int bossHeathPercentage = Math.round((float) boss.getHeath() / 500 * 100);
            if(bossHeathPercentage>=0){
                g2d.drawString("Máu Boss: " + bossHeathPercentage + " %", 20, 60);
            }
        }
        
        // NẾU TRÒ CHƠI KẾT THÚC
        // nếu trò chơi không còn trong trạng thái chạy inGame là false), 
        //đoạn mã sẽ vẽ hình ảnh tương ứng với trạng thái game over hoặc kết thúc trò chơi:
        if (!inGame) {
            CotChuyenNeuThua cotchuyen=new CotChuyenNeuThua();
            JFrame frame = new JFrame("Cốt chuyện");
            ImageIcon img = new ImageIcon("src/effect/Icon.png");
            frame.setIconImage(img.getImage());
            frame.setSize(1280, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(cotchuyen);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            cotchuyen.CuaSoCotChuyenNeuThua=frame;
            CuaSoGameBanAlien.dispose();
            stopBackgroundMusic();
        }
        
    }
   
    // KIỂM TRA VA CHẠM -> THAY ĐỔI GIÁ TRỊ ĐIỂM SỐ - MÁU HIỆN TẠI
    // sử dụng để kiểm tra va chạm giữa phi thuyền của người chơi (pt) và tàu vũ trụ của người ngoài hành tinh (alien)
    public void checkCollisions() {

        // : Kiểm tra xem trò chơi còn đang diễn ra
        if (!inGame) {

            return;
        }
        //  Lấy hình chữ nhật (rectangle) bao quanh phi thuyền người chơi. 
        //Hình chữ nhật này được sử dụng để xác định vị trí và kích thước của phi thuyền.
        Rectangle r3 = pt.getBounds();

        //  duyệt qua danh sách tàu vũ trụ của người ngoài hành tinh (ListAlien).
        for (int ListAlien_run = 0; ListAlien_run < ListAlien.size(); ListAlien_run++) {

            //Lấy tàu vũ trụ của người ngoài hành tinh hiện tại từ danh sách.
            AlienSpaceShip alien = ListAlien.get(ListAlien_run);
            // Lấy hình chữ nhật bao quanh tàu vũ trụ của người ngoài hành tinh. Hình chữ nhật này xác định vị trí và kích thước của tàu vũ trụ người ngoài hành tinh
            Rectangle r2 = alien.getBounds();
            // Kiểm tra xem hình chữ nhật bao quanh phi thuyền người chơi (r3) có va chạm với hình chữ nhật bao quanh tàu vũ trụ của người ngoài hành tinh (r2) không
            if (r3.intersects(r2)) {
                //Giảm điểm máu của phi thuyền người chơi: pt.heath--. Điểm máu được giảm xuống,
                pt.heath--;
                // nếu điểm máu dưới hoặc bằng 0, phi thuyền người chơi sẽ không còn hiển thị và trò chơi kết thúc 
                if (pt.heath <= 0) {
                    pt.setVisible(Boolean.FALSE);
                    inGame = false;
                    stopBackgroundMusic();
                }
                //  đồng nghĩa với việc phi thuyền người ngoài hành tinh đã bị tiêu diệt.
                alien.setVisible(Boolean.FALSE);
            }

            // : Lấy danh sách các viên đạn từ tàu vũ trụ của người ngoài hành tinh. Mỗi tàu vũ trụ của người ngoài hành tinh có một danh sách các viên đạn (bao gồm cả viên đạn của phi thuyền).
            ArrayList<AlienBullets> ms = alien.getBullets();
            // duyệt qua danh sách các viên đạn từ tàu vũ trụ của người ngoài hành tinh
            for (AlienBullets m : ms) {
                //Lấy hình chữ nhật bao quanh viên đạn hiện tại. Hình chữ nhật này xác định vị trí và kích thước của viên đạn.
                Rectangle r4 = m.getBounds();
                // hình chữ nhật bao quanh viên đạn (r4) có va chạm với hình chữ nhật bao quanh phi thuyền người chơi (r3)  và phi thuyền người chơi đang hiển thị (pt.isVisible())
                if (r4.intersects(r3) && pt.isVisible()) {
                    // Điểm máu được giảm xuống
                    pt.heath--;
                    // nếu điểm máu dưới hoặc bằng 0, phi thuyền người chơi sẽ không còn hiển thị và trò chơi kết thúc 
                    if (pt.heath <= 0) {
                        // phi thuyền người chơi sẽ không còn hiển thị và trò chơi kết thúc 
                        pt.setVisible(Boolean.FALSE);
                        inGame = false;
                        stopBackgroundMusic();
                    }
                    // việc viên đạn của tàu vũ trụ người ngoài hành tinh đã va chạm và bị hủy bỏ.
                    m.setVisible(Boolean.FALSE);
                }
            }
        }
        // danh sách các viên đạn từ phi thuyền người chơi
        ArrayList<SpaceShipBullets> ms = pt.getBullets();
        // duyệt qua danh sách các viên đạn từ phi thuyền người chơi.
        for (SpaceShipBullets m : ms) {

            Rectangle r1 = m.getBounds();// : Lấy hình chữ nhật bao quanh viên đạn hiện tại từ phi thuyền người chơi.
            //duyệt qua danh sách tàu vũ trụ của người ngoài hành tinh (ListAlien) để kiểm tra va chạm.
            for (int ListAlien_run = 0; ListAlien_run < ListAlien.size(); ListAlien_run++) {

                AlienSpaceShip alien = ListAlien.get(ListAlien_run);

                Rectangle r2 = alien.getBounds();// Lấy hình chữ nhật bao quanh tàu vũ trụ của người ngoài hành tinh
                /*hình chữ nhật bao quanh viên đạn từ phi thuyền người chơi (r1) có va chạm với hình chữ nhật bao quanh tàu vũ trụ của người ngoài hành tinh (r2)
                và tàu vũ trụ của người ngoài hành tinh đang hiển thị (alien.isVisible()) không*/
                if (r1.intersects(r2) && alien.isVisible()) {
                    //  để bắt đầu một chuỗi hiệu ứng hoặc sự kiện sau khi va chạm xảy ra
                    currentFrameExp = 0;
                    alien.heath--;// điểm sức khỏe giảm
                    if (alien.heath <= 0) {
                        //Đặt tàu vũ trụ của người ngoài hành tinh không còn hiển thị: 
                        alien.setVisible(Boolean.FALSE);
                    }
                    //Phi thuyền người chơi là không hiển thị. Điều này có nghĩa rằng viên đạn sẽ biến mất trên màn hình sau khi va chạm với tàu vũ trụ của người ngoài hành tinh.
                    m.setVisible(Boolean.FALSE);
                }
            }
        }
        /* nếu đang ở trạng thái "boss" (Gaming là "boss"), 
        thì đoạn mã kiểm tra va chạm giữa đạn của boss và tàu vũ trụ của người chơi.
        Nếu có va chạm (r1.intersects(r3)), thì tàu vũ trụ của người chơi mất 2 điểm sức khỏe (pt.heath -= 2) 
        và đạn của boss sẽ biến mất (m.setVisible(Boolean.FALSE)).
        Nếu sức khỏe của tàu vũ trụ giảm xuống dưới hoặc bằng 0, 
        thì tàu vũ trụ của người chơi sẽ trở nên không hiển thị (pt.setVisible(Boolean.FALSE)) 
        và trò chơi kết thúc (inGame = false).*/
        
        
        if(Gaming == "boss"){
            ArrayList<BossBullets> ms_boss = boss.getBullets();
        
            for (BossBullets m : ms_boss) {

                Rectangle r1 = m.getBounds();

                    if (r1.intersects(r3)) {
                        currentFrameExp =0;
                        pt.heath -= 2;
                        if(pt.heath <= 0){
                            pt.setVisible(Boolean.FALSE);
                            inGame = false;
                            stopBackgroundMusic();
                        }
                        m.setVisible(Boolean.FALSE);
                    }
            }
            
            /*kiểm tra va chạm giữa đạn của tàu vũ trụ của người chơi và boss. 
            Nếu có va chạm (r1.intersects(r2)), tàu vũ trụ của boss sẽ mất 1 điểm sức khỏe (boss.heath--)
            và đạn của tàu vũ trụ của người chơi sẽ biến mất (m.setVisible(Boolean.FALSE)). 
            Nếu sức khỏe của boss giảm xuống 0,
            thì boss sẽ trở nên không hiển thị (boss.setVisible(Boolean.FALSE)).*/
            for (SpaceShipBullets m : ms) {
                Rectangle r1 = m.getBounds();
                Rectangle r2 = boss.getBounds();

                if (r1.intersects(r2) ) {
                    currentFrameExp =0;
                    boss.heath --;
                    if(boss.heath == 0){
                        boss.setVisible(Boolean.FALSE);
                    }
                    m.setVisible(Boolean.FALSE);
                }
            }
        }
    }
    
    
    // TẢI HÌNH NỀN TỪ 1 TỆP ẢNH - GÁN CHO BIẾN backGround
    private void loadBG(String name){
        URL imgUrl = null;
        imgUrl = getClass().getClassLoader().getResource(name);
        if (imgUrl == null) {
            System.err.println("Couldn't find file: ");
        } else {
            try {
                backGround = ImageIO.read(imgUrl);  // load image via URL
            } catch (IOException ex) {
            }
        }
    }
}