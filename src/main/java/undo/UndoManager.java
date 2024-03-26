package main.java.undo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.java.action.EditAction;
import main.java.observer.ActionsStackObserver;

public class UndoManager {

	private static UndoManager uniqueUndoManager;
	
	private Stack<EditAction> undoStack;
	private Stack<EditAction> redoStack;
	private List<ActionsStackObserver> undoObservers;
	private List<ActionsStackObserver> redoObservers;
	
	private UndoManager() {
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
		this.undoObservers = new ArrayList<>();
		this.redoObservers = new ArrayList<>();
	}
	
	public static UndoManager getInstance() {
		if(uniqueUndoManager == null) {
			uniqueUndoManager = new UndoManager();
		}
		
		return uniqueUndoManager;
	}
	
	public void undo() {
		if(!this.undoStack.isEmpty()) {
			EditAction action = this.undoStack.pop();
			action.executeUndo();
			if(this.undoStack.isEmpty()) {
				notifyUndoStackObservers(true);
			}
			this.redoStack.push(action);
			notifyRedoStackObservers(false);
		}else {
			notifyUndoStackObservers(true);
		}
	}
	
	public void redo() {
		if (!this.redoStack.isEmpty()) {
			EditAction action = this.redoStack.pop();
			action.executeDo();
			if (this.redoStack.isEmpty()) {
				notifyRedoStackObservers(true);
			}
			this.undoStack.push(action);
			notifyUndoStackObservers(false);
		} else {
			notifyRedoStackObservers(true);
		}
	}
	
	public void push(EditAction c) {
		this.redoStack.clear();
		this.undoStack.push(c);
		notifyUndoStackObservers(false);
		notifyRedoStackObservers(true);
	}
	
	public void addUndoStackObserver(ActionsStackObserver observer) {
		if (!undoObservers.contains(observer)) {
			undoObservers.add(observer);
		}
	}

	public void removeUndoStackObserver(ActionsStackObserver observer) {
		undoObservers.remove(observer);
	}

	private void notifyUndoStackObservers(boolean empty) {
		if (empty) {
			for (ActionsStackObserver observer : undoObservers) {
				observer.stackEmpty();
			}
		} else {
			for (ActionsStackObserver observer : undoObservers) {
				observer.stackHasElements();
			}
		}
	}

	public void addRedoStackObserver(ActionsStackObserver observer) {
		if (!redoObservers.contains(observer)) {
			redoObservers.add(observer);
		}
	}

	public void removeRedoStackObserver(ActionsStackObserver observer) {
		redoObservers.remove(observer);
	}

	private void notifyRedoStackObservers(boolean empty) {
		if (empty) {
			for (ActionsStackObserver observer : redoObservers) {
				observer.stackEmpty();
			}
		} else {
			for (ActionsStackObserver observer : redoObservers) {
				observer.stackHasElements();
			}
		}
	}
}
