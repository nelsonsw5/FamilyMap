package Encoder;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class ObjectEncoder {

    public static String Serialize (Object obj) {
        return new Gson().toJson(obj);
    }

    public static <T> T deserialize (String s, Type returnType) {
        return new Gson().fromJson(s, returnType);
    }
}