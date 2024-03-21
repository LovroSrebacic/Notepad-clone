package editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import iterator.LinesIterator;
import location.Location;
import observer.CursorObserver;


public class TextEditorModel {

	private List<String> lines;
	private Location cursorLocation;
	private List<CursorObserver> cursorObservers;
	
	public TextEditorModel(String text) {
		this.lines = new ArrayList<>(Arrays.asList(text.split("\\n")));
		this.cursorLocation = new Location(0, 0);
		this.cursorObservers = new ArrayList<>();
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
	
	public void setCursorLocation(Location cursorLocation) {
		this.cursorLocation = cursorLocation;
	}

	public Location getCursorLocation() {
		return this.cursorLocation;
	}
	
	public Iterator<String> allLines() {
		return new LinesIterator(lines, 0, lines.size());
	}

	public Iterator<String> linesRange(int start, int end) {
		return new LinesIterator(lines, start, end);
	}
	
	public void addCursorObserver(CursorObserver cursorObserver) {
		if(!this.cursorObservers.contains(cursorObserver)) {
			this.cursorObservers.add(cursorObserver);
		}
	}
	
	public void removeCursorObserver(CursorObserver cursorObserver) {
		this.cursorObservers.remove(cursorObserver);
	}
	
	public void notifyCursorObservers() {
		for (CursorObserver cursorObserver : cursorObservers) {
			cursorObserver.updateCursorLocation(cursorLocation);
		}
	}
	
	public void moveCursorLeft() {
		if(cursorLocation.getX() == 0) {
			if(cursorLocation.getY() != 0) {
				cursorLocation.setLocation(lines.get(cursorLocation.getY() - 1).length(), cursorLocation.getY() - 1);
			}else {
				return;
			}
		}else {
			cursorLocation.updateLocation(-1, 0);
		}
		
		notifyCursorObservers();
	}
	
	public void moveCursorRight() {
		if(cursorLocation.getY() == lines.size() - 1) {
			if(cursorLocation.getX() != lines.get(cursorLocation.getY()).length()) {
				cursorLocation.updateLocation(1, 0);
			}else {
				return;
			}
		}else {
			if(cursorLocation.getX() != lines.get(cursorLocation.getY()).length()) {
				cursorLocation.updateLocation(1, 0);
			}else {
				cursorLocation.setLocation(0, cursorLocation.getY() + 1);
			}
		}
		
		notifyCursorObservers();
	}
	
	public void moveCursorUp() {
		if (cursorLocation.getY() == 0) {
			return;
		} else {
			if (cursorLocation.getX() <= lines.get(cursorLocation.getY() - 1).length()) {
				cursorLocation.setLocation(cursorLocation.getX(), cursorLocation.getY() - 1);
			} else {
				cursorLocation.setLocation(lines.get(cursorLocation.getY() - 1).length(), cursorLocation.getY() - 1);
			}
		}
		notifyCursorObservers();
	}
	
	public void moveCursorDown() {
		if (cursorLocation.getY() == lines.size() - 1) {
			return;
		} else {
			if (cursorLocation.getX() <= lines.get(cursorLocation.getY() + 1).length()) {
				cursorLocation.setLocation(cursorLocation.getX(), cursorLocation.getY() + 1);
			} else {
				cursorLocation.setLocation(lines.get(cursorLocation.getY() + 1).length(), cursorLocation.getY() + 1);
			}
		}
		notifyCursorObservers();
	}
}
