/**
 * ListTest.java
 * 
 * An interactive driver program to test list functions.
 * This program is good to run with the debugger.
 * 
 * Based on earlier demos by THC, Scot Drysdale, and others.
 *   
 * @author Scot Drysdale 
 */
import java.util.Scanner;

public class ListTest {

    public static void main(String args[]) {
        // Create a list to play with, initially empty.
        CS10LinkedList<String> theList = new HeaderSllExCred<String>();

        char command; // a command
        String name;  // a name
        Scanner input = new Scanner(System.in);

        do {
            System.out.print("Command (q, C, a, F, L, c, r, p, f, l, n, h, H, g, s, ?): ");
            command = input.nextLine().charAt(0);

            switch (command) {
            case 'q': // Quit
                System.out.println("Bye!");
                break;

            case 'C': // Clear
                theList.clear();
                break;

            case 'a': // Add
                System.out.print("Enter name: ");
                name = input.nextLine();
                theList.add(name);
                break;

            case 'F': // Add first
                System.out.print("Enter name: ");
                name = input.nextLine();
                theList.addFirst(name);
                break;

            case 'L': // Add last
                System.out.print("Enter name: ");
                name = input.nextLine();
                theList.addLast(name);
                break;

            case 'c': // Contains
                System.out.print("Enter name: ");
                name = input.nextLine();
                if (theList.contains(name))
                    System.out.println("Found " + name);
                else
                    System.out.println("Didn't find " + name);
                break;

            case 'r': // Remove
                theList.remove();
                break;

            case 'p': // Print
                if (theList.isEmpty())
                    System.out.println("List is empty");
                else
                    System.out.print(theList);
                break;

            case 'f': // Head
                System.out.println(theList.getFirst());
                break;

            case 'l': // Tail
                System.out.println(theList.getLast());
                break;

            case 'n': // Next
                System.out.println(theList.next());
                break;

            case 'h': // Has Next?
                if (theList.hasNext())
                    System.out.println("The list has a next element");
                else
                    System.out.println("The list does not have a next element");
                break;

            case 'H': // Has Next?
                if (theList.hasCurrent())
                    System.out.println("The list has a current element");
                else
                    System.out
                            .println("The list does not have a current element");
                break;

            case 'g': // Get Current
                System.out.println(theList.get());
                break;

            case 's': // Set current
                System.out.print("Enter name: ");
                name = input.nextLine();
                theList.set(name);
                break;

            case '?': // Print all the commands
                System.out.println("Commands are\n  q: quit\n  C: clear\n  a: add\n  "
                                + "F: addFirst\n  L: addLast\n  c: contains\n  r: remove\n  p: print\n  "
                                + "f: getFirst\n  l: getLast\n  n: next\n  h: hasNext\n  H: hasCurrent\n  "
                                + "g: getCurrent\n  s: setCurrent\n  ?: print this command list\n");
                break;

            default:
                System.out.println("Huh?");
            }
        } while (command != 'q');
    }
}
