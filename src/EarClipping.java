import java.util.*;
import edu.macalester.graphics.*;

/**
 * This class implements the Ear Clipping Algorithm 
 * @Author Christian Lentz and Nolan Meyer 
 */
public class EarClipping {
    
    /**
     * Triangulate a convex polygon using ear clipping 
     * @param V Linked List representing convex polygon 
     * @return A list holding a list of diagonals and a list of triangles 
     */
    public static ArrayList<ArrayList<ArrayList<Point>>> clipEars(ArrayList<Point> V) { 
        
        // Create circular doubly linked list of points, and list to store diagonals
        CircularLinkedList<Point> vertices = new CircularLinkedList<>();
        ArrayList<ArrayList<Point>> diagonals = new ArrayList<ArrayList<Point>>();
        ArrayList<ArrayList<Point>> triangles = new ArrayList<ArrayList<Point>>(); 
        ArrayList<ArrayList<ArrayList<Point>>> retLists = new ArrayList<ArrayList<ArrayList<Point>>>(); 
    
        // Add points to linked list
        for (int i = V.size()-1; i >= 0; i--) {
            vertices.push(V.get(i));
        }
        
        // Initialize starting conditions
        int vertices_size = V.size();
        Node<Point> headNode = vertices.getHead();
        
        while (vertices_size > 3){

            // Get the set of 3 points we are considering as a potential ear
            Point v0;
            Point v1;
            Point v2;
            v0 = headNode.getPrevious().getData();
            v1 = headNode.getData();
            v2 = headNode.getNext().getData();

            // Create and check the diagonal 
            // These are more helpful if the polyogn is not convex!
            Line diagonal = new Line(v0, v2);
            Boolean is_valid_diagonal = true;

            // If it is a valid diagonal, add it to the list and remove the central vertex from the linked list of points
            if (is_valid_diagonal){
                // add a diagonal
                ArrayList<Point> a_diagonal = new ArrayList<>();
                a_diagonal.add(v0);
                a_diagonal.add(v2);
                diagonals.add(a_diagonal);
                // add a triangle 
                ArrayList<Point> a_triangle = new ArrayList<>();
                a_triangle.add(v0); 
                a_triangle.add(v1); 
                a_triangle.add(v2); 
                triangles.add(a_triangle); 
                // delete the point the diagonal skipped 
                vertices.delete(v1);
                V.remove(v1);
                vertices_size = vertices_size - 1;
            }   
            
            headNode = headNode.getNext().getNext();
        } 

        // add the last triangle
        ArrayList<Point> a_triangle = new ArrayList<>();
        a_triangle.add(V.get(0)); 
        a_triangle.add(V.get(1)); 
        a_triangle.add(V.get(2)); 
        triangles.add(a_triangle);

        // return the lists 
        retLists.add(diagonals); 
        retLists.add(triangles);
        return retLists; 
    }
}
