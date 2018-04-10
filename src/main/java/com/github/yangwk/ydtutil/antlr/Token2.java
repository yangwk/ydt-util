package com.github.yangwk.ydtutil.antlr;

import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Token2 {
	private int startIndex;
	private int stopIndex;
	
	//首、尾相同，说明token相同
	public Token2(Token token) {
		this(token.getStartIndex(), token.getStopIndex());
	}

	public Token2(int startIndex, int stopIndex) {
		this.startIndex = startIndex;
		this.stopIndex = stopIndex;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(startIndex).append(stopIndex).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) { return false; }
		Token2 rhs = (Token2) obj;
		
		return new EqualsBuilder()
	                 .append(startIndex, rhs.startIndex)
	                 .append(stopIndex, rhs.stopIndex)
	                 .isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("startIndex", startIndex)
				.append("stopIndex", stopIndex)
				.toString();
	}

	
	
}
