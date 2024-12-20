package mw.y2024.day16

import spock.lang.Specification

class d16 extends Specification {

  def "Day 16 - reindeers in a maze"() {
    given:
      def (String fileName, long p1) = ['/y2024/data16-test.txt', 2028]
//      def (String fileName, long p1) = ['/y2024/data16.txt', 10092]

      def input = this.getClass().getResource( fileName ).readLines()
      def (start, end, walls, width, height) = load( input )
      print( walls, start, end, width, height )

    when:
      findCheapestPath( start, end, walls, width, height )
      def result = 0

    then:
      result == p1
  }


  def findCheapestPath( Point start, Point end, Set<Point> walls, int width, int height ) {
    boolean debug = true
    Head head = new Head( start, Direction.RIGHT )


    if( debug ) print( boxes, walls, robotPosition, width, height )
    return head.cost
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

  enum Direction {
    UP( 0, -1 ), DOWN( 0, 1 ), LEFT( -1, 0 ), RIGHT( 1, 0 )
    int dx, dy
    Direction( int dx, int dy ) {
      this.dx = dx
      this.dy = dy
    }
  }

  class Head {
    Point position
    Direction direction
    Set<Point> visited = []
    long cost = 0
    Head( Point position, Direction direction ) {
      this.position = position
      this.direction = direction
    }
  }

  void print( Set<Point> walls, Point start, Point end, int width, int height ) {
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        if( walls.find{it.x==x && it.y==y} ) print( "▓" )
        else if( start.x == x && start.y == y )  print( "S" )
        else if( end.x == x && end.y == y )  print( "E" )
        else print( "░" )
      }
      println()
    }
  }

  def load( List<String> input ) {
    Set<Point> walls = []
    Point start = null
    Point end = null

    input.eachWithIndex { String line, int y ->
      line.eachWithIndex { String c, int x ->
        switch( c ) {
          case '#' : walls << new Point( x, y ); break
          case 'S' : start = new Point( x, y ); break
          case 'E' : end = new Point( x, y )
        }
      }
    }

    int width = walls.max() { it.x }.x + 1
    int height = walls.max() { it.y }.y + 1

    return [start, end, walls, width, height]
  }
}
