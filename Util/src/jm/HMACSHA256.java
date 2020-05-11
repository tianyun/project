package jm;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA256 {
	public static void main(String[] args) {
		byte[] data = new byte[] { (byte) 0x92, 0x40, 0x07, (byte) 0x99, (byte) 0xde, 0x5d, (byte) 0xdb, 0x54, (byte) 0xe4, 0x2a, 0x47,(byte)  0xe8,
				0x17, 0x7d, 0x06, 0x1f, (byte) 0x90, (byte) 0xd7, 0x7e, 0x7b, 0x27, 0x6a, 0x30, (byte) 0x8c, (byte) 0xda, (byte) 0xa3, (byte) 0xb2, 0x24, 0x2f,
				(byte) 0xfd, (byte) 0xfe, (byte) 0x8b, 0x15, 0x24, (byte) 0x87, (byte) 0xd1, (byte) 0xd2, 0x3b, 0x39, (byte) 0xaf, (byte) 0xd9, (byte) 0xa2, 0x7f, 0x52, 0x29, (byte) 0xe7,
				(byte) 0xe4, 0x5f, 0x7c, (byte) 0x90, (byte) 0xd3, (byte) 0xed, 0x78, (byte) 0x90, (byte) 0x9b, (byte) 0xbe, (byte) 0xd5, (byte) 0xf8, 0x23,(byte)  0x8c, (byte) 0xee,(byte)  0x95, 0x34,
				(byte) 0xa0, 0x76, (byte) 0x8b, (byte) 0x93, 0x14, 0x06, 0x12, 0x07, 0x00, 0x14, 0x03, 0x18, 0x01, 0x02, 0x15, 0x00, 0x16,
				0x05, 0x1b, 0x01, 0x01, 0x1c, 0x00, 0x17, 0x00 };
		byte[] SECURE_SIGN_ON_CODE = new byte[] { 0x30, 0x59, 0x30, 0x13, 0x06, 0x07, 0x2A, (byte) 0x86, 0x48,
				(byte) 0xCE, 0x3D, 0x02, 0x01, 0x06, 0x08, 0x2A, };
		
		byte[] result = computeHmacWithSha256(SECURE_SIGN_ON_CODE, ByteBuffer.wrap(data));
		
		
		
		for (int i = 0; i < result.length; i++) {
			if(i>1&&i%10==0) {
				System.out.println();
			}
			System.out.print(" "+result[i]);	
		}
		
		System.out.println();
		System.out.println("================================");

		System.out.println(byteArrayToHexString(result));
	}

	
	
	/**
	 * 来自于爱德华的hashsha256加密
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static byte[] computeHmacWithSha256(byte[] key, ByteBuffer data) {
		final String algorithm = "HmacSHA256";
		Mac mac;
		try {
			mac = Mac.getInstance(algorithm);
		} catch (NoSuchAlgorithmException ex) {
			// Don't expect this to happen.
			throw new Error("computeHmac: " + algorithm + " is not supported: " + ex.getMessage());
		}

		try {
			mac.init(new SecretKeySpec(key, algorithm));
		} catch (InvalidKeyException ex) {
			// Don't expect this to happen.
			throw new Error("computeHmac: Can't init " + algorithm + " with key: " + ex.getMessage());
		}
		int savePosition = data.position();
		mac.update(data);
		data.position(savePosition);
		return mac.doFinal();
	}

	/**
	 * 将加密后的字节数组转换成字符串
	 *
	 * @param b 字节数组
	 * @return 字符串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		String res="";
		for (int i = 0; i+1 < hs.length(); i+=2) {
			res += String.valueOf(hs.charAt(i));
			res += String.valueOf(hs.charAt(i+1));
			res += " ";
            if (((i+2) % 20 == 0) && i >= 18)
            	res += "\n";
        }
		return res.toLowerCase();
	}

	/**
	 * sha256_HMAC加密
	 * 
	 * @param message 消息
	 * @param secret  秘钥
	 * @return 加密后字符串
	 */
	public static byte[] sha256_HMAC( byte[] secret,byte[] message) {
		String hash = "";
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret, "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(message);
			hash = byteArrayToHexString(bytes);
			return bytes;

		} catch (Exception e) {
			System.out.println("Error HmacSHA256 ===========" + e.getMessage());
		}
		return null;
	}

}