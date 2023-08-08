import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}. This will require the
 * existence of an arithmetic expression already created via another program
 * (xml-expression-generator), then using that expression (x+y/z, for example),
 * and the help of a recursive method, this program will evaluate that
 * expression and generate the appropriate result.
 *
 * @author Omar Takruri
 *
 */
public final class XMLTreeExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        int evaluated = 0;

        //Addition(+):
        if (exp.label().equals("plus")) {
            evaluated = evaluate(exp.child(0)) + evaluate(exp.child(1));
        }

        //Subtraction(-):
        if (exp.label().equals("minus")) {
            evaluated = evaluate(exp.child(0)) - evaluate(exp.child(1));

            //Checks if we got a negative value:
            if (evaluated < 0) {
                Reporter.fatalErrorToConsole("Cannot have a negative value");
            }
        }

        //Multiplication(*):
        if (exp.label().equals("times")) {
            evaluated = evaluate(exp.child(0)) * evaluate(exp.child(1));
        }

        //Division(/);
        if (exp.label().equals("divide")) {
            int denominator = evaluate(exp.child(1));

            //Check if we are dividing by zero:
            if (denominator == 0) {
                Reporter.fatalErrorToConsole("Cannot divide by zero");
            }
            evaluated = evaluate(exp.child(0)) / denominator;
        }

        /*
         * Base Case - If the node is of label value = "number", it returns the
         * attribute value of "value" (String) as an "int". Otherwise, it will
         * keep on recursively getting the arithmetic operation in the given
         * expression as follows: The idea is to close the child nodes first, so
         * I would keep on getting a 1st value (left) + operation (based on the
         * child) + 2nd value (right). The operation is hard coded, based on the
         * name of the parent node (minus, plus, divide, or multiply).
         */

        if (exp.label().equals("number")) {
            evaluated = Integer.parseInt(exp.attributeValue("value"));
        }

        return evaluated;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
