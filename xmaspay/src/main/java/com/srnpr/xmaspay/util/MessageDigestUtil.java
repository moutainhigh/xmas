package com.srnpr.xmaspay.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * Ideally, we can use commons-codec for the following task, but since glassfish
 * has its own repacked 1.2 commons-codec, but I want the SHA512 in
 * commons-codec-1.4.jar and I did not found the way to workaround.
 * 
 */
public class MessageDigestUtil extends BaseClass {

	private static final String HMAC_SHA512_ALGORITHM = "HmacSHA512";

	/**
	 * MD5 hash function to be "cryptographically secure", the result is 32 HEX
	 * characters.
	 * <ol>
	 * <li>Given a hash, it is computationally infeasible to find an input that
	 * produces that hash</li>
	 * <li>Given an input, it is computationally infeasible to find another
	 * input that produces the same hash</li>
	 * </ol>
	 * 
	 * @param plainText
	 * @return the MD5 of the input text
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(byte[] plainText) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		String md5 = new BigInteger(1, messageDigest.digest(plainText))
				.toString(16);
		if (md5.length() < 32) {
			md5 = "0" + md5;
		}

		messageDigest.reset();

		return md5;
	}
	
	public static long md5(String plainText) throws Exception {
		long result = System.currentTimeMillis();
		try {
//			plainText += result;
			byte[] plain = plainText.getBytes("UTF-8");
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			BigInteger md5 = new BigInteger(1, messageDigest.digest(plain));			
			result = md5.longValue();
		} catch (Throwable e) {
			throw new Exception(e);
		}
		return result;
	}

	/**
	 * MD5 hash function to be "cryptographically secure", the result is 32 HEX
	 * characters.
	 * <ol>
	 * <li>Given a hash, it is computationally infeasible to find an input that
	 * produces that hash</li>
	 * <li>Given an input, it is computationally infeasible to find another
	 * input that produces the same hash</li>
	 * </ol>
	 * 
	 * @param plainText
	 * @param salt
	 * @return the MD5 of the input text
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(byte[] plainText, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(salt);
		String md5 = new BigInteger(1, messageDigest.digest(plainText))
				.toString(16);
		if (md5.length() < 32) {
			md5 = "0" + md5;
		}

		messageDigest.reset();

		return md5;
	}

	/**
	 * SHA-512 hash function to be "cryptographically secure", the result is 128
	 * HEX characters
	 * <ol>
	 * <li>Given a hash, it is computationally infeasible to find an input that
	 * produces that hash</li>
	 * <li>Given an input, it is computationally infeasible to find another
	 * input that produces the same hash</li>
	 * </ol>
	 * 
	 * @param plainText
	 * @param salt
	 * @return the SHA-512 of the input text
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sha512(byte[] plainText)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
		String sha512 = new BigInteger(1, messageDigest.digest(plainText))
				.toString(16);
		if (sha512.length() < 128) {
			sha512 = "0" + sha512;
		}

		messageDigest.reset();


		return sha512;
	}

	/**
	 * Computes RFC 2104-compliant HMAC signature.
	 * 
	 * @param data
	 *            The data to be signed
	 * @param secretKey
	 *            the secretKey for signature
	 * @return The base64-encoded RFC 2104-compliant HmacSHA512 signature.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String rfc2104HmacSha512(String data, String secretKey) {
		String result;
		try {
			// get an hmac_sha512 key from the raw key bytes
			SecretKeySpec signingKeySpec = new SecretKeySpec(
					secretKey.getBytes(Charset.forName("UTF-8")),
					HMAC_SHA512_ALGORITHM);

			// get an hmac_sha512 Mac instance and initialize with the signing
			// key
			Mac mac = Mac.getInstance(HMAC_SHA512_ALGORITHM);
			mac.init(signingKeySpec);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes(Charset
					.forName("UTF-8")));

			// base64-encode the hmac
			result = new String(Base64.encodeBase64(rawHmac),
					Charset.forName("UTF-8"));

			
		} catch (Exception e) {
			throw new IllegalStateException("Calculate HmacSHA512 failed: "
					+ e.getMessage());
		}

		return result;
	}

	public static String calculateSignature(String httpMethod, String date,
			String resourcePath, String secretKey) {
		// calculate the signature
		// StringToSign = HTTP-Method + "\n" + Date + "\n" + resourcePath +
		// "\n";
		StringBuilder stringToSign = new StringBuilder();
		stringToSign.append(httpMethod).append("\n");
		stringToSign.append(date).append("\n");
		stringToSign.append(resourcePath).append("\n");
		
		String calculatedSignature = rfc2104HmacSha512(stringToSign.toString(),
				secretKey);
		return calculatedSignature;
	}

	/**
	 * 针对密码进行MD5加密,其中salt为掩码.一般为用户名<br />
	 * An MD5 hash help function to be "cryptographically secure", the result is
	 * 32 HEX characters.
	 * <p>
	 * It is purpose to be compatible with {@literal Spring's}
	 * {@link MessageDigestPasswordEncoder#encodePassword(java.lang.String, java.lang.Object)}
	 * , which merges the password with salt as: <br />
	 * {@code (password + " " + salt.toString() + "}" }
	 * 
	 * <ol>
	 * <li>Given a hash, it is computationally infeasible to find an input that
	 * produces that hash</li>
	 * <li>Given an input, it is computationally infeasible to find another
	 * input that produces the same hash</li>
	 * </ol>
	 * 
	 * @param plainText
	 * @param salt
	 * @return the MD5 of the input text
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String Md5Encode(String password, Object salt) {
		return springMd5PasswordEncode(password, salt, true,true);
	}
	
	public static String Md5EncodeUpper(String password, Object salt) {
		return springMd5PasswordEncode(password, salt, true,false);
	}

	public static String springMd5PasswordEncode(String password, Object salt,
			boolean strict,boolean toLowerCase) {
		String saltedPass = mergePasswordAndSalt(password, salt, false);
		MessageDigest messageDigest = getMessageDigest();
		byte[] digest;
		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
		return new String(Hex.encodeHex(digest, toLowerCase));
	}

	private static final MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	private static String mergePasswordAndSalt(String password, Object salt,
			boolean strict) {
		if (password == null) {
			password = "";
		}

		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1)
					|| (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException(
						"Cannot use { or } in salt.toString()");
			}
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}

	/**
	 * calculate the md5 of the file as linux command 'md5sum file'.
	 * 对文件进行MD5返回文件或文件夹的MD5值
	 * 
	 * @param file
	 *            the while path to the file
	 * @return
	 * @throws Exception 
	 */
	public static String md5File(String file) throws Exception {
		MessageDigest md = null;
		InputStream fis = null;
		StringBuilder hexString = null;

		try {
			md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}

			byte[] mdbytes = md.digest();
			// convert the byte to hex format
			hexString = new StringBuilder();
			for (int i = 0; i < mdbytes.length; i++) {
				String hex = Integer.toHexString(0xff & mdbytes[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

		} catch (Exception ex) {
			throw new Exception("md5File: error to close inputstream of file: {}");
		} finally {
			try {
				fis.close();
			} catch (Exception ex) {
				throw new Exception("md5File: error to close inputstream of file: {}");
			}
		}

		return hexString.toString();
	}
	
	/**
	 * 计算RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @param algorithms
	 * 		签名算法
	 * @return 签名值
	 */
	public static String calRsaSign(String content, String privateKey, String input_charset, String algorithms) throws Exception {

		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));

		KeyFactory keyf = KeyFactory.getInstance("RSA");

		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		java.security.Signature signature = java.security.Signature.getInstance(algorithms);

		signature.initSign(priKey);
		signature.update(content.getBytes(input_charset));

		byte[] signed = signature.sign();

		return Base64.encodeBase64String(signed);

	}
}
