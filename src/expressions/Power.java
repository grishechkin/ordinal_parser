package expressions;

import ordinals.OrdinalNormaliseExpression;

public class Power extends BinaryExpression {
    public Power(Expression first, Expression second) {
        super(first, second);
    }

    @Override
    protected OrdinalNormaliseExpression concatenate(OrdinalNormaliseExpression first, OrdinalNormaliseExpression second) {
        return OrdinalNormaliseExpression.power(first, second);
    }
}
