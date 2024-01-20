package ordinals;

import java.util.ArrayList;
import java.util.List;

public class OrdinalNormaliseExpression {
    private List<Element> elements = new ArrayList<>();

    OrdinalNormaliseExpression() {
    }

    public OrdinalNormaliseExpression(int c) {
        if (c != 0) {
            elements.add(new Element(new OrdinalNormaliseExpression(), c));
        }
    }

    public OrdinalNormaliseExpression(String name) {
        elements.add(new Element(new OrdinalNormaliseExpression(1), 1));
    }

    public OrdinalNormaliseExpression(final OrdinalNormaliseExpression other) {
        elements = copy(other.elements);
    }

    public OrdinalNormaliseExpression(List<Element> elements) {
        this.elements = elements;
    }

    public OrdinalNormaliseExpression(Element element) {
        elements.add(new Element(element));
    }

    private Element get(int i) {
        return elements.get(i);
    }

    private int size() {
        return elements.size();
    }

    public boolean isZero() {
        return size() == 0;
    }

    private Element getFirst() {
        return get(0);
    }

    private Element getLast() {
        return get(size() - 1);
    }

    private void removeFirst() {
        elements.remove(0);
    }

    private void removeLast() {
        elements.remove(size() - 1);
    }

    private OrdinalNormaliseExpression append(Element element) {
        List<Element> elements = copy(this.elements);
        elements.add(new Element(element));
        return new OrdinalNormaliseExpression(elements);
    }

    private static List<Element> copy(List<Element> elements) {
        List<Element> result = new ArrayList<>();
        for (Element element : elements) {
            result.add(new Element(element));
        }
        return result;
    }

    int concatenate(final OrdinalNormaliseExpression other) {
        for (int i = 0; i < Math.min(size(), other.size()); i++) {
            Element thisElement = get(i);
            Element otherElement = other.get(i);

            int concat = thisElement.getW().concatenate(otherElement.getW());
            if (concat == 1) {
                return 1;
            } else if (concat == -1) {
                return -1;
            }

            int cmp = Integer.compare(thisElement.getA(), otherElement.getA());
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(size(), other.size());
    }

    public boolean equals(final OrdinalNormaliseExpression other) {
        return this.concatenate(other) == 0;
    }

    private OrdinalNormaliseExpression getC() {
        if (getLast().getW().isZero()) {
            return new OrdinalNormaliseExpression(elements.subList(0, size() - 1));
        }
        return new OrdinalNormaliseExpression(this);
    }

    private Element getM() {
        if (getLast().getW().isZero()) {
            return new Element(getLast());
        }
        return new Element(new OrdinalNormaliseExpression(), 0);
    }

    public static OrdinalNormaliseExpression add(final OrdinalNormaliseExpression first, final OrdinalNormaliseExpression second) {
        if (second.isZero()) {
            return new OrdinalNormaliseExpression(first);
        }
        if (first.isZero()) {
            return new OrdinalNormaliseExpression(second);
        }

        OrdinalNormaliseExpression result = new OrdinalNormaliseExpression(first);

        while (!result.isZero() && result.getLast().concatenatePow(second.getFirst()) == -1) {
            result.removeLast();
        }

        if (result.isZero()) {
            return new OrdinalNormaliseExpression(second);
        }

        int startPos = 0;
        if (second.getFirst().concatenatePow(result.getLast()) == 0) {
            startPos = 1;
            result.getLast().setA(result.getLast().getA() + second.getFirst().getA());
        }

        for (int i = startPos; i < second.size(); i++) {
            result = result.append(new Element(second.get(i)));
        }
        return result;
    }

    private static OrdinalNormaliseExpression multiply(final OrdinalNormaliseExpression first, final Element element) {
        if (element.isZero()) {
            return new OrdinalNormaliseExpression();
        }

        OrdinalNormaliseExpression result;
        if (element.getW().isZero()) {
            result = new OrdinalNormaliseExpression(first);
            result.getFirst().setA(result.getFirst().getA() * element.getA());
        } else {
            result = new OrdinalNormaliseExpression(first.getFirst());
            result.getFirst().setW(add(result.getFirst().getW(), element.getW()));
            result.getFirst().setA(element.getA());
        }
        return result;
    }

    public static OrdinalNormaliseExpression multiply(final OrdinalNormaliseExpression first, final OrdinalNormaliseExpression second) {
        if (second.isZero() || first.isZero()) {
            return new OrdinalNormaliseExpression();
        }

        OrdinalNormaliseExpression result = multiply(first, second.getFirst());
        for (int i = 1; i < second.size(); i++) {
            result = add(result, multiply(first, second.get(i)));
        }
        return result;
    }

    private static OrdinalNormaliseExpression power(final OrdinalNormaliseExpression first, final Element element) {
        if (element.isZero()) {
            return new OrdinalNormaliseExpression(1);
        }
        if (first.isZero()) {
            return new OrdinalNormaliseExpression();
        }

        OrdinalNormaliseExpression result;
        if (element.getW().isZero()) {
            result = new OrdinalNormaliseExpression(first);
            for (int i = 1; i < element.getA(); i++) {
                result = multiply(result, first);
            }
        } else {
            result = new OrdinalNormaliseExpression(first.getFirst());
            if (!result.getFirst().getW().isZero()) {
                result.getFirst().setW(multiply(result.getFirst().getW(), element));
            } else if (result.getFirst().getA() != 1) {
                result = power(new OrdinalNormaliseExpression("w"), element);
            }
            result.getFirst().setA(1);
        }
        return result;
    }

    public static OrdinalNormaliseExpression power(final OrdinalNormaliseExpression first, final OrdinalNormaliseExpression second) {
        if (second.isZero()) {
            return new OrdinalNormaliseExpression(1);
        }

        OrdinalNormaliseExpression result = power(first, second.getFirst());
        for (int i = 1; i < second.size(); i++) {
            result = multiply(result, power(first, second.get(i)));
        }

        return result;
    }
}
