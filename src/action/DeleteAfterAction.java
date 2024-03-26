package action;

import java.util.List;

import editor.TextEditorModel;
import location.Location;

public class DeleteAfterAction implements EditAction{
	
	private TextEditorModel model;

	public DeleteAfterAction(TextEditorModel model) {
		this.model = model;
	}

	@Override
	public void executeDo() {
		List<String> newLines = model.getLines();
		Location newCursorLocation = model.getCursorLocation();

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
		model.notifyTextObservers();
	}

	@Override
	public void executeUndo() {
		// TODO Auto-generated method stub
		
	}
}
