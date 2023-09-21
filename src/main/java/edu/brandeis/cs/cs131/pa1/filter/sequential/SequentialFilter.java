package edu.brandeis.cs.cs131.pa1.filter.sequential;

import edu.brandeis.cs.cs131.pa1.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic
 * functionality of all filters. Each filter should extend this class and
 * implement functionality that is specific for this filter.
 * 
 * You should not modify this class.
 * 
 * @author cs131a
 *
 */
public abstract class SequentialFilter extends Filter {
	/**
	 * The input pipe for this filter
	 */
	protected Pipe input;
	/**
	 * The output pipe for this filter
	 */
	protected Pipe output;

	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof SequentialFilter) {
			SequentialFilter sequentialNext = (SequentialFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null) {
				this.output = new Pipe();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}

	/**
	 * Processes the input pipe and passes the result to the output pipe
	 */
	public void process() {
		while (!input.isEmpty()) {
			String line = input.read();
			String processedLine = processLine(line);
			if (processedLine != null) {
				output.write(processedLine);
			}
		}
	}

	/**
	 * Called by the {@link #process()} method for every encountered line in the
	 * input queue. It then performs the processing specific for each filter and
	 * returns the result. Each filter inheriting from this class must implement its
	 * own version of processLine() to take care of the filter-specific processing.
	 * 
	 * @param line the line got from the input queue
	 * @return the line after the filter-specific processing
	 */
	protected abstract String processLine(String line);

}
