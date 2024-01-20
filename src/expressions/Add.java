package expressions;

import ordinals.OrdinalNormaliseExpression;

public class Add extends BinaryExpression {
    public Add(Expression first, Expression second) {
        super(first, second);
    }

    @Override
    protected OrdinalNormaliseExpression concatenate(OrdinalNormaliseExpression first, OrdinalNormaliseExpression second) {
        return OrdinalNormaliseExpression.add(first, second);
    }
}
