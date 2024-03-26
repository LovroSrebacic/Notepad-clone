package main.java.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import main.java.editor.TextEditor;

public class OpenAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private TextEditor editor;
	private Path path;

	public OpenAction(TextEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(editor) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fc.getSelectedFile();
		Path path = file.toPath();

		if (!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(editor, "Could not open file " + file.getName(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

		byte[] bytes = null;

		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(editor, "Reading file " + file.getName() + " : " + e1.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.path = path;
		this.editor.openNewFile(new String(bytes, StandardCharsets.UTF_8));
	}

	public Path getPath() {
		return path;
	}
}
