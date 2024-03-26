package clipboard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import action.DeleteRangeAction;
import editor.TextEditorModel;
import editor.TextEditorPanel;
import observer.SelectionObserver;

public class CutAction extends AbstractAction implements SelectionObserver {

	private static final long serialVersionUID = 1L;

	private ClipboardStack stack;
	private TextEditorModel model;
	private TextEditorPanel panel;

	public CutAction(ClipboardStack stack, TextEditorModel model, TextEditorPanel panel) {
		this.stack = stack;
		this.model = model;
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.stack.putText(this.model.getSelectedText());
		this.model.executeAction(new DeleteRangeAction(model), "DeleteRangeAction");
		this.panel.revalidate();
		this.panel.repaint();
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
