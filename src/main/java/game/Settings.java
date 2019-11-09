package game;

/**
 * Contain static constants.
 */
public class Settings {
    // WINDOW CONFIGURATION
    public static int CANVAS_WIDTH = 700;
    public static int CANVAS_HEIGHT = 420;
    public static int WINDOW_WIDTH = 800;
    public static int WINDOW_HEIGHT = 600;

    // MAP CONFIGURATION
    public static int TILE_WIDTH = 64;
    public static int TILE_HEIGHT = 64;
    public static int MAP_WIDTH = 640;
    public static int MAP_WIDTH_IN_TILES = MAP_WIDTH/TILE_WIDTH;
    public static int MAP_HEIGHT = 384;
    public static int MAP_HEIGHT_IN_TILES = MAP_HEIGHT/TILE_HEIGHT;

    // TILE TYPE ID
    public static final int ROAD = 0;
    public static final int MOUNTAIN = 1;
    public static final int TOWER = 2;

    // ENEMY RESOLUTION
    public static final int ENEMY_WIDTH = 64;
    public static final int ENEMY_HEIGHT = 64;

    // ENEMY HP
    public static final int NORMAL_HP = 5;
    public static final int TANKER_HP = 10;
    public static final int SMALLER_HP = 2;
    public static final int BOSS_HP = 13;

    //ENEMY VELOCITY
    public static final int NORMAL_VELOCITY = 2;
    public static final int TANKER_VELOCITY = 2;
    public static final int SMALLER_VELOCITY = 2;
    public static final int BOSS_VELOCITY = 2;

    public static final int NORMAL_ENEMY_DEFENSE = 0;


    // TOWER
    public static final int NORMAL_TOWER_DAMAGE = 1;
    public static final int BULLET_WIDTH = 10;
    public static final int BULLET_HEIGHT = 10;

}
