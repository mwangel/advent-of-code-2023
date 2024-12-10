package mw.y2024.day09

import spock.lang.Specification

class d09 extends Specification {

  def "Day 09"() {
    given:
      def data = this.getClass().getResource( '/y2024/data09.txt' ).text
      def testdata = this.getClass().getResource( '/y2024/data09-test.txt' ).text

    when:
      def testResult = solve09stupidEdition( testdata )
    then:
      summit(testResult) == 1928

    when:
      def result = solve09stupidEdition( data )
    then:
      summit(result) == 6463499258318 // 95082 is too low
  }

  BigInteger summit( List list ) {
    BigInteger sum = 0
    list.eachWithIndex {int entry, int index ->
      sum += entry * index
    }
    return sum
  }

  def solve09stupidEdition( String data ) {
    // rearrange the data as per https://adventofcode.com/2024/day/9
    def sum = data.collect { it.toLong() }.sum()
    println( sum )
    def list = new LinkedList<Integer>()
    int id = 0
    data.eachWithIndex { String s, int index ->
      int n = s.toInteger()
      if( index % 2 == 0 ) {
        // for even indexes add the index number to the list
        n.times {list.add( id ) }
        id++
      }
      else {
        // for odd indexes add spaces to the list
        n.times { list.add( -1 ) }
      }
    }

    int insertionIndex = 0

    // now we have a list of numbers and spaces
    while( true ) {
      // Skip trailing spaces.
      while( list.last == -1 ) {
        list.removeLast()
      }

      // Find the first space from the left.
      insertionIndex = list.findIndexOf( { it == -1}  )
      if( insertionIndex == -1 ) { break }

      // Move the last number in the list to the insertion index.
      list[insertionIndex] = list.last
      list.removeLast()
    }

    return list
  }

}
