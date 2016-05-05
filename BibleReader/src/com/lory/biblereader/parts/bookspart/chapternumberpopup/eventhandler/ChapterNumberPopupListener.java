package com.lory.biblereader.parts.bookspart.chapternumberpopup.eventhandler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import com.lory.biblereader.parts.bookspart.chapternumberpopup.ChapterNumberPopupShell;

/**
 * Event handler for KeyEvents on the ChapterNumberPopupShell.
 *
 * @author lorandKutvolgyi
 *
 */
public class ChapterNumberPopupListener extends KeyAdapter {
    private static final int CACHE_CLEARING_DELAY = 1000;
    private static final int POPUP_CLOSING_DELAY = 1000;
    private static final int MAXIMUM_CACHE_SIZE = 3;
    private final List<Character> cache = new ArrayList<>(3);
    private final Composite composite;
    private final ChapterNumberPopupShell shell;

    public ChapterNumberPopupListener(Composite composite, ChapterNumberPopupShell shell) {
        this.composite = composite;
        this.shell = shell;
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        if (isEscCharacter(event)) {
            shell.close();
        }
        if (!isNumberCharacter(event)) {
            return;
        }
        if (isStartingWithZero(event)) {
            return;
        }
        if (!isCacheFull()) {
            cache.add((char) event.keyCode);
        }
        if (!composite.isDisposed() && !cache.isEmpty() && isLabelWithCachedNumberExist()) {
            selectNumLabel(concatCachedChars(), composite);
        }
        clearCacheWithDelay();
    }

    private boolean isEscCharacter(final KeyEvent e) {
        return e.keyCode == SWT.ESC;
    }

    private boolean isNumberCharacter(final KeyEvent e) {
        return e.keyCode >= '0' && e.keyCode <= '9';
    }

    private boolean isStartingWithZero(final KeyEvent e) {
        return cache.isEmpty() && e.keyCode == '0';
    }

    private boolean isCacheFull() {
        return cache.size() == MAXIMUM_CACHE_SIZE;
    }

    private boolean isLabelWithCachedNumberExist() {
        return composite.getChildren().length >= Integer.valueOf(concatCachedChars());
    }

    private void clearCacheWithDelay() {
        Display.getCurrent().timerExec(CACHE_CLEARING_DELAY, () -> cache.clear());
    }

    private String concatCachedChars() {
        StringBuilder result = new StringBuilder("");
        for (Character cachedChar : cache) {
            result.append(cachedChar);
        }
        return result.toString();
    }

    private void selectNumLabel(String key, Composite composite) {
        Label label = (Label) composite.getChildren()[Integer.valueOf(key) - 1];
        Event event = new Event();
        event.data = POPUP_CLOSING_DELAY;
        label.notifyListeners(SWT.MouseDown, event);
    }
}
