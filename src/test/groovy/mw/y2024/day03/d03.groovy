package mw.y2024.day03


import spock.lang.Specification

class d03 extends Specification {

  def "Day 03"() {
    given: def input = this.getClass().getResource( '/y2024/data03.txt' ).readLines().join()

    when: def c = load(input)
    then: c.collect { Mul mul -> mul.execute().toLong() }.sum() == 187825547

    when: def d = load2(input)
    then: d.collect { Mul mul -> mul.execute().toLong() }.sum() == 85508223
  }

  def "2024-04 in Groovy as a Spock test case"() {
    given:
      def text = this.getClass().getResource( '/y2024/data03.txt' ).readLines().join()
      def ops = text.findAll(/mul\((\d+),(\d+)\)/)
      def res = ops.sum {
        def parts = it[4..-2].split(",").collect { it.toLong() }
        parts[0] * parts[1]
      }
    expect: res == 187825547
  }

  def load( String text ) {
    def s = text.findAll(/mul\((\d+),(\d+)\)/)

    def muls = s.collect { String line ->
      Mul.parse( line )
    }

    return muls
  }


  def load2( String text ) {
    def hits = text.findAll(/mul\((\d+),(\d+)\)|do\(\)|don't\(\)/)

    boolean doIt = true
    def muls = []

    // Step through the hits and parse the Mul objects,
    // using the doIt flag to decide if we should parse or not.
    hits.each { String hit ->
      if( doIt && hit.startsWith( "mul" ) ) { muls << Mul.parse( hit ) }
      else if( hit == "do()" ) { doIt = true }
      else if ( hit == "don't()" ) { doIt = false }
    }

    return muls
  }

  Op parseOp( String s ) {
    if( s.startsWith( "mul" ) ) { return Mul.parse( s ) }
    else if( s == "do()" ) { return new Do() }
    else if( s == "don't()" ) { return new Dont() }
    else { throw new IllegalArgumentException( "Unknown operation: $s" ) }
  }
}
