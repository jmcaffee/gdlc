package runtime.main;

public interface IProblem {

	public abstract String getDesc();

	public abstract void setDesc(String desc);

	public abstract int getId();

	public abstract void setId(int id);

	public abstract String getMsg();

	public abstract void setMsg(String msg);

}