package felix.game.start.entities;

import felix.game.start.gfx.Screen;
import felix.game.start.level.Level;

/**
 * Created by IntelliJ IDEA.
 */
public abstract class Entity {
    public int x, y;
    protected Level level;

    public Entity(Level level) {
        init(level);
    }

    public void init(Level level) {
        this.level = level;
    }

    public abstract void tick();
    public abstract void render(Screen screen);
}
