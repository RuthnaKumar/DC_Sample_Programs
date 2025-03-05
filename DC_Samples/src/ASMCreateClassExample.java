import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class ASMCreateClassExample {
    public static void main(String[] args) throws Exception {
        // Create a ClassWriter
        ClassWriter classWriter = new ClassWriter(0);

        // Define the class header
        classWriter.visit(V1_8, ACC_PUBLIC, "com/example/MyClass", null, "java/lang/Object", null);

        // Define the default constructor
        MethodVisitor constructor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(ALOAD, 0);
        constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

        // Define the main method
        MethodVisitor main = classWriter.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        main.visitCode();
        main.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        main.visitLdcInsn("Hello ASM!");
        main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        main.visitInsn(RETURN);
        main.visitMaxs(2, 1);
        main.visitEnd();

        // End the class
        classWriter.visitEnd();

        // Get the byte array of the generated class
        byte[] bytecode = classWriter.toByteArray();

        // Save the class to a file (optional)
        java.nio.file.Files.write(java.nio.file.Paths.get("./MyClass.class"), bytecode);
    }
}
