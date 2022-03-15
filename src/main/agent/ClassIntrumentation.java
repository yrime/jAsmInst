package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassIntrumentation extends ClassVisitor {
    static int i = 0;
    public ClassIntrumentation(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("instrumented class: " + name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if(name.equals("__afl_maybe_log") == false){
          //  System.out.println("instrumented method: " + name);
            methodVisitor = new MethodTransformer(methodVisitor, access, name, descriptor, signature, exceptions);
        }
        return methodVisitor;
    }

    @Override
    public void visitEnd() {
       // System.out.println("class was instrumented");
        super.visitEnd();
    }

    private class MethodTransformer extends MethodVisitor{
        String name;
        public MethodTransformer(MethodVisitor methodVisitor,
                                 int access, String name,
                                 String descriptor, String signature,
                                 String[] exceptions) {
            super(ASM9, methodVisitor);
            this.name = name;
        }

        private void instr(String test){
            //super.visitIntInsn(Opcodes.BIPUSH, 9999);
            // super.visitLdcInsn(new Integer);
            //super.visitIntInsn(Opcodes.BIPUSH);

            // super.visitVarInsn(Opcodes.ILOAD, 1);
            int hash = Integer.hashCode(name.hashCode() + i);
            i += 2 + 2*i;
//            int a = 1;
            super.visitLdcInsn(hash);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "natif/upd", "__afl_maybe_log", "(I)V", false);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            instr("visitCode");
        }

        @Override
        public void visitInsn(int opcode) {
            switch (opcode){
                case Opcodes.IRETURN:
                case Opcodes.LRETURN:
                case Opcodes.FRETURN:
                case Opcodes.DRETURN:
                case Opcodes.ARETURN:
                case Opcodes.RETURN:
                    instr("visitInsn");
            }
            super.visitInsn(opcode);
        }
    /*
        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }
    */
/*
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            instr("visitMethodInsn");
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
*/
        @Override
        public void visitJumpInsn(int opcode, Label label) {
            super.visitJumpInsn(opcode, label);
            instr("visitJumpInsn");
        }

  /*      @Override
        public void visitLabel(Label label) {
            super.visitLabel(label);
            instr("visitLabel");
        }
*/
        @Override
        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
            instr("visitTableSwitchInsn");
            super.visitTableSwitchInsn(min, max, dflt, labels);
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            instr("visitTryCatchBlock");
            super.visitTryCatchBlock(start, end, handler, type);
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels){
            super.visitLookupSwitchInsn(dflt, keys, labels);
            instr("visitLookupSwitchInsn");
        }
    }
}
