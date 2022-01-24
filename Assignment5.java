/*Radhika Banerjea
 * November 23, 2021
 * This is a user login system for a program using a GUI. 
 * The application has the hawthorn logo, a field for the username and a field for the password to tbe inputted
The user will write a username and password in the fields designated for them. 
Once the user presses “Login”, the program checks whether the user is registered in a preexisting file and 
whether the password provided is valid.
 */

//importing what is necessary
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import exceptionbasics.EmptyStringException;

public class Assignment5 {
	//variables for the text inputs for username and password
	
/**
 * This is the main method
 * @param args
 * @throws FileNotFoundException
 */
	public static void main(String[] args) throws FileNotFoundException {
		//variable for the file
		Text userName;
		Text text_password;
		File handle = new File("LoginInfo.txt");

		//setting the display, the shell, and naming and sizing the shell
		Display display = Display.getDefault();
		Shell shlLoginApp = new Shell();
		shlLoginApp.setImage(SWTResourceManager.getImage("C:\\Users\\radhi\\eclipse-workspace\\Assignment 5 \u2013 GUI and JavaDoc\\images\\site-logo.png"));
		shlLoginApp.setText("Login App");
		shlLoginApp.setSize(689, 501);
		
		//making a vertical line down the center
		Label label = new Label(shlLoginApp, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(337, 36, 2, 320);
		
		//labels that say username and password
		Label lblUsername = new Label(shlLoginApp, SWT.NONE);
		lblUsername.setBounds(41, 70, 81, 25);
		lblUsername.setText("Username:");
		Label lblPassword = new Label(shlLoginApp, SWT.NONE);
		lblPassword.setBounds(41, 220, 81, 25);
		lblPassword.setText("Password:");
		
		
		//text boxes for the username and password
		userName = new Text(shlLoginApp, SWT.BORDER);
		userName.setBounds(41, 121, 237, 31);
		text_password = new Text(shlLoginApp, SWT.BORDER | SWT.PASSWORD);
		text_password.setBounds(41, 262, 237, 31);
		
		
		//creating login button
		Button btnLogin = new Button(shlLoginApp, SWT.NONE);
		btnLogin.setBounds(53, 372, 154, 35);
		btnLogin.setText("Login");
		
		
		//getting the hawthorn logo, setting it as a variable and putting it on the GUI.
		Label lblNewLabel = new Label(shlLoginApp, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage("C:\\Users\\radhi\\eclipse-workspace\\Assignment 5 \u2013 GUI and JavaDoc\\images\\site-logo.png"));
		lblNewLabel.setBounds(418, 52, 193, 268);
		
		
		//action listener for the register button
		Button btnRegister = new Button(shlLoginApp, SWT.NONE);
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//calling register constructor
					new Register();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			
			}
		});
		btnRegister.setBounds(271, 372, 154, 35);
		btnRegister.setText("Register");
		
		
		//action listener for the forgot password button
		Button btnForgotPassword = new Button(shlLoginApp, SWT.NONE);
		btnForgotPassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					//the message box variable
					MessageBox messageBox = new MessageBox(shlLoginApp, SWT.ICON_WARNING);
					if ((userName.getText()).equals("")){
						messageBox.setMessage("you must input a username");
						messageBox.open();

					}
					//message box if username is not found
					else if (getEmail(userName.getText(),handle).equals("")) {
						messageBox.setMessage("Username not found");
						messageBox.open();

					}
					//message box sending the email found.
					else {
						messageBox.setMessage("An email has been sent to " + getEmail(userName.getText(),handle));
						messageBox.open();

					}
			
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnForgotPassword.setBounds(482, 372, 154, 35);
		btnForgotPassword.setText("Forgot Password?");
		
 
	
		//selection listener for the button
		
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messagebox = new MessageBox(shlLoginApp, SWT.ICON_WARNING);

				//try and catch for errors
				try {
					//checking if both username and password were entered
					if (userName.getText().equals("")||text_password.getText().equals("")) {
						messagebox.setMessage("You need to input BOTH username and password");
						messagebox.open();
					}
					//cehcking if successful login
					else if (validLogin(userName.getText(), text_password.getText(), handle)){
						messagebox.setMessage("Successful Login");
						messagebox.open();
					}
					//if not successful, then unsuccessful login.
					else {
						System.out.println("enetered the else");
						messagebox.setMessage("Unsuccessful Login");
						messagebox.open();

					}
				
				}
				catch (Exception e1) {
					
				}
			}
		});
		
		//closes all the chidlren
		shlLoginApp.addListener(SWT.Close, new Listener()
	        {
	           @Override
	           public void handleEvent(Event event)
	           {
	              shlLoginApp.dispose();
	              display.dispose();
	           }
	        });
		
		shlLoginApp.open();
		shlLoginApp.layout();
		while (!shlLoginApp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	/**
	 * This method checks if the user has enetered a valid login
	 * @param userName - represents what the user entered as the username
	 * @param pwd - represents what the user enetered as the password
	 * @param handle - represents the file that is passed that is read to see if the username and password are found in this file
	 * @return returns boolean of whether or not the login is valid
	 * @throws IOException
	 */
	public static boolean validLogin(String userName, String pwd, File handle) throws IOException{
		try {
			//variable for the file scanner, the message box, the inputed username, and the inputed password
			Scanner inputFile = new Scanner(handle);				
			
			//boolean variable that says whether there is a next line.
			       boolean statement = inputFile.hasNextLine();
			//while there is a next line
			while (inputFile.hasNextLine()) {
				//splits the file line that its on into an array, ignoring the ;
				String[]tokens = (inputFile.nextLine()).split(";");
				//if the first spot of the array is the username inputed, and the second spot is the password inputed, then a message box opens and the boolean variable is set to false to exit the while loop
				if (tokens[0].equals(userName)&&tokens[1].equals(encode(pwd,6))) {
					return true;
				}
				//if the first spot of the array isn't the inputed username and the second spot of the array isnt the inputed password it is the last spot of the file, then the unsuccessful message box pops up.
				else if(!(inputFile.hasNextLine())){
					return false;

				}
			}	
			//closing scanner
			inputFile.close();
		}	
		
		catch(IOException e1) {
		}
		catch(Exception e1) {
		}
		
		return false;
	}
	
	/**
	 * This method cycles through the file and takes the user's inputed username and checks for the associated email
	 * @param userName - represents the inputted username from the user
	 * @param handle - represents the file that is checked to find the email.
	 * @return returns the email that corresponds to the inputted username
	 * @throws IOException
	 */
	public static String getEmail(String userName, File handle) throws IOException{
		Scanner inputFile = new Scanner (handle);
		boolean statement = inputFile.hasNextLine();
		//while there is a next line
		while (statement) {
			
			//splits the file line that its on into an array, ignoring the ;
			String[]tokens = (inputFile.nextLine()).split(";");
			//if the first spot of the array is the username inputed, and the second spot is the password inputed, then a message box opens and the boolean variable is set to false to exit the while loop
			if (tokens[0].equals(userName)) {
				return tokens [4];
			}
			else if(!(inputFile.hasNextLine())) {
				statement = false;
			}
		}
		return "";
	}
	
	
	/*This method takes the sentence char Array made from the msg and goes through the ASCII codes for all the
	 * characters that are the alphabet uppercase and lower case. Then the key is added to those ASCII code numbers
	 * to shift the characters for the Caesar cypher. If the character goes past z when the key is added, then 26 is 
	 * subtracted from that number to loop it back to the beginning of the alphabet.
	 * The encripted version is then returned as a string.
	 * 
	 */
	
	/**
	 * This method takes the user's inputed password and encrypts it.
	 * @param msg - the password that the user entered that is going to be encoded.
	 * @param key - the key that is used to encode the entered password.
	 * @return returns the encoded password.
	 */
	public static String encode(String msg, int key) {
		char [] sentence = msg.toCharArray();
		String encoded = "";
		for (int i=0;i<sentence.length;i++) {
			if (sentence[i]<=90 &&sentence[i]>=65) {
				sentence [i] = (char)(sentence [i] + key);
				if (sentence [i]>90) {
					sentence [i] = (char)(sentence [i] - 26);
				}
			}
			else if (sentence[i]<=122 &&sentence[i]>=97) {
				sentence [i] = (char)(sentence [i] + key);
				if (sentence [i]>122) {
					sentence [i] = (char)(sentence [i] - 26);
				}
			}
			encoded = encoded + (String.valueOf(sentence [i]));
		}
		return encoded;
	}
}
