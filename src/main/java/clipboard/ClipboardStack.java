package main.java.clipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.java.observer.ClipboardObserver;

public class ClipboardStack {

	private Stack<String> texts;
	private List<ClipboardObserver> clipboardObservers;
	
	public ClipboardStack() {
		this.texts = new Stack<>();
		this.clipboardObservers = new ArrayList<>();
	}
	
	public void addClipboardObserver(ClipboardObserver clipboardObserver) {
		if(!this.clipboardObservers.contains(clipboardObserver)) {
			this.clipboardObservers.add(clipboardObserver);
		}
	}
	
	public void removeClipboardObserver(ClipboardObserver clipboardObserver) {
		this.clipboardObservers.remove(clipboardObserver);
	}
	
	private void notifyClipboardObservers() {
		for(ClipboardObserver clipboardObserver : clipboardObservers) {
			clipboardObserver.updateClipboard();
		}
	}
	
	public boolean hasText() {
		return !this.texts.isEmpty();
	}
	
	public String getText() {
		return this.texts.peek();
	}
	
	public String removeText() {
		String text = this.texts.pop();
		notifyClipboardObservers();
		return text;
	}
	
	public void putText(String text) {
		this.texts.push(text);
		notifyClipboardObservers();
	}
}
