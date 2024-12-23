package mw.y2024.day13

import spock.lang.Shared
import spock.lang.Specification

class d13 extends Specification {


  def "Day 13 - integer division"() {
    given:
//      def (fileName, expected1) = ['/y2024/data13-test.txt', 480]
      def( fileName, expected1 ) = [ '/y2024/data13.txt', 624 ]
      def input = this.getClass().getResource( fileName ).readLines()

    when:
      def result1 = part1( input )

    then:
      result1 == expected1
  }


  // In Part 2 the prizes coordinates are increased by 10_000_000_000_000.


  def part1( List<String> data ) {
    def result = 0
    def patternA = ~/Button A: X\+(?<x>\d+), Y\+(?<y>\d+)/ // Button A: X+0, Y+0
    def patternB = ~"Button B: X\\+(?<x>\\d+), Y\\+(?<y>\\d+)" // Button B: X+0, Y+0
    def patternP = ~"Prize: X=(?<x>\\d+), Y=(?<y>\\d+)" // Prize: X=0, Y=0

    for( int index = 0; index < data.size(); index++ ) {
      def line = data[index]

      int xStepA = 0
      int yStepA = 0
      int xStepB = 0
      int yStepB = 0
      int targetX = 0
      int targetY = 0

      if( line.isEmpty() ) continue

      def matcherA = line =~ patternA
      if( !matcherA.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      xStepA = matcherA.group( "x" ) as int
      yStepA = matcherA.group( "y" ) as int

      line = data[++index]
      def matcherB = line =~ patternB
      if( !matcherB.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      xStepB = matcherB.group( "x" ) as int
      yStepB = matcherB.group( "y" ) as int

      line = data[++index]
      def matcherP = line =~ patternP
      if( !matcherP.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      targetX = matcherP.group( "x" ) as int
      targetY = matcherP.group( "y" ) as int

      //println( "A:$xStepA,$yStepA - B:$xStepB,$yStepB - P:$targetX,$targetY" )
      def paths = findPaths( xStepA, yStepA, xStepB, yStepB, targetX, targetY )
      if( paths ) {
        def cheapest = findCheapestPath( paths )
        def cost = 3 * cheapest[0] + cheapest[1]
        println( paths )
        println( "Cheapest is ${cheapest} : ${cost}" )
        result += cost
      }
      else {
        println( "No path found for $xStepA,$yStepA / $xStepB,$yStepB -> $targetX,$targetY " )
      }
      println( "----" )
    }
    return result
  }


  def findCheapestPath( List<List<Long>> paths ) {
    def cheapest = paths.min { List<Long> p -> 3*p[0] + p[1] }
    return cheapest
  }


  def findPaths( int xa, int ya, int xb, int yb, int xp, int yp ) {
    def paths = []

    for( int i = 0; i < 100; i++ ) {
      def x = i * xa
      def y = i * ya
      if( (xp - x) % xb == 0 && (yp - y) % yb == 0 ) {
        def j = (xp - x) / xb
        if( j == (yp - y) / yb ) {
          paths.add( [i.toLong(), j.toLong()] )
        }
      }
    }
    return paths
  }

}
