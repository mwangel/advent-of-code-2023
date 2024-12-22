package mw.y2024.day19

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import spock.lang.Specification

class d19 extends Specification {

  def "Day 19 - spelling with towels"() {
    given:
//      def (String fileName, long p1, long p2) = ['/y2024/data19-test.txt', 6, 16]
      def (String fileName, long p1, long p2) = ['/y2024/data19.txt', 317, 883_443_544_805_484]

      def input = this.getClass().getResource( fileName ).readLines()
      def (towels, combinations) = load( input )
      println( towels )

    when:
      def result1 = findPossibleCombinations( towels, combinations )
    then:
      result1 == p1

    when:
      def result2 = part2( towels, combinations )
    then:
      result2 == p2
  }


  def findPossibleCombinations( List<String> towels, List<String> combinations ) {
    def result = 0
    combinations.each { String combination ->
      print( "Testing    : $combination - " )
      if( eat( combination, towels ) ) {
        result++
        println( "✓" )
      }
      else println("❌")
    }
    println( "------------------" )
    return result
  }

  @Memoized
  @CompileStatic
  def eat( String s, List<String> towels ) {
    //println( "Eating: $s" )
    if( s.isEmpty() ) {
      return true
    }

    def alternatives = towels.findAll { s.startsWith(it) }
    if( alternatives.isEmpty() ) {
      return false
    }
    else {
      for( int i=0; i < alternatives.size(); i++ ) {
        def test = eat( s - alternatives[i], towels )
        if( test ) {
          return true
        }
      }
      return false
    }
  }


  def part2( List<String> towels, List<String> combinations ) {
    def result = 0
    combinations.each { String combination ->
      print( "Testing: $combination - " )
      def n = countTheWays( combination, towels )
      println( n )
      result += n
    }
    return result
  }

  @Memoized
  @CompileStatic
  long countTheWays( String s, List<String> towels ) {
    //println( "Eating: $s" )
    if( s.isEmpty() ) {
      return 1L
    }

    def alternatives = towels.findAll { s.startsWith(it) }
    if( alternatives.isEmpty() ) {
      return 0L
    }
    else {
      long sum = 0
      for( int i=0; i < alternatives.size(); i++ ) {
        long test = countTheWays( s - alternatives[i], towels )
        if( test ) {
          sum += test
        }
      }
      return sum
    }
  }


  def load( List<String> input ) {
    List<String> towels = []
    List<String> combinations = []

    def readingTowels = true
    input.each { String line ->
      if( line.isEmpty() ) {
        readingTowels = false
      }
      else if( readingTowels ) {
        towels.addAll( line.trim().split(", ") )
      }
      else { // reading combinations
        combinations.add( line )
      }
    }
    return [towels, combinations]
  }
}
