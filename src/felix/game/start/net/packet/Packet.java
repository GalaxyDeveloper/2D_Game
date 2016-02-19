package felix.game.start.net.packet;

import felix.game.start.net.GameClient;
import felix.game.start.net.GameServer;

/**
 * Created by IntelliJ IDEA.
 */
public abstract class Packet {
    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01);
        private int packateId;
        private PacketTypes(int packateId) {
            this.packateId = packateId;
        }

        public int getPackateId() {
            return packateId;
        }
    }

    public byte pakcateId;

    public Packet(int packetId) {
        this.pakcateId = (byte) packetId;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public static PacketTypes lookupPacket(int packetId) {
        for (PacketTypes pt: PacketTypes.values()) {
            if(pt.getPackateId() == packetId ) {
                return pt;
            }
        }

        return PacketTypes.INVALID;
    }
}
