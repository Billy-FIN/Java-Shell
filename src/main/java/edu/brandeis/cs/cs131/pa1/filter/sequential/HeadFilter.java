package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
* This class provides the implementation of a head filter.
* It can return up to the first 10 lines from piped input.
* A head filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class HeadFilter extends SequentialFilter {

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
	 * Constructs a new head filter
	 * 
	 */
	public HeadFilter() {
		output = new Pipe();
	}
	
	/**
	 * Constructs a new head filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 */
	public HeadFilter(Boolean flag, String redirectFileName) {
		output = new Pipe();
		this.flag = flag;
		this.redirectFileName = redirectFileName;
	}
	
	/**
	 * Processes the input pipe and returns up the first 10 lines to the output pipe
	 */
	public void process() {
		// must have input
		if(input == null) {
			throw new IllegalArgumentException("input" + "head");
		}
		int count = 0;
		while (!input.isEmpty() && count < 10) {
			String line = input.read();
			output.write(line);
			count++;
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
	 * Does nothing. Implemented here due to abstraction of parent class
	 * 
	 * @param line String not used
	 * @return ""
	 */
	public String processLine(String line) {
		return "";
	}
}
