package mw.y2024.day15

import spock.lang.Specification

class d15 extends Specification {

  def "Day 15 - flawed robot walking"() {
    given:
//      def (String fileName, long p1) = ['/y2024/data15-test.txt', 2028]
//      def (String fileName, long p1) = ['/y2024/data15-test2.txt', 10092]
      def (String fileName, long p1) = ['/y2024/data15.txt', 10092]

      def input = this.getClass().getResource( fileName ).readLines()
      def (start, boxes, walls, moves, width, height) = load( input )
      print( boxes, walls, start, width, height )
      println( moves )

    when:
      doMove( start, boxes, walls, moves, width, height )
      def result = calculatePart1( boxes )

    then:
      result == p1
  }


  /**
   * Return the number of boxes that can be pushed from the given point in the given direction.
   * If a wall is encountered before an open space is found, return 0.
   */
  int boxesToPush( Point from, Set<Point> boxes, Set<Point> walls, int dx, int dy ) {
    def x = from.x
    def y = from.y
    def p = new Point( x, y )
    def count = 0
    while( true ) { // Assume that there are walls all around us.
      p.x += dx
      p.y += dy

      if( walls.contains(p) ) // Should work because Point.hashCode() and equals() are overridden
        return 0 // The way is blocked: We found a wall before we found an open space.

      if( boxes.contains(p) )
        count++
      else  // open space
        return count // We can push this many boxes one step.
    }
  }

  def doMove( Point start, Set<Point> boxes, Set<Point> walls, String moves, int width, int height ) {
    def robotPosition = new Point( start.x, start.y )
    def dx = 0
    def dy = 0
    boolean debug = false

    moves.eachWithIndex { String move, int moveIndex ->
      dx = 0; dy = 0
      switch( move ) {
        case '^' : dy = -1; break
        case 'v' : dy = 1; break
        case '<' : dx = -1; break
        case '>' : dx = 1; break
      }

      def nextPosition = new Point( robotPosition.x + dx, robotPosition.y + dy )
      if( walls.contains( nextPosition ) ) {
        if( debug ) println( "Blocked from moving $move to $nextPosition (wall)" )
        if( debug ) print( boxes, walls, robotPosition, width, height )
        return
      }
      if( !boxes.contains(nextPosition) ) {
        if( debug ) println( "Moving $move to $nextPosition" )
        robotPosition = nextPosition
        if( debug ) print( boxes, walls, robotPosition, width, height )
        return
      }

      // If we get here then there is a box in the way, see if we can push it.
      int boxesToPush = boxesToPush( robotPosition, boxes, walls, dx, dy )
      if( boxesToPush == 0 ) {
        if( debug ) println( "Blocked from moving $move to $nextPosition (boxes all the way to the wall)" )
      }
      else {
        // Push the boxes (actually just remove the first one and add another where we found empty space).
        if( debug ) println( "Can push $boxesToPush boxes in direction $move ($dx,$dy) from $robotPosition to $nextPosition" )
        robotPosition = nextPosition

        boxes.remove( nextPosition )
        boxes.add( new Point( robotPosition.x + dx * boxesToPush, robotPosition.y + dy * boxesToPush ) )
      }

      if( debug ) print( boxes, walls, robotPosition, width, height )
    }
    return boxes
  }

  def calculatePart1( Set<Point> boxes ) {
    return boxes.sum { it.x + 100 * it.y }
  }


  class Point {
    int x, y
    Point( int x, int y ) {
      this.x = x
      this.y = y
    }
    int hashCode() {
      return x << 16 + y
    }
    boolean equals( Object o ) {
      if( o == null ) return false
      if( o.is(this) ) return true
      if( o.getClass() != this.getClass() ) return false
      Point p = (Point) o
      return p.x == x && p.y == y
    }
    String toString() {
      return "($x,$y)"
    }
  }


  void print( Set<Point> boxes, Set<Point> walls, Point start, int width, int height ) {
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        if( boxes.find{it.x==x && it.y==y} ) {
          print( "O" )
        }
        else if( walls.find{it.x==x && it.y==y} ) {
          print( "#" )
        }
        else if( start.x == x && start.y == y ) {
          print( "@" )
        }
        else {
          print( "_" )
        }
      }
      println()
    }
  }

  def load( List<String> input ) {
    Set<Point> boxes = []
    Set<Point> walls = []
    Point start = null
    String moves = ""

    def readingMap = true
    input.eachWithIndex { String line, int y ->
      if( line.isEmpty() ) {
        readingMap = false
      }
      else if( readingMap ) {
        line.eachWithIndex { String c, int x ->
          switch( c ) {
            case '#' : walls << new Point( x, y ); break
            case 'O' : boxes << new Point( x, y ); break
            case '@' : start = new Point( x, y )
          }
        }
      }
      else {
        moves += line.trim()
      }
    }

    int width = walls.max() { it.x }.x + 1
    int height = walls.max() { it.y }.y + 1

    return [start, boxes, walls, moves, width, height]
  }
}
