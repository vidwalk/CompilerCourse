/*
 * 27.09.2016 Minor edit
 * 11.11.2009 New package structure
 * 27.10.2006 Original version
 */
 
package laba;


public class Address
{
	public int displacement;
	
	
	public Address()
	{
		displacement = 10;
	}
	
	
	public Address(  int displacement )
	{

		this.displacement = displacement;
	}
	
	
	public Address( Address a, int increment )
	{
		this.displacement = a.displacement + increment;
	}
	
	
	public Address( Address a )
	{
		this.displacement = 0;
	}
	
	
	public String toString()
	{
		return " displacement=" + displacement;
	}
}