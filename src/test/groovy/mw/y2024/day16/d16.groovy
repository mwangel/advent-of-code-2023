package mw.y2024.day16

import spock.lang.Specification

class d16 extends Specification {

  def "Day 16 - reindeers in a maze"() {
    given:
//      def (String fileName, long p1) = ['/y2024/data16-test.txt', 7036]
//      def (String fileName, long p1) = ['/y2024/data16-test2.txt', 11048]
      def (String fileName, long p1) = ['/y2024/data16.txt', 10092]

      def input = this.getClass().getResource( fileName ).readLines()
      def (start, end, walls, width, height) = load( input )
      print( walls, start, end, width, height )

    when:
      def result = findCheapestPath( start, end, walls, width, height )

    then:
      result == p1
  }


  def findCheapestPath( Point start, Point end, Set<Point> walls, int width, int height ) {
    boolean debug = true
    final long MOVE_COST = 1
    final long TURN_COST = 1000

    // Add the three initial heads.
    Head a = new Head( start, Direction.RIGHT, 0 )
    Head b = new Head( start, Direction.UP, TURN_COST )
    b.visited << start
    Head c = new Head( start, Direction.DOWN, TURN_COST )
    c.visited << start
    Set<Head> heads = [a, b, c]

    // Loop until we find the end.
    while( true ) {
      // Find the head with the lowest cost.
      Head headNearestEnd = heads.min { (end.x - it.position.x) + (it.position.y-end.y) }
      Head head = heads.min { it.cost }
      println( "Nearest end (of ${heads.size()}): ${end.x-headNearestEnd.position.x + headNearestEnd.position.y-end.y} @${headNearestEnd.position} £${headNearestEnd.cost}, testing ${head.position} ${head.direction} £${head.cost}" )
      heads.remove( head )

      // Check if we can move in the direction we are facing.
      Point nextPoint = new Point( head.position.x + head.direction.dx, head.position.y + head.direction.dy )
      if( walls.contains(nextPoint) ) {
        // Blocked. Abandon this head.
      }
      else if( head.visited.contains(nextPoint) ) {
        // Already visited. Abandon this head.
      }
      else if( nextPoint == end ) {
        // We have reached the end.
        println( "Found end: $head.cost + 1. Path: ${head.visited}" )
        print( walls, start, end, width, height, head.visited )
        return head.cost + MOVE_COST
      }
      else {
        // Move forward. Add the new head and the two representing turns (in the new position).
        Head nextHead = new Head( nextPoint, head.direction, head.cost + MOVE_COST )
        nextHead.visited.addAll( head.visited )
        nextHead.visited.add( head.position )
        addIfBetter( heads, nextHead )

        Head leftTurnHead = new Head( nextPoint, leftTurnFrom(head.direction), nextHead.cost + TURN_COST )
        leftTurnHead.visited.addAll( head.visited )
        leftTurnHead.visited.add( head.position )
        Point forwardFromLeft = new Point( nextPoint.x + leftTurnHead.direction.dx, nextPoint.y + leftTurnHead.direction.dy )
        if( !walls.contains(forwardFromLeft) && !leftTurnHead.visited.contains( forwardFromLeft ) )
          addIfBetter( heads, leftTurnHead )
          //heads.add( leftTurnHead )

        Head rightTurnHead = new Head( nextPoint, rightTurnFrom(head.direction), nextHead.cost + TURN_COST )
        rightTurnHead.visited.addAll( head.visited )
        rightTurnHead.visited.add( head.position )
        Point forwardFromRight = new Point( nextPoint.x + rightTurnHead.direction.dx, nextPoint.y + rightTurnHead.direction.dy )
        if( !walls.contains(forwardFromRight) && !rightTurnHead.visited.contains( forwardFromRight ) )
          addIfBetter( heads, rightTurnHead )
          //heads.add( rightTurnHead )
      }

    }

    if( debug ) print( boxes, walls, robotPosition, width, height )
    return head.cost
  }

  void addIfBetter( Set<Head> heads, Head head ) {
    def existing = heads.findAll { it.position == head.position && it.direction == head.direction }
    if( existing ) {
      heads.removeAll( existing )
      existing << head
      heads << existing.min { it.cost }
    }
    else {
      heads.add( head )
    }
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

  Direction leftTurnFrom( Direction direction ) {
    switch( direction ) {
      case Direction.UP: return Direction.LEFT
      case Direction.DOWN: return Direction.RIGHT
      case Direction.LEFT: return Direction.DOWN
      case Direction.RIGHT: return Direction.UP
    }
  }
  Direction rightTurnFrom( Direction direction ) {
    switch( direction ) {
      case Direction.UP: return Direction.RIGHT
      case Direction.DOWN: return Direction.LEFT
      case Direction.LEFT: return Direction.UP
      case Direction.RIGHT: return Direction.DOWN
    }
  }


  class Head {
    Point position
    Direction direction
    Set<Point> visited = []
    long cost = 0

    Head( Point position, Direction direction, long cost ) {
      this.position = position
      this.direction = direction
      this.cost = cost
    }

    String toString() {
      return "${position}_${direction}_£$cost"
    }

    int hashCode() {
      return Objects.hash( position, direction, cost )
    }

    boolean equals( Object o ) {
      if( o == null ) return false
      if( o.is(this) ) return true
      if( o.getClass() != this.getClass() ) return false
      Head h = (Head)o
      return h.position == position && h.direction == direction && h.cost == cost
    }
  }

  void print( Set<Point> walls, Point start, Point end, int width, int height, Set<Point> visited = [] ) {
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        Point p = new Point( x, y )
        if( walls.contains(p) ) print( "█" )
        else if( visited && visited.contains(p) ) print( "▒" ) // ·■▄░▒▓█
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
