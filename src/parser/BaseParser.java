package parser;

public class BaseParser {
    private final CharSource source;
    private final static char END = 0;
    private char lst;

    public BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    public BaseParser(final String source) {
        this.source = new StringSource(source);
        take();
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected char take() {
        final char ret = lst;
        lst = source.hasNext() ? source.next() : END;
        return ret;
    }

    protected boolean test(final char expected) {
        return lst == expected;
    }

    protected char test() {
        return lst;
    }

    protected void expect(char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + lst + "'");
        }
    }

    protected boolean testDigit() {
        return Character.isDigit(lst);
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }
}
