package action;

import java.util.ArrayList;
import java.util.List;

import editor.TextEditorModel;
import location.Location;
import location.LocationRange;

public class DeleteRangeAction implements EditAction{
	private TextEditorModel model;
	private ArrayList<String> previousLines;
	private Location previousCursorLocation;
	private LocationRange previousRange;
	
	public DeleteRangeAction(TextEditorModel model) {
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
		LocationRange newRange = model.getSelectionRange();
		
		List<String> linesCopy = new ArrayList<>(newLines);
		Location cursorLocationCopy = new Location(newCursorLocation);
		LocationRange rangeCopy = new LocationRange(newRange);
		
		int numberOfLines = newRange.getNumberOfLines();
		Location start = null;
		Location end = null;
		
		if (newRange.getEnd().getY() > newRange.getStart().getY()) {
			start = newRange.getStart();
			end = newRange.getEnd();
		}else if(newRange.getEnd().getY() == newRange.getStart().getY()) {
			if(newRange.getEnd().getX() > newRange.getStart().getX()) {
				start = newRange.getStart();
				end = newRange.getEnd();
			}else {
				end = newRange.getStart();
				start = newRange.getEnd();
			}
		}else {
			end = newRange.getStart();
			start = newRange.getEnd();
		}
		
		if(newCursorLocation.equals(end)) {
			newCursorLocation.setLocation(start.getX(), start.getY());
		}
		
		if(numberOfLines == 0) {
			String newLine = newLines.get(start.getY()).substring(0, start.getX()) + newLines.get(start.getY()).substring(end.getX());
			newLines.set(start.getY(), newLine);
		}else {
			String newLine = newLines.get(start.getY()).substring(0, start.getX()) + newLines.get(end.getY()).substring(end.getX());
			boolean newLinePut = false;
			List<String> reconstructedFile = new ArrayList<>();
			for(int i = 0; i < newLines.size(); i++) {
				if(i >= start.getY() && i <= end.getY()) {
					if(!newLinePut) {
						reconstructedFile.add(newLine);
					}
					newLinePut = true;
				}else {
					reconstructedFile.add(newLines.get(i));
				}
			}
			
			newLines = reconstructedFile;
		}
		
		model.setLines(newLines);
		model.setSelectionRange(null);
		
		previousLines = new ArrayList<>(linesCopy);
		previousCursorLocation = cursorLocationCopy;
		previousRange = rangeCopy;
		
		model.notifySelectionObservers();
		model.notifyCursorObservers();
		model.notifyTextObservers();
	}

	@Override
	public void executeUndo() {
		model.setLines(previousLines);
		model.setCursorLocation(previousCursorLocation);
		model.setSelectionRange(previousRange);
		model.notifyCursorObservers();
		model.notifyTextObservers();
		model.notifySelectionObservers();
	}

}
