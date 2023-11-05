/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author ACER
 */
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
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

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameThuThapTimJavaFX extends Application {
    Pane CuaSoGameThuThapTimJavaFX;
    private static final int GAME_WIDTH = 1280;
    private static final int GAME_HEIGHT = 720;
    private static final int PLAYER_WIDTH = 64;
    private static final int PLAYER_HEIGHT = 64;
    private static final int ITEM_WIDTH = 32;
    private static final int ITEM_HEIGHT = 32;
    private static final int PLAYER_SPEED = 5;
    private static final int ITEM_SPEED = 1;
    private static final int MAX_HEALTH = 100;

    private int playerX = 50;
    private int playerHealth = 0;
    private int score = 0;

    private Image backgroundImage;
    private Image playerImage;
    private Image itemImage;

    private Random random = new Random();

    private List<FallingItem> items = new ArrayList<>();

    private boolean gameOverWindowDisplayed = false;

//    private Sequencer backgroundMusic;
    private Clip backgroundMusic;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Collect Item Game");
        Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        loadImages();
        createRandomItems();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (!gameOverWindowDisplayed) {
                    movePlayer();
                    createRandomItems();
                    moveItems();
                    checkCollision();
                }

                drawBackground(gc);
                drawPlayer(gc);
                drawItems(gc);
                drawPlayerHealth(gc);
                drawScore(gc);

                if (playerHealth >= MAX_HEALTH && !gameOverWindowDisplayed) {
                    // Open a new window or transition to another screen
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
                    cotchuyen.CuaSoCotChuyenSauKhiThuNhap = frame;
                    primaryStage.close();
                    stopBackgroundMusic();
                }
            }
        }.start();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));

        primaryStage.setScene(scene);
        primaryStage.show();
        loadBackgroundMusic("src/sound/CollectItem.wav");
    }

    private void loadImages() {
        backgroundImage = new Image("file:src/background/back_venha.jpg");
        playerImage = new Image("file:src/character/char_chibi.png");
        itemImage = new Image("file:src/vatpham/item_1.png");
    }

    private void drawBackground(GraphicsContext gc) {
        gc.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT);
    }

    private void drawPlayer(GraphicsContext gc) {
        gc.drawImage(playerImage, playerX, GAME_HEIGHT - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private void drawItems(GraphicsContext gc) {
        for (FallingItem item : items) {
            if (!item.isCollected()) {
                gc.drawImage(itemImage, item.getX(), item.getY(), ITEM_WIDTH, ITEM_HEIGHT);
            }
        }
    }

    private void drawPlayerHealth(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(10, 10, playerHealth, 20);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(10, 10, MAX_HEALTH, 20);
    }

    private void drawScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 20));
        gc.fillText("Score: " + score, 10, 40);
    }

    private void movePlayer() {
        if (playerX < 0) {
            playerX = 0;
        } else if (playerX > GAME_WIDTH - PLAYER_WIDTH) {
            playerX = GAME_WIDTH - PLAYER_WIDTH;
        }
    }

    private void moveItems() {
        List<FallingItem> itemsToRemove = new ArrayList<>();
        for (FallingItem item : items) {
            item.move(ITEM_SPEED);
            if (item.getY() > GAME_HEIGHT) {
                itemsToRemove.add(item);
            }
        }
        items.removeAll(itemsToRemove);
    }

    private void checkCollision() {
        for (FallingItem item : items) {
            if (!item.isCollected() && playerCollidesWithItem(playerX, GAME_HEIGHT - PLAYER_HEIGHT, item)) {
                item.collect();
                score += 10;
                playerHealth += 10;
            }
        }
    }

    private boolean playerCollidesWithItem(int playerX, int playerY, FallingItem item) {
        return playerX < item.getX() + ITEM_WIDTH
                && playerX + PLAYER_WIDTH > item.getX()
                && playerY < item.getY() + ITEM_HEIGHT
                && playerY + PLAYER_HEIGHT > item.getY();
    }

    private void createRandomItems() {
        if (random.nextDouble() < 0.02) {
            int itemX = random.nextInt(GAME_WIDTH - ITEM_WIDTH);
            int itemY = 0;
            FallingItem item = new FallingItem(itemX, itemY, ITEM_WIDTH, ITEM_HEIGHT);
            items.add(item);
        }
    }

    private void handleKeyPress(javafx.scene.input.KeyCode keyCode) {
        if (keyCode == javafx.scene.input.KeyCode.LEFT) {
            playerX -= PLAYER_SPEED;
        } else if (keyCode == javafx.scene.input.KeyCode.RIGHT) {
            playerX += PLAYER_SPEED;
        }
    }

    private Rectangle getPlayerBounds() {
        return new Rectangle(playerX, GAME_HEIGHT - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private void openGameOverWindow(Stage primaryStage) {
        // Add your code to open a new window or transition to another screen here
        // For example, you can create a new JavaFX Stage and Scene for the game over screen.
        // Remember to set gameOverWindowDisplayed to true to prevent multiple windows from opening.
    }

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

    class FallingItem {

        private int x;
        private int y;
        private int width;
        private int height;
        private boolean collected;

        public FallingItem(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.collected = false;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean isCollected() {
            return collected;
        }

        public void move(int speed) {
            y += speed;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        public void collect() {
            collected = true;
        }
    }
}
