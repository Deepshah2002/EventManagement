package EventManagement;
/*
Event Handler

Features
- Event Creation
- Event Listing
- Event Reminders
- Event Editing and Deletion
- Search and Filtering

Optional
- Integration with Calendar Apps
- Categories and Labels
- User Authentication and Personalization
- Recurring Events

Optimisation
- If user enters time in different format other than am/pm or 24-hour.


Methods
- addEvent
- deleteEvent
- editEvent//resume
- notify
- findEvent
- display
 */

/* Imports */
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/* Event Class */
class Event
{
    /* Global */
    /* Initializing the Node head and tail */
    Node head = null;
    Node tail = null;
    Scanner scan = new Scanner(System.in);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    /*Initial value of id is 0 and it increments whenever new node object is created. */
    static public int id;

    static class Node
    {
        /* Node Variables */
        int ID;
        String eventName;
        LocalTime startTime;
        LocalTime endTime;
        LocalDate eventstartDate;
        LocalDate eventendDate;
        Node prev;
        Node next;

        /* Node Constructor */
        Node(int ID,String eventName, LocalTime startTime, LocalTime endTime, LocalDate eventstartDate, LocalDate eventendDate)
        {
            this.ID = ID;
            this.eventName = eventName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.eventstartDate = eventstartDate;
            this.eventendDate = eventendDate;

        }
    }


