package application;

import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class MainController implements Initializable {
	@FXML private TextField accountId;
	@FXML private PasswordField accountPassword;
	@FXML private TextField inputNewTitle;
	@FXML private TextField inputNewId;
	@FXML private TextField inputNewPassword;
	@FXML private Button loginBtn;
	@FXML private Button registrationBtn;
	@FXML private ListView<String> dataListView;
	
	File accountData = new File("accountdata.txt");
	File dataList = new File("dataList.txt");
	SHA256 sha256 = new SHA256();
	String secretKey = "skey";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.print("test");
		registrationBtn.setVisible(false);
	}
	
	public void loginAction(ActionEvent e) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(accountData, true);
		BufferedReader br = new BufferedReader(new FileReader(accountData));
		String usrId = br.readLine();
		String usrPassword = br.readLine();
		
		System.out.print(usrId + " " + usrPassword);
		br.close();
		
		if(usrId == null && usrPassword == null) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(accountData));
			bw.write(sha256.encrypt(accountId.getText()) + "\n");
			bw.write(sha256.encrypt(accountPassword.getText()) + "\n");

			bw.flush();
			bw.close();
			
			System.out.println("새로운 계정 생성 완료");
			fileOutputStream.close();
			return;
		}
		
		if(validateId(accountId.getText(), usrId) && validatePassword(accountPassword.getText(), usrPassword)) {
			System.out.println("로그인 성공");
			initDataList();
			loginBtn.setVisible(false);
			registrationBtn.setVisible(true);
		}

		fileOutputStream.close();
	}
	
	public void addData(ActionEvent e) throws Exception {
		System.out.println("addData");
		FileWriter fw = new FileWriter(dataList, true);
		AES256 aes256 = new AES256();
		
		String newTitle = inputNewTitle.getText();
		String newId = inputNewId.getText();
		String newPassword = inputNewPassword.getText();
		String newItem = newTitle + " " + newId + " " + newPassword;
		fw.write(aes256.encryptAES256(newItem, secretKey) + "\n");
		dataListView.getItems().add(newItem);

		fw.close();
	}
	
	private boolean validateId(String inputId, String cryptogram) throws NoSuchAlgorithmException {
		if(cryptogram.equals(sha256.encrypt(inputId))) return true;
		return false;
	}
	
	private boolean validatePassword(String inputPassword, String cryptogram) throws NoSuchAlgorithmException {
		if(cryptogram.equals(sha256.encrypt(inputPassword))) return true;
		return false;
	}
	
	private void initDataList() throws Exception {
		FileOutputStream dataListfileOutputStream = new FileOutputStream(dataList, true);
		BufferedReader br = new BufferedReader(new FileReader(dataList));
		AES256 aes256 = new AES256();
		while(true) {
			String item = br.readLine();
			
			if(item == null) {
				br.close();
				dataListfileOutputStream.close();
				return;
			}
			item = aes256.decryptAES256(item, secretKey);
			dataListView.getItems().add(item);
		}
	}
}
