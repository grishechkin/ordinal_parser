package expressions;

import ordinals.OrdinalNormaliseExpression;

public class Variable implements Expression {
    @Override
    public OrdinalNormaliseExpression normalise() {
        return new OrdinalNormaliseExpression("w");
    }
}
