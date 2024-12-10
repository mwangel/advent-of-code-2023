package mw.y2024.day04

import spock.lang.Specification

class d04 extends Specification {

  def "Day 04"() {
    given:
      def data = this.getClass().getResource( '/y2024/data04.txt' ).readLines()
//      def data = this.getClass().getResource( '/y2024/data04.txt' ).readLines()

    when:
      def result = findAllXmases( data )
    then:
      result == 2562 // test data has 18

    when:
      def result2 = findAllMasCrosses( data )
    then:
      result2 == 1902 // test data has 9
  }


  def directions = [[1, 0], [0, 1], [-1, 0], [0, -1],   // E, S, N, W
                    [1, 1], [-1, 1], [1, -1], [-1, -1]] // SE, SW, NE, NW

  def findAllXmases( List<String> data ) {
    def xmases = 0
    for( int y=0; y < data.size(); y++ ) {
      for( int x=0; x < data[y].size(); x++ ) {
        // Each dir contains steps in x and y for searching in one direction.
        directions.each { List<Integer> dir ->
          def word = ""
          4.times { int n ->
            word += getChar( data, x + n * dir[0], y + n * dir[1] )
          }
          if( word == "XMAS" ) xmases++
        }
      }
    }
    return xmases
  }

  def getChar( List<String> data, int x, int y ) {
    if( x < 0 || y < 0 || y >= data.size() || x >= data[y].size() ) { return ' ' }
    return data[y][x]
  }

  def findAllMasCrosses( List<String> data ) {
    def xmases = 0
    for( int y=0; y < data.size(); y++ ) {
      for( int x=0; x < data[y].size(); x++ ) {
        def s =
          (int)getChar( data, x, y ) + (int)getChar( data, x+2, y ) +
          (int)getChar( data, x+1, y+1 )
          (int)getChar( data, x, y+2 ) + (int)getChar( data, x+2, y+2 )
        if( s == 385 ) { // 65 + 2*77 + 2*83
          xmases++
        }
      }
    }
    return xmases
  }

  def findAllMasCrossesWithRegex( List<String> data ) {
    def xmases = 0
    for( int y=0; y < data.size(); y++ ) {
      for( int x=0; x < data[y].size(); x++ ) {
        def s =
          getChar( data, x, y ) + getChar( data, x+1, y ) + getChar( data, x+2, y ) +
            getChar( data, x, y+1 ) + getChar( data, x+1, y+1 ) + getChar( data, x+2, y+1 ) +
            getChar( data, x, y+2 ) + getChar( data, x+1, y+2 ) + getChar( data, x+2, y+2 )
        if( s.matches( "M.M.A.S.S" ) ||
          s.matches( "S.M.A.S.M" ) ||
          s.matches( "S.S.A.M.M" ) ||
          s.matches( "M.S.A.M.S" ) )
        {
          xmases++
        }
      }
    }
    return xmases
  }

}
