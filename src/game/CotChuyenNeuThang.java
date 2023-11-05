/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ACER
 */
public class CotChuyenNeuThang extends JPanel implements KeyListener{
    JFrame CuaSoCotChuyenNeuThang;
    private int currentScene = 0;
    private boolean changeBackground = false;
    private String[] characterNames = {"","","Luna","Usagi",""};
    private String[] dialogues = {
        "Sau khi đã tiêu địch hết tàu địch, Usagi đã giải cứu trái đất thành công\nvà những người thân yêu của mình",
        "Cuộc sống của mọi người đã lại bình thường",
        "Cảm ơn bạn đã đồng hành cùng với chúng tớ giải cứu trái đất!",
        "Tớ rất vui vì đã giúp ích được cho mọi người",
        "Usagi trở về nhà và đã gặp lại người thân của mình\nĐây có lẽ là một kỷ niệm không thể quên đối với cô!"
    };
    private ImageIcon[] characterImages = {
        scaleImage(new ImageIcon("src/character/char_0.png"), 250, 400),
        scaleImage(new ImageIcon("src/character/char_0.png"), 250, 400),
        scaleImage(new ImageIcon("src/character/cat_2.png"), 400, 400),
        scaleImage(new ImageIcon("src/character/usagi_2.png"), 350, 400),
            scaleImage(new ImageIcon("src/character/canhgiadinh.jpg"), 400, 400)
    };
    private ImageIcon background = new ImageIcon("src/background/back_venha.jpg");
    private ImageIcon dialogueFrame = new ImageIcon("src/character/dialogue.png");

    private boolean showLuaChon = false; // Biến để kiểm soát hiển thị nút

    private Clip backgroundMusic;
    
    public CotChuyenNeuThang() {
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
        loadBackgroundMusic("src/sound/Thang.wav");
    }

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
    
    // Hàm thay đổi kích thước hình ảnh
    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentScene == 4 && !changeBackground) {
            // Thay đổi hình nền khi đến thoại của nhân vật thứ 3
            background = new ImageIcon("src/background/back_kit.jpg");
            changeBackground = true; // Đánh dấu rằng đã thay đổi hình nền
        }
        
        // Vẽ hình nền
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);

        // Vẽ hộp thoại hình chữ nhật
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(450, 400, 800, 200);

        // Hiển thị tên nhân vật
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString(characterNames[currentScene], 460, 430);

        // Hiển thị hình ảnh nhân vật
        characterImages[currentScene].paintIcon(this, g, 10, 350);

        // Hiển thị hình khung đối thoại
        dialogueFrame.paintIcon(this, g, 10, 400);

        // Hiển thị đoạn đối thoại
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String[] lines = dialogues[currentScene].split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 500, 500 + i * 30);
        }

        
        
        if (showLuaChon) {
            // Hiển thị nút "Start Game"
            JButton startButton = new JButton("Tạm biệt và hẹn gặp lại");
            startButton.setFont(new Font("Arial", Font.PLAIN, 18));
            startButton.setBounds(500, 200, 300, 50);
            startButton.setBackground(new Color(0, 0, 0, 128)); // Màu nền đen trong suốt
            startButton.setForeground(Color.WHITE); // Màu chữ màu trắng
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
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