    /* Check for time format */
    static LocalTime parseTime(String Time) {
        /*
        Parse any time to given format. h:mm a - means 12h format with am/pm marker.
        H:m means 24h format without am/pm marker.
        Returns Local time object.
        */
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            return LocalTime.parse(Time, formatter);
        }
        catch (Exception e)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m");
            return LocalTime.parse(Time, formatter);
        }
    }

    /* Check for Date format */
    static LocalDate parseDate(String Date)
    {
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(Date, formatter);
        }
        catch (Exception e)
        {
            // Handle the exception or provide an alternative format if needed
            e.printStackTrace();
            return null;
        }

    }

    /* Create new event */
    public void addEvent()
    {
        /* Accept Event Name */
        System.out.print("Enter event name you want to add: ");
        String eventName = scan.nextLine();

        /* Accept Start Time, Parse and Print */
        System.out.print("Enter event start time (h:mm am/pm or H:m): ");
        String sTime = scan.nextLine();
        LocalTime startTime = parseTime(sTime);
        System.out.println(startTime);

         /* Accept End Time, Parse and Print */
        System.out.print("Enter event end time (h:mm am/pm or H:m): ");
        String eTime = scan.nextLine();
        LocalTime endTime = parseTime(eTime);
        System.out.println(endTime);

        /* Accept Start Date and Parse*/
        System.out.print("Kindly Enter event start date in dd-MM-YYYY format: ");
        String sDate = scan.nextLine();
        LocalDate eventstartDate = parseDate(sDate);

        /* Accept End Date and Parse*/
        System.out.print("Kindly Enter event end date in dd-MM-YYYY format: ");
        String eDate = scan.nextLine();
        LocalDate eventendDate = parseDate(eDate);

        /* If end date is before start date then add 1 day to end date ---> what if the end date is 3 days before start date */
        /*
           Calculates the number of days between eventendDate and eventstartDate and then adds the difference to end date if its before eventstartDate
           and also adds 1 to the final value to make it more than eventstartDate
        */

        try
        {
            if (eventstartDate.equals(eventendDate)) {
                if (endTime.isBefore(startTime)) {
                    eventendDate = eventendDate.plusDays(1);
                    System.out.println(eventendDate);
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Invalid date format. Please enter a date in the format dd-MM-yyyy.");
        }

        /* New Node Object */
        Node node = new Node(++id, eventName, startTime, endTime, eventstartDate, eventendDate);//2011

        /* If head doesn't exists which means there is no event then initialize head by assigning the first even to head
           also make the head as the tail because it is the first event.
         */
        if (head==null)
        {
            node.next = null;
            node.prev = null;
            head = node;
            tail = head;
        }

        /* If head exists then assign the current node (i.e. the node after or before which we will add a new event. */
        else
        {
            //2012 2015 2013
            Node currentNode = head;
            while(currentNode!=null)
            {

                /* Compare the eventstartDate of currentNode with the node we are adding. */
                int compare = currentNode.eventstartDate.compareTo(node.eventstartDate);

                /* If currentNode date is after node..; (2013) 2015; Node to be added is in bracket.*/
                if (compare > 0)
                {
                    /* If there is no other event before currentNode (Maybe it is the first event),
                       then assign currentNode address to next of node that we are adding and assign
                        node address to the prev of currentNode. Also now the new head is the node
                        that we added.
                    */


                    /* Since currentNode keeps changing with each iteration because we check where we can add the new
                    node, we need to check everytime if the currentNode exists or not.*/
                    if (currentNode.prev==null)
                    {
                        node.next = currentNode;
                        currentNode.prev = node;
                        head=node;
                    }

                    /*
                    If there is an event before currentNode, then create a temporary node and assign the address of the
                    node before currentNode to that temporary node. Now the currentNode prev will contain the address of
                    the node we are adding and the node next will contain the address of the currentNode.
                    */
                    else
                    {
                        Node temp = currentNode.prev;
                        currentNode.prev = node;
                        node.prev = temp;
                        temp.next = node;
                        node.next = currentNode;
                    }
                    break;
                }

                /*
                If the currentNode date is before new node date then move to the next node for checking same
                conditions; 2013 (2015); Node to be added is in bracket.
                */
                else if (compare < 0)
                {
                    currentNode = currentNode.next;
                }
                /* If currentNode date is same as node date then compare startTime */
                else
                {
                    int compareTime = currentNode.startTime.compareTo(node.startTime);

                    /* If currentNode startTime is after node startTime.. */
                    if (compareTime > 0)
                    {
                        /*
                        If there is no other event before currentNode (Maybe it is the first event),
                        then assign currentNode address to next of node that we are adding and assign
                        node address to the prev of currentNode. Also now the new head is the node
                        that we added.
                        */
                        if (currentNode.prev==null)
                        {
                            node.next = currentNode;
                            currentNode.prev = node;
                            head = node;
                        }

                        /*
                        If there is an event before currentNode, then create a temporary node and assign the address of the
                        node before currentNode to that temporary node. Now the currentNode prev will contain the address of
                        the node we are adding and the node next will contain the address of the currentNode.
                        */
                        else
                        {
                            Node temp = currentNode.prev;
                            currentNode.prev = node;
                            node.prev = temp;
                            temp.next = node;
                            node.next = currentNode;
                        }
                        break;
                    }

                    /*
                    If the currentNode time is before new node time then move to the next node for checking same
                    conditions; 2013 (2015); Node to be added is in bracket.
                    */
                    else if (compareTime < 0)
                    {
                        currentNode = currentNode.next;
                    }

                    /* If currentNode time is same as node time then just add node after already currentNode  */
                    else
                    {
                        /* Adding a new event if there is no other event after currentNode*/
                        //2011 (2013)
                        Node temp = currentNode.next;
                        currentNode.next = node;
                        node.prev = currentNode;

                        /* Adding a new event between 2 events*/
                        // 2011 (2013) 2015
                        if (temp!=null)
                        {
                            node.next = temp;

                            //2015.prev
                            temp.prev = node;
                        }
                        break;
                    }

                }

            }

            /* If the event we are trying to add is greatest among all the events then just make it the tail. */
            if (currentNode == null)
            {
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
        }

    }
    public void deleteEvent()
    {
        System.out.print("Enter event id to be deleted: ");
        int id = scan.nextInt();

        Node temp = head;

        /* currentNode.prev and currentNode.next respectively */
        Node previous,ahead;

        /* If head exists.. */
        while(temp!=null)
        {
            /* If the ID entered matches one of the ids of the events in the Doubly Linked List then assign the prev
            and next addresses of the currentNode to the respective variables. */
            if (temp.ID == id)
            {
                previous = temp.prev;
                ahead = temp.next;

                /* Check if there is no other event before or after the event being deleted. */
                if (ahead==null && previous==null)
                {
                    head = null;
                    tail = null;
                    break;
                }
                /* If there is a node before the event being deleted and there is no event after it.. */
                else if (ahead == null)
                {
                    previous.next = null; //Assign the next of previous node as null
                    tail = previous; //Assign the tail as the previous node
                    break;
                }

                /* If there is a node after the event being deleted and there is no event before it.. */
                else if (previous == null)
                {
                    ahead.prev = null; //Assign the prev of the next node as null.
                    head = ahead; //Assign the head as the next node.
                    break;
                }
                /* If there is an event both before and after the event being deleted..*/
                else
                {
                    /* Assign the address of the next event to the event being deleted, to the "next" of previous
                    event and Assign the address of the previous event to the "prev" of the next event. */
                    previous.next = ahead;
                    ahead.prev = previous;
                    break;
                }
            }

            /* If the ID is found then delete the event else move to the next event..*/
            temp = temp.next;
        }
        /* If there is no matching ID then error. */
        if (temp == null){
            System.out.println("ID doesn't exist.");
        }
    }

    public void editEvent()
    {
        displayEvents();
        System.out.print("Enter the ID of the event you want to edit: ");
        int ID = scan.nextInt();

            System.out.println("1. Update Event Name " +
                    "2. Update Event Timing  " +
                    "3. Update Event Date  " +
                    "4. Exit");

            System.out.print("Enter your choice: ");
            Scanner scan = new Scanner(System.in);
            int choice = scan.nextInt();

            Node currentNode = head;
            switch (choice) {
                case 1 :
                    while(currentNode!=null)
                    {
                        if (currentNode.ID == ID)
                        {
                            System.out.print("Enter new event name: ");
                            scan.nextLine();
                            currentNode.eventName = scan.nextLine();
                            break;
                        }
                        currentNode = currentNode.next;
                    }
                    if (currentNode == null){
                        System.out.println("ID doesn't exist.");
                    }
                    break;

                case 2 :
                    while(currentNode!=null)
                    {
                        if (currentNode.ID == ID)
                        {
                            System.out.print("Enter new start time:");
                            currentNode.startTime = parseTime(scan.nextLine());
                            System.out.print("Enter new end time:");
                            currentNode.endTime = parseTime(scan.nextLine());
                            break;
                        }
                        currentNode = currentNode.next;
                    }
                    if (currentNode == null){
                        System.out.println("ID doesn't exist.");
                    }
                    break;
                case 3 :
                    while(currentNode!=null)
                    {
                        if (currentNode.ID == ID)
                        {
                            System.out.print("Enter new start date:");
                            currentNode.eventstartDate = parseDate(scan.nextLine());
                            System.out.print("Enter new end date:");
                            currentNode.eventstartDate = parseDate(scan.nextLine());
                            break;
                        }
                        currentNode = currentNode.next;
                    }
                    if (currentNode == null){
                        System.out.println("ID doesn't exist.");
                    }
                    break;

                case 4 : break;
                default : System.out.println("Enter valid choice.");
            }

    }

    public void findEvent(){
        System.out.println("1. Find schedule by Event name  " +
                           "2. Find event by date and time  ");

        System.out.print("Enter your choice: ");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();

        switch (choice) {
            /* Find by Event Name */
            case 1:
                int eventCount = 0;
                Node currentNode = head; //Initializing the currentNode as the first node i.e. head.

                System.out.print("Enter Event name: ");
                scan.nextLine(); //To consume the extra /n after we accepted an integer.
                String EName = scan.nextLine();

                /* If currentNode is not null.. */
                while(currentNode!=null){
                    /*
                    Check if the event name entered by user is the same as currentNode (Event) else move on..
                    then print the start date, end date, start time, end time.
                    */

                    if (currentNode.eventName.equals(EName))
                    {
                        eventCount++;
                        System.out.println("Event Name: " + currentNode.eventName + "\nDate " +
                                currentNode.eventstartDate.format(dateFormatter) + " To " + currentNode.eventendDate.format(dateFormatter) + "\nTimings: "
                                + currentNode.startTime + " To " + currentNode.endTime);
                    }
                    currentNode = currentNode.next; //Move to the next event if current one is not what we want.
                }

                /* If the eventCount is 0 then there is no such event.   */
                if (eventCount==0)
                {
                    System.out.println(EName+" does not exist.");
                }
                break;

            //Find by date and time
            case 2 :
                Node temp = head;
                System.out.print("Enter Event date: ");
                scan.nextLine();
                String eDate = scan.nextLine();
                LocalDate epDate = parseDate(eDate);


                while (temp!=null)
                {
                    /* If the date entered is between the start and end dates.*/
                    if ((temp.eventstartDate.isEqual(epDate) || temp.eventstartDate.isBefore(epDate)) &&
                            (temp.eventendDate.isEqual(epDate) || temp.eventendDate.isAfter(epDate)))
                    {
                        System.out.println(temp.eventName+" already exists between these dates.");
                    }
                    else if(temp.eventstartDate.isEqual(temp.eventendDate))
                    {
                            System.out.print("Enter Event time: ");
                            String eTime = scan.nextLine();
                            LocalTime epTime = parseTime(eTime);
                        if((temp.startTime.equals(epTime) || temp.startTime.isBefore(epTime)) &&
                                (temp.endTime.equals(epTime) || temp.endTime.isAfter(epTime)))
                        {
                            System.out.println(temp.eventName+" already exists in this time slot.");
                        }
                        else
                        {
                            System.out.println("There is no event at specified date or time.");
                        }
                    }
                    else
                    {
                            System.out.println("There is no event at specified date or time.");
                    }





                    temp = temp.next;
                }


//                Date eDate = format.parse(scan.nextLine());
                //resume - try catch
                break;
//            case 3 -> e.editEvent();
//            case 4 -> e.findEvent();
//            case 5 -> e.displayEvents();
            default : System.out.println("NOT FOUND");
        }
    }

    public void displayEvents()
    {
        Node temp=head;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (temp == null)
        {
            System.out.println("No events found.");
            return;
        }
        while(temp!=null)
        {
            System.out.println("Event ID.: " +temp.ID);
            System.out.println("Event Name: " +temp.eventName);
            System.out.println("Start Date: " +temp.eventstartDate.format(dateFormatter));
            System.out.println("End Date: " +temp.eventendDate.format(dateFormatter));
            System.out.println("Start Time: " +temp.startTime);
            System.out.println("End Time: " +temp.endTime);

            temp=temp.next;
        }
    }
}

/* Main Class */
public class EventManager {
    public static void main(String[] args) {
        Event e = new Event();

        while (true) {
            System.out.println("1. Add event  " +
                               "2. Delete event  " +
                               "3. Edit event  " +
                               "4. Find Event  " +
                               "5. Display Event "+
                               "6. Exit"
                                );

            System.out.print("Enter your choice: ");
            Scanner scan = new Scanner(System.in);
            int choice = scan.nextInt();

            switch (choice) {
                case 1 -> e.addEvent();
                case 2 -> e.deleteEvent();
                case 3 -> e.editEvent();
                case 4 -> e.findEvent();
                case 5 -> e.displayEvents();
                case 6 -> System.exit(0);
                default -> System.out.println("Enter valid choice.");
            }
        }
    }
}
