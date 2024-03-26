package main.java.undo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import main.java.editor.TextEditorPanel;
import main.java.observer.ActionsStackObserver;

public class UndoAction extends AbstractAction implements ActionsStackObserver{

	private static final long serialVersionUID = 1L;
	
	private UndoManager undoManager;
	private TextEditorPanel panel;
	
	public UndoAction(TextEditorPanel panel) {
		this.undoManager = UndoManager.getInstance();
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		undoManager.undo();
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
