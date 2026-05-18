package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

import java.util.function.Predicate;

public class TextInputComponent extends AbstractComponent implements Measurable {

    private static final int PAD = 4;

    private String value = "";
    private int cursorPos = 0;
    private int selectionStart = -1;

    private final String placeholder;
    private final int maxLength;
    private final Predicate<String> filter;
    private final String onChangeAction;
    private final String onSubmitAction;

    public TextInputComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                              String placeholder, int maxLength,
                              Predicate<String> filter, String onChangeAction, String onSubmitAction) {
        super(id, visible, enabled, preferredSize);
        this.placeholder    = placeholder;
        this.maxLength      = maxLength;
        this.filter         = filter;
        this.onChangeAction = onChangeAction;
        this.onSubmitAction = onSubmitAction;
    }

    @Override public ComponentType getType() { return ComponentType.TEXT_INPUT; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        return Size.fixed(80, MeasureProvider.lineHeight() + PAD * 2);
    }

    public void moveCursor(int pos, boolean selecting) {
        int clamped = Math.max(0, Math.min(pos, value.length()));
        if (selecting) {
            if (selectionStart == -1) selectionStart = cursorPos;
        } else {
            selectionStart = -1;
        }
        cursorPos = clamped;
    }

    public void moveCursorBy(int delta, boolean selecting) {
        moveCursor(cursorPos + delta, selecting);
    }

    public void moveCursorToStart(boolean selecting) { moveCursor(0, selecting); }
    public void moveCursorToEnd(boolean selecting)   { moveCursor(value.length(), selecting); }

    public void moveCursorByWord(int direction, boolean selecting) {
        int pos = cursorPos;
        if (direction > 0) {
            while (pos < value.length() && value.charAt(pos) == ' ') pos++;
            while (pos < value.length() && value.charAt(pos) != ' ') pos++;
        } else {
            while (pos > 0 && value.charAt(pos - 1) == ' ') pos--;
            while (pos > 0 && value.charAt(pos - 1) != ' ') pos--;
        }
        moveCursor(pos, selecting);
    }

    public boolean insert(String text) {
        deleteSelection();
        String next = value.substring(0, cursorPos) + text + value.substring(cursorPos);
        if (next.length() > maxLength) return false;
        if (filter != null && !filter.test(next)) return false;
        value = next;
        cursorPos += text.length();
        return true;
    }

    public boolean deleteBackward() {
        if (hasSelection()) { deleteSelection(); return true; }
        if (cursorPos == 0) return false;
        value = value.substring(0, cursorPos - 1) + value.substring(cursorPos);
        cursorPos--;
        return true;
    }

    public boolean deleteForward() {
        if (hasSelection()) { deleteSelection(); return true; }
        if (cursorPos >= value.length()) return false;
        value = value.substring(0, cursorPos) + value.substring(cursorPos + 1);
        return true;
    }

    public void selectAll() {
        selectionStart = 0;
        cursorPos = value.length();
    }

    public String getSelectedText() {
        if (!hasSelection()) return "";
        int lo = Math.min(cursorPos, selectionStart);
        int hi = Math.max(cursorPos, selectionStart);
        return value.substring(lo, hi);
    }

    public String cut() {
        String selected = getSelectedText();
        deleteSelection();
        return selected;
    }

    private void deleteSelection() {
        if (!hasSelection()) return;
        int lo = Math.min(cursorPos, selectionStart);
        int hi = Math.max(cursorPos, selectionStart);
        value = value.substring(0, lo) + value.substring(hi);
        cursorPos = lo;
        selectionStart = -1;
    }

    @Deprecated
    public boolean tryInsert(String text) { return insert(text); }

    @Deprecated
    public boolean tryDelete() { return deleteBackward(); }

    public boolean hasSelection()      { return selectionStart != -1 && selectionStart != cursorPos; }
    public int getCursorPos()          { return cursorPos; }
    public int getSelectionStart()     { return selectionStart; }
    public String getValue()           { return value; }
    public void setValue(String v)     { this.value = v; cursorPos = v.length(); selectionStart = -1; }
    public String getPlaceholder()     { return placeholder; }
    public int getMaxLength()          { return maxLength; }
    public String getOnChangeAction()  { return onChangeAction; }
    public String getOnSubmitAction()  { return onSubmitAction; }
}