package mw.y2024.day06


import spock.lang.Specification

class d06 extends Specification {

  def "Day 06"() {
    given:
      //def( fileName, expectedResult ) = [ '/y2024/data06-test.txt', 41 ]
      def( fileName, expectedResult ) = [ '/y2024/data06.txt', 4977 ]

      def (map, x, y) = findAndReplaceStartPosition(
        this.getClass().getResource( fileName ).readLines()
      )

    when:
      def result = walk( map, x, y )
    then:
      result.size(  ) == expectedResult
  }


  def directions = [[0, -1, "up"], [1, 0, "right"], [0, 1, "down"], [-1, 0, "left"]] // N, E, S, W

  def findAndReplaceStartPosition( List<String> lines ) {
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

    while( true ) {
      visited << "$x,$y" // Mark this position as visited.
      //visited << "$x,$y,${dir[2]}" // Mark this position as visited.

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
          //println( "Moving ${dir[2]} from $x,$y to $nextX,$nextY" )
          x = nextX
          y = nextY
          break
        }
        else if( c == '#') {
          // The way is blocked, turn right.
          dirIndex = (dirIndex + 1) % directions.size()
          dir = directions[dirIndex]
          //println( "Blocked at $x,$y, turning right to face ${dir[2]}" )
        }
      }
    }
  }

  def getChar( List<String> data, int x, int y ) {
    if( x < 0 || y < 0 || y >= data.size() || x >= data[y].size() ) { return ' ' }
    return data[y][x]
  }

}
