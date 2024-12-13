package mw.y2024.day11

import groovy.transform.CompileStatic
import spock.lang.Specification
import spock.lang.Unroll

class d11 extends Specification {

  @Unroll
  def "Using non-recursive rundown returns #expected for #blinks blinks and input #input"() {
    given:
      // We can actually find the results for all numbers in the input and store them in this map.
      Map<Long,List<Long>> knownChildNumbers = [:].withDefault { [] as List<Long> }
      Map<String,Long> knownCounts = [:]

      def unknowns = new HashSet<Long>( input )

      while( unknowns.size() ) {
        long nr = unknowns.first()
        def result = nonrecursiveRundown( nr )
        knownChildNumbers[nr].addAll( result )
        unknowns.remove( nr )
        result.each { long n ->
          if( !knownChildNumbers.containsKey( n ) ) {
            unknowns << n
          }
        }
      }
      println("Now we know the results of ${knownChildNumbers.size()} numbers. Try blinking $blinks times.")

    when:
      def childCount = input.sum { long nr -> countChildrenRecursively( nr, knownChildNumbers, knownCounts, blinks ) }

    then:
      childCount == expected

    where:
      expected        || blinks | input
      3               || 1      | [125L, 17L] as LinkedList<Long>
      5               || 3      | [125L, 17L] as LinkedList<Long>
      9               || 4      | [125L, 17L] as LinkedList<Long>
      22              || 6      | [125L, 17L] as LinkedList<Long> // given in the puzzle
      31              || 7      | [125L, 17L] as LinkedList<Long>
      55312           || 25     | [125L, 17L] as LinkedList<Long> // given in the puzzle
      65601038650482  || 75     | [125L, 17L] as LinkedList<Long>
      11              || 1      | [4L, 4841539L, 66L, 5279L, 49207L, 134L, 609568L, 0L] as LinkedList<Long>
      212655          || 25     | [4L, 4841539L, 66L, 5279L, 49207L, 134L, 609568L, 0L] as LinkedList<Long> // Part 1
      253582809724830 || 75     | [4L, 4841539L, 66L, 5279L, 49207L, 134L, 609568L, 0L] as LinkedList<Long> // Part 2
  }


  @Unroll
  def "Non-recursive rundown for #nr returns #expected"() {
    expect:
      nonrecursiveRundown( nr ) == expected

    where:
      nr     || expected
      0      || [1L]
      1      || [2024]
      11     || [1, 1]
      22     || [2, 2]
      123456 || [123, 456]
  }


  // Find the result (as a List) of "nr" by applying the rules of the game.
  def nonrecursiveRundown( long nr ) {
    if( nr == 0 ) return [1]

    String s = nr.toString()
    int x = s.size()
    if( x % 2 == 0 ) {
      int xx = x >> 1
      long n1 = s[0..xx-1].toLong()
      long n2 = s[xx..x-1].toLong()
      return [n1, n2]
    }

    return [nr * 2024L]
  }


  /**
   * Starting on "nr" recursively find all the children and count them.
   * @param nr The number to start from.
   * @param knownChildren A map of known child NUMBERS for each number, eg. [ 0:[1], 1234:[12,34] ]
   * @param knownCounts A map of known final child COUNTS for each number at each level, eg. [ "0;1":1, "1234;1":2 ]
   * @param level The number of levels to go down from here. Decreases by 1 for each recursion so start with 75 if you want to blink 75 times.
   */
  @CompileStatic
  long countChildrenRecursively( long nr, Map<Long, List<Long>> knownChildren, Map<String,Long> knownCounts, int level ) {
    // We know the result for this number at this level.
    String key = "$nr;$level"
    if( knownCounts.containsKey( key ) ) {
      return knownCounts[key]
    }

    def childNumbers = knownChildren[nr]
    if( childNumbers.size() == 0 ) { throw new RuntimeException( "No children found for $nr (level = $level)" ) }

    if( level == 1 ) {
      knownCounts[key] = childNumbers.size().toLong()
      return childNumbers.size()
    }

    long childCount = 0
    childNumbers.each { long n ->
      childCount += countChildrenRecursively( n, knownChildren, knownCounts, level - 1 )
    }
    knownCounts[key] = childCount

    return childCount
  }

}
