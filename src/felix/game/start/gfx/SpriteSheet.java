package felix.game.start.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 */
public class SpriteSheet {
    public String path;
    public int width;
    public int height;
    public int[] pixels;

    public SpriteSheet(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (image == null) {
            return;
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.path = path;

        pixels = image.getRGB(0, 0, width, height, null, 0, width);

        for (int i=0; i<pixels.length; i++) {
            pixels[i] = (pixels[i] & 0xff) / 64;
        }

        for (int i=0; i<8; i++) {
            System.out.println(pixels[i]);
        }

    }
}
