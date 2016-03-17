package runtime.compiler;

import runtime.main.IProblem;
import runtime.parser.SimpleNode;

public interface IErrorContext {

	public abstract void addError(IProblem err);

	public abstract boolean hasErrors();

	public abstract String dumpErrors();

	public abstract String getParentRuleIdentifier(SimpleNode node);

}