//Abe Hamed, azh210000

import java.util.Scanner;
import java.io.*;

//Class that is designed to hold a linked list of linkedlist, each holding an object of type Seat. 
//Only the first node of each linked list will be "down" pointed to the head of the linked list below it.
public class Auditorium {

	private Node<Seat> head;
	private int rows; //The size of the list in rows.
	private int cols; //The size of the list in columns. Or the amount of times a down pointer is used to get to the bottom. 
	
	//Default constructor. Technically, we have no use for this method.
	//It is simply here to cover Java's substitute, when we technically MUST use the overloaded constructor.
	//It will just make an empty head, so that perhaps the programmer can add nodes manually later. 
	public Auditorium()
	{
		head = new Node<Seat>(); //An empty node object.
	}
	
	//NOTE: The exception was already handled in Main, where we performed input validation on the user's filename input.
	//"try catch Exception e" is just a way to tell Java that we have already done the work. There is no use for it. 
	public Auditorium(String filename) 
	{
		File myFile = new File(filename);
		Scanner inFile = null;
		
		String str = "";
		int currentRow = 1;
		int currentCol = 1;
		
		try
		{
			inFile = new Scanner(myFile);
		}
		catch (Exception e)
		{ //For whatever happens within the file, is unknown. We already performed input validation in main, so there should be nothing wrong. 
			System.out.println("Error");
		}
		
		rows = 1; //Rows should always start at one. We need to have something in the array to start with. 
		while (inFile.hasNext())
		{
			str = inFile.next(); //We take a string, and then split it into chunks 1 character wide, in order to read each character into its own object.
			String[] tokens = str.split(""); //Number of Nodes depends on the number of characters in the file
			addDown(new Seat(rows, (char) 65, tokens[0].charAt(0))); //We create the first subhead of the row. (It will be the main head if there is no array)

			for (cols = 1; cols < tokens.length; cols++)  //It's - 1, because we already made the first node of the row as the head!
			{
				addRight(new Seat(rows, (char) (cols + 65), tokens[cols].charAt(0)), rows); //Then, stemming off of that, we create all of the nodes that complete the row
			}	
		    rows++;
		}	
		rows--; //Decrement rows because we assumed that there was 1 already to start with. All of this is to prevent a crash. 
		inFile.close();
	}
	
	//This method is going to add Nodes onto the tail end of a row LinkedList. 
	//It will ONLY add "next" and previous" pointers for the last element added. (This method only works on one row).
	//It will not use down pointers. That will be a different method. 
	//"Row" parameter allows you to pick the row that you want a Node added to. 
	//The designated row must have a subhead!
	public boolean addRight(Seat x, int row)
	{
		//Since the list is empty, we'll need to make our first head. 
		if (head == null)
		{
			head = new Node<Seat>(x);
			return true;
		}
		//Since the list is NOT empty we'll create new nodes based off the existing head. 
		
		
		//Traversing the linkedList DOWNWARDS first
		Node<Seat> current = head;
		int numRows = 1;
		while (current.getDown() != null && numRows < row)
		{
			current = current.getDown();
			numRows++;
		}

		//Traversing the linkedList RIGHT next
		while (current.getNext() != null)
		{
			current = current.getNext();
		}
		Node<Seat> n = new Node<Seat>(x, null, current, null); //Basically saying that the previous pointer is pointing to the Node before it, and that the next pointer is null (it's the last element)
		current.setNext(n); //Setting next pointer of the Node before
		return true;
		
	}
	
	
	
	//This method only adds Nodes to the bottom of the first Node in a row. Essentially, it is making
	//the virtually pointing downward string of Nodes that will all be "heads" of their own row linkedlists. 
	public boolean addDown(Seat x)
	{
		//If auditorium is empty, we make our first head. 
		if (head == null)
		{
			head = new Node<Seat>(x); // A new seat is made without any pointers, and just the payload x. 
			return true;
		}
		
		//Since the list isn't empty, we'll make an entirely new node that isn't head
		Node<Seat> current = head;
		
		while (current.getDown() != null)
		{
		    current = current.getDown();
		}
		
		//Making a new node, and then setting the Node before it to point to it using the "down" pointer. 
		//There is no up pointer, so the new Node has no pointers eminating from it. 
		Node<Seat> n = new Node<Seat>(x);
		current.setDown(n);
		return true;
	}
	
