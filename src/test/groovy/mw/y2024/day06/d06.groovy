package mw.y2024.day06


import spock.lang.Specification

import java.util.stream.IntStream

class d06 extends Specification {

  def "Day 06"() {
    given:
//      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data06-test.txt', 41, 6 ]
      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data06.txt', 4977, 1729 ]

      def (map, x, y) = findStartPosition(
        this.getClass().getResource( fileName ).readLines()
      )

    when:
      def result = walk( map, x, y )
    then:
      result.size(  ) == expectedResult1

    when:
      int maxX = map[0].size()
      int maxY = map.size()
      int loops = 0
      int progress = 0
      maxY.times { int yblock ->
        int percent = yblock * 100 / maxY
        if( percent != progress && percent % 10 == 0 ) {
          progress = percent
          println( "Progress: $progress%" )
        }
        maxX.times { int xblock ->
          def key = "$xblock,$yblock"
          if( result.contains( key ))
            if( findLoop( map, x, y, xblock, yblock ) )
              loops++
        }
      }
    then:
      loops == expectedResult2
  }


  def directions = [[0, -1, "up"], [1, 0, "right"], [0, 1, "down"], [-1, 0, "left"]] // N, E, S, W

  def findStartPosition( List<String> lines ) {
    for( int y=0; y < lines.size(  ); y++ ) {
      def line = lines[y]
      int x = line.indexOf( "^" )
      if( x >= 0 ) {
        return [lines, x, y]
      }
    }
    throw new RuntimeException( "No start position found" )
  }

  def walk( List<String> data, int startX, int startY ) {
    int x = startX
    int y = startY
    int dirIndex = 0
    def dir = directions[dirIndex]
    Set<String> visited = new HashSet<String>()

    // Until we leave the map...
    while( true ) {
      visited << "$x,$y" // Mark this position as visited.

      // Move in a straight line until we hit a wall or leave the map.
      while( true ) {
        int nextX = x + dir[0]
        int nextY = y + dir[1]
        if( nextX < 0 || nextY < 0 || nextX >= data[y].size() || nextY >= data.size() ) {
          // We have left the map, return from the walk.
          println( "Left the map at $x,$y" )
          return visited
        }

        def c = data[nextY][nextX] // Peek at the next position.
        if( c == '.' || c == '^' ) {
          // Move to the next position.
          x = nextX
          y = nextY
          break
        }

        // The way is blocked, turn right.
        dirIndex = (dirIndex + 1) % directions.size()
        dir = directions[dirIndex]
      }
    }
  }


  /**
   * Find a square loop in the map.
   * A loop does not count if it takes more than 4 turns to complete it.
   */
  def findLoop( List<String> data, int startX, int startY, int blockX, int blockY ) {
    StringBuilder log = new StringBuilder()
    if( blockX == startX && blockY == startY ) return false
    if( data[blockY][blockX] == '#' ) return false
    int x = startX
    int y = startY
    int dirIndex = 0
    def dir = directions[dirIndex]
    Map<String, Long> visited = [:].withDefault { 0 }
    LinkedList<String> turns = new LinkedList<String>()

    // Move in a straight line until we hit a wall or leave the map.
    while( true ) {
      def key = "$x,$y,$dirIndex".toString(  )
      if( visited.containsKey( key ) ) {

        if( turns.size()  >= 5 ) {
          if( turns.last == turns[turns.size() - 5] ) {
            // We have found a loop.
//            log.append( "Short loop found at x:$x, y:$y given a new obstruction at x:$blockX, y:$blockY\n" )
//            println( log.toString() )
            return true
          }
          else {
            if( visited[key] > 2 ) {
//              println( "Long loop at $x,$y. Turns: $turns" )
              return true
            }
          }
        }
      }
      visited[key]++ // Mark this position as visited while walking in this direction.

      // Move once, or keep turning right until we can move.
      while( true ) {
        int nextX = x + dir[0]
        int nextY = y + dir[1]

        if( nextX < 0 || nextY < 0 || nextX >= data[y].size() || nextY >= data.size() ) {
          // We have left the map, return from the walk.
          return false // It is not a loop if we leave the map.
        }

        def c = data[nextY][nextX] // Peek at the next position.
        if( (nextX != blockX || nextY != blockY) && (c!= '#') ) {
          // Move to the next position.
          x = nextX
          y = nextY
          break
        }

        // The way is blocked, turn right.
        dirIndex = (dirIndex + 1) % directions.size()
        dir = directions[dirIndex]
        turns << "$x,$y,${dir[2]}".toString(  )
        if( turns.size() > 3 && (turns.last == turns[turns.size()-3] ) ) {
//          log.append( "Loop found at x:$x, y:$y given a new obstruction at x:$blockX, y:$blockY\n" )
//          println( log.toString() )
          return true
        }
      }
    }
  }



  def getChar( List<String> data, int x, int y ) {
    if( x < 0 || y < 0 || y >= data.size() || x >= data[y].size() ) { return ' ' }
    return data[y][x]
  }

}
