import java.util.*;
import edu.macalester.graphics.*;

/**
 * This class finds the convex hull of a set of points passed to it using 
 * the Quick Hull Algorithm and Selection Sort
 * @Author Christian Lentz and Nolan Meyer 
 * 
 * Inspired by psuedocode from the Macalester Comp 221 curriculum 
 */
public class QuickHull {

    // an array of points to hold the randomly generated points
    Point[] pointArr; 

    // constructor 
    public QuickHull(ArrayList<Point> randomPoints) { 
        this.pointArr = new Point[randomPoints.size()]; 
        pointArr = convertToArray(randomPoints); 
    }

    /**
     * A front end for the Quick Hull algorithm. Sorts the points in preparation for Quick Hull 
     * @param a set of points to find the hull 
     */
    public ArrayList<Point> runQuickHull() { 

        // initialize sets for quick hull 
        sortPoints(); 
        ArrayList<Point> ConvexHull = new ArrayList<Point>(); 
        Point A = pointArr[0]; 
        Point B = pointArr[pointArr.length - 1]; 

        // sets to partition above and below line AB 
        ArrayList<Point> setL = new ArrayList<Point>(); 
        ArrayList<Point> setR = new ArrayList<Point>(); 

        // for each point (not A/B) partition into left and right sets 
        for (Point p : pointArr) { 
            if (!p.equals(A) && !p.equals(B)) { 
                if (isAbove(A, B, p)) { 
                    setL.add(p); 
                } if (isAbove(B, A, p)) { 
                    setR.add(p); 
                }
            }
        }

        // recursively find the left and right hull, return their union 
        ArrayList<Point> LeftHull = findHull(setL, A, B); 
        ArrayList<Point> RightHull = findHull(setR, B, A); 
        ConvexHull.add(A); 
        ConvexHull = union(ConvexHull, LeftHull); 
        ConvexHull = union(ConvexHull, RightHull); 
        ConvexHull.add(B); 
        return ConvexHull;
    }

    /**
     * A recurise method to find the convex hull of a set of points that is to the 
     * left or right of the AB line 
     * @param S the set of sorted points 
     * @param P left endpoint of the line 
     * @param Q right endpoint of the line
     * @return the convex hull on that side of the line 
     */
    private ArrayList<Point> findHull(ArrayList<Point> S, Point P, Point Q) { 
        // if the set to consider is empty just return it 
        if (S.isEmpty()) { 
            return S; 
        } else { 
            // get the point furthest from the PQ line and remove from S
            Point C = getFurthest(S, P, Q); 
            S.remove(C); 
            ArrayList<Point> S1 = new ArrayList<>(); 
            ArrayList<Point> S2 = new ArrayList<>(); 
            // for each remaining point in S 
            for (Point p : S) {
                // partition into left and right based on C
                if (isAbove(P, C, p)) { 
                    S1.add(p); 
                } if (isAbove(C, Q, p)) {
                    S2.add(p);
                }
            }
            // recurr and return 
            ArrayList<Point> H1 = findHull(S1, P, C); 
            ArrayList<Point> H2 = findHull(S2, C, Q);
            H2.add(C); 
            return (union(H1, H2)); 
        }
    }

    /**
     * Get the point furthes from the PQ line. The calculatios here are defined by the 
     * section 'Line Defined by Two Points' on this page: 
     * 
     * https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
     * 
     * @param S the set of points to check 
     * @param P left endpoint of the line
     * @param Q right endpoint of the line 
     * @return the point in S furthest from the line segments defined by P and Q 
     */
    private Point getFurthest(ArrayList<Point> S, Point P, Point Q) { 
        Point currBest = null; 
        double currFurthest = 0; 
        for (Point currPoint : S) { 
            // deltaX and deltaY for PQ line 
            double QPx = (Q.getX() - P.getX()); 
            double QPy = (Q.getY() - P.getY());
            // deltaX * (Py - currentY)
            double d1 = QPx * (P.getY() - currPoint.getY()); 
            // (Px - currentX) * deltaY
            double d2 = (P.getX() - currPoint.getX()) * QPy; 
            // building formula 
            double numerator = Math.abs(d1 - d2); 
            double denominator = Math.sqrt((QPx*QPx) + (QPy*QPy)); 
            double result = numerator/denominator; 
            if (result > currFurthest) { 
                currBest = currPoint;
                currFurthest = result; 
            }
        }
        return currBest; 
    }

    /**
     * Determine if a point is above or below the line from A to B
     * @param A first point of the line 
     * @param B last point of the line 
     * @param p the point to check 
     * @return true or false 
     */
    private boolean isAbove(Point A, Point B, Point p) { 
        double d1 = (p.getX() - A.getX()) * (B.getY() - A.getY()); 
        double d2 = (p.getY() - A.getY()) * (B.getX() - A.getX()); 
        double d = d1 - d2; 
        return (d > 0); 
    } 

    /**
     * Add the points from set 2 to set 1
     * @param Set1
     * @param Set2
     * @return the union of the two sets 
     */
    private ArrayList<Point> union (ArrayList<Point> Set1, ArrayList<Point> Set2) { 
        if (!Set2.isEmpty()) { 
            for (Point p : Set2) { 
                Set1.add(p); 
            }
        }
        return Set1; 
    }

    /**
     * Sort the array of randomly generated points by x coordinate using Selection 
     * Sort. Since the set of points is always small, the increase in run-time that 
     * comes with selection sort is negligable here. 
     * 
     * @Note: this implementation of quick sort is for cartesian points, and is different 
     * than the selection sort for sorting polar coordinates in drawer class
     */
    private void sortPoints() { 
        int n = pointArr.length; 
        // for each point in the array 
        for (int i = 0; i < n; i++) { 
            int minPos = i;
            // for each point after i in the array 
            for (int j = i+1; j < n; j++) { 
                double xj = pointArr[j].getX(); 
                double xi = pointArr[minPos].getX(); 
                double yj = pointArr[j].getY(); 
                double yi = pointArr[j].getY(); 
                // compare x coordinates 
                if (xj < xi) { 
                    minPos = j; 
                }
                // compare y for tiebreakers 
                if (xj == xi && yj < yi) { 
                    minPos = j; 
                }
            }
            swap(i, minPos); 
        }
    }

    /**
     * Swap two points in an array. Helper for the lomuto partition
     * @param i the current point to be swapped
     * @param minPos the position that we found to swap with i 
     */
    private void swap(int i, int minPos) { 
        Point xi = pointArr[i]; 
        pointArr[i] = pointArr[minPos]; 
        pointArr[minPos] = xi;
    }

    /**
     * Convert the ArrayList of points to an unsorted array so that we may sort them and 
     * find their convex hull
     * @param points a random set of points 
     * @return an unsorted array of points 
     */
    public Point[] convertToArray(ArrayList<Point> points) { 
        int i = 0; 
        for (Point pt : points) { 
            pointArr[i] = pt; 
            i++;  
        } 
        return pointArr; 
    } 
}
