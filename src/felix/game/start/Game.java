package felix.game.start;

import felix.game.start.entities.Player;
import felix.game.start.gfx.Screen;
import felix.game.start.gfx.SpriteSheet;
import felix.game.start.level.Level;
import felix.game.start.net.GameClient;
import felix.game.start.net.GameServer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
    public final int WIDTH = 160;
    public final int HEIGHT = WIDTH / 12 * 9;
    public final int SCALE = 3;
    public final String NAME = "2D GAME";
    private boolean running;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private Screen screen;
    private JFrame frame;
    private int tickCount = 0;
    private InputHandler input;
    private int[] colors = new int[6 * 6 * 6];
    private Level level;
    private Player player;

    private GameClient socketClient;
    private GameServer socketServer;

    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        input = new InputHandler(this);
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet_new.png"));
        level = new Level("levels/level1.png");
        player = new Player(level, 0, 0, input, JOptionPane.showInputDialog("Please enter username:"));
        level.addEntity(player);

        socketClient.sendData("ping".getBytes());
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();

        if(JOptionPane.showConfirmDialog(this, "Do you want to run the server?") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;
        int ticks = 0;
        int frames = 0;
        double delta = 0D;
        long lastTimer = System.currentTimeMillis();

        init();
        while (running) {
            boolean shouldRender = true;
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (delta >= 1) {
                tick();
                ticks++;
                delta--;
                shouldRender = true;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }
            if ((System.currentTimeMillis() - lastTimer) >= 1000L) {
                System.out.println(frames + " : " + ticks);
                lastTimer += 1000L;
                ticks = 0;
                frames = 0;
            }
        }
    }

    private void tick() {
        tickCount++;
        requestFocus();
        level.tick();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        int xOffset = player.x - (screen.width / 2);
        int yOffset = player.y - (screen.height / 2);

        level.renderTiles(screen, xOffset, yOffset);

//        for (int y = 0; y < 32; y++) {
//            for (int x = 0; x < 32; x++) {
//                screen.render(x<<3, y<<3, 0, Colors.get(555, 505, 055, 550), false, false);
//            }
//        }

//        for(int x=0; x<level.width; x++) {
//            int color = Colors.get(-1, -1, -1, 000);
//            if (x % 10 == 0 && x != 0) {
//                color = Colors.get(-1, -1, -1, 500);
//            }
//            Font.render((x % 10) + "", screen, x * 8, 0, color);
//        }
        level.renderEntities(screen);
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colorCode = screen.pixels[x + y * screen.width];
                if (colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
