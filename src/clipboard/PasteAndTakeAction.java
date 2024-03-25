package clipboard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import editor.TextEditorModel;
import observer.ClipboardObserver;

public class PasteAndTakeAction extends AbstractAction implements ClipboardObserver {

	private static final long serialVersionUID = 1L;

	private ClipboardStack stack;
	private TextEditorModel model;

	public PasteAndTakeAction(ClipboardStack stack, TextEditorModel model) {
		this.stack = stack;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.model.insert(this.stack.removeText());
		this.model.executeAction("InsertTextAction");
	}

	@Override
	public void updateClipboard() {
		if (this.stack.hasText()) {
			this.setEnabled(true);
		} else {
			this.setEnabled(false);
		}
	}

}
