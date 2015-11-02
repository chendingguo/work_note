package com.chinadaas.rsa;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.security.Key;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.chinadaas.command.tools.ClsSystem;
import com.chinadaas.util.ClientTool;

/**
 * RSA加密算法
 * 
 * 
 * 
 */
public class RSAEncryption {
	/**
	 * RSA加密算法
	 * */
	private static RSAEncryption rsaEncrypt = null;

	/**
	 * 指定加密算法为RSA
	 */
	private static final String ALGORITHM = "RSA";
	/**
	 * 指定公钥存放文件
	 * */
	private static String PUBLIC_KEY_FILE = "F:\\PublicKey.crt";
	/**
	 * 指定私钥存放文件
	 * */
	private static String PRIVATE_KEY_FILE = "F:\\PrivateKey.crt";

	/**
	 * 公钥
	 * */
	private static Key PublicKey;
	/**
	 * 私钥
	 * */
	private static Key PrivateKey;

	/**
	 * RSA加密算法
	 * */
	private RSAEncryption() {
		// 将文件中的公钥对象读出

		{
			try {

				PUBLIC_KEY_FILE=ClientTool.getProperty("public.key.path");
				// System.out.println("|----PUBLIC_KEY_FILE:"+PUBLIC_KEY_FILE);
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
				PublicKey = (Key) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				PRIVATE_KEY_FILE=ClientTool.getProperty("private.key.path");
				// System.out.println("|----PRIVATE_KEY_FILE:"+PRIVATE_KEY_FILE);
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
				PrivateKey = (Key) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param source
	 *            源数据
	 * @return 加密后数据
	 */
	public static String PrivateEncrypt(String source) {
		return initialize().doEncrypt(source, PrivateKey);
	}

	/**
	 * 初始化RSA加密算法
	 * */
	public static RSAEncryption initialize() {
		// if (rsaEncrypt == null) {
		// synchronized (RSAEncryption.class) {
		// if (rsaEncrypt == null) {
		rsaEncrypt = new RSAEncryption();
		// }
		// }
		// }
		return rsaEncrypt;
	}

	/**
	 * 公钥加密
	 * 
	 * @param source
	 *            源数据
	 * @return 加密后数据
	 */
	public static String PublicEncrypt(String source) {
		return initialize().doEncrypt(source, PublicKey);
	}

	/**
	 * 公钥解密
	 * 
	 * @param cryptograph
	 *            密文
	 * @return 解密后数据
	 */
	public static String PublicDecrypt(String cryptograph) {
		return initialize().doDecrypt(cryptograph, PublicKey);
	}

	/**
	 * 加密
	 * 
	 * @param source
	 *            源数据
	 * @param key
	 *            加密KEY
	 * @return 加密后数据
	 */
	private String doEncrypt(String source, Key key) {
		try {
			// 得到Cipher对象来实现对源数据的RSA加密
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 执行加密操作
			byte[] b1 = cipher.doFinal(source.getBytes("UTF-8"));
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(b1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密 针对IBM JDK 采用第三方的加解密 BouncyCastleProvider处理
	 * 
	 * @param cryptograph
	 *            密文
	 * @param key
	 *            解密KEY
	 * @return 解密后数据
	 */
	private String doDecrypt4IbmJdk(String cryptograph, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM,
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, key);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b1 = decoder.decodeBuffer(cryptograph);
			// 执行解密操作
			byte[] b = cipher.doFinal(b1);
			int j = 0;

			for (int i = b.length; i > 0; i--) {
				byte bt = b[i - 1];
				if (bt == -1) {
					j = i;
					break;
				}
			}
			String s = new String(b, "UTF-8");
			s = s.substring(j, s.length()).trim();
			System.out.println("服务器返回的随机密钥为:" + s);
			return s;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param cryptograph
	 *            密文
	 * @param key
	 *            解密KEY
	 * @return 解密后数据
	 */
	private String doDecrypt(String cryptograph, Key key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b1 = decoder.decodeBuffer(cryptograph);
			// 执行解密操作
			byte[] b = cipher.doFinal(b1);
			return new String(b, "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 私钥解密
	 * 
	 * @param cryptograph
	 *            密文
	 * @return 解密后数据
	 */
	public static String PrivateDecrypt(String cryptograph) {
		return initialize().doDecrypt(cryptograph, PrivateKey);
	}

	public static void main(String[] args) {

	}

}
