package main.java.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LinesIterator implements Iterator<String>{

	private List<String> lines;
	private int currentIndex;
	private int endIndex;
	
	public LinesIterator(List<String> lines) {
		this.lines = lines;
		this.currentIndex = 0;
		this.endIndex = 0;
	}
	
	public LinesIterator(List<String> lines, int startIndex, int endIndex) {
		if(endIndex > lines.size()) {
			throw new IndexOutOfBoundsException("End index is greater than number of lines. End index: " + endIndex + " Number of lines: " + lines.size());
		}
		if(endIndex < startIndex) {
			throw new IndexOutOfBoundsException("End index is greater than start index. End index: " + endIndex + " Start index: " + startIndex);
		}
		
		this.lines = lines;
		this.currentIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	@Override
	public boolean hasNext() {
		if(currentIndex < endIndex && lines.get(currentIndex) != null) {
			return true;
		}
		return false;
	}

	@Override
	public String next() {
		if(hasNext()) {
			return lines.get(currentIndex++);
		}
		throw new NoSuchElementException("There are no more lines.");
	}

}
