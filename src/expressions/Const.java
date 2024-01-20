package expressions;

import ordinals.OrdinalNormaliseExpression;

public class Const implements Expression {
    private final int number;

    public Const(int number) {
        this.number = number;
    }

    @Override
    public OrdinalNormaliseExpression normalise() {
        return new OrdinalNormaliseExpression(number);
    }
}
