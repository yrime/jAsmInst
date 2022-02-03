import reader.ReadClasses;

import java.util.logging.Logger;

public class jAsmInst {
    public static Logger log;
    public static void main(String[] args){
        log = Logger.getLogger("jAsmLog");
        if(args.length == 1){
            ReadClasses rc = new ReadClasses(args[0], "");
        }else{
            ReadClasses rc = new ReadClasses(args[0], args[1]);
        }
    }
}
