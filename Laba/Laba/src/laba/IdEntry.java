package laba;

import laba.ast.Declaration;

public class IdEntry
{
	public String id;
	public Declaration attr;
	
	
	public IdEntry(String id, Declaration attr )
	{
		this.id = id;
		this.attr = attr;
	}
	
	
	public String toString()
	{
		return id;
	}
}