import java.util.*;
import java.awt.Color;
import java.lang.Math;

import edu.macalester.graphics.*;
import edu.macalester.graphics.ui.*;

/**
 * A class to accompany the Ear Clipping algorithm, used to visualize the 
 * algorithm while in action. This class makes use of various sorting 
 * techniques, as well as quick hull. 
 * 
 * @Author Christian Lentz and Nolan Meyer
 */
public class Drawer {

   // global variables
   private static final int WINDOW_WIDTH = 1000;
   private static final int WINDOW_HEIGHT = 800;
   private ArrayList<Point> pointSet;
   private ArrayList<Point> sortedHull; 
   private ArrayList<ArrayList<ArrayList<Point>>> earClippings; 
   private ArrayList<ArrayList<Point>> diagonals; 
   private ArrayList<ArrayList<Point>> triangles;
   private int N = 30; // number of points to randomly generate
   private CanvasWindow canvas;
   private int xPos = 85;
   private int baseButtonY = 35;
   private int buttonDiff = 25;
   private double boxX = buttonDiff;
   private double boxY;
   private double boxWidth = WINDOW_WIDTH - (2 * boxX);
   private double boxHeight;
   private ArrayList<Color> colors; 

   // graphics groups
   private GraphicsGroup buttons = new GraphicsGroup();
   private GraphicsGroup boxes = new GraphicsGroup();
   private GraphicsGroup points = new GraphicsGroup();

   // class objects
   private QuickHull QH; 

   /**
    * Constructor for the drawer class
    */
   private Drawer() {
      this.canvas = new CanvasWindow("Triangulator", WINDOW_WIDTH, WINDOW_HEIGHT);
      this.pointSet = new ArrayList<Point>();
      this.sortedHull = new ArrayList<Point>(); 
      canvas.setBackground(Color.BLACK);
      canvas.add(buttons);
      canvas.add(boxes);
      canvas.add(points);
      getColors(); 
      createGraphics(); 
   }

   // ----- this section of code pertains to generating random points -----

   /**
    * Generate N random points as the starting point for Quick Hull and Ear Clipping
    * 
    * @param N
    * @return a set of random points
    */
   private void generatePoints() {
      if (pointSet.isEmpty()) { 
         for (int n = 0; n < N; n++) {
            // generate new point and store coordinates
            Point newP = new Point(getRandX(), getRandY());
            pointSet.add(newP);
            // generate graphics object for the point
            Ellipse point = new Ellipse(newP.getX(), newP.getY(), 10, 10);
            point.setFillColor(Color.WHITE);
            points.add(point);
            // update the canvas
            canvas.draw();
            canvas.pause(125);
         }
      }
   }

   /**
    * Remove the points from the canvas when button clicked
    */
   private void removePoints() { 
      if (!pointSet.isEmpty()) { 
         points.removeAll(); 
         pointSet.clear();
      }
      if (!sortedHull.isEmpty()) { 
         sortedHull.clear(); 
      }
      if (!triangles.isEmpty()) { 
         triangles.clear();
      }
   }

   /**
    * Generate a random X coordinte
    * 
    * @return a new X coordinate
    */
   private double getRandX() {
      double minX = boxX + 15;
      double maxX = boxX + boxWidth - 15;
      return Math.floor(Math.random() * (maxX - minX + 1) + minX);
   }

   /**
    * Generate a random Y coordinte
    * 
    * @return a new Y coordinate
    */
   private double getRandY() {
      double minY = boxY + 15;
      boxHeight = WINDOW_HEIGHT - boxY - buttonDiff;
      double maxY = boxY + boxHeight - 15;
      return Math.floor(Math.random() * (maxY - minY + 1) + minY);
   }

   // ----- this section of code pertains to quick hull ----- 

   /**
    * Update the set of points to be only the convex hull by calling the convex hull algorithm. 
    * Then draw the line segments that create the convex polygon. 
    */
   private void callQuickHull() {
      if (sortedHull.isEmpty()) { 
         // create the Quick Hull object and execute algorithm 
         QH = new QuickHull(pointSet); 
         ArrayList<Point> hull = new ArrayList<Point>(); 
         hull = QH.runQuickHull();
         // remove the old points 
         points.removeAll(); 
         pointSet.clear(); 
         // add the new points for the convex hull
         for (Point p : hull) { 
            // generate new point and store coordinates
            Point newP = new Point(p.getX(), p.getY());
            pointSet.add(newP);
            // generate graphics object for the point
            Ellipse point = new Ellipse(newP.getX(), newP.getY(), 10, 10);
            point.setCenter(point.getX(), point.getY());
            point.setFillColor(Color.WHITE);
            points.add(point);
            canvas.draw();
         }
         drawPolygon(); 
      } 
   }

