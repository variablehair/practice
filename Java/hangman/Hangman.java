/* AWT Hangman
 * Tom Li | Nov 2014
 * https://github.com/variablehair
 * 
 * Basic hangman implementation using AWT. Written for practice purposes only.
 * Feel free to steal whatever you want from this.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Random;

public class Hangman extends Frame implements ActionListener, WindowListener, KeyListener {
	/* Various variables used by the game */
	final String ALPHABET = "a b c d e f g h i j k l m n o p q r s t u v w x y z ";
	final String WORDLIST = "wordsEn.txt"; //src: http://www-01.sil.org/linguistics/wordlists/english/
	final int TOTAL_WORDS = 109582; // total number of words in list
	
	// the shortest and longest allowable lengths of words for the game
	final int MIN_WORD_LENGTH = 4;
	final int MAX_WORD_LENGTH = 15;
	
	// PH is a placeholder for a letter that hasn't been guessed yet
	final String PH = " _ ";
	
	LineNumberReader reader;
	
	String currentWord = "";
	String matchedWord = ""; // holds all the letters successfully matched so far
	String remainingLetters = new String(ALPHABET);
	
	Button NewGame = new Button("New Game");
	Label currentWordLabel = new Label("Press the New Game button to get started.");
	Label remainingLettersLabel = new Label(remainingLetters, Label.CENTER);
	
	/* constructor; sets up UI.
	 *
	 * The game uses a BorderLayout to hold all its components.
	 * NORTH holds the image that gets drawn as the player guesses incorrectly.
	 * WEST holds the new game button.
	 * CENTER holds the current word being guessed.
	 * EAST is empty.
	 * SOUTH holds the remaining unguessed letters.
	 */
	 
	public Hangman() {
		setLayout(new BorderLayout());
		add(new Label("THIS IS A PLACEHOLDER"), BorderLayout.NORTH);
		add(currentWordLabel, BorderLayout.CENTER);
		add(NewGame, BorderLayout.WEST);
		add(remainingLettersLabel, BorderLayout.SOUTH);
		
		// add a windowlistener so we can close the game
		addWindowListener(this);
		
		// add a listener to the New Game button
		NewGame.addActionListener(this);
		
		// adds a KeyListener to the button because it is the only thing that can get focus.
		NewGame.addKeyListener(this); 
		
		// displays the game
		setTitle("Hangman");
		setSize(400, 200);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Hangman();
	}
	
	private class HangmanCanvas extends Canvas {
		public HangmanCanvas() {
			setSize(400, 300);
		}
			
		public HangmanCanvas(int w, int h) {
			setSize(w, h);
		}
		
		@Override
		public void paint (Graphics g) {	
			Graphics2D painter = (Graphics2D) g;
		}
	}
	
	/* newWord is called whenever a new game is started and returns a random word from the wordlist.
	 *
	 * It creates a random number generator and generates a number between 0 and the total number of
	 * words in the list. It then reads lines in from the wordlist until it reaches the given line;
	 * if that word is of appropriate length, then it is used; if not, it will keep going down the list
	 * until an appropriate word is found. Once such a word is found, it will return it.
	 * 
	 * Despite two while loops, the method uses only a single traversal, so it should run in O(n) time.
	 */
	private String newWord(String wordlist) throws FileNotFoundException, IOException {
		Random rand = new Random();
		String ret = "";
		int target;
		
		try {
			reader = new LineNumberReader(new FileReader(WORDLIST));	
		} catch (FileNotFoundException e) {
			throw e;
			}

		target = rand.nextInt(TOTAL_WORDS);
		
		try {
			while(reader.getLineNumber() < target) {
			
			// so that ret isn't overwritten pointless n-1 times, we add a check to only write when needed
				if (reader.getLineNumber() == target-1)
					ret = reader.readLine();
				else
					reader.readLine();
			}
			while(ret.length() < MIN_WORD_LENGTH || ret.length() > MAX_WORD_LENGTH) {
				ret = reader.readLine();
				/* for some wordlists that end in words that are too long or too short, this could break.
				   We will assume the user uses the provided wordlist and not handle this case. */
			}
		} catch (IOException e) {
			throw e;
			}
		System.out.println(ret);
		return ret;
	}
	
	/* This is essentially the code for creating a new game. It is placed here instead of in another function
	 * because the New Game button has the only ActionListener, and nothing else needs to be covered here.
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String word = "";
		
		try {
			word = newWord(WORDLIST);
		} catch (FileNotFoundException ex) {
			System.out.println("No wordlist found.");
			System.exit(0);
		} catch (IOException ex) {
			System.out.println("IOException; exiting program.");
			System.exit(0);
		}
		
		String temp = "";
		// fills the currentWord Label with placeholders
		for (int i = 0; i < word.length(); i++) {
			temp += PH;
		}
		
		remainingLetters = new String(ALPHABET);
		remainingLettersLabel.setText(remainingLetters);
		currentWordLabel.setText(temp);
		currentWord = word;
		
	}
	
	/* Like actionPerformed, this is essentially all of the code for playing the game itself, because
	 * it is the only keyListener. */
	 
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		String s = ""+c; // creates a String so we can use certain String functions like contains().
		
		if (ALPHABET.contains(s) && remainingLetters.contains(s)) {
			// uses the substring method to "remove" a character from remainingLetters.
			remainingLetters = remainingLetters.substring(0, remainingLetters.indexOf(s)) +
					remainingLetters.substring(remainingLetters.indexOf(s) + 2, remainingLetters.length());
			remainingLettersLabel.setText(remainingLetters);
			
			if (currentWord.contains(s) && !(matchedWord.contains(s))) {
				int i = currentWord.indexOf(s, 0);
				while (i != -1) {
					char[] out = currentWordLabel.getText().toCharArray();
					out[3*i+1] = c;
					currentWordLabel.setText(new String(out));
					i = currentWord.indexOf(s, i+1);
					System.out.println(i);
				}

			}

/*			while (currentWord.contains(s)) {
				
				int i = currentWord.indexOf(s);
				displayChar(c, i);
				if (i == currentWord.length()-1) {
					currentWord = currentWord.substring(0, i);
				}
				else {
					currentWord = currentWord.substring(0, i) +
								  currentWord.substring(i + 1, currentWord.length());
				}
				System.out.println(currentWord); 
			} */
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	
	// these next functions need to be overridden, but are not used
	public void windowOpened(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	public void keyPressed(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }
	
}

