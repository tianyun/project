package jm;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @author javaС����
 */
public class SignatureEcdsaUtils {
	public static final String ALGORITHM = "EC";
	public static final String SIGN_ALGORITHM = "SHA256withECDSA";

	// ��ʼ����Կ��
	public static KeyPair initKey() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
			ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
			generator.initialize(ecGenParameterSpec, new SecureRandom());
			generator.initialize(256);
			return generator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ��ȡ��Կ
	public static byte[] getPublicKey(KeyPair keyPair) {
		byte[] bytes = keyPair.getPublic().getEncoded();
		return bytes;
	}

	// ��ȡ��Կ
	public static String getPublicKeyStr(KeyPair keyPair) {
		byte[] bytes = keyPair.getPublic().getEncoded();
		return encodeHex(bytes);
	}

	// ��ȡ˽Կ
	public static byte[] getPrivateKey(KeyPair keyPair) {
		byte[] bytes = keyPair.getPrivate().getEncoded();
		return bytes;
	}

	// ��ȡ˽Կ
	public static String getPrivateKeyStr(KeyPair keyPair) {
		byte[] bytes = keyPair.getPrivate().getEncoded();
		return encodeHex(bytes);
	}

	// ǩ��
	public static byte[] sign(byte[] data, byte[] privateKey, String signAlgorithm) {
		try {
			// ��ԭʹ��
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			// 1��ʵ����Signature
			Signature signature = Signature.getInstance(signAlgorithm);
			// 2����ʼ��Signature
			signature.initSign(priKey);
			// 3����������
			signature.update(data);
			// 4��ǩ��
			return signature.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ��֤
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign, String signAlgorithm) {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			// 1��ʵ����Signature
			Signature signature = Signature.getInstance(signAlgorithm);
			// 2����ʼ��Signature
			signature.initVerify(pubKey);
			// 3����������
			signature.update(data);
			// 4��ǩ��
			return signature.verify(sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ����׼16���Ʊ���
	public static String encodeHex(final byte[] data) {
		return encodeHex(data, true);
	}

	// ����ת16���Ʊ���
	public static String encodeHex(final byte[] data, final boolean toLowerCase) {
		final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return new String(out);
	}

	public static void main(String[] args) {
		String str = "javaС����";
		byte[] data = str.getBytes();
		// ��ʼ����Կ��
		KeyPair keyPair = initKey();
		byte[] publicKey = getPublicKey(keyPair);
		System.out.println("��Կ������" + publicKey.length);
		System.out.println(Arrays.toString(publicKey));
		byte[] privateKey = getPrivateKey(keyPair);
		System.out.println("=========================");
		String kk = (encodeHex(privateKey));
		System.out.println(kk);
		for (int i = 0; i < kk.length(); i = i + 2) {
			if (i > 1 && i % 20 == 0) {
				System.out.println();
			}
			System.out.print("" + kk.charAt(i) + kk.charAt(i + 1) + " ");

		}
		System.out.println();
		System.out.println("˽Կ������" + encodeHex(privateKey).length());
		System.err.println(Arrays.toString(privateKey));
		// ǩ��
		byte[] sign = sign(str.getBytes(), privateKey, SIGN_ALGORITHM);
		System.out.println("=========================");
		System.out.println("ǩ��������" + sign.length);
		System.err.println(Arrays.toString(sign));

		byte[] sign1 = sign(str.getBytes(), privateKey, SIGN_ALGORITHM);
		System.out.println("=========================");
		System.out.println("ǩ��������" + sign1.length);
		System.err.println(Arrays.toString(sign1));
		// ��֤
		boolean b1 = verify(data, publicKey, sign, SIGN_ALGORITHM);
		System.out.println("��֤:" + b1);
		// ��֤
		boolean b2 = verify(data, publicKey, sign1, SIGN_ALGORITHM);
		System.out.println("��֤:" + b2);
	}
}
