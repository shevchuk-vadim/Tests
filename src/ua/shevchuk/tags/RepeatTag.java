package ua.shevchuk.tags;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class provides an implementation of the IterationTag interface
 * and defines JSP tag named "repeat". This tag repeats his body
 * as many times as specified in its "number" attribute. In the body of the tag
 * it is possible to use the variable that expose the the number of repeat.
 * The name of this variable can be specify in optional tag attribute named "varCount".
 * The variable is created in pageContext.   
 */
@SuppressWarnings("serial")
public class RepeatTag extends TagSupport {
	
	private int number;
	private int count;
	private String varCount;
	
	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setVarCount(String varCount) {
		this.varCount = varCount;
	}
	
	@Override
	public int doStartTag() {
		if (++count <= number) {
			setAttributeVarCount();
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}
	
	@Override
	public int doAfterBody() {
		if (++count <= number) {
			setAttributeVarCount();
			return EVAL_BODY_AGAIN;
		} else {
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() {
		count = 0;
		setAttributeVarCount();
		return EVAL_PAGE;
	}
	
	private void setAttributeVarCount() {
		if (varCount != null) {
			pageContext.setAttribute(varCount, count);
		}
		
	}
	
}
