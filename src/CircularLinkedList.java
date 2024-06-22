/**
 * This class implements a circular doubly linked list 
 * @Author Christian Lentz and Nolan Meyer
 * 
 * Adapted from: Sunny Srinidhi 
 * https://blog.contactsunny.com/tech/circular-double-linked-list-implementation-in-java 
 */

public class CircularLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;


    public void push(T value) {

        // If the list is empty, the head will be null.
        // So create a new node for the head, add the value,
        // and then make the tail the same as the head.
        if (this.head == null) {
            this.head = new Node<T>();
            this.head.setData(value);
            this.tail = this.head;
        } else {
    
            // If the head is not empty, it means that there are already
            // node in the list. So we create a new node, save the value,
            // add it in front of the head, make the head the tail connections.
            Node<T> newNode = new Node<T>();
            newNode.setData(value);
            newNode.setNext(this.head);
            newNode.setPrevious(this.tail);
    
            // We also change the links in the current head and tail, 
            // so that we don't lose any references.
            this.head.setPrevious(newNode);
            this.tail.setNext(newNode);
    
            // It is also important to make the new node the head,
            // so that when we add a new node later, we're  maintaining
            // the chain properly.
            this.head = newNode;
        }
    }


    public Node<T> getHead(){
        Node<T> currentNode;
        if (this.head != null) {
            currentNode = this.head;
        }
        else{
            currentNode = null;
        }
        return currentNode;
    }


    public void traverse() {
        if (this.head != null) {
            Node<T> currentNode = this.head;
            System.out.print("Forward traverse: ");
            while (currentNode != null) {
                System.out.print(currentNode.getData() + " -> ");
                if (currentNode.getNext() == this.head) {
                    break;
                }
                currentNode = currentNode.getNext();
            }
            System.out.println("END");
    
        } else {
            System.out.println("Linked list is empty.");
        }
    }
    

    public void reverseTraverse() {
        if (this.tail != null) {
            Node<T> currentNode = this.tail;
            System.out.print("Reverse traverse: ");
            while (currentNode != null) {
                System.out.print(currentNode.getData() + " -> ");
                if (currentNode.getPrevious() == this.tail) {
                    break;
                }
                currentNode = currentNode.getPrevious();
            }
            System.out.println("END");
    
        } else {
            System.out.println("Linked list is empty.");
        }
    }
    

    public void addToTail(T value) {
        if (this.head == null) {
            this.head = new Node<T>();
            this.head.setData(value);
            this.tail = this.head;
        } else {
            Node<T> newNode = new Node<T>();
            newNode.setData(value);
            newNode.setPrevious(this.tail);
            newNode.setNext(this.head);
            this.tail.setNext(newNode);
            this.tail = newNode;
        }
    }
    

    public void delete(T value) {
        if (this.head != null) {
            Node<T> currentNode = this.head;
            Node<T> previousNode = this.head;
            while (currentNode != null) {
                if (currentNode.getPrevious() != null) { 
                    previousNode = currentNode.getPrevious();
                }
                if (currentNode.getData() == value) {
                    previousNode.setNext(currentNode.getNext());
                    if (currentNode.getNext() != null) {
                        currentNode.getNext().setPrevious(previousNode);
                    }
                    System.out.println("Deleted first node with value " + value);
                    break;
                } else {
                    previousNode = currentNode;
                    currentNode = currentNode.getNext();
                }
            }
        }
    }
    

    public void addAfterIndex(T value, int index) {
        if (this.head == null) {
            this.head = new Node<T>();
            this.head.setData(value);
            this.tail = this.head;
        } else {
            int nodeIndex = 0;
            Node<T> currentNode = this.head;
            while (index > nodeIndex) {
                if (currentNode.getNext() != null) {
                    currentNode = currentNode.getNext();
                }
                nodeIndex++;
            }
            if (nodeIndex == index) {
                Node<T> newNode = new Node<T>();
                newNode.setData(value);
                newNode.setNext(currentNode.getNext());
                newNode.setPrevious(currentNode);
                if (currentNode.getNext() != null) {
                    currentNode.getNext().setPrevious(newNode);
                }
                currentNode.setNext(newNode);
            }
        }
    }
    

    public void deleteNodeAtIndex(int index) {
        if (this.head != null) {
            int nodeIndex = 0;
            Node<T> currentNode = this.head;
            Node<T> previousNode = this.head;
            while (nodeIndex != index) {
                previousNode = currentNode;
                if (currentNode.getNext() != null) {
                    currentNode = currentNode.getNext();
                }
                nodeIndex++;
            }
            previousNode.setNext(currentNode.getNext());
            if (currentNode.getNext() != null) {
                currentNode.getNext().setPrevious(previousNode);
            }
        }
    }
    

    public void deleteNodeAfterIndex(int index) {
        if (this.head != null) {
            int nodeIndex = 0;
            Node<T> currentNode = this.head;
            while (nodeIndex != index) {
                if (currentNode.getNext() != null) {
                    currentNode = currentNode.getNext();
                }
                nodeIndex++;
            }
            if (currentNode.getNext() != null) {
                Node<T> nodeToBeDeleted = currentNode.getNext();
                currentNode.setNext(nodeToBeDeleted.getNext());
                if (nodeToBeDeleted.getNext() != null) {
                    nodeToBeDeleted.getNext().setPrevious(nodeToBeDeleted.getPrevious());
                }
            } else {
                currentNode.setNext(null);
            }
        }
    }
}