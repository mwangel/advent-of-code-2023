package mw.y2024.day14

import spock.lang.Specification

class d14 extends Specification {

  def "Day 14 - robotic security"() {
    given:
//      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14-test.txt', 12, 100, 11, 7, 12]
//      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14-test2.txt', 1, 1, 11, 7, 1]
      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14.txt', 500, 100, 101, 103, 217132650]
      def input = this.getClass().getResource( fileName ).readLines()

    when:
      def robots = load( input )
      print( robots, maxx, maxy )
      robots.each { Robot r -> r.move( 100, maxx, maxy ) }
      def safetyFactor = calculateSafetyFactor( robots, maxx, maxy )

    then:
      robots.size() == count
      safetyFactor == safety
  }


  def "Day 14 - robotic tree"() {
    given:
//      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14-test.txt', 12, 100, 11, 7, 12]
//      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14-test2.txt', 1, 1, 11, 7, 1]
      def (fileName, count, steps, maxx, maxy, safety) = ['/y2024/data14.txt', 500, 100, 101, 103, 0]
      def input = this.getClass().getResource( fileName ).readLines()

    when:
      def robots = load( input )

      // These look like "something": 52, 153, 254, 355, 456, 557, 658, 759, 860, 961, ... with a step of 101
      // So let's step 52 and then 101 until we find a tree.
      // For this input there will be a tree at totalsteps = 6516 (52 + 64*101)

      int skip = 52
      robots.each { Robot r -> r.move( skip, maxx, maxy ) }
      // print( robots, maxx, maxy ); println("\n\n")

      int totalsteps = skip
      int n = 0
      while( true ) {
        if( n % 10000 == 0 ) println( "Step $n" )
        if( n > 1000 ) {
          print( "i give up" )
          break
        }
        n += 1
        totalsteps += 101
        robots.each { Robot r -> r.move( 101, maxx, maxy ) }
        println("\n step $n, total $totalsteps")
        // print( robots, maxx, maxy ); println("-------------------------------------------------------")

        if( isTree( robots, maxx, maxy ) ) {
          println( "Tree at $n" )
          print( robots, maxx, maxy )
          println("-------------------------------------------------------")
          break
        }
      }
      def safetyFactor = calculateSafetyFactor( robots, maxx, maxy )

    then:
      robots.size() == count
      totalsteps == 6516
  }


  long calculateSafetyFactor( List<Robot> robots, int maxx, int maxy ) {
    int x2 = maxx >> 1
    int y2 = maxy >> 1
    def q1bots = robots.findAll {
      it.x < x2 && it.y < y2
    }
    def q2bots = robots.findAll {
      it.x > x2 && it.y < y2
    }
    def q3bots = robots.findAll {
      it.x < x2 && it.y > y2
    }
    def q4bots = robots.findAll {
      it.x > x2 && it.y > y2
    }
    return q1bots.size() * q2bots.size() * q3bots.size() * q4bots.size()
  }

  boolean isTree( List<Robot> robots, int maxx, int maxy ) {
    for( int y=0; y < maxy; y++ ) {
//      def linebots = robots.findAll {
//        it.y == y
//      }.sort {
//        it.x
//      }
//
//      // If there are bots on x=0 or x=maxx-1 then it is not a tree
//      if( linebots.find {
//        it.x == 0 || it.x == maxx - 1
//      } ) {
//        return false
//      }

      // if there are gaps between the bots on this line then it is not a tree
//      for( int i = 0; i < linebots.size() - 1; i++ ) {
//        if( linebots[i + 1].x - linebots[i].x > 1 ) {
//          return false
//        }
//      }

    }

    // if any bot is in the same place as another bot then it is not a tree
    for( int i = 0; i < robots.size(); i++ ) {
      def r = robots[i]
      if( robots.find { Robot other ->
        other.id != r.id && other.x == r.x && other.y == r.y
      } ) {
        return false
      }
    }
    return true
  }

