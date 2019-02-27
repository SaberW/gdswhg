package com.force.librarybase.utils.rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;

public class RSAEncrypt {

    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBQb1rHLX5RxnM87ZNZvTF5680"
            + "HiU8RrlRmUV28SN1NTZonsfj9eq9IUVVOghoroCTzJ74o6tv36XrtV33tPnVMI+O"
            + "cJQ+XOfdfldORru6rEvzVkI7ncLHTwngDnOflqVgLIn1vx9gAzEu4PnNHc3CyuMA"
            + "ALGTfCUWLscbdDTq1wIDAQAB";

    public static final String DEFAULT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMFBvWsctflHGczz"
            + "tk1m9MXnrzQeJTxGuVGZRXbxI3U1Nmiex+P16r0hRVU6CGiugJPMnvijq2/fpeu1"
            + "Xfe0+dUwj45wlD5c591+V05Gu7qsS/NWQjudwsdPCeAOc5+WpWAsifW/H2ADMS7g"
            + "+c0dzcLK4wAAsZN8JRYuxxt0NOrXAgMBAAECgYAzKjuDC0PmbnuXNoLydSqi5tcM"
            + "/lovXi4cO28XsXoIwwDXrAoSAX6XeiKIEOg/BsxF1SvVekeeQ+ECya7KMSDfabKA"
            + "zNraL3WIPvv0d0hYzovaF4g6J4LG741GvEILPLPiU/toK5pRyD+rONd+ED8e9heo"
            + "iLVESX9s+4enk32hKQJBAOb283Mww5Tn/g6Dduwa5us7TvW0/105BBvfQG9ZiBC2"
            + "Ss7Yhd50wtCm57wmR4in0MsOQwdTUw3Uzj68bkKZfVMCQQDWNG6PRLduXzf/pf5J"
            + "bXdsixPl/6ij6JxRVfAGIjqY/pq7KjKlxnbbjroKvrwLT9RTVKIOmjZ5rbZURbTd"
            + "IuftAkAUnOujVzHNY1sYPoFplwkO628jnLDByCnmtukQRwROwYYxqb9g2Dje2FaM"
            + "QzpVJu0Kgk5XJAegj2KM1NJeXUaDAkAu1Je3StavkrGzep71frE1UdKv3g0IiLpk"
            + "Ld2j+afF+yPo93jLNXCW38VcxAZokrNg5IfpmV1/jLLoCbVZA2ChAkBTRVTw5bIT"
            + "Noe7Elugtir1nmJL4q1aAeQtRlEk1EH03GVBT/nVxM/rUmbc0m81sApjVUbSue+c"
            + "TmR0rOxccUFY";

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    /**
     * 字 数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     *  取私钥
     *
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     *  取公钥
     *
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     * <p>
     * 私钥文件名
     *
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory
                    .generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // cipher = Cipher.getInstance("RSA"); // Java
            cipher = Cipher.getInstance("RSA/None/PKCS1Padding"); // Android
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // cipher = Cipher.getInstance("RSA"); // Java
            cipher = Cipher.getInstance("RSA/None/PKCS1Padding"); // Android
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 字 数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    /**
     * public static void main(String[] args) { RSAEncrypt rsaEncrypt = new
     * RSAEncrypt();
     *
     * // System.out.println("PKEY:"+RSAEncrypt.DEFAULT_PUBLIC_KEY); //
     * System.out.println("SKEY:"+RSAEncrypt.DEFAULT_PRIVATE_KEY);
     *
     * String username = "18569065795"; String password = "caodoubi";
     *
     * try { rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
     * rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
     *
     * byte[] en_uname = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(),
     * username.getBytes("utf-8")); byte[] en_upass =
     * rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(),
     * password.getBytes("utf-8"));
     *
     * String b_uname = new Base64Encoder().encode(en_uname); String b_upass =
     * new Base64Encoder().encode(en_upass);
     *
     * System.out.println("1 b_uname:" + b_uname);
     * System.out.println("1 b_upass:" + b_upass);
     *
     * byte[] de_uname = new BASE64Decoder().decodeBuffer(b_uname); byte[]
     * de_upass = new BASE64Decoder().decodeBuffer(b_upass);
     *
     * byte[] re_uname = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(),
     * de_uname); byte[] re_upass =
     * rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), de_upass);
     *
     * System.out.println("2 re_uname:" + new String(re_uname));
     * System.out.println("2 re_upass:" + new String(re_upass)); } catch
     * (Exception e) { System.err.println(e.getMessage()); } }
     **/

}
