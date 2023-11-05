/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author ACER
 */
import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class GameMenuJavaFX extends Application {
    private Stage primaryStageGameMenuApp;
    private Clip backgroundMusic;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStageGameMenuApp = primaryStage;
        primaryStage.setTitle("Game Menu");

        // Tạo nền menu
        StackPane root = new StackPane();
        root.setPrefSize(1280, 720);

        // Đặt hình nền
        Image backgroundImage = new Image("file:src/background/background_menu.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(backgroundImg));

        // Tạo nút "Play"
        Image playButtonImage = new Image("file:src/background/background_menu.jpg");
        ImageView playButtonImageView = new ImageView(playButtonImage);
        Button playButton = new Button("", playButtonImageView);
        playButton.setOnAction(event -> handlePlayButtonClick());

        // Đặt vị trí nút "Play"
        playButton.setTranslateX(0);
        playButton.setTranslateY(0);

        // Xóa viền của nút "Play"
        playButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Tạo sự kiện cho phím Enter
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handlePlayButtonClick();
            }
        });

//        root.getChildren().add(playButton);
        
        // Tạo label cho thông tin
        Label playLabel = new Label("Nhấn Enter để bắt đầu");
        playLabel.setTextFill(Color.WHITE); // Đặt màu chữ
        playLabel.setStyle("-fx-font-size: 20;"); // Đặt kích thước chữ
        playLabel.setTranslateX(320);
        playLabel.setTranslateY(250); // Đặt vị trí theo trục Y

        root.getChildren().addAll(playButton,playLabel);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Đặt icon cho cửa sổ
        primaryStage.getIcons().add(new Image("file:src/effect/Icon.png"));
        primaryStage.show();
        loadBackgroundMusic("src/sound/Main.wav");
    }

    private void handlePlayButtonClick() {
        CotChuyenTrongNha cotchuyen = new CotChuyenTrongNha();
        JFrame frame = new JFrame("Cốt chuyện bắt đầu chiến đấu");
        ImageIcon img = new ImageIcon("src/effect/Icon.png");
        frame.setIconImage(img.getImage());
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(cotchuyen);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        cotchuyen.CuaSoCotChuyenTrongNha = frame;
        primaryStageGameMenuApp.close();
        stopBackgroundMusic();
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

}

