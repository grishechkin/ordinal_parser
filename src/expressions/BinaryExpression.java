package expressions;

import ordinals.OrdinalNormaliseExpression;

public abstract class BinaryExpression implements Expression {
    protected final Expression first;
    protected final Expression second;

    public BinaryExpression(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public OrdinalNormaliseExpression normalise() {
        return concatenate(first.normalise(), second.normalise());
    }

    abstract protected OrdinalNormaliseExpression concatenate(OrdinalNormaliseExpression first, OrdinalNormaliseExpression second);
}
