package file;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import editor.TextEditor;
import editor.TextEditorModel;

public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private TextEditor editor;
	private TextEditorModel model;

	public SaveAction(TextEditor editor, TextEditorModel model) {
		this.editor = editor;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Path openedFile = this.editor.getOpenedFile();

		if (openedFile == null) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save file");
			if (fc.showSaveDialog(editor) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(editor, "File is not saved!", "WARNING", JOptionPane.WARNING_MESSAGE);
				return;
			}
			openedFile = fc.getSelectedFile().toPath();
		}

		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = model.allLines();
		while (iterator.hasNext()) {
			sb.append(iterator.next()).append("\n");
		}

		byte[] text = sb.toString().getBytes(StandardCharsets.UTF_8);

		try {
			Files.write(openedFile, text);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(editor,
					"While writing to file " + openedFile.toFile().getName() + ":" + e1.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(editor, "File saved", "Info", JOptionPane.INFORMATION_MESSAGE);

	}

}
