package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
* This class provides the implementation of a wc filter.
* It can count the number of lines, words, and characters
* in the piped input. A wc filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class WordCountFilter extends SequentialFilter {
	
	/**
	 * The boolean flag to determine whether this filter needs
	 * to have a redirect filter
	 */
	private boolean flag;
	
	/**
	 * Used for redirect filter to redirect the output to this <file>
	 */
	private String redirectFileName;
	
	/**
	 * Stores the number of lines while processing
	 */
	private int lines;
	
	/**
	 * Stores the number of characters while processing
	 */
	private int characters;
	
	/**
	 * Stores the number of words while processing
	 */
	private int words;
	
	/**
	 * Constructs a new wc filter
	 * 
	 */
	public WordCountFilter() {
		output = new Pipe();
		lines = 0;
		characters = 0;
		words = 0;
	}
	
	/**
	 * Constructs a new wc filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 */
	public WordCountFilter(Boolean flag, String redirectFileName) {
		output = new Pipe();
		lines = 0;
		characters = 0;
		words = 0;
		this.flag = flag;
		this.redirectFileName = redirectFileName;
	}
	
	/**
	 * Processes the input pipe and passes the counting result to the output pipe
	 * 
	 */
	public void process() {
		// must have input
		if(input == null) {
			throw new IllegalArgumentException("input" + "wc");
		}
		// processes the input content
		while (!input.isEmpty()) {
			String line = input.read();
			processLine(line);
		}
		output.write(lines + " " + words + " " + characters);
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(redirectFileName);
			this.setNextFilter(redirect);
			redirect.process();
			output = null;
		}
	}
	
	/**
	 * Splits the content by certain characters and
	 * then updates the numbers of lines, characters, and
	 * words
	 * 
	 * @param line String one of parts of input
	 * @return ""
	 */
	public String processLine(String line) {
		String[] newLines = line.split("\n");
		lines += newLines.length;
		for(String word: line.split("\\s+|\n")) {
			if(!word.equals("")) {
				words++;
			}
		}
		characters += line.length();
		return "";
	}
}
