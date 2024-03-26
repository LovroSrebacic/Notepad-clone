package undo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import editor.TextEditorPanel;
import observer.ActionsStackObserver;

public class RedoAction extends AbstractAction implements ActionsStackObserver {
	private static final long serialVersionUID = 1L;

	private UndoManager undoManager;
	private TextEditorPanel panel;

	public RedoAction(TextEditorPanel panel) {
		this.undoManager = UndoManager.getInstance();
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		undoManager.redo();
		this.panel.revalidate();
		this.panel.repaint();
	}

	@Override
	public void stackEmpty() {
		this.setEnabled(false);
	}

	@Override
	public void stackHasElements() {
		this.setEnabled(true);
	}
}
