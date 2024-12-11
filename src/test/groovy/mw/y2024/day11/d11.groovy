package mw.y2024.day11

import groovy.transform.CompileStatic
import spock.lang.Specification
import spock.lang.Unroll

class d11 extends Specification {

  @Unroll
  def "Day 11 part 1"() {
    when:
      def result = part1( input, blinks )

    then:
      long startTime = System.currentTimeMillis()
      result == expected
      println "Time: ${System.currentTimeMillis() - startTime} ms"

    where:
      expected || blinks | input
      55312    || 25     | [125, 17] as LinkedList<Long>
      212655   || 25     | [4, 4841539, 66, 5279, 49207, 134, 609568, 0] as LinkedList<Long>
  }


  def "something else"() {
    when:
      Map<Long,Set<Long>> known = [:].withDefault { [] as Set<Long> }
      known[0L] << 1L

      //def input = [125, 17] as LinkedList<Long> // This results in us knowing the results of 76 numbers.
      def input = [4, 4841539, 66, 5279, 49207, 134, 609568, 0] as LinkedList<Long> // This results in us knowing the results of ? numbers.

      input.each { long nr ->
        recursiveRundown( nr, known )
      }
      println("Now we know the results of ${known.size()} numbers.")

    then:
      false
  }

  def "Using non-recursive rundown"() {
    when:
      Map<Long,Set<Long>> known = [:].withDefault { [] as Set<Long> }
      known[0L] << 1L

      def input = [125, 17] as LinkedList<Long> // This results in us knowing the results of 76 numbers.
      input = [4, 4841539, 66, 5279, 49207, 134, 609568, 0] as LinkedList<Long> // This results in us knowing the results of ? numbers.

      def unknowns = [] as Set<Long>
      unknowns.addAll( input )

      while( unknowns.size() ) {
        def nr = unknowns.first()
        def result = nonrecursiveRundown( nr )
        known[nr].addAll( result )
        unknowns.remove( nr )
        result.each { long n ->
          if( !known.containsKey( n ) ) {
            unknowns << n
          }
        }
      }
      println("Now we know the results of ${known.size()} numbers.")

      long childCount = 0
      input.each { long nr ->
        childCount += countChildren( nr, known )
      }

    then:
      known.size() == 3960
      childCount == 1
  }


  // Find the result (as a List) of "nr" by applying the rules of the game.
  def nonrecursiveRundown( long nr ) {
    String s = nr.toString()
    int x = s.size()
    if( x % 2 == 0 ) {
      int xx = x >> 1
      long n1 = s[0..xx-1].toLong()
      long n2 = s[xx..x-1].toLong()
      return [n1, n2]
    }

    return [nr * 2024]
  }

  // Starting on "nr" recursively find all the children and count them.
  def countChildren( long nr, Map<Long, Set<Long>> known ) {
    def results = known[nr]
    if( results.size() == 0 ) {
      return 0
    }
    else {
      return known[nr].sum { long n ->
        countChildren( n, known )
      }
    }
  }


  def recursiveRundown( long nr, Map<Long,Set<Long>> known ) {
    if( known.containsKey( nr ) )
      return known[nr]

    String s = nr.toString()
    int x = s.size()
    if( x % 2 == 0 ) {
      int xx = x >> 1
      //Set<Long> result = new HashSet<>()
      long n1 = s[0..xx-1].toLong()
      long n2 = s[xx..x-1].toLong()
      known[nr] << n1
      known[nr] << n2
      recursiveRundown( n1, known )
      recursiveRundown( n2, known )
      return known[nr]
    }

    known[nr] << nr * 2024
    recursiveRundown( nr * 2024, known )
    return known[nr]
  }


  /**
   * This is the most straight-forward solution to part 1.
   * Unfortunately it will run out of memory when the number of blinks is too high.
   **/
  @CompileStatic
  long part1( LinkedList<Long> input, int blinks ) {
    blinks.times { int blink ->
      def nextGen = [] as LinkedList<Long>
      input.each { Long nr ->
        String s = nr.toString()
        int x = s.size()

        if( nr == 0 ) nextGen << 1L
        else if( x % 2 == 0 ) {
          int xx = x >> 1
          nextGen << s[0..xx-1].toLong()
          nextGen << s[xx..x-1].toLong()
        }
        else nextGen << nr * 2024
      }
      println( " $blink: $input -> $nextGen\n" )
      input = nextGen
    }

    return input.size()
  }
}
