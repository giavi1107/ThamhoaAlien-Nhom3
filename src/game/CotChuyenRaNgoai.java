/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author ACER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
//import javax.sound.midi.InvalidMidiDataException;
//import javax.sound.midi.MidiSystem;
//import javax.sound.midi.MidiUnavailableException;
//import javax.sound.midi.Sequence;
//import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class CotChuyenRaNgoai extends JPanel implements KeyListener{
    JFrame CuaSoCotChuyenRaNgoai;
    private int currentScene = 0;
    private String[] characterNames = {"Usagi","Usagi", ""};
    private String[] dialogues = {
        "Thành phố đã xảy ra chuyện gì thế này?",
        "Cái quái gì đang bay trên trời vậy?",
        "Usagi đang ngước nhìn bầu trời..."
    };
    private ImageIcon[] characterImages = {
        scaleImage(new ImageIcon("src/character/usagi_4.jpg"), 400, 400),
        scaleImage(new ImageIcon("src/character/usagi_4.jpg"), 400, 400),
        scaleImage(new ImageIcon("src/character/char_0.png"), 350, 400)
    };
    private ImageIcon background = new ImageIcon("src/background/back_city.png");
    private boolean changeBackground = false;
    private ImageIcon dialogueFrame = new ImageIcon("src/character/dialogue.png");

    private boolean showLuaChon = false; // Biến để kiểm soát hiển thị nút

//    private Sequencer backgroundMusic;
    private Clip backgroundMusic;
    
    public CotChuyenRaNgoai() {
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        
        // Thêm sự kiện click chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentScene < characterNames.length - 1) {
                    currentScene++;
                    repaint();
                } else {
                    showLuaChon = true; // Hiển thị nút để bắt đầu game
                    repaint();
                }
            }
        });

        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentScene < characterNames.length - 1) {
                    currentScene++;
                } else {
                    showLuaChon = true; // Hiển thị nút để bắt đầu game
                }
                repaint();
            }
        });
        timer.start();
        loadBackgroundMusic("src/sound/RaNgoai.wav");
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
    void loadBackgroundMusic(String wavFilePath) {
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

    
    // Hàm thay đổi kích thước hình ảnh
    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Kiểm tra xem có nên thay đổi hình nền hay không
        if (currentScene == 2 && !changeBackground) {
            // Thay đổi hình nền khi đến thoại của nhân vật thứ 3
            background = new ImageIcon("src/background/back_ufo.jpg");
            changeBackground = true; // Đánh dấu rằng đã thay đổi hình nền
        }
//        else if (currentScene == 2 && !changeBackground) {
//            // Thay đổi hình nền khi đến thoại của nhân vật thứ 3
//            background = new ImageIcon("src/background/back_city.png");
//            changeBackground = true; // Đánh dấu rằng đã thay đổi hình nền
//        }
        
        // Vẽ hình nền
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);

        // Vẽ hộp thoại hình chữ nhật
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(450, 400, 800, 200);

        // Hiển thị tên nhân vật
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(characterNames[currentScene], 460, 430);

        // Hiển thị hình ảnh nhân vật
        characterImages[currentScene].paintIcon(this, g, 0, 300);

        // Hiển thị hình khung đối thoại
        dialogueFrame.paintIcon(this, g, 10, 400);

        // Hiển thị đoạn đối thoại
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] lines = dialogues[currentScene].split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 500, 500 + i * 30);
        }

        if (showLuaChon) {
            // Hiển thị nút "Start Game"
            JButton startButton = new JButton("Bạn muốn biết chuyện gì đã xảy ra với thành phố.");
            startButton.setFont(new Font("Arial", Font.PLAIN, 18));
            startButton.setBounds(380, 200, 500, 50);
            startButton.setBackground(new Color(0, 0, 0, 128)); // Màu nền đen trong suốt
            startButton.setForeground(Color.WHITE); // Màu chữ màu trắng
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CotChuyenDoiThoaiMeo cotchuyen = new CotChuyenDoiThoaiMeo();
                    JFrame frame = new JFrame("Thu thập vật phẩm");
                    ImageIcon img = new ImageIcon("src/effect/Icon.png");
                    frame.setIconImage(img.getImage());
                    frame.setSize(1280, 720);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(cotchuyen);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    cotchuyen.CuaSoCotChuyenDoiThoaiMeo=frame;
                    // đóng cửa sổ này
                    CuaSoCotChuyenRaNgoai.dispose();
                    stopBackgroundMusic();
                }
            });
            startButton.setVisible(true);
            this.add(startButton);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Xử lý sự kiện khi phím Enter được nhấn
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (currentScene < characterNames.length - 1) {
                currentScene++;
                repaint();
            } else {
                showLuaChon = true; // Hiển thị nút để bắt đầu game
                repaint();
            }
        }
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
