package etceterum.libra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import biz.source_code.base64Coder.Base64Coder;

// http://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
public final class Serializations {
    private Serializations() {
        // don't
    }
    
    public static Object deserialize(String string) throws IOException, ClassNotFoundException {
        if (null == string) {
            return null;
        }
        final ByteArrayInputStream bis = new ByteArrayInputStream(Base64Coder.decode(string));
        final ObjectInputStream ois = new ObjectInputStream(bis);
        Object object = ois.readObject();
        ois.close();
        return object;
    }
    
    // returns null on failure
    public static Object tryDeserialize(String string) {
        try {
            return deserialize(string);
        }
        catch (Exception e) {
            // no-op
        }
        return null;
    }
    
    public static String serialize(Serializable object) throws IOException {
        if (null == object) {
            return null;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.close();
        return new String(Base64Coder.encode(bos.toByteArray()));
    }
    
    // returns null on failure
    public static String trySerialize(Serializable object) {
        try {
            return serialize(object);
        }
        catch (Exception e) {
            // no-op
        }
        return null;
    }
}
