package com.lory.biblereader.toolbar.eventhandler;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.SWT;

import com.lory.biblereader.model.Book;
import com.lory.biblereader.model.Chapter;
import com.lory.biblereader.model.CurrentChapter;
import com.lory.biblereader.parts.leftstack.bookspart.treesorter.AbstractBooksOrder;
import com.lory.biblereader.parts.leftstack.bookspart.treesorter.BooksComparator;
import com.lory.biblereader.parts.upperrightstack.historypart.History;

@Creatable
@Singleton
public class BookChanging {

	@Inject
	private CurrentChapter currentChapter;

	public void change(int keyCode, BooksComparator booksComparator, History history) {
		if (!isSelectedChapter()) {
			return;
		}
		AbstractBooksOrder comparator = booksComparator.current();
		Book book = currentChapter.getChapter().getBook();
		int index = comparator.getBooks().indexOf(book);
		Chapter chapter = getBook(comparator, index, keyCode).getChapter(1);
		currentChapter.setChapter(chapter);
		history.addChapter(chapter);
	}

	private Book getBook(AbstractBooksOrder comparator, int index, int keyCode) {
		int newIndex;
		if (keyCode == SWT.ARROW_LEFT) {
			newIndex = index == 0 ? comparator.getBooks().size() - 1 : index - 1;
		} else {
			assert keyCode == SWT.ARROW_RIGHT;
			newIndex = index == comparator.getBooks().size() - 1 ? 0 : index + 1;
		}
		return comparator.getBooks().get(newIndex);
	}

	private boolean isSelectedChapter() {
		return currentChapter.getChapter() != null;
	}
}
