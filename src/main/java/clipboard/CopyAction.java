package main.java.clipboard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import main.java.editor.TextEditorModel;
import main.java.observer.SelectionObserver;

public class CopyAction extends AbstractAction implements SelectionObserver {

	private static final long serialVersionUID = 1L;

	private ClipboardStack stack;
	private TextEditorModel model;
	
	public CopyAction(ClipboardStack stack, TextEditorModel model) {
		this.stack = stack;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.stack.putText(this.model.getSelectedText());
	}

	@Override
	public void hasSelection(boolean selection) {
		if (selection) {
			this.setEnabled(true);
		} else {
			this.setEnabled(false);
		}
	}

}
