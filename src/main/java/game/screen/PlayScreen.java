package game.screen;
import game.*;
import game.store.TowerStore;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Contain UI elements, including Game Field. Responsible for handling user input.
 */
public class PlayScreen extends Screen {
    public static boolean isAutoPlay = false;
    public static MouseEvent mouse;
    private static GraphicsContext graphicsContextPro; //la 1 bien static de dung trong cai khac
    private static int money = Settings.PLAYER_START_MONEY;
    private static int health = Settings.PLAYER_START_HP;
    public static Group group;
    public static AnimationTimer timer;
    public static boolean isPause = false;
    private static double fps = 0;
    private static Text moneyTxt;
    private static Text healthTxt;
    private final Font brushUp = Font.loadFont(getClass().getResourceAsStream("/iCielBrushUp.otf"), 20);
    private Canvas canvas;
    public PlayScreen() {
        setAndPlayMedia();
        canvas = new Canvas(Settings.CANVAS_WIDTH, Settings.CANVAS_HEIGHT);
        group = new Group(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        graphicsContextPro = canvas.getGraphicsContext2D();
        GameField gameField = new GameField();
        if(WelcomeScreen.isLoadData){
            GameField.loadOrderGame = true; //nếu load game cũ thì cho bằng true
            new GameManager().loadData();
            WelcomeScreen.isLoadData = false;
        }
        if(GameField.mapURL.contains("map_1")){
            GameField.spawner = new Spawner(GameField.loadOrderGame, 0, 3);
        }else if(GameField.mapURL.contains("map_2")) {
            GameField.spawner = new Spawner(GameField.loadOrderGame, 9, 5);
        }else{
            GameField.spawner = new Spawner(GameField.loadOrderGame, 0, 3);
        }
        GameField.loadOrderGame = false; //sau khi new spawner thì cho bằng false để cho lần load sau

        Text fpsTxt = new Text(Double.toString(fps));
        fpsTxt.setX(10);
        fpsTxt.setY(20);
        fpsTxt.setFill(Color.RED);
        //AI
        StudentRobot.findPositionsAdvantage();
        StudentRobot.readTranningResult("data/trainning_result.txt");
        // Run program
        timer = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, Settings.CANVAS_WIDTH, Settings.CANVAS_HEIGHT);
                gameField.renderAll(gc);
                gameField.runAll();
                whenWinning();
                fps = 1000000000.0 / (l - lastTime);
                fpsTxt.setText(Integer.toString((int)fps));
                lastTime = l;
                //AI
                if(isAutoPlay){
                    StudentRobot.putTowerGenius(GameField.numberOfEnemy, money);
                }
            }
        };
        timer.start();
        group.getChildren().add(fpsTxt);

        addAllElements();

        this.scene = new Scene(group, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        scene.setOnMouseMoved(t->{
            mouse = t;
        });

    }

    private void whenWinning(){
        if(Spawner.getNumOfWavesRemaining() == 0 && GameField.numberOfEnemy == 0){
            GameStage.signNewScreen(new GameWinningScreen());
        }
    }

    private void addAllElements() {
        addBottomPanel();
        group.getChildren().add(new TowerInfoPanel());
    }

    @Override
    public void clear() {
        timer.stop();
        GameField.clear();
        group.getChildren().clear();
        PlayScreen.money = Settings.PLAYER_START_MONEY;
        PlayScreen.health = Settings.PLAYER_START_HP;
        scene = null;
        System.gc();
    }

    public static void spendMoney(int price) {
        money -= price;
        if (money < 0) {
            money = 0;
        }
        moneyTxt.setText(Integer.toString(money));
    }

    public static void playerTakeDamage() {
        health--;
        if (health <= 0) {
            GameStage.signNewScreen(new GameOverScreen());
        }
        healthTxt.setText(Integer.toString(health));
    }

    public static void rewardPlayer(int dropReward) {
        money += dropReward;
        moneyTxt.setText(Integer.toString(money));
    }

    public static int getMoney() {
        return PlayScreen.money;
    }

    private void addBottomPanel() {
        // Create new store
        TowerStore towerStore = new TowerStore();
        towerStore.setBuyEvent(canvas);
        // Create player info box
        VBox playerInfo = addPlayerInfos();
        // Create bottom panel
        HBox bottomPanel = new HBox();
        bottomPanel.setLayoutX(0);
        bottomPanel.setLayoutY(480);
        bottomPanel.setPrefSize(800, 120);
        // Add to scene
        bottomPanel.getChildren().addAll(playerInfo, towerStore.getStore());
        group.getChildren().add(bottomPanel);
    }

    private VBox addPlayerInfos() { // TODO: Clean the code if possible, it's hideous
        VBox playerInfo = new VBox();
        playerInfo.setPrefSize(150, 120);
        playerInfo.setPadding(new Insets(20));
        playerInfo.setAlignment(Pos.CENTER);
        playerInfo.setStyle("-fx-background-color: #abeab4");
        // add money display
        HBox moneyDisplay = new HBox();
        moneyDisplay.setSpacing(10);
        ImageView coinImage = new ImageView(Settings.COIN_STACK_IMG);
        moneyTxt = new Text(Integer.toString(PlayScreen.money));
        moneyTxt.setFont(brushUp);
        moneyDisplay.getChildren().addAll(coinImage, moneyTxt);

        // add player hp display
        HBox playerHpDisplay = new HBox();
        playerHpDisplay.setSpacing(10);
        ImageView hpImage = new ImageView(Settings.HEART_IMG);
        healthTxt = new Text(Integer.toString(PlayScreen.health));
        healthTxt.setFont(brushUp);
        playerHpDisplay.getChildren().addAll(hpImage, healthTxt);

        playerInfo.getChildren().addAll(moneyDisplay, playerHpDisplay);
        return playerInfo;
    }
    private void setAndPlayMedia() {
        MediaPlayer mediaPlayer = new MediaPlayer(MediaManager.PLAYSCREEN_FX);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }
}
