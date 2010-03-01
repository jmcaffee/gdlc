package runtime.visitors;

import runtime.compiler.CompilerContext;
import runtime.compiler.LookupData;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.compiler.LookupData.MinMax;
import runtime.elements.XmlElem;
import runtime.main.CompileError;
import runtime.main.CompileMgr;
import runtime.main.CompileWarning;
import runtime.main.Log;
import runtime.parser.ASTLookupDef;
import runtime.parser.ASTVarRef;

public class LookupNodeVisitor extends DepthFirstVisitor {
	
	CompilerContext ctx = null;
	
	public LookupNodeVisitor(CompilerContext ctx){ this.ctx = ctx; }
	
	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTLookupDef, java.lang.Object)
	 */
	public Object visit(ASTLookupDef node, Object data){
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("LOOKUP");
		
		String[] attribs = {"Name"};
		me.setAttributeOrder(attribs);

		me.putAttribute("Name", node.getAlias());
		
		XmlElem xParam = new XmlElem("XParameter");
		XmlElem yParam = new XmlElem("YParameter");

		node.jjtGetChild(0).jjtAccept(this, xParam);
		node.jjtGetChild(1).jjtAccept(this, yParam);
		
		me.appendXml(xParam.toXml());
		me.appendXml(yParam.toXml());
		
		// Handle lookup data
		LookupData lkData = this.ctx.getLookupData(node.getName());
		try{
			formatLookupData(lkData, me);
		}catch(IndexOutOfBoundsException e){
			ctx.addError(new CompileError(CompileError.errors.OUTPUTERROR,
					new String("Index out of bounds error occurred while outputting lookup data [" + node.getAlias() + "]")));
			ctx.addWarning(new CompileWarning(CompileWarning.warnings.CSVLINELENGTH,
					new String("The line length format is incorrect for lookup [" + node.getAlias() + "]")));
//			System.out.println("*** Corrupted Lookup CSV data ***");
//			System.out.println("    Index out of bounds error occurred while outputting lookup data [" + node.getAlias() + "]");
		}
		
		// Add xml data to the parent element
		elem.appendXml(me.toXml());
		return elem;
	}
	
	public Object visit(ASTVarRef node, Object data) {
		XmlElem elem = (XmlElem)data;

		XmlElem me = null;
		String varName = node.getName();
		
		if(this.ctx.containsVar(new VarDpm(varName))){
			VarDpm dpm = (VarDpm)this.ctx.getVar(new VarDpm(varName));
			me = new XmlElem(dpm.getVarType());
			me.setIsShortTag(true);

			me.putAttribute("Name", dpm.getAlias());
			
			String[] attOrder = {"Name"};
			me.setAttributeOrder(attOrder);
		} 
		else {
			VarPpm ppm = (VarPpm)this.ctx.getVar(new VarPpm(varName));
			me = new XmlElem(ppm.getVarType());
			me.setIsShortTag(true);

			me.putAttribute("Name", ppm.getAlias());
			me.putAttribute("DataType", ppm.getDataType());
			me.putAttribute("Type", ppm.getType());
			
			String[] attOrder = {"Name", "DataType", "Type"};
			me.setAttributeOrder(attOrder);
		}
		
		if(null == me){
			Log.error("Missing variable.");
			return elem;
		}
		
		elem.appendXml(me.toXml());

		return elem;
	}


	protected void formatLookupData(LookupData data, XmlElem parent){
		XmlElem row = new XmlElem("Row");
		XmlElem col = new XmlElem("Column");
		String[] attOrder = {"MinValue", "MaxValue"};
		row.setAttributeOrder(attOrder);
		col.setAttributeOrder(attOrder);
		int valIndex = 0;
		
		for(MinMax rMinMax : data.rowMinMaxs){
			row.putAttribute("MinValue", rMinMax.getMin());
			row.putAttribute("MaxValue", rMinMax.getMax());
			
			for(MinMax cMinMax : data.colMinMaxs){
				col.putAttribute("MinValue", cMinMax.getMin());
				col.putAttribute("MaxValue", cMinMax.getMax());
				col.appendXml(data.values.get(valIndex++));
				row.appendXml(col.toXml());
				col.clearContent();
			}
			
			parent.appendXml(row.toXml());
			row.clearContent();
		}
	}
}