   /**
    * After finding the Convex Hull, draw the polygonal shape by connecting the points
    * with line segments. Sort points in ccw order before doing this. The general steps
    * for sorting these points:
    *
    *    1) find the center of the convex hull
    *    2) use the center to translate to points to (0,0)
    *    3) use atan2() method in Java.lang.math to find to polar coordinate of each point. 
    *    4) sort polar coordinates in increasing order 
    */
   private void drawPolygon() { 
      Point center = getCenter(); 
      ArrayList<Point> centeredHull = translatePoints(center);
      sortCCW(centeredHull, center); 
      System.out.println("Sorted Hull");
      canvas.pause(1000); 
      //points.removeAll(); 
      canvas.draw();
      drawHull(); 
   }

   /**
    * Compute the center of the points returned by quick hull by finding the
    * average of their x and y coordinates.
    * @return the center of the convex hull defined by pointSet
    */
   private Point getCenter() { 
      double n = pointSet.size(); 
      double xSum = 0; 
      double ySum = 0;
      for (Point p : pointSet) { 
         xSum += p.getX(); 
         ySum += p.getY(); 
      }
      Point center = new Point(xSum/n, ySum/n); 
      return center; 
   }

   /**
    * Translate the convex hull to be centered about (0,0) using 
    * a center point 
    * @param center 
    * @return an array containing the points of the centered convex hull 
    */
   private ArrayList<Point> translatePoints(Point center) { 
      ArrayList<Point> centered = new ArrayList<>();
      for (Point p : pointSet) { 
         double newX = p.getX() - center.getX(); 
         double newY = p.getY() - center.getY(); 
         Point curr = new Point(newX, newY);
         centered.add(curr);  
      }
      return centered; 
   }

   /**
    * Use the array of points that is the conex hull centered at (0,0) to sort the 
    * points of the convex hull. Use the center point as a reference. This is just 
    * a modified selection sort. 
    * @param centeredHull
    * @return the convex hull points sorted ccw 
    */
   private void sortCCW(ArrayList<Point> centeredHull, Point center) {
      // loop variables 
      int n = centeredHull.size();  
      double smallestTheta = Double.MAX_VALUE; 
      Point nextPt = null; 
      // while we haven't sorted all of the points 
      int i = 0; 
      while (i < n) {
         // check the centered hull points and find the next smallest theta 
         // (in terms of polar coords)
         for (Point p : centeredHull) { 
            double newTheta = Math.atan2(p.getY(), p.getX()); 
            // update loop variables 
            if (newTheta < smallestTheta) { 
               smallestTheta = newTheta; 
               nextPt = p; 
            }
         }
         // create new point to add to the sorted hull
         double newX = nextPt.getX() + center.getX(); 
         double newY = nextPt.getY() + center.getY(); 
         Point toAdd = new Point(newX, newY); 
         sortedHull.add(toAdd); 
         // update loop variables 
         smallestTheta = Double.MAX_VALUE;
         centeredHull.remove(nextPt); 
         i++;  
      }
   }

   /**
    * Use line segments tp draw the convex hull. 
    */
   private void drawHull() { 
      // get first point to draw from 
      Point start = sortedHull.get(0); 
      int n = sortedHull.size();  
      // iteratively add lines by getting the next and updating 'start'
      for (int i = 1; i < n; i++) { 
         Point next = sortedHull.get(i); 
         addLine(start, next); 
         start = next; 
      } 
      // add line frm last point to first line
      addLine(start, sortedHull.get(0)); 
   }

   // ----- this section of code pertains to ear clipping -----

   /**
    * Call the triangulation algorithm
    */
   private void callEarClip() {
      if (!sortedHull.isEmpty()) { 
         earClippings = EarClipping.clipEars(sortedHull); 
         triangles = earClippings.get(1);
         diagonals = earClippings.get(0);  
         drawTriangles(); 
      }
   }

   /**
    * Add line segments to the canvas to triangulate the polygon. 
    * Fill in the triangels 
    */
   private void drawTriangles() { 
      // for each diagonal in the set 
      for (ArrayList<Point> diag : diagonals) { 
         Point start = diag.get(1); 
         Point next = diag.get(0); 
         addLine(start, next); 
      }
      // for each triangle 
      int i = 0; 
      for (ArrayList<Point> triangle : triangles) { 
         addTriangle(triangle, i); 
         i++; 
         if (i >= colors.size()) { 
            i = 0; 
         }
      }
   }

   // ----- this section of code creates the graphics/buttons ----- 

   /**
    * Create the buttons on the canvas
    */
   private void createGraphics() {

      // create buttons
      Button pointGen = createPointsButton();
      Button quickHull = createQHButton(pointGen);
      Button triangulate = createTriangulateButton(quickHull);
      Button clear = createClearButton(triangulate);

      // create UI box
      addBox(clear.getY() + clear.getHeight() + buttonDiff);
   }

