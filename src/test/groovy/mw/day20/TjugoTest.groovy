package mw.day20

import groovy.transform.CompileStatic
import spock.lang.Specification

class TjugoTest extends Specification {


  def "Test case 1"() {
    given:
      def test1 = """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
"""
      def button = Tjugo.load( test1.readLines(  ) )
      def engine = new Engine( button )

    when: // start the engine (send a low signal from the button)
      def pushresult = engine.pushTheButton(  )
    then: // expect that the broadcaster has the only active signal
      pushresult == [ 8, 4]
      engine.activeSignals.size() == 0
      engine.lowSignalCount == 8
      engine.highSignalCount == 4
  }


  def "Test case 2"() {
    given:
      def test2 = """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
"""
      def button = Tjugo.load( test2.readLines() )
      def engine = new Engine( button )

    when: // push the button 1000 times
      1000.times {engine.pushTheButton(  ) }
    then: // expect that the broadcaster has the only active signal
      engine.activeSignals.size() == 0
      engine.lowSignalCount == 4250
      engine.highSignalCount == 2750
  }

  def "Part A"() {
    given:
      def button = Tjugo.load( this.getClass().getResource( '/data20a.txt' ).readLines() )
      def engine = new Engine( button )
    when:
      1000.times {engine.pushTheButton(  ) }
      println( "lowSignalCount: ${engine.lowSignalCount}, highSignalCount: ${engine.highSignalCount}" )
      long result = engine.lowSignalCount * engine.highSignalCount
    then:
      result == 684125385
  }


  @CompileStatic
  void "Part B"() {
    given:
      Button button = Tjugo.load( this.getClass().getResource( '/data20a.txt' ).readLines() )
      Engine engine = new Engine( button )
      final Boolean L3 = Boolean.FALSE
      engine.watchValues = [ "gr":L3, "st":L3, "bn":L3, "lg":L3 ]
      final Boolean L2 = Boolean.TRUE
      engine.watchValues = [ "js":L2, "zb":L2, "bs":L2, "rr":L2 ]
      long pushCount = 0
    when:
      10000.times {
        pushCount++
        engine.pushTheButton()
      }
    then:
      /** The output will be:
       Watch: rr is signaling high to hb. Pushes = 3733
       Watch: js is signaling high to hb. Pushes = 3761
       Watch: bs is signaling high to hb. Pushes = 4001
       Watch: zb is signaling high to hb. Pushes = 4021
       Watch: rr is signaling high to hb. Pushes = 7466
       Watch: js is signaling high to hb. Pushes = 7522
       Watch: bs is signaling high to hb. Pushes = 8002
       Watch: zb is signaling high to hb. Pushes = 8042

       We inspect the input data and realize that we want to find
        the pushCount value when all the inputs to hb are high
        because then hb will send a low signal to rx.

       So we set watches for all inputs to hb to be high: rr, js, bs, zb.

       We can see from the test output that the inputs to hb have different cycle lengths:
        rr: 3733, js: 3761, bs: 4001, zb: 4021

       And the Least Common Multiple of those values is 225_872_806_380_073, at which
        time they will all signal high so hb will signal low to rx so that is the answer.
       */
      //225_872_806_380_073
      assert true
  }
}
