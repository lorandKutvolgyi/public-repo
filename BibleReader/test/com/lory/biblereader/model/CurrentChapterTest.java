package com.lory.biblereader.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * Unit test for {@link CurrentChapter}.
 *
 * @author lorandKutvolgyi
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Display.class })
public class CurrentChapterTest {
    @Mock
    private CurrentChapter underTest;
    @Mock
    private Chapter chapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(CurrentChapter.class, "instance", underTest);
        CurrentChapter.setCurrentChapter(chapter);
    }

    @Test
    public void testSetCurrentChapterWhenChapterIsSetShouldNotifyObservers() {
        verify((Observable) underTest, VerificationModeFactory.times(1)).notifyObservers();
    }

    @Test
    public void testGetCurrentChapterWhenChapterIsSetShouldReturnThatChapter() {
        assertEquals(chapter, CurrentChapter.getCurrentChapter());
    }
}
