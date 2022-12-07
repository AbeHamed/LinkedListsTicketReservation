//Abe Hamed, azh210000

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class Main 
{
	public static void main(String[] args)
	{
		//declaring the Scanners that I will be using to pass to other methods and classes. 
		//The System.in Scanner will be passed exclusively, and I will not be making another object so that Zybooks doesn't mess up. 
		Scanner kbd = new Scanner(System.in);
		Scanner inFile = null;
		File myFile = null;
		String fileChoice = "";
		int menuChoice = 1;
		
		//All declarations are -1, so that we will forcibly enter the while loops for input validation that have methods designed 
		//to return -1 if given a garbage value.
		int adultChoice = -1;
		int childChoice = -1;
		int seniorChoice = -1;
		int rowChoice = -1;
    	int colChoice = -1;
    	int answerToYN = -1;
		
		//This while (true) loop is designed to terminate once the user enters the correct filename.
		//Using exception handling, I can prompt the user to enter the correct file without crashing the program. 
		while (true)
		{
			try
			{
				System.out.println("Enter filename: ");
				fileChoice = kbd.nextLine();
				myFile = new File(fileChoice);
				inFile = new Scanner(myFile); //if we get pas this line, that means that the input is successful.
				break;  //Breaks out of the input validation loop, because the user will have picked the correct file at this stage of code. 
			}
			catch (FileNotFoundException e)
			{
				System.out.println("File not found."); //Error mesage for when the user doesn't enter a found file name
			}
		}
		
		System.out.println("\n"); //Spacing
		
		//Creating a new auditorium object, based on the file provided. 
		Auditorium auditorium = new Auditorium(fileChoice);
		//Remember, this object already has all of the file' contents inside.
		
	    while (menuChoice == 1) //this simply loops the program if the user chooses to. Like a real ticket booth. 
	    {
	    	adultChoice = -1; //These are all -1, in order to force the while loops to begin. For input validation.
	    	childChoice = -1;
	    	seniorChoice = -1;
	    	rowChoice = -1;
	    	colChoice = -1;
	    	answerToYN = -1;
	    	
	    	menuChoice = -1; //Force the next loop to excecute
	    	
	    	//This next loop performs input validation for the user. Explained in greater detail in the method. 
	    	while (menuChoice == -1) // -1 means that the input was invalid. 
				menuChoice = integerUserInput("1. Reserve Seats\n2. Exit", 1, 2, kbd);
	    	
	    	//If the user chooses to Exit at this stage, they will be redirected to the status report, and the program will end. 
	    	//To reiterate, the status report will still print the cost and status of the EXISTING linkedList, even if the user did not 
	    	//add any existing reservations. 
	    	if (menuChoice == 2)
	    	{
	    		statusReport(auditorium);
	    		printToFile(auditorium);
	    		break;
	    	}
	    	
	    	//At this point, the user wants to continue. Thus, we display the auditorium:
	    	
	    	System.out.println(auditorium.basicToString());
	    	
	    	//At this point, the user is picking seats to reserve. We begin input validation. 
	    	
	    	while (rowChoice == -1) //Loops until the input is correct (not -1)
	    		rowChoice = integerUserInput("Enter valid row number: ", 1, auditorium.getRows(), kbd);
	    	
	    	while (colChoice == -1) //Loops until the input is correct (not -1)
	    		colChoice = charUserInput("Enter valid starting seat letter: ", 65, auditorium.getCols() + 64, kbd); //Notice that + 64 and 65 are simply conversions to char
	 
	    	while (adultChoice == -1) //Same as above.
	    		adultChoice = integerUserInput("Enter number of adult tickets: ", 0, auditorium.getCols() + 1 - colChoice, kbd); //We don't want the user to have a selection that extends outside of the auditorium
	        
	    	while (childChoice == -1)
	    		childChoice = integerUserInput("Enter number of child tickets: ", 0, auditorium.getCols() + 1 - colChoice, kbd); //same as above
	    	
	    	while (seniorChoice == -1) //Again, we want to keep looping until the user inputs a non-junk value.
	    		seniorChoice = integerUserInput("Enter number of senior tickets: ", 0, auditorium.getCols() + 1 - colChoice, kbd); //same as above
	    	
	    	int totalChoice = adultChoice + childChoice + seniorChoice;
	    	
	    	//In case the user enters zero total tickets. It would be smart of the program to realize this and loop the user back to the menu,
	    	//so that the program does not "reserve" an empty spot. 
	    	if (totalChoice == 0)
	    	{
	    		System.out.println("No tickets requested. Try again.\n");
	    		continue; //Just loops back to the main menu.
	    	}
	    	//Now, we check if that selection is available or not.
	    	
	    	boolean isAvailable = isAvailable(auditorium, totalChoice, rowChoice, colChoice);
	    	int[] suggestedCoordinates = new int[2]; //This'll hold the coordinates for the ***BEGINNING*** of the selection that the method finds. 
	    	//suggestedCoordinates[0] is the Column, or X coordinate.
	    	//suggestedCoordinates[1] is the Row, or Y coordinate.
	
	    	//Again, if the initial selection is available, we simply book it and move on. 
	    	//There is again no need to initiate the search algorithm.
	    	if (isAvailable)
	    		reserveSeats(auditorium, rowChoice, colChoice, adultChoice, childChoice, seniorChoice);
	    	else //Otherwise, we search for the best possible seat and save it to the variable "suggestedCoordinates". we'll submit this to the user later. 
	    	{ 
	    		suggestedCoordinates = bestSeatFinder(auditorium, adultChoice, childChoice, seniorChoice);
	    		System.out.println("Requested seats unavailable.\n");
	    		
	    		//Now, there is potential the program could not come up with a best available seat selection.
	    		//Thus, if the bestAvailableSeat method returned -1 for either of its coordinates, then we let the user know...
	    		//And, we'll loop back to the main menu to start the cycle again. 
	    		
	    		if (suggestedCoordinates[0] == -1) //"If the program couldn't find any other seats"
	    			System.out.println("No other seats to suggest.\n");
	    		else
	    		{ //This entire row is the formatting for printing out the best avaialable seats. Should look something like this- "3B - 3J"
	    			System.out.println("Best available seats:\n      " + suggestedCoordinates[1] + (char) (suggestedCoordinates[0] + 64) + " - " + suggestedCoordinates[1] + (char) (suggestedCoordinates[0] + 63 + totalChoice));
	    			
	    			//Begin input validation, for 'Y' and 'N'.
	    			while (answerToYN == -1 || (answerToYN != 25 && answerToYN != 14)) //We keep looping until the user gets it right! 24 is Y and 13 is N in ASCII.
	    				answerToYN = charUserInput("\nComplete booking using best available? (Y/N)", 78, 89, kbd);
	    			
	    			if (answerToYN == 25)
	    			{
	    				reserveSeats(auditorium, suggestedCoordinates[1], suggestedCoordinates[0], adultChoice, childChoice, seniorChoice);
	    			}
	    			//ELSE, if they picked 'N', then we simply don't reserve anything. We'll loop right back to the main menu,
	    			//and thus the program is now done!
	    		}	
	    	}	
	    }
	    inFile.close();
	}
	
	//Here, we initially check whether the customer's selection is available or not
    //We are, in other words, just checking to see if the array in the specified position is empty or not.
	//There is no algorithm or complexity here. 
	public static boolean isAvailable(Auditorium aud, int totalChoice, int rowChoice, int colChoice)
	{
		char checking;
		boolean isAvailable = true;
		
		for (int i = colChoice; i < colChoice + totalChoice; i++)
    	{
    		checking = aud.getNodePayload(rowChoice, i).getTicketType();
    		
    		if (checking == 'A' || checking == 'S' || checking == 'C')
    		{
    			isAvailable = false;
    			break;
    		}
    	}
		return isAvailable;
	}
	public static int[] bestSeatFinder(Auditorium aud, int adults, int childs, int seniors)
	{
		//Notice that values are arrays, so that they will have an X and Y component.
		int totalChoice = adults + childs + seniors;
		double thisDistance; //the distance from the current selection to the mid point
		double bestDistance = Math.pow(aud.getRows() * aud.getCols() , 2); //Setting this to an arbitrarily high number, so we can perform a "less than" comparison algorithm for each selection
		double[] theMidPoint = {(aud.getCols() / 2.0) + .5, (aud.getRows() / 2.0) + .5}; //This is the AUDITORIUM's midpoint. 
		double[] currentMidPoint = new double[2]; //Will have an X and Y component.
		int[] chosenCoordinates = {-1, -1}; //Will have an X and Y component. -1 so that default is NOT found
		double[] chosenMidPoint = new double[2];

		for (int i = 1; i <= aud.getRows(); i++) //The row that we are searching on
		{
			for (int j = 1; j <= aud.getCols() - totalChoice + 1; j++) //The number of possible selections on each row. +1 is just for an off by one error.
			{
				//Firstly, checking if the current selection is even available at all. 
				if (isAvailable(aud, totalChoice, i, j))
				{
					//Now, we get the current midpoint of the selection. We'll use this to compute the distance between it
					//and the midpoint of the auditorium. 
					//X Component                                      //Y Component 
					currentMidPoint[0] = (totalChoice / 2.0) + j - .5; currentMidPoint[1] = i; //Dividing by 2, b/c midpoint is just half.
					
					//Now we begin the algorithm. We will calculate the distance between the selection's midpoint and the auditorium midpoint.
					
					//Distance is equal to sqrt((x2 - x1)^2 + (y2 - y1)^2) 
					thisDistance = Math.sqrt(Math.pow(theMidPoint[0] - currentMidPoint[0], 2) + Math.pow(theMidPoint[1] - currentMidPoint[1], 2));
					
					if (thisDistance < bestDistance) 
					{
						//Then this selection is worthy of being the best
						chosenCoordinates[0] = j; chosenCoordinates[1] = i;
						chosenMidPoint[0] = currentMidPoint[0]; chosenMidPoint[1] = currentMidPoint[1];
						bestDistance = thisDistance;
					}
					
					//***After this point, all cases that deal with EQUAL distances, with two selections in a tie. ****
					else if (thisDistance == bestDistance)
					{ //If the distance between the new distance between the selection and the middle row is less than the best selection's distance between the selection and the middle row...
						if (Math.abs(currentMidPoint[1] - theMidPoint[1]) < Math.abs(chosenMidPoint[1] - theMidPoint[1]))
						{
							//Then this selection is worthy of being the best
							chosenCoordinates[0] = j; chosenCoordinates[1] = i;
							chosenMidPoint[0] = currentMidPoint[0]; chosenMidPoint[1] = currentMidPoint[1];
							bestDistance = thisDistance;
						}
						//If they are equal, then we must again dig deeper and apply more tests to find out which selection is more worthy. 
						else if (Math.abs(currentMidPoint[1] - theMidPoint[1]) == Math.abs(chosenMidPoint[1] - theMidPoint[1]))
						{//Now, we check which of those two selections had the smaller row number:
							if (currentMidPoint[1] < chosenMidPoint[1]) //If the new selection does indeed have the smaller row number, and they are equidistant rows from the middle row...
							{
								//Then this selection is worthy of being the best
								chosenCoordinates[0] = j; chosenCoordinates[1] = i;
								chosenMidPoint[0] = currentMidPoint[0]; chosenMidPoint[1] = currentMidPoint[1];
								bestDistance = thisDistance;
							}
							//If their row numbers are equal, or in other words they are both on the row of the midPoint...
							else if (currentMidPoint[1] == chosenMidPoint[1])
							{ //Then we must compare the columns now. We must make sure that the seat with the smaller letter/number is picked as worthy.
								if (currentMidPoint[0] < chosenMidPoint[0])
								{
									//Then this selection is worthy of being the best.
									chosenCoordinates[0] = j; chosenCoordinates[1] = i;
									chosenMidPoint[0] = currentMidPoint[0]; chosenMidPoint[1] = currentMidPoint[1];
									bestDistance = thisDistance;
								}
								//Else: The previously deemed best selection had the smaller column letter. Thus, that selection is more worthy than this one. 
								//Nothing happens. 
							}
						}
						//Or else, the distance between rows is greater than before. Thus, the instructions say that this new selection
						//is not as favorable as the one before. So we do not treat this as a contender.
					}
					//Else, the distance between the midPoint and the new selection is simply greater than the previously deemed best selection.
					//Thus, it is FARTHER away from the midPoint and is NOT a contender. 
				}
				//Else, do nothing. An occupied selection means nothing to us. It is not a contender for distance. 	
			}
		}
		return chosenCoordinates; //This returns -1 if there was no available seat selection at all.
	}
	
	//Method that will acually access the linkedList, and change '.' or empty seats into A, then C, then S sequentially 
	//so that the linkedlist will carry the new objects. 
	public static void reserveSeats(Auditorium aud, int rows, int cols, int adults, int childs, int seniors)
	{ //i starts at cols, because we assume a common starting position. We go from starting, to the end of A, to the end of A start of C, etc 
		for (int i = cols; i < cols + adults; i++) //We loop through the amount of adults we have first, because the guidelines strictly say that adults are on the left.
	    {
	    	aud.setNodePayload(new Seat(rows, (char) (i + 65), 'A'), rows, i); //Keep in mind the + 65 is a conversion to char.
	    }
		for (int i = cols + adults; i < cols + adults + childs; i++)
		{
			aud.setNodePayload(new Seat(rows, (char) (i + 65), 'C'), rows, i);
		}
		for (int i = cols + adults + childs; i < cols + adults + childs + seniors; i++)
		{
			aud.setNodePayload(new Seat(rows, (char) (i + 65), 'S'), rows, i); 
		}
			
		System.out.println("\nBooking complete."); 
		//At this point the entire linkedlist has changed. Remember that only '.' ticketTypes are altered at all. 
	}
		
	
	//Input validation for the user to select their choice to reserve seats or exit.
	//All cases of user input are covered. Entering a string or double will simply print 
	//the error message and loop back to ask again. Gracefully handles all input.
	public static int integerUserInput(String prompt, int lowBound, int highBound, Scanner scnr)
	{
		int temp = 0;
		try
		{
			System.out.println(prompt); //will print out anything that you pass, to PROMPT 
			temp = scnr.nextInt(); //tries to get the integer input
		}
		catch (InputMismatchException e)
		{
			System.out.println("Input should be a number."); //prints passed error message
			scnr.next(); //this clears the buffer of the integer that was read before, because scanners do not advance past the given input if there was an error
			return -1; //-1 means that the user input was unsuccessful.
		}
		
		if (temp > highBound || temp < lowBound) //Checks to see if the input is in bounds. Inclusive.
		{
			System.out.println("Input must be between or including " + lowBound + " and " + highBound + ".");
			return -1; //-1 means that the user input was unsuccessful.
		}
		
		return temp; //if nothing else failed, then we return the value the user inputted.
		
	}
	
	
	//Input validation for the user to select their choice to reserve seats or exit.
    //All cases of user input are covered. Entering a string or double will simply print 
	//the error message and loop back to ask again. Gracefully handles all input.
	
	//The input validation method below is also designed to only allow the range of values that you pass to it. 
	//NOTE: must convert bounds from int to char to properly compare.
	public static int charUserInput(String prompt, int lowBound, int highBound, Scanner scnr)
	{
		char temp = '?'; //Some arbitrary character
		
	    System.out.println(prompt); //will print out anything you pass (the prompt in this case)
	    temp = scnr.next().toUpperCase().charAt(0); //tries to get the char input
		
	    //This if statement has a special purpose- to check from ASCII if the input is a number or not. 
	    //If it is a number, we gracefully handle it and pront out the error message. Remember there are only letters between 65 and 122, so this makes sense. 
		if (temp < 65 || temp > 122)
		{
			System.out.println("Input should be a letter."); //prints error message
			//scnr.next(); //this clears the buffer of the integer that was read before, because scanners do not advance past the given input if there was an error
			return -1; //-1 means that the user input was unsuccessful.
		}
		
		//Now we check the bounds that we put on the input.
		if (temp > highBound || temp < lowBound) //Checks to see if the input is in bounds. Inclusive.
		{
			System.out.println("Input must be between or including " + (char) (lowBound) + " and " + (char) (highBound) + ".");
			return -1; //-1 means that the user input was unsuccessful.
		}
			
		return temp - 64; //if nothing else failed, then we return the value the user inputted.
			
	}

	//this method does ONE task - printing out the amount of tickets and money from the information in the array
	public static void statusReport(Auditorium aud)
	{
		//checking the amount of adults, children, and seniors in the array. 
		int totalSeats = aud.getRows() * aud.getCols();
		int totalTickets = 0;
		int totalAdultTickets = 0;
		int totalChildTickets = 0;
		int totalSeniorTickets = 0;
		double totalSales = 0.0;
			
		final double adultRate = 10.0;
		final double childRate = 5.0;
		final double seniorRate = 7.5;
			
		char temp = ' ';
			
		//This will loop through the entire linkedList via the method in Auditorium. 
		for (int i = 1; i <= aud.getRows(); i++)
		{
			for (int j = 1; j <= aud.getCols(); j++)
			{
				temp = (aud.getNodePayload(i, j)).getTicketType(); //This is pulling the payload char value out of the Seat object of the Node. 
					
				if (temp == 'A') //We increment counters for each of the ticket types, so that later we can print out to the screen how many we have of each.
					totalAdultTickets++;
				else if (temp == 'C')
					totalChildTickets++;
				else if (temp == 'S')
					totalSeniorTickets++;
			}
		}
			
		//now it prints out all of that information onto the screen
			
		totalTickets = totalAdultTickets + totalChildTickets + totalSeniorTickets;
		//Total sales is the number of tickets multiplied by the rates. You may change the rates as you wish.
		totalSales = (totalAdultTickets * adultRate) + (totalChildTickets * childRate) + (totalSeniorTickets * seniorRate);
			
		System.out.println("Total Seats: " + totalSeats);
		System.out.println("Total Tickets: " + totalTickets);
		System.out.println("Adult Tickets: " + totalAdultTickets);
		System.out.println("Child Tickets: " + totalChildTickets);
		System.out.println("Senior Tickets: " + totalSeniorTickets);
		System.out.printf("Total Sales: $%.2f\n", totalSales);
	}
	
	public static void printToFile(Auditorium aud)
	{
		PrintWriter outFile = null; //Declaring a printwriter to output the status of the linked list. 
		try
		{
			outFile = new PrintWriter("A1.txt"); //Users- make sure to enter a file that is not duplicated or corrupted. You may get the error below.
		}
		catch (FileNotFoundException e)
		{ //At this point, the user may have typed some erroneous file type, 3 lines above. They need to change the output file.
			System.out.println("Hardcoded error - output file missing.");
		}
		
		//the aud.toString() method is designed to print out the linked list of seats in a fasion that does not have spaces between the characters, and representing
		//each ticket type with an 'A' or 'S' or 'C' wihout the blanket usage of '#'
		outFile.print(aud.toString());
		outFile.close();
	}
}
	
	


