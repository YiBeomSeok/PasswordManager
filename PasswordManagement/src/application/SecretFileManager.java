package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class SecretFileManager {
	public static void encryptFile(String specName, SecretKey key, IvParameterSpec iv, File inputFile, File outputFile)
			throws Exception {

		Cipher cipher = Cipher.getInstance(specName);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		try (FileOutputStream output = new FileOutputStream(outputFile);
				CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher)) {

			String data = Files.lines(inputFile.toPath()).collect(Collectors.joining("\n"));
			cipherOutput.write(data.getBytes(StandardCharsets.UTF_8));
		}
	}

	public static void decryptFile(String specName, SecretKey key, IvParameterSpec iv, File encryptedFile,
			File decryptedFile) throws Exception {

		Cipher cipher = Cipher.getInstance(specName);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		try (CipherInputStream cipherInput = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
				InputStreamReader inputStream = new InputStreamReader(cipherInput);
				BufferedReader reader = new BufferedReader(inputStream);
				FileOutputStream fileOutput = new FileOutputStream(decryptedFile)) {

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			fileOutput.write(sb.toString().getBytes(StandardCharsets.UTF_8));
		}
	}
}
