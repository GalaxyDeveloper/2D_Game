package felix.game.start.tiles;

/**
 * Created by IntelliJ IDEA.
 */
public class BasicSolidTile extends BasicTile {
    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, x, y, tileColor, levelColor);
        solid = true;
    }
}
