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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//import javax.sound.midi.InvalidMidiDataException;
//import javax.sound.midi.MidiSystem;
//import javax.sound.midi.MidiUnavailableException;
//import javax.sound.midi.Sequence;
//import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class GameThuThapTim extends JPanel {
    JFrame CuaSoGameThuThapTim;
    private int playerX = 50;
    private int playerWidth = 64;
    private int playerHeight = 64;
    private int playerSpeed = 5;
    private Timer timer;
    private Image backgroundImage;
    private Image playerImage;
    private Image itemImage;
    private int itemSpeed = 2;
    private int itemX = 400;
    // TRẠNG THÁI TRÒ CHƠI
    private boolean gameOverWindowDisplayed = false;
    
    private Clip backgroundMusic;
    
    private ArrayList<FallingItem> items;
    private int playerHealth = 0;
    private int score = 0;

    public GameThuThapTim() {
        initGame();
    }

    private void initGame() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(1280, 720));
        loadImage();
        timer = new Timer(10, new GameLoop());
        timer.start();
        items = new ArrayList<>();
        loadBackgroundMusic("src/sound/GameThuThap.wav");
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

    private void loadImage() {
        ImageIcon bg = new ImageIcon("src/background/back_venha.jpg");
        backgroundImage = bg.getImage();
        ImageIcon player = new ImageIcon("src/character/char_chibi.png");
        playerImage = player.getImage();
        ImageIcon item = new ImageIcon("src/vatpham/item_1.png");
        itemImage = item.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawPlayer(g);
        drawItems(g);
        drawPlayerHealth(g);
        drawScore(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this);
    }

    private void drawPlayer(Graphics g) {
        g.drawImage(playerImage, playerX, 600, playerWidth, playerHeight, this);
    }

    private void drawItems(Graphics g) {
        for (FallingItem item : items) {
            if (!item.isCollected) {
                g.drawImage(itemImage, item.getX(), item.getY(), item.getWidth(), item.getHeight(), this);
            }
        }
    }

    private void drawPlayerHealth(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(10, 10, playerHealth, 20);
        g.setColor(Color.BLACK);
        g.drawRect(10, 10, 100, 20);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 40);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                playerX -= playerSpeed;
            } else if (key == KeyEvent.VK_RIGHT) {
                playerX += playerSpeed;
            }
        }
    }

    private class GameLoop implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerX < 0) {
                playerX = 0;
            } else if (playerX > getWidth() - playerWidth) {
                playerX = getWidth() - playerWidth;
            }

            itemX -= itemSpeed;
            if (itemX < 0) {
                itemX = 400;
            }

            createRandomItems();
            moveItems();
            checkCollision();
            if (score >= 100 && !gameOverWindowDisplayed) {
                CotChuyenSauKhiThuNhap cotchuyen = new CotChuyenSauKhiThuNhap();
                JFrame frame = new JFrame("Cốt chuyện sau khi thu thập");
                ImageIcon img = new ImageIcon("src/effect/Icon.png");
                frame.setIconImage(img.getImage());
                frame.setSize(1280, 720);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(cotchuyen);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                gameOverWindowDisplayed = true; // Đánh dấu rằng cửa sổ đã được hiển thị
                cotchuyen.CuaSoCotChuyenSauKhiThuNhap=frame;
                CuaSoGameThuThapTim.dispose();
                stopBackgroundMusic();
            }
            repaint();
        }
    }
//    public void stopBackgroundMusic() {
//        if (backgroundMusic != null && backgroundMusic.isRunning()) {
//            backgroundMusic.stop();
//            backgroundMusic.close();
//        }
//    }
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }


    private void createRandomItems() {
        if (Math.random() < 0.02) {
            int itemX = (int) (Math.random() * getWidth());
            int itemY = 0;
            FallingItem item = new FallingItem(itemX, itemY, 32, 32);
            items.add(item);
        }
    }

    private void moveItems() {
        for (FallingItem item : items) {
            item.move();
        }
    }

    private void checkCollision() {
        Rectangle playerRect = new Rectangle(playerX, 600, playerWidth, playerHeight);

        for (FallingItem item : items) {
            if (!item.isCollected && playerRect.intersects(item.getBounds())) {
                item.collect();
                score += 10;
                playerHealth += 10;
            }
        }
    }

//    private void updateGameStatus() {
//        playerHealth -= 1;
//
//        
//    }
}
