package laba;

import java.util.Vector;

import laba.ast.Declaration;

public class IdentificationTable
{
	private Vector<IdEntry> table = new Vector<IdEntry>();
	
	
	public IdentificationTable()
	{
	}
	
	
	public void enter( String id, Declaration attr )
	{
		IdEntry entry = find( id );
		
		if( entry != null )
			System.out.println( id + " declared twice" );
		else
			table.add( new IdEntry( id, attr ) );
	}
	
	
	public Declaration retrieve( String id )
	{
		IdEntry entry = find( id );
		
		if( entry != null )
			return entry.attr;
		else
			return null;
	}
	
	
	private IdEntry find( String id )
	{
		for( int i = table.size() - 1; i >= 0; i-- )
			if( table.get(i).id.equals( id ) )
				return table.get(i);
				
		return null;
	}
	
	public int size() {
		return table.size();
	}
}
