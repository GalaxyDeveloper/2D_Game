package felix.game.start.entities;

import felix.game.start.InputHandler;
import felix.game.start.gfx.Colors;
import felix.game.start.gfx.Font;
import felix.game.start.gfx.HealthBar;
import felix.game.start.gfx.Screen;
import felix.game.start.level.Level;

/**
 * Created by IntelliJ IDEA.
 */
public class Player extends Mob {
	private InputHandler input;
	private int color = Colors.get(-1, 111, 145, 543);
	public int scale = 1;
	private boolean isSwimming = false;
	private int tickCount = 0;
	private String username;
	private int originalUsernameLength;
	private int healthLevel = 10;

	public Player(Level level, int x, int y, InputHandler input, String username) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.username = username;
		this.originalUsernameLength = username.length();
	}

	@Override
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void tick() {
		int xa = 0, ya = 0;
		if (input.up.isPressed())
			ya -= 1;
		if (input.down.isPressed())
			ya += 1;
		if (input.left.isPressed())
			xa -= 1;
		if (input.right.isPressed())
			xa += 1;
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			if (hasCollided(xa, ya)) {
				if (tickCount % 20 == 0) {
					healthLevel = healthLevel - 1;
				}
			} else {
				if (healthLevel < 10 && (tickCount % 100 == 0)) {
					healthLevel=healthLevel+1;
				}
			}
			isMoving = true;
		} else {
			isMoving = false;
		}

		if (level.getTile(x >> 3, y >> 3).getId() == 3) {
			isSwimming = true;
		}
		if (isSwimming && level.getTile(x >> 3, y >> 3).getId() != 3) {
			isSwimming = false;
		}
		tickCount++;
	}

	@Override
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (movingDir == 1) {
			xTile += 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2;

		if (isSwimming) {
			int waterColor = 0;
			yOffset += 4;
			if (tickCount % 60 < 15) {
				waterColor = Colors.get(-1, -1, 225, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset -= 1;
				waterColor = Colors.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColor = Colors.get(-1, 115, -1, 225);
			} else if (45 <= tickCount % 60 && tickCount % 60 < 60) {
				yOffset -= 1;
				waterColor = Colors.get(-1, 225, 115, -1);
			}
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, 0x00,
					1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor,
					0x01, 1);
		}
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile
				* 32, color, flipTop, scale);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
				(xTile + 1) + yTile * 32, color, flipTop, scale);
		if (!isSwimming) {
			screen.render(xOffset + (modifier * flipBottom),
					yOffset + modifier, xTile + (yTile + 1) * 32, color,
					flipBottom, scale);
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
					+ modifier, (xTile + 1) + (yTile + 1) * 32, color,
					flipBottom, scale);
		}

		if (healthLevel == 0 && username.length() == this.originalUsernameLength) {
			username = username + " Died";
		}

		Font.render(username, screen,
				xOffset - (username.length() - 1) / 2 * 8, yOffset - 10,
				Colors.get(-1, -1, -1, 555), 1);
		
		HealthBar.render(healthLevel, screen, xOffset - 6 * 10 / 2,
				yOffset - 10 - 8, Colors.get(-1, 500, 115, -1), 1);
		
	}
}
