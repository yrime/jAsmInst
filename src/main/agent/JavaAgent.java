package agent;

import java.lang.instrument.Instrumentation;

public class JavaAgent {
    static int count = 0;


    public static void premain(String arg, Instrumentation inst) {
        System.out.println("Hello! I`m java agent");
        int opt = 0;
            JavaTransformer jTransformer = new JavaTransformer();
            inst.addTransformer(jTransformer);

    }
}

