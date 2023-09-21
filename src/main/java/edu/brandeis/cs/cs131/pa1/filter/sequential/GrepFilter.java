package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
* This class provides the implementation of a grep filter.
* It can return all lines from piped input that contain <query>.
* A grep filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class GrepFilter extends SequentialFilter {
	
	/**
	 * The boolean flag to determine whether this filter needs
	 * to have a redirect filter
	 */
	private boolean flag;
	
	/**
	 * The instance stores the key words to match the input lines
	 */
	private String query;
	
	/**
	 * Used for redirect filter to redirect the output to this <file>
	 */
	private String redirectFileName;
	
	/**
	 * Constructs a new grep filter
	 * 
	 * @param query String User's key words to match the input lines
	 */
	public GrepFilter(String query) {
		this.query = query;
		output = new Pipe();
	}
	
	/**
	 * Constructs a new tail filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 * @param query 			String 	User's key words to match the input lines                
	 */
	public GrepFilter(boolean flag, String query, String redirectFileName) {
		this.flag = flag;
		this.query = query;
		this.redirectFileName = redirectFileName;
		output = new Pipe();
	}
	
	/**
	 * Processes the input pipe and returns all the lines that contains
	 * user's key words (<query>)
	 */
	public void process() {
		// must have input
		if(input == null) {
			throw new IllegalArgumentException("input" + "grep " + query);
		}
		while (!input.isEmpty()) {
			String line = input.read();
			processLine(line);
		}
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(redirectFileName);
			this.setNextFilter(redirect);
			redirect.process();
			output = null;
		}
	}
	
	/**
	 * Checks whether one line of pipe input contains user's key words (<query>)
	 * 
	 * @param line String one line from the input pipe
	 * @return ""
	 */
	public String processLine(String line) {
		if(line.indexOf(query) != -1) {			// if query exists in this line
			output.write(line);
		}
		return "";
	}
	
}
