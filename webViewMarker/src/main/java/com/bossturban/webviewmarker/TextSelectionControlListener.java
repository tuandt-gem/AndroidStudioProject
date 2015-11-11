package com.bossturban.webviewmarker;

public interface TextSelectionControlListener {
    void jsError(String error);
    void jsLog(String message);
    void startSelectionMode();
    void endSelectionMode();
    void saveSelection(int start, int end, int startVerseNumber, int endVerseNumber, String text, int fontSize);

    /**
     * Tells the listener to show the context menu for the given range and selected text.
     * The bounds parameter contains a json string representing the selection bounds in the form 
     * { 'left': leftPoint, 'top': topPoint, 'right': rightPoint, 'bottom': bottomPoint }
     * @param range
     * @param text
     * @param handleBounds
     * @param isReallyChanged
     */
    void selectionChanged(String range, String text, int fontSize, String handleBounds, boolean isReallyChanged);

    void setContentWidth(float contentWidth);

    void onSingleTouch(int start, int end);

    void notifyRangeCoords(int left, int top, int bottom, int right, int fontSize);

    void noteClicked(int id);
}
