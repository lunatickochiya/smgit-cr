package com.crack.smartgit;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;

public class CrackAgent {
    public static void premain(final String args, final Instrumentation inst) {
        inst.addTransformer(new MethodEntryTransformer(args));
    }

    private static class MethodEntryTransformer implements ClassFileTransformer {
        private String selectVersion;

        public MethodEntryTransformer(String selectVersion) {
            this.selectVersion = selectVersion;
        }

        @Override
        public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
            try {
                HashMap<String, String> target = VersionMap.GetTargetVersion(this.selectVersion);
                String entry = target.get(VersionMap.ENTRY);
                String method = target.get(VersionMap.METHOD);
                String parmTypeList = target.get(VersionMap.PARMTYPELIST);
                LogHelper.Debug("Version=" + VersionMap.CurrentVersion + ",Entry=" + entry + ",Method=" + method + ",ParmTypeList=" + parmTypeList);
                if (className != null && className.startsWith(entry)) {
                    final ClassReader classReader = new ClassReader(classfileBuffer);
                    final ClassNode classNode = new ClassNode();
                    classReader.accept(classNode, 0);
                    final List<MethodNode> methodNodes = classNode.methods;
                    for (final MethodNode methodNode : methodNodes) {
                        if (method.equals(methodNode.name)) {
                            final Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);
                            final Type returnType = Type.getReturnType(methodNode.desc);
                            if (argumentTypes.length != 2) {
                                continue;
                            }
                            if (returnType != Type.VOID_TYPE) {
                                continue;
                            }
                            String argumentClassNames = parmTypeList;
                            for (int i = 0; i < argumentTypes.length; ++i) {
                                final String tmpArgumentClassName = argumentTypes[i].getClassName();
                                final int classIndex = argumentClassNames.indexOf(tmpArgumentClassName);
                                if (-1 != classIndex) {
                                    if (0 == classIndex) {
                                        argumentClassNames = argumentClassNames.substring(classIndex);
                                    } else {
                                        argumentClassNames = argumentClassNames.substring(0, classIndex);
                                    }
                                }
                            }
                            final InsnList insnList = methodNode.instructions;
                            insnList.clear();
                            insnList.add(new InsnNode(Opcodes.RETURN));
                            methodNode.exceptions.clear();
                            methodNode.visitEnd();
                            final ClassWriter cw = new ClassWriter(0);
                            classNode.accept(cw);
                            LogHelper.Debug("patched，" + className + " -> " + methodNode.name + " -> " + methodNode.desc);
                            return cw.toByteArray();
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                LogHelper.Error(e.getMessage());
            }
            return classfileBuffer;
        }
    }
}
