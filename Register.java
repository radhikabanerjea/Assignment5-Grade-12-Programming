/*Radhika Banerjea
 * November 16, 2021
 * opens a register window that will register users and catch any errors with their registration
 * 
 */
//importing what is necessary
import java.util.Scanner;
import java.io.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Register {
	//necessary class variables
	protected static Shell shell;
	private static Text firstname;
	private static Text lastname;
	private static Text email;
	private static Text username;
	private static Button btnRegister;
	private static Label lblFirstName;
	private static Label lblLastName;
	private static Label lblEmailAddress;
	private static Label lblUsername;
	private static Label lblPassword;
	private static Text password;
	
	/**
	 * constructor that is called in Assignment 5 class
	 * @throws IOException
	 */
	public Register () throws IOException {
		//creating the display, creating the contents of the Register Display, opening the shell and its layout
		Display display = Display.getDefault();
		createContents(display);
		shell.open();
		shell.layout();
		//loop that the shell is on while it has not been disposed
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}			
	}
	



	/**
	 * This method creates all the display contents for the shell - all the buttons, labels, text boxes are made here. The action listener for the register button is also here and it checks all the conditions necessary for the registration to be valid - like the valid email, the valid password, the valid username, and checking whether the email, username, and password were all inputed
	 * @param display the display of the shell
	 * @throws IOException
	 */
	public static void createContents(Display display) throws IOException {
		File handle = new File("LoginInfo.txt");
		// making all the labels, text boxes, buttons, and shell
		shell = new Shell();
		shell.setSize(479, 660);
		shell.setText("SWT Application");
		
		Label lblFillOutThis = new Label(shell, SWT.NONE);
		lblFillOutThis.setBounds(27, 34, 259, 25);
		lblFillOutThis.setText("Fill out this form to register.");
		
		firstname = new Text(shell, SWT.BORDER);
		firstname.setBounds(27, 126, 309, 31);
		
		lastname = new Text(shell, SWT.BORDER);
		lastname.setBounds(27, 215, 309, 31);
		
		email = new Text(shell, SWT.BORDER);
		email.setBounds(27, 310, 309, 31);
		
		username = new Text(shell, SWT.BORDER);
		username.setBounds(27, 404, 309, 31);
		
		btnRegister = new Button(shell, SWT.NONE);
		
		btnRegister.setBounds(330, 551, 105, 35);
		btnRegister.setText("Register");
		
		lblFirstName = new Label(shell, SWT.NONE);
		lblFirstName.setBounds(27, 95, 98, 25);
		lblFirstName.setText("First Name:");
		
		lblLastName = new Label(shell, SWT.NONE);
		lblLastName.setBounds(27, 184, 110, 25);
		lblLastName.setText("Last Name:");
		
		lblEmailAddress = new Label(shell, SWT.NONE);
		lblEmailAddress.setText("Email Address:");
		lblEmailAddress.setBounds(27, 280, 139, 25);
		
		lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(27, 373, 98, 25);
		lblUsername.setText("Username: ");
		
		lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(27, 457, 81, 25);
		lblPassword.setText("Password:");
		
		password = new Text(shell, SWT.BORDER);
		password.setBounds(30, 490, 306, 31);

		String[] usersInfo = readFile(handle);

		//array that holds all the information that is going to be on the file.
	
		btnRegister.addSelectionListener(new SelectionAdapter() {
			@Override

			public void widgetSelected(SelectionEvent e) {
				MessageBox messagebox = new MessageBox(shell, SWT.ICON_WARNING);
				try {
					//array that contains all the user's input.
					
					//checks if the password, email, and username are all filled in.
					if(password.getText().equals("")||username.getText().equals("")||email.getText().equals("")) {
						messagebox.setMessage("You must fill in the password, username, and email.");
						messagebox.open();
					}
					//checks if the email is valid
					else if (!(validEmail(email.getText()))) {
						messagebox.setMessage("invalid email");
						messagebox.open();
					}
					//checks if the username is taken
					else if(!(validUserName(username.getText(),usersInfo))) {
						messagebox.setMessage("This username is taken");
						messagebox.open();
					}
					//checks for a weak password
					else if (inFile(password.getText())) {
						messagebox.setMessage("Please choose a stronger password");
						messagebox.open();
					}
					//checks if the password is less than four characters

					else if(!(validPassword(password.getText()))) {
						messagebox.setMessage("Password must be longer than four characters");
						messagebox.open();
					}
					//writes on the file, taking the user's new input.
					else {

						String infoNewUser = username.getText() + ";" + (encode(password.getText(),6)) + ";" + firstname.getText() + ";" + lastname.getText() + ";" + email.getText();
						writeFile(infoNewUser,usersInfo, handle);
						shell.close();

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * This method counts the number of lines in the file.
	 * @param handle thhe file on which lines are being counted. 
	 * @return the amount of lines that are in the file
	 * @throws IOException
	 */
	public static int countRecords(File handle) throws IOException{
		int linecount = 0;
		Scanner inputFile = new Scanner (handle);
		//cycles through the file and counts each next line. then adds one to linecount if there is a next line and moves down a line.
		while(inputFile.hasNextLine()){  //the method hasNextLine checks whether there is a next line in the file
			inputFile.nextLine();
			linecount++;
		} 
		inputFile.close();
		return linecount;
		
	}
	
	/**
	 * method that takes a given password and encodes it with a given key.
	 * @param msg the password that is written by the user that will be encoded.
	 * @param key the key that is passed a a paramater for which you should 
	 * @return the encoded
	 */
	/*This method takes the sentence char Array made from the msg and goes through the ASCII codes for all the
	 * characters that are the alphabet uppercase and lower case. Then the key is added to those ASCII code numbers
	 * to shift the characters for the Caesar cypher. If the character goes past z when the key is added, then 26 is 
	 * subtracted from that number to loop it back to the beginning of the alphabet.
	 * The encripted version is then returned as a string.
	 * 
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
	
	/**
	 * this method cycles through the lines of the file and reads each line into slots of an array.
	 * @param handle The File that is being read into an array
	 * @return returns the array that contains all the information in the File
	 * @throws IOException
	 */
	public static String[] readFile(File handle) throws IOException{
		

		//array that is the length of the file, scanner variable
		String[] input = new String[countRecords(handle)];
		Scanner inputFile = new Scanner(handle);
			
		//cycling through the file and adding each line of it to one slot of the array.
			for(int i = 0;i<(input.length);i++) {
				input[i] = inputFile.nextLine();

			}
			inputFile.close();
		
		return input;
	}
	
	/**
	 * This method cycles through the array from readFile() and writes each line onto a file
	 * @param infoNewUser the string that contains the new information put when they press the register button
	 * @param usersInfo array from readFile() that has all the original information from the File
	 * @param handle File that is being written onto
	 * @throws IOException
	 */
	public static void writeFile(String infoNewUser,String[] usersInfo, File handle) throws IOException{
		//printwriter
		PrintWriter output = new PrintWriter(handle);

		try {
			Scanner inputFile = new Scanner (handle);
			//cycling through the given array and writing everything on it onto the file
			for (int i = 0; i<(usersInfo.length + 1); i++) {
				if (i==(usersInfo.length)) {
					output.println(infoNewUser);
				}
				else {
					//writing the most recent user input onto the file.
					System.out.println(usersInfo[i]);
					output.println(usersInfo [i]);
				}
			}
			inputFile.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		output.close();
		

	}
	/**
	 * method that checks if a given password is on a file of bad passwords
	 * @param pwd password that is checked for on the file
	 * @return returns whether the password is on the file
	 * @throws IOException
	 */
	public static boolean inFile(String pwd) throws IOException{
		//defining the file, scanner, a boolean that says whether there is a next file. 
		File handle = new File("BadPasswords.txt");
		Scanner inputFile = new Scanner (handle);
		boolean statement = inputFile.hasNextLine();
		
		//cycles through the file and returns true if the password given is on the file.
		while (statement) {
			String token = inputFile.nextLine();
			if (pwd.equals(token)) {
				inputFile.close();
				return true;
			}
			else if(!(inputFile.hasNextLine())){
				inputFile.close();
				return false;
			}

		}
		inputFile.close();
		return false;
	}
	
	/**
	 * when given a string and a character, this method checks how many times the character is in the string
	 * @param word String being looked through
	 * @param letter character being looked for
	 * @return
	 */
	public static int countChar(String word, char letter) {
		//converts a string given to a char array
		char[]letters = word.toCharArray();
		int count = 0;
		//counts how many times the character passed as a parameter appears in the string.
		for (int i = 0;i<letters.length;i++) {
			if (letters[i]==letter) {
				count++;
			}
		}
		return count;
	}
	/**
	 * THis method checks whether a string has more than four characters
	 * @param pwd string being checked
	 * @return returns true if the password is more than three characters
	 * @throws IOException
	 */
	public static boolean validPassword(String pwd) throws IOException{
		//makes the password into a character array and checks if the length is less than four
		//if it is, then return false
		if ((pwd.toCharArray()).length<4){
			return false;
		}
		return true;
	}
	
	/**
	 * THis method checks whether a string is present in an array
	 * @param userName name that is checked for in the array
	 * @param usersInfo the array that the username is being checked in
	 * @return returns boolean whether or not the username is in the FIle
	 */
	public static boolean validUserName(String userName,String[] usersInfo) {
		System.out.println("username is: "+userName);
		//cycles through the usersInfo array and splits the slot its on with the semicolons
		//then it checks if the first spot of the split up array is equal to the username. If it is, then it returns false.
		for (int i = 0;i<usersInfo.length;i++) {
			String [] tokens = usersInfo[i].split(";");
			System.out.println(tokens[0]);
			if (tokens[0].equals(userName)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method checks if and email is valid. This method checks if a given email has one @ sign, at least one '.', and characters before and after the @ and the .
	 * @param email String that is being checked if it is valid
	 * @return returns whether the email is valid or not.
	 */
	public static boolean validEmail(String email) {
		//array with all the characters of the email.
		char [] letters = email.toCharArray();
		
		//There has to be only one @ and at least 1 .
		if (!(countChar(email, '@')==1)) {
			return false;
		}	
		if (countChar(email, '.')<1) {
			return false;
		}
		
		for (int i = 0;i<letters.length;i++) {
			
			if (letters[i]=='@') {
				//checks if the first place is the @ sign
				if (i==0) {
					return false;
				}

				//while loop checks if there is a letter from the beginning to the @ sign
				boolean statement = true;
				while (statement) {
					for (int k = 0; k<i;k++) {
						
						if (((letters [k]>=65)&&(letters [k]<=90))){
							System.out.println("i have entered letter checking slot 1 a");
							statement = false;
							k=i;
						}
						
						else if((letters [k]>=97)&&(letters [k]<=122)){
							System.out.println("i have entered letter checking slot 1 b");
							statement = false;
							k=i;
						}
						else if (k==(i-1)) {
							System.out.println("i returned false 1");
							return false;
						}
					}
					
				}
				
				//new for loop for after the @ sign
				for (int j = i+1;j<letters.length;j++) {
					if (letters[j]=='.') {
						//if the spaces between the  @ and the dot is less than 2, invalid email.
						if ((j-i)<2) {
							return false;
						}
						//if the dot is the last place, false
						else if (j==(letters.length-1)) {
							return false;
						}
						else {
							//if the characters between the @ and the . are not letters then false
							boolean state2 = true;
							while (state2) {
								for (int k = i+1; k<j;k++) {
	
									if (((letters [k]>=65)&&(letters [k]<=90))){
										System.out.println("i have entered letter checking slot 2 a");
										state2 = false;
										k=j;
									}
									
									else if((letters [k]>=97)&&(letters [k]<=122)){
										System.out.println("i have entered letter checking slot 2 b");
										state2 = false;
										k=j;
									}
									else if (k==(j-1)) {
										System.out.println("i returned false 2");
										return false;
									}
								}
							}
							//if the characters between the . and the end are not letters then false.
							boolean state3 = true;
							while (state3) {
								for (int k = j+1; k<letters.length;k++) {
	
									if (((letters [k]>=65)&&(letters [k]<=90))){
										System.out.println("i have entered letter checking slot 3 a");
										state3 = false;
										k=letters.length;
										j=letters.length;

									}
									
									else if((letters [k]>=97)&&(letters [k]<=122)){
										System.out.println("i have entered letter checking slot 3 b");
										state3 = false;
										k=letters.length;
										j=letters.length;

									}
									else if (k==(letters.length-1)) {
										System.out.println("i returned false 3");
										return false;
									}
								}
							}
						}
					}
					//checks if the dot was after the @, if not then false.
					else if (j==(letters.length-1)) {
						System.out.println("I returned false because the dot isnt after the @");
						return false;
					}
				}
				
			}	
		}
		
		

		return true;
	}

}
