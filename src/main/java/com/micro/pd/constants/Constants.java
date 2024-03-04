package com.micro.pd.constants;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Constants {
	public static String file_url = "https://www.mediafire.com/file_premium/zvlz0u76j24yaih/PD_NV.apk/file";
	public static String update_url = "https://www.mediafire.com/file/o61q0nu2jdcb7vs/PD_PS.apk/file";
	public static String count_url = "https://storage.googleapis.com/update_mobile/dpc.txt";
	public static String otp = "";
	public static String otpresend = "";
	public static int intGlobalCount = 1;
	public static String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCekzvHH/LTpqfuf2928s/X9XP1mu/QNysoZaIAzRUPtL0CYpjeWM734nNFrvs1hZ1Ql7NG3pwKEm6+A28//vmQZr67l5tkQ6KX3A7ZSuNfHeoWLALEb/c5rDgCDNj6Cluunp/8/7UgR82kDf9zebj8kcQRAGS346aJL8Svt8JZHQIDAQAB";

	public static String Encrypt(String paramString1, String paramString2)
			throws GeneralSecurityException {
		Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		localCipher.init(Cipher.ENCRYPT_MODE, getData(paramString2));
		byte[] arrayOfByte = Base64.getEncoder().encode(localCipher.doFinal(paramString1.getBytes()));
		return new String(arrayOfByte);
	}

	public static String Encrypt1(String paramString1, String paramString2)
			throws GeneralSecurityException {
		Cipher localCipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
		localCipher.init(Cipher.ENCRYPT_MODE, getData(paramString2));
		byte[] arrayOfByte = Base64.getEncoder().encode(localCipher.doFinal(paramString1.getBytes()));
		return new String(arrayOfByte);
	}

	public static String Decrypt(String paramString1, String paramString2)
			throws GeneralSecurityException {
		Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		localCipher.init(Cipher.DECRYPT_MODE, getData(paramString2));
		byte[] arrayOfByte = Base64.getDecoder().decode(localCipher.doFinal(paramString1.getBytes()));
		return new String(arrayOfByte);
	}

	public static PublicKey getData(String paramString) throws InvalidKeySpecException, NoSuchAlgorithmException {
		X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(paramString.getBytes()));
		return KeyFactory.getInstance("RSA").generatePublic(localX509EncodedKeySpec);
	}

	public static void getUriFromDisplayName(Stage stage, String likestr) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Directory");
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			File[] files = selectedDirectory.listFiles(new GenericExtFilter(likestr));

			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isFile()) {
						file.delete();
					}
				}
			}
		}
	}

	// Other methods and constants can be similarly converted

	public static class GenericExtFilter implements FilenameFilter {
		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	public static boolean changeMode() {
		try {
			// Implementing the equivalent functionality for changing mode in JavaFX is platform-dependent
			// You may need to use a different approach based on your requirements
			// For example, you can show an alert informing the user to manually change the mode in their system settings
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("Please manually change the mode in system settings.");

			alert.showAndWait();

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
//	public static String decodeResponse(String paramString)
//			throws Exception {
//		return decode(reverseString(decode(paramString)));
//	}
//
//	public static String encodeResponse(String paramString)
//			throws Exception {
//		return encode(reverseString(encode(paramString)));
//	}
//
//	public static String encode(String paramString) {
//		byte[] data = Base64.getEncoder().encode(paramString.getBytes());
//		String str2 = new String(data, StandardCharsets.UTF_8);
//		return str2;
//	}
//
//	// ERROR //
//	public static String decode(String paramString) {
//
//		byte[] data = Base64.getDecoder().decode(paramString);
//		String str2 = new String(data, StandardCharsets.UTF_8);
//		return str2;
//	}
//
//	public static String reverseString(String paramString) {
//		if (paramString.length() > 0) {
//			StringBuilder localStringBuilder = new StringBuilder();
//			localStringBuilder.append(paramString);
//			return localStringBuilder.reverse().toString();
//		}
//		return "";
//	}

	public static String getYYYMMDD(String paramString) {
		try {
			StringBuilder localStringBuilder = new StringBuilder();
			localStringBuilder.append(paramString.substring(6, 10).toString());
			localStringBuilder.append("-");
			localStringBuilder.append(paramString.substring(3, 5).toString());
			localStringBuilder.append("-");
			localStringBuilder.append(paramString.substring(0, 2).toString());
			paramString = localStringBuilder.toString();
		} catch (Exception localException) {
		}
		return paramString;
	}
	public static String getDDMMYYYY(String paramString) {
		try {
			StringBuilder localStringBuilder = new StringBuilder();
			localStringBuilder.append(paramString.substring(8, 10).toString());
			localStringBuilder.append("/");
			localStringBuilder.append(paramString.substring(5, 7).toString());
			localStringBuilder.append("/");
			localStringBuilder.append(paramString.substring(0, 4).toString());
			paramString = localStringBuilder.toString();
		} catch (Exception localException) {
		}
		return paramString;
	}

	public static String decodeResponse(String paramString)
			throws Exception {
		return decode(reverseString(decode(paramString)));
	}

	public static String encodeResponse(String paramString)
			throws Exception {
		return encode(reverseString(encode(paramString)));
	}

	public static String encode(String paramString) {
		byte[] data = Base64.getEncoder().encode(paramString.getBytes());
		String str2 = new String(data, StandardCharsets.UTF_8).replaceAll("[\\n\\r]", "");
		return str2;
	}

	// ERROR //
	public static String decode(String paramString) {
		byte[] data = Base64.getDecoder().decode(paramString);
//		byte[] data = Base64.decode(paramString, Base64.DEFAULT);
		String str2 = new String(data, StandardCharsets.UTF_8).replaceAll("[\\n\\r]", "");;
		return str2;
	}

	public static String reverseString(String paramString) {
		if (paramString.length() > 0) {
			StringBuilder localStringBuilder = new StringBuilder();
			localStringBuilder.append(paramString);
			return localStringBuilder.reverse().toString();
		}
		return "";
	}

}
