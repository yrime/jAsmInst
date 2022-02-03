package reader;

//import modify.Added;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import transformer.Transformer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ReadClasses {
    private String classWay = "";
    public ReadClasses(String jarname, String classWay) {
        try {
            this.classWay = classWay;
            URL jarUrl = new URL("file:///"+jarname);
            URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl});
            JarFile jar = new JarFile(jarname);
            readClass(jar, loader);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void readClass(JarFile jar, URLClassLoader loader) {
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); )
        {
            JarEntry entry = entries.nextElement();
            String file = entry.getName();

            if (file.endsWith(".class"))
            {
                String classname =
                        file.replace('/', '.').substring(0, file.length() - 6);//.split("\\$")[0];
                try {
                    if(classname.indexOf(this.classWay) == -1 && this.classWay != ""){
                        continue;
                    }
                    Class<?> c = loader.loadClass(classname);
                    InputStream is = c.getClassLoader().getResourceAsStream(file);
                    byte[] out = Transformer.transform(is.readAllBytes());
                    File outFile = new File(String.format("ModifyClasses/%s", file));
                    Files.createDirectories(outFile.getParentFile().toPath());

                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        fos.write(out);
                    }
                    //writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
