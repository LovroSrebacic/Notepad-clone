package action;

import java.util.ArrayList;
import java.util.List;

import editor.TextEditorModel;
import location.Location;
import location.LocationRange;

public class DeleteAfterAction implements EditAction{
	
	private TextEditorModel model;
	private Location previousCursorLocation;
	private List<String> previousLines;
	private LocationRange previousRange;

	public DeleteAfterAction(TextEditorModel model) {
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

		if (newCursorLocation.getX() == newLines.get(newCursorLocation.getY()).length()) {
			if(newLines.size() > 1) {
				newLines.set(newCursorLocation.getY(), newLines.get(newCursorLocation.getY()) + newLines.get(newCursorLocation.getY() + 1));
				newLines.remove(newCursorLocation.getY() + 1);
			}else {
				return;
			}
		} else {
			String newLine = newLines.get(newCursorLocation.getY()).substring(0, newCursorLocation.getX())
					+ newLines.get(newCursorLocation.getY()).substring(newCursorLocation.getX() + 1);
			newLines.set(newCursorLocation.getY(), newLine);
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
