/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author ACER
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javafx.stage.Stage;
//import javax.sound.midi.InvalidMidiDataException;
//import javax.sound.midi.MidiSystem;
//import javax.sound.midi.MidiUnavailableException;
//import javax.sound.midi.Sequence;
//import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class GameMenu extends JPanel implements ActionListener, KeyListener {
    JFrame CuaSoGameMenu;
    
    // KHAI BÁO 2 NÚT CHƠI VÀ THOÁT GAME
    private JButton playButton;
//    private JButton quitButton;
    
//    private Sequencer backgroundMusic;
    private Clip backgroundMusic;
    
    public GameMenu() {
        // ĐẶT HÌNH NỀN CHO MENU
        setLayout(new BorderLayout());  // Sử dụng BorderLayout để đặt hình ảnh nền vào background

        // Tạo một JPanel làm background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // VẼ HÌNH NỀN BẰNG ĐƯỜNG ĐẪN
//                ImageIcon background = new ImageIcon("src/background/background_menu.jpg");
//                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // KÍCH THƯỚC HÌNH NỀN MENU
        backgroundPanel.setPreferredSize(new Dimension(1280, 720));

        // THÊM VÀO MÀN HÌNH CHÍNH
        add(backgroundPanel, BorderLayout.CENTER);

        // TẠO HÌNH ẢNH NÚT CHƠI
        ImageIcon playButtonIcon = new ImageIcon("src/background/background_menu.jpg");
        playButton = new JButton(playButtonIcon);
        // ĐẶT TỌA ĐỘ VÀ KÍCH THƯỚC CHO HÌNH ẢNH
        playButton.setBounds(0, 0, 1280, 720);  // X, Y, Width, Height
        // XÓA VIỀN / KHUNG
        playButton.setBorderPainted(false);
        playButton.addActionListener(this);

//        quitButton = new JButton("Quit");
//        // Đặt tọa độ và kích thước cho nút "Quit"
//        quitButton.setBounds(800, 500, 200, 50);  // X, Y, Width, Height
//        quitButton.addActionListener(this);

        // Đặt các nút trên backgroundPanel
        backgroundPanel.setLayout(null); // Sử dụng layout kiểu null để có thể tự điều chỉnh tọa độ và kích thước
        backgroundPanel.add(playButton);
//        backgroundPanel.add(quitButton);
        
        // Tạo JLabel để hiển thị dòng văn bản
        JLabel startLabel = new JLabel("Nhấn Enter để bắt đầu");
        startLabel.setForeground(Color.WHITE); // Đặt màu văn bản
        startLabel.setFont(new Font("Arial", Font.PLAIN, 25)); // Đặt font và kích thước văn bản

        // Đặt vị trí của JLabel bên dưới nút "Play"
        startLabel.setBounds(800, 600, 482, 30);

        // Thêm JLabel vào backgroundPanel
        backgroundPanel.add(startLabel);
    }


//    public void stopBackgroundMusic() {
//        if (backgroundMusic != null && backgroundMusic.isRunning()) {
//            backgroundMusic.stop();
//            backgroundMusic.close();
//        }
//    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // nếu nhấn vào nút start
        if (e.getSource() == playButton) {
            CotChuyenTrongNha cotchuyen=new CotChuyenTrongNha();
            JFrame frame = new JFrame("Cốt chuyện bắt đầu chiến đấu");
            ImageIcon img = new ImageIcon("src/effect/Icon.png");
            frame.setIconImage(img.getImage());
            frame.setSize(1280, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(cotchuyen);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            cotchuyen.CuaSoCotChuyenTrongNha=frame;
            CuaSoGameMenu.dispose();
            stopBackgroundMusic();
        }
        // nếu nhấn vào nút quit game
//        else if (e.getSource() == quitButton) {
//            System.exit(0); // Quit the application
//        }
    }

//    void loadBackgroundMusic(String midiFilePath) {
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMenu menu = new GameMenu();
            JFrame menuFrame = new JFrame("Game Menu");
            ImageIcon img = new ImageIcon("src/effect/Icon.png");
            menuFrame.setIconImage(img.getImage());
            menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuFrame.getContentPane().add(menu);
            menuFrame.pack();
            menuFrame.setSize(1280, 720);
            menuFrame.setVisible(true);
            menuFrame.setLocationRelativeTo(null);
            menu.loadBackgroundMusic("src/sound/Main.wav");
            menu.CuaSoGameMenu= menuFrame;
            
            // Gắn KeyListener vào CuaSoGameMenu
            menuFrame.addKeyListener(menu);
            menuFrame.setFocusable(true);
            menuFrame.requestFocus();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Xử lý sự kiện khi phím Enter được nhấn
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            CotChuyenTrongNha cotchuyen=new CotChuyenTrongNha();
            JFrame frame = new JFrame("Cốt chuyện bắt đầu chiến đấu");
            ImageIcon img = new ImageIcon("src/effect/Icon.png");
            frame.setIconImage(img.getImage());
            frame.setSize(1280, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(cotchuyen);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            cotchuyen.CuaSoCotChuyenTrongNha=frame;
            CuaSoGameMenu.dispose();
            stopBackgroundMusic();
        }
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void playButtonClick() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setStage(Stage primaryStage) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
