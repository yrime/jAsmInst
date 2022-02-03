package natif;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class upd {

    public static void __afl_maybe_log(int i){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("javalog.txt", true));

            writer.write("null ");

        //    System.out.println("natif " + i + " ");
            //  bb.put(i, (byte) 1);
            writer.write("i: " + i + "\n");
            // writer.write(bb.array().toString());
            writer.close();
            natifUpd.mapUp(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}