  package mw.y2024.day13

import spock.lang.Shared
import spock.lang.Specification

class d13 extends Specification {


  def "Day 13 - integer division"() {
    given:
      def( fileName, expected1 ) = [ '/y2024/data13-test.txt', 480 ]
//      def( fileName, expected1 ) = [ '/y2024/data13.txt', 624 ]
      def input = this.getClass().getResource( fileName ).readLines()

    when:
      def result1 = part1( input )

    then:
      result1 == expected1
  }


  def part1( List<String> data ) {
    def result = 0
    def patternA = ~/Button A: X\+(?<x>\d+), Y\+(?<y>\d+)/ // Button A: X+0, Y+0
    def patternB = ~"Button B: X\\+(?<x>\\d+), Y\\+(?<y>\\d+)" // Button B: X+0, Y+0
    def patternP = ~"Prize: X=(?<x>\\d+), Y=(?<y>\\d+)" // Prize: X=0, Y=0

    for( int index=0; index < data.size(); index++ ) {
      def line = data[index]

      int xStepA = 0
      int yStepA = 0
      int xStepB = 0
      int yStepB = 0
      int targetX = 0
      int targetY = 0

      if( line.isEmpty(  )) continue

      def matcherA = line =~ patternA
      if( !matcherA.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      xStepA = matcherA.group("x") as int
      yStepA = matcherA.group("y") as int

      line = data[++index]
      def matcherB = line =~ patternB
      if( !matcherB.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      xStepB = matcherB.group("x") as int
      yStepB = matcherB.group("y") as int

      line = data[++index]
      def matcherP = line =~ patternP
      if( !matcherP.matches() ) throw new RuntimeException( "Unexpected line: $line" )
      targetX = matcherP.group("x") as int
      targetY = matcherP.group("y") as int

      println( "A:$xStepA,$yStepA - B:$xStepB,$yStepB - P:$targetX,$targetY" )
    }
    return result
  }

}
