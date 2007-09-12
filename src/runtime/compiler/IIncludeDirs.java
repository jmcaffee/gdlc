package runtime.compiler;


public interface IIncludeDirs {
	public abstract void addIncludeDir(String iDir);
	
	public abstract String joinPath(String dir, String file);

	public abstract String findIncludePath(String filePath);

}
