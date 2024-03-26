package editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import action.EditAction;
import action.InsertTextAction;
import iterator.LinesIterator;
import location.Location;
import location.LocationRange;
import observer.CursorObserver;
import observer.SelectionObserver;
import observer.TextObserver;
import undo.UndoManager;

public class TextEditorModel {

	private List<String> lines;
	private Location cursorLocation;
	private LocationRange selectionRange;

	private List<CursorObserver> cursorObservers;
	private List<TextObserver> textObservers;
	private List<SelectionObserver> selectionObservers;

	
	private UndoManager undoManager;

	public TextEditorModel(String text) {
		this.lines = new ArrayList<>(Arrays.asList(text.split("\\n")));
		this.cursorLocation = new Location(0, 0);

		this.cursorObservers = new ArrayList<>();
		this.textObservers = new ArrayList<>();
		this.selectionObservers = new ArrayList<>();
		
		this.undoManager = UndoManager.getInstance();
	}

	public void executeAction(EditAction action, String actionName) {
		if (action != null) {
			action.executeDo();
			undoManager.push(action);
		} else {
			System.err.println("Action '" + actionName + "' is not found.");
		}
	}
	
	public void undoAction(EditAction action, String actionName) {
		if (action != null) {
			action.executeUndo();
		} else {
			System.err.println("Action '" + actionName + "' is not found.");
		}
	}

