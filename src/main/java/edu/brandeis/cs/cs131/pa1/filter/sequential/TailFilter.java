package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.util.LinkedList;
import java.util.Queue;

/**
* This class provides the implementation of a tail filter.
* It can return up to the last 10 lines from piped input.
* A tail filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class TailFilter extends SequentialFilter {
	
	/**
	 * Keeps 10 lines from piped input in order to always have
	 * track of the last 10 lines
	 */
	private Queue<String> lastTenLines = new LinkedList<String>();
	
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
	 * Constructs a new tail filter
	 * 
	 */
	public TailFilter() {
		output = new Pipe();
	}
	
	/**
	 * Constructs a new tail filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 */
	public TailFilter(Boolean flag, String redirectFileName) {
		output = new Pipe();
		this.flag = flag;
		this.redirectFileName = redirectFileName;
	}
	
	/**
	 * Processes the input pipe and returns up the last 10 lines to the output pipe
	 */
	public void process() {
		// must have input
		if(input == null) {
			throw new IllegalArgumentException("input" + "tail");
		}
		while (!input.isEmpty()) {
			String line = input.read();
			lastTenLines.add(line);
			// maintains the size of queue as 10
			if(lastTenLines.size() > 10) {
				lastTenLines.poll();
			}
		}
		for(int i = 1; i <= 10; i++) {
			if(lastTenLines.peek() != null) {
				// passes the last lines to the output
				output.write(lastTenLines.poll());								
			}
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