	//Returns the payload of the node that you specify in coordinate directions. 
	//Only takes the column in integer form, so remember to convert. 
	public Seat getNodePayload(int row, int col)
	{ //We obviously have nothing to do if there is no head, so we don't return anything. 
		if (head == null)
		{
			return null;
		}
		//Creating an arbitray current variable to traverse the linkedlist using pass by reference 
		Node<Seat> current = head;
		int currentRow = 1;
		int currentCol = 1;
		
		//The below while loop traverses the linkedlist in the down direction. 
		//Going down the linkedlist first, because we won't be able to use down pointers after going to the right! 
		while (current.getDown() != null && currentRow < row)
		{
			current = current.getDown();
			currentRow++;
		}

		//Traversing the linkedList RIGHT next. 
		while (current.getNext() != null && currentCol < col)
		{
			current = current.getNext(); //Goes right the amount of times you specify.
			currentCol++;
		}
		
		return current.getPayload(); //returns the payload at that coordinate of the linkedList.
	}
	
	//Sets the payload of the node you specify with coordinates row, col, to the object x.
	//Again, be sure to convert to integer notation on the column lettering. 
	public boolean setNodePayload(Seat x, int row, int col)
	{ //We obviously have nothing to do if there is no head, so we don't set anything. 
		if (head == null)
		{
			return false;
		}
		//again an arbitrary current node to traverse the list. 
		Node<Seat> current = head;
		int currentRow = 1;
		int currentCol = 1;
		
		//Simply cannot go right first, because the only nodes with down pointers are those at the beginning of the linked list.
		while (current.getDown() != null && currentRow < row)
		{
			current = current.getDown();
			currentRow++;
		}

		//Traversing the linkedList RIGHT next
		while (current.getNext() != null && currentCol < col)
		{
			current = current.getNext();
			currentCol++; //We do currentCol++ until reaching the desired spot in the linked list. 
		}
		
		current.setPayload(x);
		return true;
	}
	
	//simply returns the head of the auditorium. 
	public Node<Seat> getHead()
	{
		return head;
	}
	
	//returns the number of rows of the auditorium. 
	public int getRows()
	{
		return rows;
	}
	
	//returns the number of columns of the auditorium. 
	public int getCols()
	{
		return cols;
	}
	
	//sets the head of the entire linkedList. 
	public void setHead(Node<Seat> h)
	{
		head = h;
	}
	
	//This method is designed to return a string with the entire linked list showing ticket types for each seat.
	
	public String toString()
	{
		Node<Seat> current = head;
		String build = "";
		int rowCounter = 1;
		for (int i = 0; i < rows; i++) //Goes to the limit of the size of the list.
		{
			for (int j = 0; j < cols; j++)
			{
				build+= current.toString() + ""; //"adding" onto a string the values of ticket type. 
				current = current.getNext();
			}
			current = head.getDown(); //Now that the first row is done, we go down a row. 
			for (int k = 1; k < rowCounter; k++)
			{
				current = current.getDown();
			}
			build+="\n"; //After we are done with the row, we go down a line in the string, so that the output isn't just one line. 
			rowCounter++;
		}
		
		return build;
	}
	
	//Basic toString makes all of the A and S and C into a #. It does not tell the user the exact ticket type of 
	//each seat. It also prints the numbering and lettering of rows and columns to help with readability.
	public String basicToString()
	{
		System.out.println("\n\n"); //spacing 
		System.out.print("  "); //a small indentation, so that the alphabetical lettering of the columns aligns with the actual symbols.
		for (int i = 65; i < cols + 65; i++)
		{
			System.out.print((char) i); //this prints the alphabetical labeling of columns
		}
		System.out.println(); //Now we go down a line and begin printing the numbers and symbols.

		Node<Seat> current = head;
		String build = "";
		int rowCounter = 1;
		char comparing; //This the character we use to determine if we print a # or . 
		for (int i = 0; i < rows; i++)
		{
			build += rowCounter + " "; //Printing the vertical numbering of rows on the side. We leave an indentation after it for neatness. 
			for (int j = 0; j < cols; j++)
			{ //
				comparing = current.getPayload().getTicketType();
				if (comparing == 'A' || comparing == 'C' || comparing == 'S') //We compare the value of the payload, to our knowledge of ticket types.
					build+= "#"; //If there is ANYTHING but '.', then the seat is probably taken. So we just represent it as a '#' for simplicity.
				else
					build+= "."; //Otherwise, it's open. Or just a '.'
				
				current = current.getNext();
			}
			current = head.getDown(); //Resetting the current node back to its default head.down position.
			for (int k = 1; k < rowCounter; k++) //We did the line above, in order to maket this part easier. 
			{
				current = current.getDown(); //Iterating one extra time down the array.
			}
			build+="\n"; //once we reach the end of the line, we start a new line. 
			rowCounter++;
		}
		return build;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
