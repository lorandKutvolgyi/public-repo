package com.lory.biblereader.parts.upperrightstack.bookmarkpart.treeprovider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.lory.biblereader.model.TreeElement;
import com.lory.biblereader.parts.upperrightstack.bookmarkpart.BookMarkManager;

public class TreeContentProvider implements ITreeContentProvider {

	private BookMarkManager bookMarkManager;

	public TreeContentProvider(BookMarkManager bookMarkManager) {
		this.bookMarkManager = bookMarkManager;
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((TreeElement) parentElement).getChildren().toArray();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return bookMarkManager.getCategories().keySet().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return ((TreeElement) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return !((TreeElement) element).getChildren().isEmpty();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}