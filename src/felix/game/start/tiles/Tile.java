package felix.game.start.tiles;

import felix.game.start.gfx.Colors;
import felix.game.start.gfx.Screen;
import felix.game.start.level.Level;

/**
 * Created by IntelliJ IDEA.
 */
public abstract class Tile {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colors.get(000, -1, -1, -1), 0xFF000000);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(-1, 333, -1, -1), 0xFF555555);
    public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00);
    public static final Tile WATER = new AnimatedTile(3, new int[][]{new int[]{0, 4}, new int[]{1,4}, new int[] {2, 4}, new int[] {1,4}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 1000);
    public static final Tile SAND = new BasicTile(4, 3, 0, Colors.get(435, 440, 111, -1), 0xFFFFFF00);
    public static final Tile WOOD = new BasicSolidTile(5, 4, 0, Colors.get(455, 421, 000, -1), 0xFF7F3300);
    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    private int levelColor;

    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) {
        this.id = (byte)id;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        tiles[id] = this;
        this.levelColor = levelColor;
    }

    public byte getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isEmitter() {
        return emitter;
    }

    public  abstract void tick();

    public abstract void render(Screen screen, Level level, int x, int y);

    public int getLevelColor() {
        return levelColor;
    }
}
