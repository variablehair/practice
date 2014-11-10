import java.io.*;
import java.lang.String;
import java.lang.Integer;

public class Sum {
	public static void main(String[] args) throws IOException {
		String DIGITS = "0123456789";
		String SPACE = " ";
		FileReader reader = new FileReader("dat.txt");
		boolean isNum = false;
		boolean isSpace = false;
		String s = "0";
		String tester = "";
		String out = "";
		Integer stat = new Integer(0);
		int total = 0;
		
		int datum = reader.read();
		
		while (datum != -1) {
			char c = (char) datum;

			isNum = DIGITS.contains(tester + c);
			isSpace = SPACE.contains(tester + c);
			
			if (!isNum && !isSpace && total == 0)
				out += c;
			else if (isNum) 
				s += c;
			else if (isSpace) {
				Integer temp = new Integer(Integer.parseInt(s));
				total += temp;
				s = "0";
			}
			else {
				System.out.println(out + total);
				total = 0;
				out = "" + c;
			}
			
			datum = reader.read();
		}
	}
}