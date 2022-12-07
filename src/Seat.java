//Abe Hamed, azh210000

public class Seat {

	private int row;
	private char seat;
	private char ticketType;
	
	//This default constructor will create a seat object that is NOT on the auditorium grid.
	//This is because we as programmers do not want an undefined seat in the middle of a live auditorium linkedList.
	//Thus, if no parameters are given, a seat with row -1 (off the grid) will be made. 
	public Seat()
	{
		row = -1;
		seat = ' ';
		ticketType = '.';	
	}
	
	//This overloaded constructor will simply set all of the fields to the given paramters.
	//There will be no partial overloaded constructors, simply because we will rarely ever initialize a seat without its full fields. 
	//A for adult, S for senior, and C for child
	public Seat(int r, char s, char t)
	{
		row = r;
		seat = s;
		ticketType = t;
	}
	
	//MUTATORS
	
	//This method will set the row of a seat
	public void setRow(int r)
	{
		row = r;
	}
	
	//This method will set the column of a seat
	public void setSeat(char s)
	{
		seat = s;
	}
	
	//This method will set the ticket type of a seat. A for adult, S for senior, and C for child
	public void setTicketType(char t)
	{
		ticketType = t;
	}
	
	//ACCESSORS
	
	//This method simply returns the row of the instance
	public int getRow()
	{
		return row;
	}
	
	//This method simply returns the column of the instance
	public char getSeat()
	{
		return seat;
	}
	
	//This method simply returns the type of the instance. A for adult, S for senior, and C for child
	public char getTicketType()
	{
		return ticketType;
	}
	
	//extremely basic toString method that will just return the ticket type. 
	public String toString()
	{
		return ticketType + "";
	}
	//To string method that also outputs the row and seat.
	//Neatly formatted for whatever the user may find use for. 
	//Used mostly for testing, not a waste of memory! 
	public String toStringExtraInfo()
	{
		String actualType = "";
		
		//This makes the output a little cleaner. Just prints out the full "adult" instead of just A, etc.
		switch (ticketType) {
		case 'A': 
			actualType = "Adult  ";
			break;
		case 'S':
			actualType = "Senior ";
			break;
		case 'C':
			actualType = "Child  ";
			break;
		case '.':
			actualType = "*OPEN* ";
			break;
		}
		
		return "Row: " + row + " | Seat: " + seat + " | " + actualType;
	}

	
}

























