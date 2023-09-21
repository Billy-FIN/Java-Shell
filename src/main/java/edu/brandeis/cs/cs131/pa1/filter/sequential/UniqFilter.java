package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
* This class provides the implementation of a uniq filter.
* It can Removes any line from the piped input that is the
* same as the previous line. A uniq filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class UniqFilter extends SequentialFilter {
	
	/**
	 * Temporally stores the content of previous line 
	 */
	private String prevLine;
	
	/**
	 * Temporally stores the content of current line 
	 */
	private String currentLine;
	
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
	 * Constructs a new uniq filter
	 * 
	 */
	public UniqFilter() {
		output = new Pipe();
	}
	
	/**
	 * Constructs a new uniq filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 */
	public UniqFilter(boolean flag, String redirectFileName) {
		output = new Pipe();
		this.flag = flag;
		this.redirectFileName = redirectFileName;
	}
	
	/**
	 * Processes the input pipe and passes the counting result to the output pipe
	 * 
	 */
	public void process() {
		// requires pipe input
		if(input == null) {
			throw new IllegalArgumentException("input" + "uniq");
		}
		while (!input.isEmpty()) {
			currentLine = input.read();
			// if the current line is not the first line
			if(prevLine != null) {
				// if the content of current line is different from previous line
				if(currentLine.compareTo(prevLine) != 0) {
					output.write(prevLine);
					prevLine = currentLine;		// update these two instances to check next line
				}
			} else {
				prevLine = currentLine;
			}
		}	
		// write the last line into the output
		output.write(currentLine);
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(redirectFileName);
			this.setNextFilter(redirect);
			redirect.process();
			output = null;
		}
	}
	
	/**
	 * Does nothing. Implemented here due to abstraction of parent class
	 * 
	 * @param line String
	 * @return ""
	 */
	public String processLine(String line) {
		return "";
	}
}
