package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
//import ClassIntrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class JavaTransformer implements ClassFileTransformer {
    int count = 0;
	private String[] packages;
	public JavaTransformer(String args){
	//	System.out.println(args);
		packages = args.split(";");
	//	System.out.println(packages);
	}
	
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        String cname = className.replaceAll("/", ".");
            //Class<?> c = loader.loadClass(cname);
        //System.out.println("load class: " + cname + " class name " );
		for(String name: packages){
			//System.out.println(name + " " + cname);
			if(cname.indexOf(name) != -1){
				ClassReader classReader = new ClassReader(classfileBuffer);
				ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
				ClassVisitor classVisitor = new ClassIntrumentation(classWriter);
				classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
				classfileBuffer = classWriter.toByteArray();
			}
		}
        return classfileBuffer;
    }
}
