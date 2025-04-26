package com.crack.smartgit;

import org.objectweb.asm.ClassReader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Usage {
    public static void main(final String[] args) {

        String version = System.getProperty("java.version");
        System.out.println("java.version=" + version);

        System.out.println(" This is a fun demo !");

//        LogHelper.Initialize();
//        LogHelper.Debug("Hello, World!");

        try {
            //jdk8 中的 ClassReader 最大支持classVersion 57(JDK 13)
            //smartgit23 的classVersion=61(JDK 17)

            byte[] classfileBuffer = Files.readAllBytes(Paths.get("C:/smartgit_dumped_smartgit_Xs.class"));
            ClassReader classReader = new ClassReader(classfileBuffer);
            System.out.println("className=" + classReader.getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
