package main.java.action;

import java.util.ArrayList;
import java.util.List;

import main.java.editor.TextEditorModel;
import main.java.location.Location;
import main.java.location.LocationRange;

public class DeleteBeforeAction implements EditAction{

	private TextEditorModel model;
	private Location previousCursorLocation;
	private List<String> previousLines;
	private LocationRange previousRange;

	public DeleteBeforeAction(TextEditorModel model) {
		this.model = model;
		previousLines = new ArrayList<String>(model.getLines());
		previousCursorLocation = new Location(model.getCursorLocation());

		if (model.hasSelection()) {
			previousRange = model.getSelectionRange();
		}
	}

	@Override
	public void executeDo() {
		List<String> newLines = model.getLines();
		Location newCursorLocation = model.getCursorLocation();
		
		List<String> linesCopy = new ArrayList<>(newLines);
		Location cursorLocationCopy = new Location(newCursorLocation);
		
		if (model.hasSelection()) {
			model.executeAction(new DeleteRangeAction(model), "DeleteRangeAction");
		}

		if (newCursorLocation.getX() == 0) {
			if (newCursorLocation.getY() != 0) {
				int previousLength = newLines.get(newCursorLocation.getY() - 1).length();
				newLines.set(newCursorLocation.getY() - 1,
						newLines.get(newCursorLocation.getY() - 1) + newLines.get(newCursorLocation.getY()));
				newLines.remove(newCursorLocation.getY());
				newCursorLocation.setLocation(previousLength, newCursorLocation.getY() - 1);
			} else {
				return;
			}
		} else {
			String newLine = newLines.get(newCursorLocation.getY()).substring(0, newCursorLocation.getX() - 1)
					+ newLines.get(newCursorLocation.getY()).substring(newCursorLocation.getX());
			newLines.set(newCursorLocation.getY(), newLine);
			newCursorLocation.setLocation(newCursorLocation.getX() - 1, newCursorLocation.getY());
		}
		
		previousLines = new ArrayList<>(linesCopy);
		previousCursorLocation = cursorLocationCopy;
		
		model.notifyCursorObservers();
		model.notifyTextObservers();
		model.notifyStatusBarObservers();
	}

	@Override
	public void executeUndo() {
		model.setLines(previousLines);
		model.setCursorLocation(previousCursorLocation);
		model.setSelectionRange(previousRange);
		model.notifyCursorObservers();
		model.notifyTextObservers();
		model.notifySelectionObservers();
		model.notifyStatusBarObservers();
	}
}
