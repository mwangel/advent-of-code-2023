package mw.day17

import groovy.transform.CompileStatic

@CompileStatic
class Main17 {
  static void main( String[] args ) {
    println "Hello world!"
  }

  class SortedPlaceSet extends TreeSet<Place> {
    SortedPlaceSet( Comparator<Place> comparator ) {
      super( comparator )
    }
  }

  int calculateCost( Place start, Place end, List<List<Integer>> data, List<String> path ) {
    int x = 0
    int y = 0
    int cost = 0
    path.each {String dir ->
      def integers = move( x, y, dir )
      (x, y) = [integers[0], integers[1]]
      cost += data[y][x]
    }
    assert x == end.x
    assert y == end.y
    println( "Cost: $cost" )
    return cost
  }


  /** This is part B */
  Place minimizeCost( Place start, Place end, List<List<Integer>> data, int width, int height ) {
    if( start.totalCost != 0 ) start.totalCost = 0
    def shortestTo = new HashMap<String, Integer>()

    def sortedHeads = new PriorityQueue<Place>( 100_000, Comparator.comparing( Place::getTotalCost ) )
    sortedHeads.add( start )

    while( true ) {
      def bestHead = sortedHeads.poll()
      if( bestHead.x == end.x && bestHead.y == end.y ) {
        show( data, bestHead )
        return bestHead
      }

      for( dir in [Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP] ) {
        if( bestHead.prevDirection != null ) {
          if( bestHead.prevDirection == Direction.RIGHT && dir == Direction.LEFT ) {continue          }
          if( bestHead.prevDirection == Direction.LEFT && dir == Direction.RIGHT ) {            continue          }
          if( bestHead.prevDirection == Direction.UP && dir == Direction.DOWN ) {            continue          }
          if( bestHead.prevDirection == Direction.DOWN && dir == Direction.UP ) {            continue          }
        }

        def integers = move( bestHead.x, bestHead.y, dir )
        int x = integers[0]
        int y = integers[1]
        if( x < 0 || x >= width || y < 0 || y >= height ) { continue        }

        // at least 4 steps in the same direction
        if( bestHead.prevDirection != null && bestHead.prevDirection != dir && bestHead.stepsInSameDirection < 4 ) {          continue        }
        def place = new Place( x, y, bestHead, dir, (bestHead.totalCost + (data[y][x])) )
        // max 10 steps in the same direction
        if( place.stepsInSameDirection > 10 ) { continue }

        if( place.x == end.x && place.y == end.y ) {
          return place
        }

        def key = "$x;$y;$dir;${place.stepsInSameDirection}".toString()
        if( shortestTo.containsKey( key ) && shortestTo.get( key ) <= place.totalCost ) {  continue        }
        shortestTo.put( key, place.totalCost )

        sortedHeads.add( place )
      }
    }
    return null
  }



  Place minimizeCost_part_a( Place start, Place end, List<List<Integer>> data, int width, int height ) {
    // Minimize the cost for going from star to end while not stepping more than 3 time in the same direction in a sequence.
    // The cost is the sum of the values in the data array.
    // The data is a 2D array with the given width and height.
    // The data contains the values 1, 2, 3, 4, 5, 6, 7, 8, 9.
    // The start and end are the coordinates of the start and end points.
    // We can move UP, DOWN, LEFT, RIGHT.
    // We can't move outside the data array.
    // We can't step more than 3 times in the same direction in a sequence.
    // We can't step on the same place more than once.
    // We can't step on the start and end points.
    // Return the minimum cost.
    // Store the path in a list of Place objects.

    if( start.totalCost != 0 ) start.totalCost = 0
    def shortestTo = new HashMap<String, Integer>()

    def sortedHeads = new PriorityQueue<Place>( 100_000, Comparator.comparing( Place::getTotalCost ) )
    sortedHeads.add( start )

    while( true ) {
      def bestHead = sortedHeads.poll()
      if( bestHead.x == end.x && bestHead.y == end.y ) {
        show( data, bestHead )
        return bestHead
      }

      for( dir in [Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP] ) {
        if( bestHead.prevDirection != null ) {
          if( bestHead.prevDirection == Direction.RIGHT && dir == Direction.LEFT ) {continue          }
          if( bestHead.prevDirection == Direction.LEFT && dir == Direction.RIGHT ) {            continue          }
          if( bestHead.prevDirection == Direction.UP && dir == Direction.DOWN ) {            continue          }
          if( bestHead.prevDirection == Direction.DOWN && dir == Direction.UP ) {            continue          }
        }

        def integers = move( bestHead.x, bestHead.y, dir )
        int x = integers[0]
        int y = integers[1]
        if( x < 0 || x >= width || y < 0 || y >= height ) { continue        }

        if( bestHead.prevDirection == dir && bestHead.stepsInSameDirection > 2 ) {          continue        }

        def place = new Place( x, y, bestHead, dir, (bestHead.totalCost + (data[y][x])) )
        //if( place.stepsInSameDirection > 3 ) { continue }
        if( place.x == end.x && place.y == end.y ) {
          return place
        }

        def key = "$x;$y;$dir;${place.stepsInSameDirection}".toString()
        if( shortestTo.containsKey( key ) && shortestTo.get( key ) <= place.totalCost ) {  continue        }
        shortestTo.put( key, place.totalCost )

        sortedHeads.add( place )
      }
    }
    return null
  }

  void show( List<List<Integer>> data, Place head ) {
    int y = 0
    println( head.totalCost )
    def xys = head.parents
    data.each {row ->
      String s = row.collect {
        it
      }.join( "" )
      def here = xys.findAll() {
        it.y == y
      }
      here.each {place ->
        s = s.substring( 0, place.x ) + place.dirIndicator + s.substring( place.x + 1 )
      }
      println( s )
      y++
    }
    println( "----------------" )
  }

  List<Integer> move( int x, int y, String dir ) {
    switch( dir ) {
      case "U":
        y--
        break
      case "D":
        y++
        break
      case "L":
        x--
        break
      case "R":
        x++
        break
    }
    return [x, y]
  }

  List<Integer> move( int x, int y, Direction dir ) {
    switch( dir ) {
      case Direction.UP:
        y--
        break
      case Direction.DOWN:
        y++
        break
      case Direction.LEFT:
        x--
        break
      case Direction.RIGHT:
        x++
        break
    }
    return [x, y]
  }


  List<List<Integer>> convertInput( List<String> lines ) {
    List<List<Integer>> data = []
    lines.each {line ->
      data << line.collect {String c -> Integer.parseInt( c )
      } as List<Integer>
    }
    return data
  }
}

