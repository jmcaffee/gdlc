package runtime.compiler;

import runtime.main.IProblem;

public interface IErrorContext {

	public abstract void addError(IProblem err);

	public abstract boolean hasErrors();

	public abstract String dumpErrors();

}