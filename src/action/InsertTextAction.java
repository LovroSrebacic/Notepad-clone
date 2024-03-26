package action;

import java.util.ArrayList;
import java.util.List;

import editor.TextEditorModel;
import location.Location;
import location.LocationRange;

public class InsertTextAction implements EditAction{
	
	private TextEditorModel model;
	private Location previousCursorLocation;
	private List<String> previousLines;
	private LocationRange previousRange;

	private String text;
	private char c;

	public InsertTextAction(TextEditorModel model) {
		this.model = model;
		previousLines = new ArrayList<String>(model.getLines());
		previousCursorLocation = new Location(model.getCursorLocation());

		if (model.hasSelection()) {
			previousRange = model.getSelectionRange();
		}
	}
	
	public void setChar(char c) {
		this.c = c;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void executeDo() {
		List<String> lines = model.getLines();
		Location cursorLocation = model.getCursorLocation();
		
		List<String> linesCopy = new ArrayList<>(lines);
		Location cursorLocationCopy = new Location(cursorLocation);
		
		if (model.hasSelection()) {
			model.executeAction(new DeleteRangeAction(model), "DeleteRangeAction");
		}

		if (text == null) {
			if (c == 10) {
				List<String> newLines = new ArrayList<>();
				for (int i = 0; i < lines.size(); i++) {
					if (cursorLocation.getY() == i) {
						newLines.add(lines.get(i).substring(0, cursorLocation.getX()));
						newLines.add(lines.get(i).substring(cursorLocation.getX()));
					} else {
						newLines.add(lines.get(i));
					}
				}

				cursorLocation.setLocation(0, previousCursorLocation.getY() + 1);
				model.setLines(newLines);
			} else {
				String newLine = lines.get(cursorLocation.getY()).substring(0, cursorLocation.getX());
				newLine += c;
				newLine += lines.get(cursorLocation.getY()).substring(cursorLocation.getX());
				model.updateLine(cursorLocation.getY(), newLine);
				cursorLocation.updateLocation(1, 0);
			}
		} else {
			String[] newLine = text.split("\\n");
			String left = lines.get(cursorLocation.getY()).substring(0, cursorLocation.getX());
			String right = lines.get(cursorLocation.getY()).substring(cursorLocation.getX());

			if (newLine.length == 1) {
				model.updateLine(cursorLocation.getY(), left + newLine[0] + right);
				cursorLocation.setLocation(left.length() + newLine[0].length(), cursorLocation.getY());
			} else {
				model.updateLine(cursorLocation.getY(), left + newLine[0]);
				ArrayList<String> newLines = new ArrayList<>();
				for (int i = 0; i < lines.size(); i++) {
					if (i == cursorLocation.getY()) {
						newLines.add(lines.get(i));
						for (int j = 1; j < newLine.length - 1; j++) {
							newLines.add(newLine[j]);
						}
						newLines.add(newLine[newLine.length - 1] + right);
					} else {
						newLines.add(lines.get(i));
					}
				}

				model.setCursorLocation(new Location(newLine[newLine.length - 1].length(),
						cursorLocation.getY() + newLine.length - 1));
				model.setLines(newLines);
			}
		}
		
		previousLines = new ArrayList<>(linesCopy);
		previousCursorLocation = cursorLocationCopy;

		model.notifySelectionObservers();
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
