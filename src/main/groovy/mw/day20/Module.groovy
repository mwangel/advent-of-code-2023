package mw.day20

interface Module {
  List<Module> inputs
  List<Module> outputs
  boolean getValue()
  void setValue( boolean value )
  def calculate()
}

abstract class ValueModule implements Module {
  List<Module> inputs = []
  List<Module> outputs = []
  boolean value = false
  String name

  void setAsInputTo( Module receiver ) {
    receiver.inputs.add( this )
    outputs.add( receiver )
  }

  boolean getValue() { return value }
  void setValue( boolean value ) { this.value = value }

  String toString() { "$name:${this.class.simpleName}" }
}

/** Constant value = false */
class Button extends ValueModule {
  Button( String name ) { this.name = name }
  def calculate() {  }
}

/** Value is the same as input */
class Broadcaster extends ValueModule {
  Broadcaster( String name ) { this.name = name }
  def calculate() { value = inputs[0].getValue() }
}

/** Invert value if input is false */
class FlipFlop extends ValueModule {
  FlipFlop( String name ) { this.name = name }

  def calculate() {
    if( inputs.size() > 1 )
      throw new IllegalStateException( "FlipFlop can only have one input" )
    if( !inputs[0].getValue() )
      value = !value
  }
}

/** Value is true if all inputs are true, else false. */
class AndGate extends ValueModule {
  AndGate( String name ) { this.name = name }

  def calculate() {
    value = inputs.all { Module m -> m.getValue() }
  }
}