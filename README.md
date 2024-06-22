# Algorithms Project Fall 2022

### Christian Lentz and Nolan Meyer 

This project contains code that builds a convex polygon triangulator. The main class, called Drawer, creates an interactive user interface that animates the process of convex polygon triangulation. The program follows this general path: 

1) Generate a set of random points in the plane  
2) Find the convex hull of these random points, using the Quick Hull algorithm and Selection Sort
  * This was adapted from pseudocode in the Macalester Comp 221 curriculum 
3) Sort the points of the convex hull in CCW order and draw the polygon. The steps for doing this are: 
  * Find the center of the hull by finding the average x and y coordinate for the points 
  * Translate the convex hull to (0, 0) on the canvas using this center point
  * Use `Math.atan2(y, x)` to transform each point from cartesian to polar coordinates, and get theta (angular distance from positive x-axis)
  * Use a modified selection sort to sort the polar coordinates from smallest to largest 
  * Translate the points back to cartesian coordinates and translate back to their original location 
  * Iteratively add line segments connecting each consecutive point
4) Triangulate the convex hull of these random points using the Ear Clipping method
  * This is a simplified verison of ear clipping, since each of these shapes is convex
  * Detailed discussion and psuedocode in our report 

This project was built over November and December of 2022 for the Macalester College Algorithm Design & Analysis course (Comp 221)
