/**
 * This class implements a node class for the linked list 
 * @Author Sunny Srinidhi 
 * 
 * Adapted from: 
 * https://blog.contactsunny.com/tech/circular-double-linked-list-implementation-in-java 
 */

public class Node<T> {

    private T data;
    private Node<T> next;
    private Node<T> previous;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }
}