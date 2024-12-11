package mw.y2024.day07

import spock.lang.Specification

class d07 extends Specification {

  def "Day 07"() {
    given:
//      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data07-test.txt', 3749, 11387 ]
      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data07.txt', 1620690235709, 145397611075341 ]
      Map data = load( this.getClass().getResource( fileName ).readLines() )

    // Part 1. Find the total that can be reached by adding or multiplying the numbers in the list.
    when:
      def sum = 0
      data.each { key, list ->
        if( solve( key, list ) ) {
          println( "Found: $key" )
          sum += key
        }
        else {
          println( "Not found: $key" )
        }
      }
    then:
      sum == expectedResult1


    // For Part 2 we get another operator so long lists will have quite a lot of possible calculations.
    // For example 12 numbers in the list requires 11 operators from a set of 3 which is 3^11 = 177147 combinations.
    when:
      BigInteger part2sum = 0
      data.each { key, list ->
        if( solve2( key, list ) ) {
          part2sum += key
        }
        println("--------------")
      }
    then:
      part2sum == expectedResult2 // 7162722844532 is too low
  }

  // Part 1. Generate all possible combinations of operators + and * with length n.
  def opCombos( int n ) {
    long max = Math.pow( 2, n )
    def result = [] as List<String>
    for( long l = 0; l < max; l++ ) {
      result.add( Long.toString(l, 2).padLeft(n,'0').collect { it == '0' ? "+" : "*" } )
    }
    return result
  }

  // Part 2. Generate combinations of operators (nothing) and ||.
  def joinCombos( int n ) {
    long max = Math.pow( 3, n )
    def result = [] as List<String>
    for( long l = 0; l < max; l++ ) {
      result.add( Long.toString(l, 3).padLeft(n,'0').collect {
        it == '0' ? "+" : it == "1" ? '*' : "||"
      } )
    }
    return result
  }


  // See if the total can be found using a combination of 3 operators: +, * and ||.
  // The "||" operator joins two numbers into a new number as if they were strings: 1 || 2 = 12.
  def solve2( long total, List<Long> originalList ) {
    boolean found = false

      def list = originalList // allLists[listIndex]
      def allOps = joinCombos( list.size() - 1 )
      println( "  Find $total with $list with ${allOps.size()} ops ..." )

      for( int c = 0; c < allOps.size(); c++ ) {
        def opList = allOps[c]
        def expression = list[0]

        for( int i = 0; i < list.size() - 1; i++ ) {
          def operator = opList[i]

          switch( operator ) {
            case "*" : expression *= list[i + 1]; break
            case "+" : expression += list[i + 1]; break
            case "||" : expression = Long.parseLong( expression.toString() + list[i + 1].toString() ); break
            default : throw new RuntimeException( "Unknown operator: $operator" )
          }

          // If the expression is larger than the sought total then we can
          // stop trying with this operator combination since the value can only increase.
          if( expression > total ) { break }
        }

        if( expression == total ) {
          println("Found: $total = $list with $opList")
          return true
        }
      }
    return found
  }


  // See if the total can be reached by adding or multiplying the numbers in the list.
  def solve( long total, List<Long> list ) {
    boolean found = false
    def allOps = opCombos( list.size() - 1 )
    for( int c = 0; c < allOps.size(); c++ ) {
      def opList = allOps[c]
      def expression = list[0]

      for( int i=0; i < list.size()-1; i++ ) {
        if( opList[i] == "*" ) {
          expression *= list[i+1]
        }
        else {
          expression += list[i+1]
        }
      }
      println( expression )

      if( expression == total ) {
        found = true
      }
      println( "--------------")
    }

    return found
  }


  def load( List<String> lines ) {
    def map = [:]
    lines.each { line ->
      def totalAndList = line.split( ":" )
      def key = totalAndList[0].toLong()
      if( map.containsKey( key ) ) {
        throw new RuntimeException( "Duplicate key: $key" )
      }
      def list = totalAndList[1].trim().split( " " ).collect { it.toLong() }
      map[ key ] = list
    }
    return map
  }
}
