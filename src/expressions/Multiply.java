package expressions;

import ordinals.OrdinalNormaliseExpression;

public class Multiply extends BinaryExpression {
    public Multiply(Expression first, Expression second) {
        super(first, second);
    }

    @Override
    protected OrdinalNormaliseExpression concatenate(OrdinalNormaliseExpression first, OrdinalNormaliseExpression second) {
        return OrdinalNormaliseExpression.multiply(first, second);
    }
}
