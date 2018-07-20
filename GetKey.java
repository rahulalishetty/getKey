
import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

public class GetKey {
    public static void printFile(File file) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line=null;
        while((line=br.readLine())!=null){
            System.out.println(line);
        }
    }
    public static void checkAndDelete(File file){
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
    public static void processFile(int MODE,SecretKey secretKey,File inputFile, File outputFile){
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(MODE,secretKey);
            FileInputStream inputStream=new FileInputStream(inputFile);
            byte[] inputbytes=new byte[(int) inputFile.length()];
            inputStream.read(inputbytes);
            byte[] outputbytes=cipher.doFinal(inputbytes);
            FileOutputStream outputStream=new FileOutputStream(outputFile);
            outputStream.write(outputbytes);
            printFile(outputFile);
            inputStream.close();
            outputStream.close();
        }catch (NoSuchAlgorithmException|NoSuchPaddingException
                |InvalidKeyException|BadPaddingException|
                IllegalBlockSizeException|IOException e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws Exception{
        KeyStore ks=KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(new File("/home/zemoso/keystore.ks")),"keystorepassword".toCharArray());
        SecretKey secretKey=(SecretKey) ks.getKey("SecretKeyAlias","entrypassword".toCharArray());
        System.out.println(secretKey);
        //Cipher cipher=Cipher.getInstance("DES");
        File file=new File("/home/zemoso/big_data/word_count.txt");
        File encrypted=new File("/home/zemoso/inf_sec/message.encryted");
        checkAndDelete(encrypted);
        File decryted=new File("/home/zemoso/inf_sec/dec.txt");
        checkAndDelete(decryted);
        printFile(file);
        try{
            processFile(Cipher.ENCRYPT_MODE,secretKey,file, encrypted);
            processFile(Cipher.DECRYPT_MODE,secretKey,encrypted,decryted);
            System.out.println("encryption and decryption complete");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