   /**
    * Create the "Generate Points" button
    * 
    * @return the new button
    */
   private Button createPointsButton() {
      Button pointGen = new Button("1) Generate Points");
      pointGen.setCenter(xPos, baseButtonY);
      buttons.add(pointGen);
      pointGen.onClick(() -> generatePoints());
      return pointGen;
   }

   /**
    * Create the "Quick Hull" button
    * 
    * @param pointGen previous button created, helps to create the next
    * @return the new button
    */
   private Button createQHButton(Button pointGen) {
      Button quickHull = new Button("2) Quick Hull");
      double QHy = pointGen.getY() + pointGen.getHeight() + buttonDiff;
      quickHull.setCenter(xPos, QHy);
      buttons.add(quickHull);
      quickHull.onClick(() -> callQuickHull());
      return quickHull;
   }

   /**
    * Create the "Triangulate" button
    * 
    * @param quickHull previous button created, helps to create the next
    * @return the new button
    */
   private Button createTriangulateButton(Button quickHull) {
      Button triangulate = new Button("3) Triangulate");
      double triangulateY = quickHull.getY() + quickHull.getHeight() + buttonDiff;
      triangulate.setCenter(xPos, triangulateY);
      buttons.add(triangulate);
      triangulate.onClick(() -> callEarClip());
      return triangulate;
   }

   /**
    * Create the "Clear Window" button
    * 
    * @param triangulate
    * @return the new button
    */
   private Button createClearButton(Button triangulate) {
      Button clear = new Button("4) Clear Window");
      double clearY = triangulate.getY() + triangulate.getHeight() + buttonDiff;
      clear.setCenter(xPos, clearY);
      buttons.add(clear);
      clear.onClick(() -> removePoints());
      return clear;
   }

   /**
    * Add a box to the canvas to frame all the points
    * 
    * @param yPos the yPosition of the upper left corner of the box
    */
   private void addBox(double yPos) {

      // create the first box
      boxY = yPos;
      double boxHeight = WINDOW_HEIGHT - boxY - buttonDiff;
      Rectangle whiteBox = new Rectangle(boxX, boxY, boxWidth, boxHeight);
      whiteBox.setFillColor(Color.WHITE);
      boxes.add(whiteBox);

      // create the second box over it
      Rectangle blackBox = new Rectangle(boxX + 5, boxY + 5, boxWidth - 10, boxHeight - 10);
      blackBox.setFillColor(Color.BLACK);
      boxes.add(blackBox);
   }

   /**
    * Add a line segment to the canvas from start point to end point 
    * @param start
    * @param next
    */
    private void addLine(Point start, Point next) { 
      Line lineSeg = new Line(start, next);
      lineSeg.setStrokeWidth(2);
      lineSeg.setStrokeColor(Color.WHITE);
      points.add(lineSeg); 
      canvas.draw(); 
      canvas.pause(400);
   }

   /**
    * Add a triangle to the canvas and color it 
    * @param tri a set of three points representing a triangle 
    */
   private void addTriangle(ArrayList<Point> tri, int i) { 
      // make triangle using Path obj
      Point p0 = tri.get(0); 
      Point p1 = tri.get(1); 
      Point p2 = tri.get(2); 
      Path triangle = Path.makeTriangle(p0.getX(), p0.getY(), 
                                        p1.getX(), p1.getY(), 
                                        p2.getX(), p2.getY()); 
      // color the triangle 
      triangle.setFillColor(colors.get(i)); 
      triangle.setStrokeColor(Color.WHITE); 
      triangle.setStrokeWidth(3);
      // add triangle to canvas 
      points.add(triangle); 
      canvas.draw(); 
      canvas.pause(400); 
   }

   /**
    * Make a list of colors for the triangles. Yes, this method is ugly. 
    */
   private void getColors() { 
      // create colors list and custom colors 
      colors = new ArrayList<Color>(); 
      Color purple = new Color(75, 0, 130);
      Color brown = new Color(139, 69, 19); 
      Color goldenrod = new Color(218, 165, 32); 
      Color seaGreen = new Color(32, 178, 170); 
      colors.add(purple); 
      colors.add(brown); 
      colors.add(goldenrod); 
      colors.add(seaGreen); 
      // add builtin colors 
      colors.add(Color.BLUE);
      colors.add(Color.MAGENTA);
      colors.add(Color.ORANGE);
      colors.add(Color.DARK_GRAY);
      colors.add(Color.RED);
      colors.add(Color.GREEN);
   }

   public static void main(String[] args) {
      new Drawer();
   }
}
