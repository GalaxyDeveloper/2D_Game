package felix.game.start.gfx;

/**
 * Created by IntelliJ IDEA.
 */
public class Font {
    private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "1234567890                      ";
    public static void render(String msg, Screen screen, int x, int y, int color, int scale) {
        msg = msg.toUpperCase();
        for (int i=0; i<msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));
            if (charIndex >=0 ) {
                screen.render(x + (i*8), y, charIndex + 30 * 32, color, 0x00, scale);
            }
        }
    }
}
