package felix.game.start.net.packet;

import felix.game.start.net.GameClient;
import felix.game.start.net.GameServer;

/**
 * Created by IntelliJ IDEA.
 */
public class Packet00Login extends Packet {
    private String username;

    public Packet00Login(byte[] data) {
        super(00);
        this.username = readData(data);
    }

    public Packet00Login(String username) {
        super(00);
        this.username = username;
    }
    @Override
    public void writeData(GameClient client) {

    }

    @Override
    public void writeData(GameServer server) {
    }
}
