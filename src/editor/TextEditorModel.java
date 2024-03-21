package editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
}
