package com.crack.smartgit.allkill;

import com.crack.smartgit.LogHelper;
import com.crack.smartgit.allkill.transformer.SmartGitTransformer;

import java.security.MessageDigest;

import java.lang.instrument.Instrumentation;

/**
 * @author Administrator
 * @descript
 * @date 2019-08-25
 */
public class Agent {
    public static void premain(final String args, final Instrumentation inst) {
        LogHelper.Initialize();
        inst.addTransformer(new SmartGitTransformer());
    }
}
