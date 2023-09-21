package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is used by filters to write output and read input.
 * 
 * You should not modify this class.
 * 
 * @author Chami Lamelas and Eitan Joseph
 */
public class Pipe {

	private Queue<String> buffer;

	public Pipe() {
		buffer = new LinkedList<String>();
	}

	/**
	 * Removes the element at the front of the pipe
	 * 
	 * @return the element at the front of the pipe
	 */
	public String read() {
		return buffer.poll();
	}

	/**
	 * Adds an element to the pipe
	 * 
	 * @param data to be added to the pipe
	 */
	public void write(String data) {
		buffer.add(data);
	}

	/**
	 * {@return number of elements in the pipe}
	 */
	public int size() {
		return buffer.size();
	}

	/**
	 * {@return a boolean indicating whether or not the pipe is empty}
	 */
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

}
