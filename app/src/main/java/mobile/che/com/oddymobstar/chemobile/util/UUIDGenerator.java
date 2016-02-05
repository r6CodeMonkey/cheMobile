package mobile.che.com.oddymobstar.chemobile.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by root on 25/02/15.
 */
public class UUIDGenerator {

    private String algorithm; //set via config files from DB.  need to set it default then update if server changes...

    public UUIDGenerator(String algorithm) {
        this.algorithm = algorithm;
    }

    public String generateAcknowledgeKey() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm).digest((UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes()).toString();
    }

    public String generateBluetoothUUID() throws NoSuchAlgorithmException {
        return (MessageDigest.getInstance(algorithm).digest("bluetooth connection client basically needs its own UUID so this will be ".getBytes())).toString();
    }

    public static String generateKey() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5").digest((UUID.randomUUID().toString() + System.currentTimeMillis()).toString().getBytes()).toString();
    }


}
