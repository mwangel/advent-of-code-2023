package mw.y2024.day03


import spock.lang.Specification

class d03 extends Specification {

  def "Day 03"() {
    given: input = this.getClass().getResource( '/y2024/data03.txt' ).readLines()

    when: def c = load(input)
    then: c.collect { Mul mul -> mul.execute().toLong() }.sum() == 187825547

    when: def d = load2(input)
    then: d.collect { Mul mul -> mul.execute().toLong() }.sum() == 85508223
  }


  def load( List<String> lines ) {
    def s = lines.join().findAll(/mul\((\d+),(\d+)\)/)

    def muls = s.collect { String line ->
      Mul.parse( line )
    }

    return muls
  }


  def load2( List<String> lines ) {
    def hits = lines.join().findAll(/mul\((\d+),(\d+)\)|do\(\)|don't\(\)/)

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
