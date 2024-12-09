package mw.y2024.day07

import spock.lang.Specification

class d07 extends Specification {

  def "Day 07"() {
    given:
//      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data07-test.txt', 3749, 6 ]
      def( fileName, expectedResult1, expectedResult2 ) = [ '/y2024/data07.txt', 4977, 1729 ]
      Map data = load( this.getClass().getResource( fileName ).readLines() )

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
  }

  def opCombos( int n ) {
    long max = Math.pow( 2, n )
    def result = [] as List<String>
    for( long l = 0; l < max; l++ ) {
      result.add( Long.toString(l, 2).padLeft(n,'0').collect { it == '0' ? "+" : "*" } )
    }
    return result
  }


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
        println("Found: $list with $opList")
        found = true
      }
      else {
        println("Not found: $list with $opList")
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
