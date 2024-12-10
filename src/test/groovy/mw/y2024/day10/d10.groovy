package mw.y2024.day10

import spock.lang.Specification

class d10 extends Specification {

  def "Day 10"() {
    given:
      def data = this.getClass().getResource( '/y2024/data10.txt' ).readLines()
//      def data = this.getClass().getResource( '/y2024/data10-test.txt' ).readLines()

      def map = data.collect { String line ->
        line.collect {it.toLong() }
      }

    when:
      def (scores, paths) = findTrails( map )
      def pathSums = paths.values().sum { List<List<Tuple2<Long, Long>> > foundpaths -> foundpaths.size() }
    then:
      //result.size(  ) == 194 // test data has 9
      scores.sum() == 472 // test data has 36
      println(pathSums)
      pathSums == 969 // test data has 81

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

    // Part 1, find the number of nines reachable from each starting point.
    def scores = []
    startPoints.each { Tuple2<Long, Long> start ->
      Set<Tuple2<Long, Long>> foundNines = new HashSet<>()
      walkFrom( start, map, foundNines )
      scores << foundNines.size()
      //println( "Found ${foundNines.size()} nines from $start" )
    }

    // Part 2, find all the unique paths to the nines.
    def paths = [:]
    startPoints.each { Tuple2<Long, Long> start ->
      List< List<Tuple2<Long, Long>> > foundPaths = []
      List<Tuple2<Long, Long>> currentPath = []
      walkFrom2( start, map, currentPath, foundPaths )
      paths[start] = foundPaths
      //println( "Found ${foundPaths.size()} paths to nines from $start" )
    }

    return [scores, paths]
  }

  def walkFrom( Tuple2<Long, Long> start, List<List<Long>> map, Set<Tuple2<Long, Long>> foundNines ) {
    // Each dir contains steps in x and y for searching in one direction.
    def currentHeight = getHeight( map, start[0], start[1] )
    if( currentHeight == 9 ) {
      foundNines << start
      return
    }

    directions.each { List<Integer> dir ->
      def newX = start[0] + dir[0]
      def newY = start[1] + dir[1]
      def newHeight = getHeight( map, newX, newY )
      if( newHeight == currentHeight + 1 ) {
        walkFrom( new Tuple2<>( newX, newY ), map, foundNines )
      }
    }
  }

  def walkFrom2( Tuple2<Long, Long> start, List<List<Long>> map, List<Tuple2<Long, Long>> path, List<List<Tuple2<Long, Long>>> foundPaths ) {
    path << start
    def currentHeight = getHeight( map, start[0], start[1] )
    if( currentHeight == 9 ) {
      def newPath = new LinkedList<>( path )
      foundPaths << newPath
      path.removeLast(  )
      return
    }

    // Each dir contains steps in x and y for searching in one direction.
    directions.each { List<Integer> dir ->
      Long newX = start[0] + dir[0]
      Long newY = start[1] + dir[1]
      Long newHeight = getHeight( map, newX, newY )
      if( newHeight == currentHeight + 1 ) {
        walkFrom2( new Tuple2<>( newX, newY ), map, path, foundPaths )
      }
    }
    path.removeLast()
  }


  def getHeight( List<List<Long>> map, long x, long y ) {
    if( x < 0 || y < 0 || y >= map.size() || x >= map[y].size() ) { return 1_000_000 }
    return map[y][x]
  }

}
