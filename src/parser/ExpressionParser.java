package parser;

import expressions.*;

public class ExpressionParser {
    public Expression parse(String expression) {
        return new Parser(expression).parseAllExpression();
    }

    private static class Parser extends BaseParser {
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
            return switch (operation) {
                case ADD -> 0;
                case MULTIPLY -> 1;
                case POW -> 2;
                default -> Integer.MAX_VALUE;
            };
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
            return switch (operation) {
                case ADD -> new Add(first, second);
                case MULTIPLY -> new Multiply(first, second);
                case POW -> new Power(first, second);
                default -> throw error("Unsupported binary operation");
            };
        }

        private void takeOperation() {
            take();
            nextOperation = Operation.UNKNOWN;
        }

        private void updateNextOperation() {
            nextOperation = switch (test()) {
                case '+' -> Operation.ADD;
                case '*' -> Operation.MULTIPLY;
                case '^' -> Operation.POW;
                default -> Operation.UNKNOWN;
            };
        }

        private Operation testOperation() {
            if (nextOperation == Operation.UNKNOWN) {
                updateNextOperation();
            }
            return nextOperation;
        }
    }
}
