package com.tiptap.speedlauncher;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;

public class Letters {

    private static final BiMap<Integer, String> letterMap = HashBiMap.create();

    static {
        letterMap.put(R.id.letterA, "A");
        letterMap.put(R.id.letterB, "B");
        letterMap.put(R.id.letterC, "C");
        letterMap.put(R.id.letterD, "D");
        letterMap.put(R.id.letterE, "E");
        letterMap.put(R.id.letterF, "F");
        letterMap.put(R.id.letterH, "H");
        letterMap.put(R.id.letterI, "I");
        letterMap.put(R.id.letterJ, "J");
        letterMap.put(R.id.letterK, "K");
        letterMap.put(R.id.letterL, "L");
        letterMap.put(R.id.letterM, "M");
        letterMap.put(R.id.letterN, "N");
        letterMap.put(R.id.letterO, "O");
        letterMap.put(R.id.letterP, "P");
        letterMap.put(R.id.letterQ, "Q");
        letterMap.put(R.id.letterR, "R");
        letterMap.put(R.id.letterS, "S");
        letterMap.put(R.id.letterT, "T");
        letterMap.put(R.id.letterU, "U");
        letterMap.put(R.id.letterV, "V");
        letterMap.put(R.id.letterW, "W");
        letterMap.put(R.id.letterX, "X");
        letterMap.put(R.id.letterY, "Y");
        letterMap.put(R.id.letterZ, "Z");
    }

    public static int getIdForLetter(String letter) {
        return letterMap.inverse().get(letter);
    }

    public static String getLetterForId(Integer id) {
        return letterMap.get(id);
    }

    public static Iterable<Map.Entry<Integer, String>> getIterable() {
        return () -> letterMap.entrySet().iterator();
    }
}