  class Robot {
    int id = 0
    static private int nextId = 0
    int x
    int y
    int vx
    int vy

    Robot( int x, int y, int vx, int vy ) {
      this.id = nextId++
      this.x = x
      this.y = y
      this.vx = vx
      this.vy = vy
    }

    String toString() {
      return "Rob $id @$x,$y v=$vx,$vy"
    }

    void move( int seconds, int maxX, int maxY ) {
      if( vx >= 0 ) {
        x = (x + vx * seconds) % maxX
      }
      else {
        x = x + vx * seconds
        if( x < 0 )
          x = (maxX + (x % maxX)) % maxX
      }

      if( vy >= 0 ) {
        y = (y + vy * seconds) % maxY
      }
      else {
        y = y + vy * seconds
        if( y < 0 )
          y = (maxY + (y % maxY)) % maxY
      }
    }
  }

  void print( List<Robot> robots, int width, int height ) {
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        def robotsHere = robots.findAll {
          it.x == x && it.y == y
        }
        if( robotsHere.size() > 0 ) {
          print( robotsHere.size() )
        }
        else {
          print( "." )
        }
      }
      println()
    }
  }

  def load( List<String> input ) {
    List<Robot> result = []
    def robotPattern = ~/p=(?<x>\d+),(?<y>\d+) v=(?<vx>[^,]+),(?<vy>.+)/ // eg: p=0,0 v=-1,0
    input.each {line ->
      def matcher = line =~ robotPattern
      if( !matcher.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      result << new Robot( matcher.group( "x" ) as int, matcher.group( "y" ) as int, matcher.group( "vx" ) as int, matcher.group( "vy" ) as int )
    }
    return result
  }


  // In part 2 the tree looks like this (some lines removed here. There are no overlapping robots though:
  //  ...................................................................................1.................
  //  .............1...............................1..............................................1........
  //  ........................................................................................1............
  //  .....................................................................................................
  //  ........1....................1.....................................................1.................
  //  ..1........1.......................................................................1.1...............
  //  .....................................................................................................
  //  .........................................1111111111111111111111111111111.........1...................
  //  .........................................1.............................1.......................1.....
  //  .........................................1.............................1.............................
  //  ....................1....................1.............................1.............................
  //  .........1............................1..1.............................1.....................1.......
  //  .........................................1..............1..............1.............................
  //  .........................................1.............111.............1.............................
  //  .......................................1.1............11111............1.............................
  //  .........................................1...........1111111...........1.............................
  //  .........................................1..........111111111..........1.............................
  //  ......1............1...............1.....1............11111............1.............................
  //  ...1........1............................1...........1111111...........1.............................
  //  .........................................1..........111111111..........1.............................
  //  .........................................1.........11111111111.........1.............................
  //  ................1............1...........1........1111111111111........1..............1.1............
  //  .........................................1..........111111111..........1.............................
  //  ..............................1..........1.........11111111111.........1.............................
  //  .........................................1........1111111111111........1.............................
  //  .........................................1.......111111111111111.......1.............................
  //  .....................1...................1......11111111111111111......1.......................1.....
  //  .........................................1........1111111111111........1..................1..........
  //  .....1...................................1.......111111111111111.......1.............................
  //  .........................................1......11111111111111111......1.......1....................1
  //  ..............1..........................1.....1111111111111111111.....1.............................
  //  .........................................1....111111111111111111111....1.............................
  //  .........................................1.............111.............1.............................
  //  .........................................1.............111.............1.............................
  //  .........................................1.............111.............1....................1........
  //  ...............1.........................1.............................1.............................
  //  ........................1............1...1.............................1.............................
  //  ...1..1..............1...1...............1.............................1.............................
  //  1........................................1.............................1.............................
  //  .........................................1111111111111111111111111111111.............................
  //  ......................................................................................1..............
  //  .....................................................................................................
  //  ...............1.............................................................................1.......
  //  ....1...............................................1...1............................................


}
