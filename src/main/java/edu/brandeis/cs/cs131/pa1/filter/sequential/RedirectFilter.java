package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;

/**
* This class provides the implementation of a redirect filter.
* It can redirects the output of a command to be stored in <file>.
* A redirect filter must have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class RedirectFilter extends SequentialFilter {
	
	/**
	 * Used for redirect filter to redirect the output to this <file>
	 */
	private String fileName;
	
	/**
	 * Constructs a new redirect filter that needs to be redirected
	 * 
	 * @param fileName	String	the <file> that the redirect filter writes
	 *                          its output	
	 */
	public RedirectFilter(String fileName) {
		// must have a parameter
		if(fileName == null) {
			throw new StringIndexOutOfBoundsException(">");
		}
		this.fileName = fileName;
	}
	
	/**
	 * Processes the input pipe and passes the content to the output method
	 */
	public void process() {
		while (!input.isEmpty()) {
			String line = input.read();
			try {
				outPrint(line);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Redirects the content to <file>
	 * 
	 * @param Stream String content needs to be redirected to <file>
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void outPrint(String stream) throws UnsupportedEncodingException, IOException {
		// uses absolute path
		// if the target file is not existed, it will create one
		// if the target file is existed, it will not overwrite it
		OutputStream out = new FileOutputStream(CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + fileName, true);
		stream += "\n";
		// output to the file
		out.write(stream.getBytes("UTF-8")); 
		out.close();
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
