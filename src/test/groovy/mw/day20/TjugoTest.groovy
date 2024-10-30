package mw.day20

import spock.lang.Specification

class TjugoTest extends Specification {


  def "Solve"() {
    given:
      def test1 = """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
"""
      def button = Tjugo.load( test1.readLines(  ) )

    when:
      button.calculate()

    then:
      broadcaster.getValue() == true
  }
}
