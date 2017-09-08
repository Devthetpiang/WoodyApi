package com.xavey.woody.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinmaungaye on 9/9/15.
 */
public class ShakeBreak {
    static String IS_MYANMAR_RANGE = "[က-အ]+|[\u1025-\u1027]";
    static String IS_UNICODE_MY = "[ဃငစဆဇဈဉညတဋဌဍဎဏဒဓနဘရဝဟဠအ]်|ျ[က-အ]ါ|ျ[ါ-း]|\u103e|\u103f|\u1031[^\u1000-\u1021\u103b\u106a\u106b\u107e-\u1084\u108f\u1090]|\u1031$|\u100b\u1039|\u1031[က-အ]\u1032|\u1025\u102f|\u103c\u103d";
    static String IS_ZAWGYI = "[\u1050-\u109f]|\u0020[\u103b\u107e-\u1084]|\u0020\u1031|^\u1031|^\103b|\u1038\u103b|\u1038\u1031|\u1033|\u1034|[\u102d\u102e\u1032]\u103b|\u1039[^\1000-1021]|\u1039$|\u108c";
    static Pattern reMyanmar = Pattern.compile(IS_MYANMAR_RANGE);
    static Pattern reUnicode_my =  Pattern.compile(IS_UNICODE_MY);
    static Pattern reZawgyi =  Pattern.compile(IS_ZAWGYI);

    public static boolean is_myanmar (String input) {
        Matcher matcher = reMyanmar.matcher(input);
        return matcher.matches();
    }

    public static boolean is_unicode_my(String input) {
        Matcher matcher = reUnicode_my.matcher(input);
        return matcher.matches();
    }

    public static boolean is_zawgyi(String input) {
        Matcher matcher = reZawgyi.matcher(input);
        return matcher.matches();
    }
}
