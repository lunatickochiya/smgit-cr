package com.crack.smartgit.allkill.transformer;

import com.crack.smartgit.LogHelper;
import com.crack.smartgit.VersionMap;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * 目标：通杀
 *
 * @author Administrator
 * @descript
 * @date 2019-08-25
 */
public class SmartGitTransformer implements ClassFileTransformer {

    public SmartGitTransformer() {
        //LogHelper.Debug("agent-SmartGitTransformer");
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        String clearClassName = className.trim();
        //LogHelper.Debug("ClassLoader=> className=" + className + ",clearClassName=" + clearClassName);

        //smartgit23中的核心类:smartgit/Xs
//        if (clearClassName.equals("smartgit/Xs")) {
//            TryWriteClassToFile(clearClassName, classfileBuffer);
//        }

        if (!"".equals(clearClassName)) {
            try {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                List<MethodNode> methodNodes = classNode.methods;

                //LogHelper.Debug("smartgit=> classNode.name=" + classNode.name + ",methodNodes.size=" + methodNodes.size());

                boolean isEntry = classNode.name.contains(VersionMap.ALL_KILL.get(VersionMap.ENTRY));
                if (isEntry) {
                    //LogHelper.Debug("smartgit=> classNode.name=" + classNode.name + ",methodNodes.size=" + methodNodes.size());
                    for (final MethodNode methodNode : methodNodes) {
                        Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);
                        Type returnType = Type.getReturnType(methodNode.desc);

                        String argumentTypeString = "";
                        for (final Type argumentType : argumentTypes) {
                            argumentTypeString += (argumentType.getClassName() + ",");
                        }
                        if (Type.VOID_TYPE != returnType) {
                            continue;
                        }
                        //自22.1版本后,patch函数参数从2个变更为3个
                        if (argumentTypes.length < 2 || argumentTypes.length > 3) {
                            continue;
                        }
                        if (!methodNode.desc.contains(VersionMap.ALL_KILL.get(VersionMap.PARMTYPELIST))) {
                            continue;
                        }

                        if (!argumentTypes[0].getClassName().equals("java.lang.String") && argumentTypes[1].getClassName().equals("java.security.MessageDigest")) {
                            //LogHelper.Debug("找到patch函数01=" + classNode.name + "." + methodNode.name + "(" + argumentTypeString + ")" + ",desc=" + methodNode.desc);

                            //清空函数体然后直接添加return指令
                            InsnList insnList = methodNode.instructions;
                            insnList.clear();
                            insnList.add(new InsnNode(Opcodes.RETURN));
                            methodNode.exceptions.clear();
                            methodNode.visitEnd();

                            //LogHelper.Debug("patch函数完成" + classNode.name + "." + methodNode.name + "(" + argumentTypeString + ")" + ",signature=" + methodNode.signature);
                            ClassWriter cw = new ClassWriter(0);
                            classNode.accept(cw);
                            byte[] classCode = cw.toByteArray();

                            //DumpClassToFile(classNode.name+"_patched", cw);

                            return classCode;
                        }
                    }
                } else {
                    //LogHelper.Error("跳过Class(" + VersionMap.ALL_KILL.get(VersionMap.ENTRY) + ")不匹配的节点,ClassNode.name=" + classNode.name);
                }
            } catch (Exception e) {
                LogHelper.Error("transform异常,className=" + className + ",错误=" + e.getMessage() + ",堆栈=" + e.getStackTrace());
            }
        }
        return classfileBuffer;
    }

    public static void DumpClassToFile(String className, ClassWriter classWriter) {
        try {
            byte[] classCode = classWriter.toByteArray();
            WriteClassToFile(className, classCode);
        } catch (Exception e) {
            LogHelper.Error("dump class失败,错误=" + e.getMessage());
        }
    }

    public static void WriteClassToFile(String className, byte[] classPayload) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("C:\\smartgit_dumped_" + className.replaceAll("/", "_") + ".class"));
        fos.write(classPayload, 0, classPayload.length);
        fos.close();
    }

    public static void TryWriteClassToFile(String className, byte[] classPayload) {
        try {
            WriteClassToFile(className, classPayload);
        } catch (Exception e) {
            LogHelper.Error("write class失败,错误=" + e.getMessage());
        }
    }
}
