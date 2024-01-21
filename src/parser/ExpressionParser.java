package parser;

import expressions.*;

import java.util.Map;

public class ExpressionParser {
    public Expression parse(String expression) {
        return new Parser(expression).parseAllExpression();
    }

    private static class Parser extends BaseParser {
        private static final Map<Character, Operation> SYMBOL_TO_OPERATION = Map.of(
                '+', Operation.ADD,
                '*', Operation.MULTIPLY,
                '^', Operation.POW);
        private static final Map<Operation, Integer> OPERATION_PRIORITY = Map.of(
                Operation.ADD, 0,
                Operation.MULTIPLY, 1,
                Operation.POW, 2);
        private Operation nextOperation = Operation.UNKNOWN;

        public Parser(String expression) {
            super(expression);
        }

        public Expression parseAllExpression() {
            Expression result = parseElement();
            checkEOFExpression();
            if (!eof()) {
                throw error("End of file expected, found: \"" + take() + "\"");
            }
            return result;
        }

        private Expression parseElement() {
            return parseElement(-1);
        }

        private Expression parseElement(int prevPriority) {
            Expression result = parseValue();
            if (eofExpression()) {
                return result;
            }
            Operation operation = testOperation();

            if (operation == Operation.POW) {
                takeOperation();
                result = new Power(result, parsePower());
            }
            if (eofExpression()) {
                return result;
            }

            operation = testOperation();
            while (getPriority(operation) > prevPriority) {
                takeOperation();
                result = getBinaryExpression(operation, result, parseElement(getPriority(operation)));
                if (eofExpression()) {
                    return result;
                }
                operation = testOperation();
            }
            return result;
        }

        private Expression parsePower() {
            Expression result = parseValue();
            if (eofExpression()) {
                return result;
            }
            Operation operation = testOperation();
            if (operation == Operation.POW) {
                takeOperation();
                return new Power(result, parsePower());
            }
            return result;
        }

        private boolean eofExpression() {
            return eof() || test(')');
        }

        private int getPriority(Operation operation) {
            return OPERATION_PRIORITY.getOrDefault(operation, Integer.MAX_VALUE);
        }

        private void checkEOFExpression() {
            if (nextOperation != Operation.UNKNOWN) {
                throw error("No last argument");
            }
        }

        private Expression parseValue() {
            if (take('(')) {
                Expression result = parseElement();
                expect(')');
                checkEOFExpression();
                return result;
            } else if (testDigit()) {
                return parseConst();
            } else if (take('w')) {
                return new Variable();
            } else {
                throw error("Argument not found");
            }
        }

        private Const parseConst() {
            StringBuilder result = new StringBuilder();
            while (testDigit()) {
                result.append(take());
            }
            return new Const(Integer.parseInt(result.toString()));
        }

        private BinaryExpression getBinaryExpression(Operation operation,
                                                     Expression first, Expression second) {
            BinaryExpression result;
            if (operation == Operation.ADD) {
                result = new Add(first, second);
            } else if (operation == Operation.MULTIPLY) {
                result = new Multiply(first, second);
            } else {
                result = new Power(first, second);
            }
            return result;
        }

        private void takeOperation() {
            take();
            nextOperation = Operation.UNKNOWN;
        }

        private void updateNextOperation() {
            nextOperation = SYMBOL_TO_OPERATION.getOrDefault(test(), Operation.UNKNOWN);
        }

        private Operation testOperation() {
            if (nextOperation == Operation.UNKNOWN) {
                updateNextOperation();
            }
            return nextOperation;
        }
    }
}
