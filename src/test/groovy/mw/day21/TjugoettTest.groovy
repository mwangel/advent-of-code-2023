package mw.day21

import groovy.transform.CompileStatic
import spock.lang.Specification

@CompileStatic
class TjugoettTest extends Specification {

  int reachableWithin( int x0, int y0, int x1, int y1, Set<String> reachable ) {
    // find the number of reachable points within the rectangle x0,y0,x1,y1

    int count = 0
    for( int x = x0; x <= x1; x++ ) {
      for( int y = y0; y <= y1; y++ ) {
        if( reachable.contains( key( x, y ) ) ) {
          count++
        }
      }
    }
    return count
  }


  int reachableInBlock( int dx, int dy, int width, int height, Set<String> reachable ) {
    // find the number of reachable points within the block dx*width,dy*height,dx*width+width-1,dy*height+height-1

    int count = 0
    int x0 = dx * width
    int y0 = dy * height
    int x1 = x0 + width - 1
    int y1 = y0 + height - 1

    for( int x = x0; x <= x1; x++ ) {
      for( int y = y0; y <= y1; y++ ) {
        if( reachable.contains( key( x, y ) ) ) {
          count++
        }
      }
    }
    return count
  }


  class LoadResult {
    Set<String> rocks
    Set<String> reachable
    int width
    int height
  }

  private String key( int x, int y ) {
    return "$x,$y"
  }

  private LoadResult load( List<String> lines ) {
    Set<String> rocks = new HashSet<>(  )
    int width = lines[0].length()
    int height = lines.size()
    Set<String> reachable = new HashSet<>(  )

    lines.eachWithIndex { String line, int y ->
      line.eachWithIndex { String c, int x ->
        if( c == '#' ) {
          rocks.add( key( x, y ) )
        }
        else if( c == "S" ) {
          reachable.add( key( x, y ) )
        }
      }
    }

    return new LoadResult( rocks:rocks, reachable:reachable, width:width, height:height)
  }

  private def stepPartA( Set<String> rocks, Set<String> reachable, int width, int height ) {
    Set<String> newReachable = new HashSet<>(  )
    reachable.each { String s ->
      int x = s.split( "," )[0] as int
      int y = s.split( "," )[1] as int
      if( x > 0 ) {
        String newKey = key( x - 1, y )
        if( !rocks.contains( newKey ) && !reachable.contains( newKey ) ) {
          newReachable.add( newKey )
        }
      }
      if( x < width - 1 ) {
        String newKey = key( x + 1, y )
        if( !rocks.contains( newKey ) && !reachable.contains( newKey ) ) {
          newReachable.add( newKey )
        }
      }
      if( y > 0 ) {
        String newKey = key( x, y - 1 )
        if( !rocks.contains( newKey ) && !reachable.contains( newKey ) ) {
          newReachable.add( newKey )
        }
      }
      if( y < height - 1 ) {
        String newKey = key( x, y + 1 )
        if( !rocks.contains( newKey ) && !reachable.contains( newKey ) ) {
          newReachable.add( newKey )
        }
      }
    }
    return newReachable
  }


  private boolean isRock( Set<String> rocks, int x, int y, int width, int height ) {
    int modx = x % width
    if( modx < 0 ) {
      modx += width
    }
    int mody = y % height
    if( mody < 0 ) {
      mody += height
    }
    return rocks.contains( key( modx, mody ) )
  }

  private Set<String> stepB( Set<String> rocks, Set<String> reachable, int width, int height ) {
    Set<String> newReachable = new HashSet<>( 2_000_000 )
    reachable.each { String s ->
      String[] parts = s.split( "," )
      int x = parts[0] as int
      int y = parts[1] as int

      // Left
      String newKey = key( x - 1, y )
      if( !isRock( rocks, x-1, y, width, height ) ) {
        newReachable.add( newKey )
      }

      // Right
      newKey = key( x + 1, y )
      if( !isRock( rocks, x+1, y, width, height ) ) {
        newReachable.add( newKey )
      }

      // Up
      newKey = key( x, y - 1 )
      if( !isRock( rocks, x, y-1, width, height ) ) {
        newReachable.add( newKey )
      }

      // Down
      newKey = key( x, y + 1 )
      if( !isRock( rocks, x, y+1, width, height ) ) {
        newReachable.add( newKey )
      }
    }
    return newReachable
  }


