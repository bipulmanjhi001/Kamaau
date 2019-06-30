package com.larvaesoft.kamaau.passwordgenerator;

import java.util.Random;

/**
 * Created by LSSIN005 on 05-04-2018.
 */

public class RandomPasswordGenerator {
    private static final String ALPHA_CAPS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUM     = "0123456789";
    private static final String SPL_NUMS   = "0123456789";

    public static char[] generatePswd(int minLen, int maxLen, int noOfDigits,int noOfCAPSAlpha,
                                       int noOfSplChars,int noOfDigits2) {
        if(minLen > maxLen)
            throw new IllegalArgumentException("Min. Length > Max. Length!");
        if( (noOfDigits + noOfCAPSAlpha +  noOfSplChars + noOfDigits2) > minLen )
            throw new IllegalArgumentException
                    ("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
        Random rnd = new Random();
        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;
        for (int i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for (int i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }
        for (int i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = SPL_NUMS.charAt(rnd.nextInt(SPL_NUMS.length()));
        }
        for (int i = 0; i < noOfDigits2; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for(int i = 0; i < len; i++) {
            if(pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        return pswd;
    }

    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        while(pswd[index = rnd.nextInt(len)] != 0);
        return index;
    }
}