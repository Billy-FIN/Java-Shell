package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
* This class provides the implementation of a print filter.
* It can print whatever it has from the input pipe (not error message).
* A print filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class PrintFilter extends SequentialFilter {
	
	/**
	 * Processes the input pipe and print to the console
	 */
	public void process() {
		while (!input.isEmpty()) {
			String line = input.read();
			processLine(line);
		}
	}
	
	/**
	 * Prints the content to the console
	 * 
	 * @param line String things need to be printed
	 * @return null
	 */
	public String processLine(String line) {
		System.out.println(line);			
		return null;
	}
	
}
