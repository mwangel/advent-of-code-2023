package mw.y2024.day08

import spock.lang.Specification

class d08 extends Specification {

  def "Day 08"() {
    given:
      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data08-test.txt', 41, 6 ]
//      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data08.txt', 4977, 1729 ]

      def input = this.getClass().getResource( fileName ).readLines()
      def (map, width, height) = loadInput( input )

    when:
      def result = 0
      println(map)
    then:
      width == 12
      height == 12
      result == expectedResult1
  }


  def loadInput( List<String> lines ) {
    Map<Character, List<Tuple2<Long,Long>>> map = [:].withDefault{ [] as List<Tuple2<Long,Long>> }

    lines.eachWithIndex { String line, int y ->
      line.eachWithIndex { String cstr, int x ->
        if( cstr != '.' ) {
          Character c = cstr[0]
          map[c] << [x, y]
        }
      }
    }

    return [map, lines[0].size(), lines.size()]
  }

  def getChar( List<String> data, int x, int y ) {
    if( x < 0 || y < 0 || y >= data.size() || x >= data[y].size() ) { return ' ' }
    return data[y][x]
  }

}
