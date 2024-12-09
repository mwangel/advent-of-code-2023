package mw.y2024.day08

import spock.lang.Specification

class d08 extends Specification {

  def "Day 08"() {
    given:
      def( fileName, w1, h1, expectedResult1, expectedResult2 ) = [ '/y2024/data08.txt', 50, 50, 354, 1263 ]
//      def( fileName, w1, h1, expectedResult1, expectedResult2 ) = [ '/y2024/data08-test.txt', 12, 12, 14, 34 ]
//      def( fileName, w1, h1, expectedResult1, expectedResult2 ) = [ '/y2024/data08-test-2.txt', 10, 10, 3, 9 ]

      def input = this.getClass().getResource( fileName ).readLines()
      def (map, width, height) = loadInput( input )

    when:
      println(map + "\n---")
      def result = calculateAntinodePositions( map, width, height )
      def r1 = result[0]
      def r2 = result[1]
      println(r1)
      paint( map, r2, width, height )
    then:
      width == w1
      height == h1
      r1.size() == expectedResult1
      r2.size() == expectedResult2
  }


  def calculateAntinodePositions( Map<Character,List<Tuple2<Long,Long>>> map, int width, int height ) {
    def antinodePositions = [] as List<Tuple2<Long,Long>>
    def antinodePositions2 = [] as List<Tuple2<Long,Long>>

    map.each { Character c, List<Tuple2<Long,Long>> positions ->
      for( int a=0; a < positions.size(); a++ ) {
        for( int b=a+1; b < positions.size(); b++ ) {
          Tuple2<Long,Long> p1 = positions[a]
          Tuple2<Long,Long> p2 = positions[b]
          antinodePositions.addAll( calculateAntinodes( p1, p2 ) )
          antinodePositions2.addAll( calculateAntinodesPart2( p1, p2, width, height ) )
        }
      }
    }
    return [antinodePositions.unique().findAll { Tuple2<Long,Long> p -> p.V1 >= 0 && p.V2 >= 0 && p.V1 < width && p.V2 < height },
            antinodePositions2.unique().findAll { Tuple2<Long,Long> p -> p.V1 >= 0 && p.V2 >= 0 && p.V1 < width && p.V2 < height }]
  }


  def calculateAntinodes( Tuple2<Long,Long> p1, Tuple2<Long,Long> p2 ) {
    def dx = p2.V1 - p1.V1
    def dy = p2.V2 - p1.V2
    def antinodes = [] as List<Tuple2<Long,Long>>

    antinodes << new Tuple2<Long,Long>(p1.first - dx, p1.second - dy)
    antinodes << new Tuple2<Long,Long>(p2.first + dx, p2.second + dy)

    return antinodes
  }


  def calculateAntinodesPart2( Tuple2<Long,Long> p1, Tuple2<Long,Long> p2, int width, int height ) {
    def dx = p2.V1 - p1.V1
    def dy = p2.V2 - p1.V2
    def antinodes = [] as List<Tuple2<Long,Long>>


    // First one way...
    Tuple2<Long,Long> t = new Tuple2<Long,Long>( p1.V1, p1.V2 )
    while( true ) {
      Tuple2<Long,Long> t2 = new Tuple2<Long,Long>( t.V1 - dx, t.V2 - dy )
      if( t2.V1 < 0 || t2.V2 < 0 || t2.V1 >= width || t2.V2 >= height ) { break }
      antinodes << t2
      t = t2
    }

    // Then the other way...
    t = new Tuple2<Long,Long>( p2.V1, p2.V2 )
    while( true ) {
      Tuple2<Long,Long> t2 = new Tuple2<Long,Long>( t.V1 + dx, t.V2 + dy )
      if( t2.V1 < 0 || t2.V2 < 0 || t2.V1 >= width || t2.V2 >= height ) { break }
      antinodes << t2
      t = t2
    }

    // Add the original antenna points
    antinodes << p1
    antinodes << p2

    return antinodes
  }


  def paint( Map<Character,List> input, List<Tuple2<Long,Long>> antinodePositions, int width, int height ) {
    // print the map with input and antinode positions
    for( int y=0; y < height; y++ ) {
      for( int x=0; x < width; x++ ) {
        def inlist = input.find { it.value.contains( [x, y] ) }
        def c = inlist ? inlist.key : '.'

        if( antinodePositions.contains( [x, y] ) ) {
          print( "#" )
        }
        else {
          print( c )
        }
      }
      println()
    }
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
