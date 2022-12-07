//Abe Hamed, azh210000

//A generic class designed to work with linked lists, trinary trees, and other data structures.
public class Node<AnyType> {
	
	private AnyType payload;
	
	private Node<AnyType> next;
	private Node<AnyType> previous;
	private Node<AnyType> down;
	
	//Default constructor, that will still initialize the Node's pointers.
	//However, it will initialize them to null, so that later they can be mutated and accessed.
	public Node()
	{
		payload = null;
		next = null;
		previous = null;
		down = null;
	}
	
	//Overloaded constructor, that will initialize only the payload of the Node. Any class can be used. 
	public Node(AnyType pl)
	{
		payload = pl;
		next = null;
		previous = null;
		down = null; //This can still be changed later. We'll leave it initialized at least. 
	}
	
	//Overloaded constructor, that will initialize everything, except for down.
	//Down is the least commonly used pointer, especially in linked lists. 
	public Node(AnyType pl, Node<AnyType> n, Node<AnyType> p)
	{
		payload = pl;
		next = n;
		previous = p;
		down = null; //This can still be changed later. We'll leave it initialized at least. 
	}
	
	//Overloaded constructor that initializes everything except for the payload.
	//Useful in the any class in which empty nodes must be firstly declared in a grid
	//and then later have their payload changed. 
	public Node(Node<AnyType> n, Node<AnyType> p, Node<AnyType> d)
	{
		payload = null;
		next = n;
		previous = p;
		down = d;
	}
	
	//Overloaded constructor that will initialize every field. 
	public Node(AnyType pl, Node<AnyType> n, Node<AnyType> p, Node<AnyType> d)
	{
		payload = pl;
		next = n;
		previous = p;
		down = d;
	}
	
	//MUTATORS
	
	//Mutator that is designed to set the "next" pointer
	public void setNext(Node<AnyType> n)
	{
		next = n;
	}
	
	//Mutator that sets the "previous" pointer
	public void setPrevious(Node<AnyType> p)
	{
		previous = p;
	}
	
	//Mutator that sets the "down" pointer
	public void setDown(Node<AnyType> d)
	{
		down = d;
	}
	
	//Mutator that setes the payload, of generic type
	public void setPayload(AnyType pl)
	{
		payload = pl;
	}
	
	//ACCESSORS
	
	//Accessor to get the next pointer
	public Node<AnyType> getNext()
	{
		return next;
	}
	
	//Accessor to get the previous pointer
	public Node<AnyType> getPrevious()
	{
		return previous;
	}

	//Accessor to get the down pointer
	public Node<AnyType> getDown()
	{
		return down;
	}

	//Accessor to get the payload, of generic type. 
	public AnyType getPayload()
	{
		return payload;
	}
	
	//returns whatever the payload's toString() method was. There is nothing extra here. 
	public String toString()
	{
		return payload.toString();
	}
	
	
	
	
	
	
	
	

}
