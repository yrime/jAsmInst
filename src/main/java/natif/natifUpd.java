package natif;

import java.net.URL;
import java.nio.ByteBuffer;

public class natifUpd {

    //   public static native ByteBuffer get_shared_bytes(String shmName);
    public static native void mapUp(int i);

    static {
        String path = System.getProperty("user.dir");
        String full = path + "\\natif_natifUpd.so";
        System.load(full);
    }
}