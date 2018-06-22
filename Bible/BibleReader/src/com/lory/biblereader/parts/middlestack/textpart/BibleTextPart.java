package com.lory.biblereader.parts.middlestack.textpart;

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.lory.biblereader.i18n.MessageService;
import com.lory.biblereader.i18n.Messages;
import com.lory.biblereader.menu.TranslationManager;
import com.lory.biblereader.model.Chapter;
import com.lory.biblereader.model.ChapterContext;
import com.lory.biblereader.parts.leftstack.bookspart.eventhandler.BooksKeyListener;
import com.lory.biblereader.parts.middlestack.textpart.eventhandler.SearchTextVerifyListener;
import com.lory.biblereader.parts.middlestack.textpart.eventhandler.TextSearchListener;

public class BibleTextPart implements Observer {

	@Inject
	private BooksKeyListener booksKeyListener;
	@Inject
	private TextSearchListener textSearchListener;
	@Inject
	private SearchTextVerifyListener searchTextVerifyListener;
	@Inject
	private TextPartManager textPartManager;
	@Inject
	private MessageService messageService;
	@Inject
	private TranslationManager translationManager;
	@Inject
	@Translation
	private Messages messages;

	private static EMenuService menuService;
	private MPart part;
	private Composite parent;
	private StyledText text;
	private Label translationLabel;

	@PostConstruct
	public void postConstruct(Composite parent, MPart part, EMenuService menuService) {
		if (BibleTextPart.menuService == null) {
			BibleTextPart.menuService = menuService;
		}
		initPart(part);
		initParent(parent);
		createTranslationLabel(parent);
		createSearchText(parent);
		createText(parent);
		registerPart(part);
		registerMenu();
	}

	private void registerMenu() {
		BibleTextPart.menuService.registerContextMenu(text, "reader.popupmenu.textpart.context");
	}

	private void initPart(MPart part) {
		this.part = part;
	}

	private void initParent(Composite parent) {
		this.parent = parent;
		parent.setLayout(new GridLayout(1, true));
	}

	private void createTranslationLabel(Composite parent) {
		translationLabel = new Label(parent, SWT.NONE);
		translationLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	}

	private Text createSearchText(Composite parent) {
		Text searchtext = new Text(parent, SWT.SEARCH | SWT.ICON_CANCEL);
		searchtext.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		searchtext.addModifyListener(textSearchListener);
		searchtext.setMessage(messageService.getMessage("searchTextPlaceholder"));
		searchtext.addVerifyListener(searchTextVerifyListener);
		return searchtext;
	}

	private void createText(Composite parent) {
		text = new StyledText(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setEditable(false);
		text.addKeyListener(booksKeyListener);
		textSearchListener.setBibleText(text);
	}

	private void registerPart(MPart part) {
		ChapterContext chapter = null;
		if (part.getPersistedState().get("title") != null) {
			chapter = new ChapterContext(part.getPersistedState().get("id"), part.getPersistedState().get("title"),
					part.getPersistedState().get("translation"));
		}
		textPartManager.registerPart(part, this, chapter);
		showTranslation(chapter);
	}

	public void showTranslation(ChapterContext chapter) {
		if (chapter == null) {
			if (!translationManager.getActiveTranslationDescription().isEmpty()) {
				translationLabel.setText(translationManager.getActiveTranslationDescription());
			}
		} else {
			translationLabel.setText(translationManager.getTranslationDescription(chapter.getTranslation()));
		}
	}

	public void showTranslation(Chapter chapter) {
		ChapterContext chapterContext = new ChapterContext(String.valueOf(chapter.getId()),
				chapter.getBook().getTitle(), chapter.getTranslation());
		showTranslation(chapterContext);
	}

	@Focus
	public void partActivated() {
		if (!part.equals(textPartManager.getActivePart())) {
			textPartManager.activatePart(part);
		}
	}

	@PersistState
	private void persist(MPart part) {
		Chapter chapter = textPartManager.getChapters().get(part);
		if (chapter != null) {
			part.getPersistedState().put("title", chapter.getBook().getTitle());
			part.getPersistedState().put("id", String.valueOf(chapter.getId()));
			part.getPersistedState().put("translation", chapter.getTranslation());
		}
	}

	@PreDestroy
	public void preDestroy() {
		textPartManager.removePart(part);
		if (textPartManager.isAnyVisiblePartExcept(part)) {
			textPartManager.activatePart(textPartManager.getAnyVisiblePartExcept(part));
		}
	}

	public void setContent(String text) {
		if (!this.text.isDisposed()) {
			this.text.setText(text);
		}
	}

	@Override
	public void update(Observable observable, Object arg) {
		textPartManager.loadCurrentChapter(part);
	}

	public void refreshTitle(String bookTitle, int chapterId) {
		part.setLabel(messageService.getMessage(bookTitle) + " " + chapterId);
	}

	@Override
	public String toString() {
		return "BibleTextPart: " + (part == null || part.getLabel() == null ? "null" : part.getLabel());
	}

	public boolean isDisposed() {
		return parent.isDisposed();
	}

}