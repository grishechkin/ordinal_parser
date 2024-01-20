package parser;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos != data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