	public List<String> getLines() {
		return this.lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public String getLine(int index) {
		return this.lines.get(index);
	}

	public void updateLine(int index, String line) {
		this.lines.set(index, line);
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
		if (!this.cursorObservers.contains(cursorObserver)) {
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

	public void moveCursorLeft(boolean shift) {
		if (!shift && this.selectionRange != null) {
			this.selectionRange = null;
		} else {
			if (cursorLocation.getX() == 0) {
				if (cursorLocation.getY() != 0) {
					cursorLocation.setLocation(lines.get(cursorLocation.getY() - 1).length(),
							cursorLocation.getY() - 1);
				} else {
					return;
				}
			} else {
				cursorLocation.updateLocation(-1, 0);
			}
		}
		notifyCursorObservers();
		notifySelectionObservers();
	}

	public void moveCursorRight(boolean shift) {
		if (!shift && this.selectionRange != null) {
			this.selectionRange = null;
		} else {
			if (cursorLocation.getY() == lines.size() - 1) {
				if (cursorLocation.getX() != lines.get(cursorLocation.getY()).length()) {
					cursorLocation.updateLocation(1, 0);
				} else {
					return;
				}
			} else {
				if (cursorLocation.getX() != lines.get(cursorLocation.getY()).length()) {
					cursorLocation.updateLocation(1, 0);
				} else {
					cursorLocation.setLocation(0, cursorLocation.getY() + 1);
				}
			}
		}

		notifyCursorObservers();
		notifySelectionObservers();
	}

	public void moveCursorUp(boolean shift) {
		if (!shift) {
			this.selectionRange = null;
		}

		if (cursorLocation.getY() == 0) {
			cursorLocation.setLocation(0, 0);
			;
		} else {
			if (cursorLocation.getX() <= lines.get(cursorLocation.getY() - 1).length()) {
				cursorLocation.setLocation(cursorLocation.getX(), cursorLocation.getY() - 1);
			} else {
				cursorLocation.setLocation(lines.get(cursorLocation.getY() - 1).length(), cursorLocation.getY() - 1);
			}
		}

		notifyCursorObservers();
		notifySelectionObservers();
	}

	public void moveCursorDown(boolean shift) {
		if (!shift) {
			this.selectionRange = null;
		}

		if (cursorLocation.getY() == lines.size() - 1) {
			cursorLocation.setLocation(lines.get(cursorLocation.getY()).length(), lines.size() - 1);
		} else {
			if (cursorLocation.getX() <= lines.get(cursorLocation.getY() + 1).length()) {
				cursorLocation.setLocation(cursorLocation.getX(), cursorLocation.getY() + 1);
			} else {
				cursorLocation.setLocation(lines.get(cursorLocation.getY() + 1).length(), cursorLocation.getY() + 1);
			}
		}

		notifyCursorObservers();
		notifySelectionObservers();
	}

	public void addSelectionLeft() {
		if (this.selectionRange == null) {
			this.selectionRange = new LocationRange();
			this.selectionRange.updateStart(this.cursorLocation.getX(), this.cursorLocation.getY());
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		if (this.selectionRange.getEnd().getY() == 0 && this.selectionRange.getEnd().getX() == 0) {
			return;
		}

		moveCursorLeft(true);

		if (this.selectionRange != null) {
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		notifyTextObservers();
		notifySelectionObservers();
	}

	public void addSelectionRight() {
		if (this.selectionRange == null) {
			this.selectionRange = new LocationRange();
			this.selectionRange.updateStart(this.cursorLocation.getX(), this.cursorLocation.getY());
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		if (this.selectionRange.getEnd().getY() == this.lines.size() - 1
				&& this.selectionRange.getEnd().getX() == this.lines.get(this.lines.size() - 1).length()) {
			return;
		}

		moveCursorRight(true);

		if (this.selectionRange != null) {
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		notifyTextObservers();
		notifySelectionObservers();
	}

	public void addSelectionUp() {
		if (this.selectionRange == null) {
			this.selectionRange = new LocationRange();
			this.selectionRange.updateStart(this.cursorLocation.getX(), this.cursorLocation.getY());
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		if (this.selectionRange.getEnd().getY() == 0) {
			return;
		}

		moveCursorUp(true);

		if (this.selectionRange != null) {
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		notifyTextObservers();
		notifySelectionObservers();
	}

	public void addSelectionDown() {
		if (this.selectionRange == null) {
			this.selectionRange = new LocationRange();
			this.selectionRange.updateStart(this.cursorLocation.getX(), this.cursorLocation.getY());
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		if (this.selectionRange.getEnd().getY() == this.lines.size() - 1) {
			return;
		}

		moveCursorDown(true);

		if (this.selectionRange != null) {
			this.selectionRange.updateEnd(this.cursorLocation.getX(), this.cursorLocation.getY());
		}

		notifyTextObservers();
		notifySelectionObservers();
	}

	public String getSelectedText() {
		Location start = null;
		Location end = null;

		if (this.selectionRange.getEnd().getY() > this.selectionRange.getStart().getY()) {
			start = this.selectionRange.getStart();
			end = this.selectionRange.getEnd();
		} else if (this.selectionRange.getEnd().getY() == this.selectionRange.getStart().getY()) {
			if (this.selectionRange.getEnd().getX() > this.selectionRange.getStart().getX()) {
				start = this.selectionRange.getStart();
				end = this.selectionRange.getEnd();
			} else {
				start = this.selectionRange.getEnd();
				end = this.selectionRange.getStart();
			}
		} else {
			start = this.selectionRange.getEnd();
			end = this.selectionRange.getStart();
		}

		if (this.selectionRange != null) {
			if (end.getY() - start.getY() == 0) {
				return this.lines.get(start.getY()).substring(start.getX(), end.getX());
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(lines.get(start.getY()).substring(start.getX()));
				sb.append("\n");
				for (int i = start.getY() + 1; i < end.getY(); i++) {
					sb.append(lines.get(i));
					sb.append("\n");
				}

				sb.append(lines.get(end.getY()).substring(0, end.getX()));

				return sb.toString();
			}
		} else {
			return "";
		}
	}

	public EditAction insert(char c) {
		EditAction insertTextAction = new InsertTextAction(this);
		((InsertTextAction) insertTextAction).setChar(c);
		return insertTextAction;
	}

	public EditAction insert(String text) {
		EditAction insertTextAction = new InsertTextAction(this);
		((InsertTextAction) insertTextAction).setText(text);
		return insertTextAction;
	}
}