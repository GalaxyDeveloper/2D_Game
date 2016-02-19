package felix.game.start.gfx;

public class HealthBar {
	public static void render(int healthLevel,  Screen screen, int x, int y, int color, int scale) {
		for (int i=0; i<healthLevel; i++) {
			screen.render(x + (i*8), y, 2 + 27 * 32, color, 0x00, scale);
		}
	}
}
