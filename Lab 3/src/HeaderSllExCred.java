
/**
 * HeaderSLL.java
 * 
 * Implementation of singly linked list. WARNING: This implementation is
 * guaranteed to work only if always given immutable objects (or at least ones
 * that do not change).
 * 
 * @author THC
 * @author Scot Drysdale converted to Java
 * @author Scot Drysdale, THC have made a number of modifications.
 * @author Scot Drysdale most recently modified on 1/12/2011
 */
public class HeaderSllExCred<T> implements CS10LinkedList<T> {

    // Instance variables
    private Element<T> currentPred;// current position in the list
    private Element<T> head;    // head of list
    private Element<T> tailPred;    // tail of list, refers to element before last

    /**
     * A private class inner representing the elements in the list.
     */
    private static class Element<T> {

        // Because this is a private inner class, these can't be seen from
        // outside SLL.
        private T data;          // reference to data stored in this element
        private Element<T> next; // reference to next item in list

        /**
         * Constructor for a linked list element, given an object.
         * 
         * @param obj
         *            the data stored in the element.
         */
        public Element(T obj) {
            next = null; // no element after this one, yet
            data = obj;  // OK to copy reference, since obj references an immutable object
        }

        /**
         * @return the String representation of a linked list element.
         */
        public String toString() {
            return data.toString();
        }
    }

    /**
     * Constructor to create an empty singly linked list.
     */
    public HeaderSllExCred() {
        clear();
    }

    /**
     * @see CS10LinkedList#clear()
     */
    public void clear() {
        // No elements are in the list, so everything is null.
    	head = new Element<T>(null); //dummyhead, null data
    	currentPred = head; //sets currentPred to head when empty so CP is not null
    	head.next = null; //head.next is null
        tailPred = head; //since there are no elements, tailPred is at the head
    }

    /**
     * @see CS10LinkedList#add()
     */
    public void add(T obj) {
        Element<T> x = new Element<T>(obj); // allocate a new element

        // There are two distinct cases, if hasCurrent (empty) or doesn't have current (not empty)
        if (hasCurrent()) { 
            // The new element is not the new head.
            x.next = currentPred.next.next; // fix the next reference for the new element
            currentPred.next.next = x;      // fix the next reference for current element
            currentPred = currentPred.next; //advances the current pred
            if (x.next == null){ //makes sure you are adding to the end of the list
            	tailPred = currentPred;} //adjusts the position of the tailPred
        } 
        else {
            currentPred.next = x; //sets current to new object
            tailPred = currentPred;//adjusts the position of the tailPred
            
        }
        

    }

    /**
     * * @see CS10LinkedList#remove()
     */
    public void remove() {

        if (!hasCurrent()) { // check whether current element exists
            System.err.println("No current item");
            return;
        }
        else{
        currentPred.next = currentPred.next.next;} // splice current out of list
        
    	Element<T> x;
    	for (x= head.next; x.next.next != null; x = x.next) //runs to the element before the last element
    		;
    	tailPred= x; //sets tailPred as this element
    }
    	

    

    /**
     * @return the String representation of this list.
     */
    public String toString() {
        String result = "";
        for (Element<T> x = head.next; x != null; x = x.next) 
            result += x.data + "->"; 
        result += "[/]";

        return result;
    }

    /**
     * @see CS10LinkedList#contains()
     */
    public boolean contains(T obj) {
        Element<T> x;

        for (x = head.next; x != null && !x.data.equals(obj); x = x.next)
            ;

        // We dropped out of the loop either because we ran off the end of the
        // list (in which case x == null) or because we found s (and so x != null).
        if (x != null) {
            currentPred = x;//Sets currentPred to x
            } 

        return x != null; //returns whether x has a value
    }

    /**
     * @see CS10LinkedList#isEmpty()
     */
    public boolean isEmpty() {
        return head.next == null; //if there is no element after dummyHead, list is empty
    }

    /**
     * @see CS10LinkedList#hasCurrent()
     */
    public boolean hasCurrent() {
        return currentPred.next != null; //if value after currentPred has no next value
    }

    /**
     * @see CS10LinkedList#hasNext()
     */
    public boolean hasNext() {
        return hasCurrent() && currentPred.next.next != null; //if has a current, and there is value after current
    }

    /**
     * @see CS10LinkedList#getFirst()
     */
    public T getFirst() {
        if (isEmpty()) {
            System.err.println("The list is empty");
            return null;
        }
        currentPred = head; //value after head is first element
        return get();
    }

    /**
     * @see CS10LinkedList#getLast()
     */
    public T getLast() {
        if (isEmpty()) {
            System.err.println("The list is empty");
            return null;
        }
        if (!hasCurrent() && !isEmpty()){ //if current is lost but the list is not empty
        	Element<T> x;
        	for (x= head.next; x.next != null; x = x.next)
        		;
        	return x.data;
        }
        else {
        	currentPred = tailPred; //Instead of looping through, we can just set the currentPred equal to tailPred to find the last
            }
            	
            return get();
            }

    /**
     * @see CS10LinkedList#addFirst()
     */
    public void addFirst(T obj) {
    	Element<T> x = new Element<T>(obj); //creates new element
    	x.next = head.next; //sets pointer of new element to value after head
    	head.next = x; //sets value after head to x
        currentPred = head; //changes the current
    }

    /**
     * @see CS10LinkedList#addLast()
     */
    public void addLast(T obj) {
        if (isEmpty())
            addFirst(obj);
        else {
            getLast();
            add(obj);
        }
    }

    /**
     * @see CS10LinkedList#get()
     */
    public T get() {
        if (hasCurrent()) {
            return currentPred.next.data; //returns current
        } else {
            System.err.println("No current item");
            return null;
        }

    }

    /**
     * @see CS10LinkedList#set()
     */
    public void set(T obj) {
        if (hasCurrent())
            currentPred.next.data = obj; //sets obj as data of current
        else
            System.err.println("No current item");
    }

    /**
     * @see CS10LinkedList#next()
     */
    public T next() {
        if (hasNext()) {
            currentPred = currentPred.next; //switches currentPred to the next currentPred
            return currentPred.next.data; //gets data of currentPred
        } else {
            System.err.println("No next item");
            return null;
        }
    }
}
