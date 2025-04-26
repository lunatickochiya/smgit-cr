package com.crack.smartgit;

import java.io.*;
import java.util.HashMap;

public final class VersionMap {
    public final static String ENTRY = "MAIN";
    public final static String METHOD = "METHOD";
    public final static String PARMTYPELIST = "PARMTYPELIST";

    public final static HashMap<String, String> V18_2_5 = new HashMap<String, String>();

    static {
        V18_2_5.put(ENTRY, "smartgit/alV");
        V18_2_5.put(METHOD, "a");
        V18_2_5.put(PARMTYPELIST, "ajA,MessageDigest");
    }

    public final static HashMap<String, String> V18_2_6 = new HashMap<String, String>();

    static {
        V18_2_6.put(ENTRY, "smartgit/alW");
        V18_2_6.put(METHOD, "a");
        V18_2_6.put(PARMTYPELIST, "ajB,MessageDigest");
    }

    public final static HashMap<String, String> V18_2_7 = new HashMap<String, String>();

    static {
        V18_2_7.put(ENTRY, "smartgit/alZ");
        V18_2_7.put(METHOD, "a");
        V18_2_7.put(PARMTYPELIST, "ajE,MessageDigest");
    }

    public final static HashMap<String, String> V19_1_1 = new HashMap<String, String>();

    static {
        V19_1_1.put(ENTRY, "smartgit/ajt");
        V19_1_1.put(METHOD, "a");
        V19_1_1.put(PARMTYPELIST, "ajr,MessageDigest");
    }

    public final static HashMap<String, String> V19_1_2 = new HashMap<String, String>();

    static {
        V19_1_2.put(ENTRY, "smartgit/ajs");
        V19_1_2.put(METHOD, "a");
        V19_1_2.put(PARMTYPELIST, "ajq,MessageDigest");
    }

    public final static HashMap<String, String> V21_2_4 = new HashMap<String, String>();

    static {
        V21_2_4.put(ENTRY, "smartgit/Ys");
        V21_2_4.put(METHOD, "a");
        V21_2_4.put(PARMTYPELIST, "VB,MessageDigest");
    }


    public final static HashMap<String, HashMap> Versions = new HashMap<String, HashMap>();

    static {
        Versions.put("18.2.5", V18_2_5);
        Versions.put("18.2.6", V18_2_6);
        Versions.put("18.2.7", V18_2_7);
        Versions.put("18.2.8", V18_2_7);//18.2.8并未更新smartgit.jar
        Versions.put("18.2.9", V18_2_7);//18.2.9并未更新smartgit.jar
        Versions.put("19.1.1", V19_1_1);
        Versions.put("19.1.2", V19_1_2);
        Versions.put("19.1.3", V19_1_2);//19.1.3中map无需改变
        Versions.put("21.2.4", V21_2_4);
    }

    public static String CurrentVersion = "18.2.5";

    public static HashMap GetTargetVersion(String version) {
        if (version != null && version.length() > 0 && Versions.containsKey(version))
            return Versions.get(version);
        return Versions.get(CurrentVersion);
    }

    /**
     * 功能描述: 通殺配置
     *
     * @param:
     * @return:
     * @author: Administrator
     * @date: 2019-08-25 15:33
     */
    public final static HashMap<String, String> ALL_KILL = new HashMap<String, String>();

    static {
        ALL_KILL.put(ENTRY, "smartgit/");
        ALL_KILL.put(METHOD, "java/lang/Math");
        ALL_KILL.put(PARMTYPELIST, "Ljava/security/MessageDigest");
    }
}
