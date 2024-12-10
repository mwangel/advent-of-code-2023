package mw.y2024.day10

import spock.lang.Specification

class d10 extends Specification {

  def "Day 10"() {
    given:
//      def data = this.getClass().getResource( '/y2024/data10.txt' ).readLines()
      def data = this.getClass().getResource( '/y2024/data10-test.txt' ).readLines()

      def map = data.collect { String line ->
        line.collect {it.toLong() }
      }

    when:
      def result = findTrails( map )
    then:
      result.size(  ) == 9 // test data has 9
  }


  def directions = [[1, 0], [0, 1], [-1, 0], [0, -1]] // E, S, W, N

  def findTrails( List<List<Long>> map ) {
    List<Tuple2<Long, Long>> startPoints = []
    int width = map[0].size()
    int height = map.size()

    height.times {int y ->
      width.times {int x ->
        if( getHeight( map, x, y ) == 0 )
          startPoints << new Tuple2<>( x, y )
      }
    }

    startPoints.each {Tuple2<Long, Long> start ->
      Set<Tuple2<Long, Long>> foundNines = new HashSet<>()
      walkFrom( start, map, foundNines )
      println( "Found ${foundNines.size()} nines from $start" )
    }

    return 0
  }

  def walkFrom( Tuple2<Long, Long> start, List<List<Long>> map, Set<Tuple2<Long, Long>> foundNines ) {
    // Each dir contains steps in x and y for searching in one direction.
    def currentHeight = getHeight( map, start[0], start[1] )
    directions.each { List<Integer> dir ->
      def newX = start[0] + dir[0]
      def newY = start[1] + dir[1]
      def newHeight = getHeight( map, newX, newY )
      if( newHeight == 9 ) {
        foundNines << new Tuple2<>( newX, newY )
      }
      else if( newHeight == currentHeight + 1 ) {
        walkFrom( new Tuple2<>( newX, newY ), map, foundNines )
      }
    }
  }

  def getHeight( List<List<Long>> map, int x, int y ) {
    if( x < 0 || y < 0 || y >= map.size() || x >= map[y].size() ) { return 1_000_000 }
    return map[y][x]
  }

}
