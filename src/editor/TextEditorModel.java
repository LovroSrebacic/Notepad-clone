package editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import iterator.LinesIterator;


public class TextEditorModel {

	private List<String> lines;
	
	public TextEditorModel(String text) {
		this.lines = new ArrayList<>(Arrays.asList(text.split("\\n")));
	}
	
	public List<String> getLines() {
		return this.lines;
	}
	
	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}
	
	public String getLine(int index) {
		return this.lines.get(index);
	}
	
	public Iterator<String> allLines() {
		return new LinesIterator(lines, 0, lines.size());
	}

	public Iterator<String> linesRange(int start, int end) {
		return new LinesIterator(lines, start, end);
	}
}
