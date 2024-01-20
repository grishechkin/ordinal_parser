package ordinals;

public class Element {
    private OrdinalNormaliseExpression w;
    private int a;

    Element(OrdinalNormaliseExpression w, int a) {
        this.w = w;
        this.a = a;
    }

    Element(Element other) {
        this.a = other.a;
        this.w = new OrdinalNormaliseExpression(other.w);
    }

    public OrdinalNormaliseExpression getW() {
        return w;
    }

    public boolean isZero() {
        return getA() == 0;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setW(final OrdinalNormaliseExpression w) {
        this.w = new OrdinalNormaliseExpression(w);
    }

    public int concatenatePow(Element other) {
        return getW().concatenate(other.getW());
    }
}