  def "Test isRock"() {
    // -6543210123456
    // 0.............
    // 1.#..#..#..#..
    // 2.............
    // 3.............
    // 4.#..#..#..#..
    // 5.............
    given:
      Set<String> rocks = [ key(1,1) ] as Set<String>
      def a = isRock( rocks, 1, 1, 3, 3 ) == true
      def b = isRock( rocks, 4, 1, 3, 3 )
      def c = isRock( rocks, 1, 4, 3, 3 )
      def d = isRock( rocks, 4, 4, 3, 3 )
      def e = !isRock( rocks, 0, 0, 3, 3 )
      def f = !isRock( rocks, 5, 1, 3, 3 )
      def g = !isRock( rocks, -1, -1, 3, 3 )
      def h = !isRock( rocks, -1, -2, 3, 3 )
      def i = !isRock( rocks, -2, -1, 3, 3 )
      def j = isRock( rocks, -2, -2, 3, 3 )
      def k = isRock( rocks, -5, -5, 3, 3 )
    expect:
      a && b && c && d && e && f && g && h && i && j && k
  }

  def "Day 21 part 1"() {
    given:
      def test1 = """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
"""
      def serializables = load( test1.readLines() )
      Set<String> rocks = serializables.rocks
      Set<String> reachable = serializables.reachable
      int width = serializables.width
      int height = serializables.height

    when:
      6.times {
        reachable = stepPartA( rocks as Set<String>, reachable as Set<String>, width as int, height as int ) as Set<String>
      }
      def b = reachable.size() == 16

    then:
      b
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
      if( o == this ) return true
      if( o.getClass() != this.getClass() ) return false
      Point p = (Point) o
      return p.x == x && p.y == y
    }
    String toString() {
      return "($x,$y)"
    }
  }

//  def "Day 21 part 2 by villuna"() {
//    // https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
//    given:
//      def input = this.getClass().getResource( '/data21.txt' ).readLines()
//      def serializables = load( input )
//      Set<String> rocks = serializables.rocks
//      Set<String> reachable = serializables.reachable
//      int width = serializables.width
//      int height = serializables.height
//      Map<Point, Long> distances = new HashMap<>(  )
//
//    when:
//      width.times { int x ->
//        height.times { int y ->
//          Point p = new Point( x, y )
//          long distance = reachable.stream().map( String::split ).map( { it[0].toInteger() } ).map( { Math.abs( it - x ) } ).mapToLong( { it } ).sum()
//          distances.put( p, distance )
//        }
//      }
//
//    then:
//      true
//  }



  def "Day 21 part 2"() {
    given:
      def test1 = """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
""".readLines(  )
      def test2 = "S.\n##".readLines(  )
      def input = this.getClass().getResource( '/data21.txt' ).readLines()

      def serializables = load( test1 )
      Set<String> rocks = serializables.rocks
      Set<String> reachable = serializables.reachable
      int width = serializables.width
      int height = serializables.height

    when:
      500.times { Integer t ->
        reachable = stepB( rocks as Set<String>, reachable as Set<String>, width as int, height as int ) as Set<String>

        int c = reachableInBlock( 0, 0, width, height, reachable )
        int r = reachableInBlock( 1, 0, width, height, reachable )
        int l = reachableInBlock( -1, 0, width, height, reachable )
        int u = reachableInBlock( 0, -1, width, height, reachable )
        int d = reachableInBlock( 0, 1, width, height, reachable )

        int rr = reachableInBlock( 2, 0, width, height, reachable )
        int ll = reachableInBlock( -2, 0, width, height, reachable )
        int uu = reachableInBlock( 0, -2, width, height, reachable )
        int dd = reachableInBlock( 0, 2, width, height, reachable )
        int lu = reachableInBlock( -1, -1, width, height, reachable )
        int ld = reachableInBlock( -1, 1, width, height, reachable )
        int ru = reachableInBlock( 1, -1, width, height, reachable )
        int rd = reachableInBlock( 1, 1, width, height, reachable )

        println( "${t.toString(  ).padLeft( 4 )}; (l :$l, c :$c, r :$r, u :$u, d :$d); ${reachable.size(  ).toString(  ).padLeft( 10 )}" )
        println( "${t.toString(  ).padLeft( 4 )}; (ll:$ll, cc:--, rr:$rr, uu:$uu, dd:$dd); ${reachable.size(  ).toString(  ).padLeft( 10 )}" )
        println( "${t.toString(  ).padLeft( 4 )}; (lu:$lu, ld:$ld, ru:$ru, rd:$rd); ${reachable.size(  ).toString(  ).padLeft( 10 )}" )
      }
      def b = reachable.size() == 3542
    then:
      b
  }
}
