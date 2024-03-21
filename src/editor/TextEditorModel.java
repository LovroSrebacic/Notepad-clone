package editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.tools.javac.Main;

import action.EditAction;
import iterator.LinesIterator;
import location.Location;
import location.LocationRange;
import observer.CursorObserver;
import observer.SelectionObserver;
import observer.TextObserver;


public class TextEditorModel {

	private List<String> lines;
	private Location cursorLocation;
	private LocationRange selectionRange;
	
	private List<CursorObserver> cursorObservers;
	private List<TextObserver> textObservers;
	private List<SelectionObserver> selectionObservers;
	
	private Map<String, EditAction> actions;
	
	public TextEditorModel(String text) {
		this.lines = new ArrayList<>(Arrays.asList(text.split("\\n")));
		this.cursorLocation = new Location(0, 0);
		
		this.cursorObservers = new ArrayList<>();
		this.textObservers = new ArrayList<>();
		this.selectionObservers = new ArrayList<>();
		
		this.actions = new HashMap<>();
		initializeActions("action");
	}
	
	private void initializeActions(String packageName) {
		//So that the Class loader can find package 'action'
		EditAction ea = new EditAction() {
			@Override
			public void executeDo() {
			}
		};
		
		try {
			for(String className : getClassNamesFromPackage(packageName)) {
				Class<?> cls = Class.forName(packageName + "." + className);
				if(EditAction.class.isAssignableFrom(cls) && !className.equals("EditAction")) {
					Constructor<?> constructor = cls.getConstructor(TextEditorModel.class);
					EditAction action = (EditAction) constructor.newInstance(this);
					actions.put(className, action);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String[] getClassNamesFromPackage(String packageName) {
		String packagePath = packageName.replace('.', '/');
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(packagePath)))) {
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> line.substring(0, line.length() - 6)) // Remove .class extension
                    .toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0]; // return empty array in case of an exception
        }
	}
	
	public void executeAction(String actionName) {
		EditAction action = actions.get(actionName);
		if(action != null) {
			action.executeDo();
		}else {
			System.err.println("Action '" + actionName + "' is not found.");
		}
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

	public LocationRange getSelectionRange() {
		return this.selectionRange;
	}
	
	public void setSelectionRange(LocationRange range) {
		this.selectionRange = range;
	}
	
	public boolean hasSelection() {
		return this.selectionRange != null;
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
	
	public void addTextObserver(TextObserver textObserver) {
		if (!this.textObservers.contains(textObserver)) {
			this.textObservers.add(textObserver);
		}
	}

	public void removeTextObserver(TextObserver textObserver) {
		this.textObservers.remove(textObserver);
	}

	public void notifyTextObservers() {
		for (TextObserver textObserver : textObservers) {
			textObserver.updateText();
		}
	}

	public void addSelectionObserver(SelectionObserver selectionObserver) {
		if (!this.selectionObservers.contains(selectionObserver)) {
			this.selectionObservers.add(selectionObserver);
		}
	}

	public void removeSelectionObserver(SelectionObserver selectionObserver) {
		this.selectionObservers.add(selectionObserver);
	}

	public void notifySelectionObservers() {
		for (SelectionObserver selectionObserver : selectionObservers) {
			if (hasSelection()) {
				selectionObserver.hasSelection(true);
			} else {
				selectionObserver.hasSelection(false);
			}
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
