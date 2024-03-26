package main.java.clipboard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import main.java.action.EditAction;
import main.java.editor.TextEditorModel;
import main.java.observer.ClipboardObserver;

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
		EditAction action = this.model.insert(this.stack.removeText());
		this.model.executeAction(action, "InsertTextAction");
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
