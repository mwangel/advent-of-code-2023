package mw.day20

interface Module {
  boolean getValue()
  void setValue( boolean value )
  String getName()
  void setName( String name )
  void addInputConnection( Module sender )
  void addOutputConnection( Module receiver )
  def receiveSignal( Module sender )

  /** Returns true or false for a pulse, or NULL to give no output */
  Boolean calculate()
}


abstract class ValueModule implements Module {
  List<Module> inputs = []
  List<Module> outputs = []
  boolean value = false
  String name

  void addInputConnection( Module sender ) { inputs << sender }
  void addOutputConnection( Module receiver ) { outputs << receiver }

  boolean getValue() { return value }
  void setValue( boolean value ) { this.value = value }

  String toString() { "$name:${this.class.simpleName}" }

  void log( s ) {
    println s
  }
}


/** Constant value = false */
class Button extends ValueModule {
  Button( String name ) { this.name = name }

  @Override
  def receiveSignal(Module sender) { return null }

  Boolean calculate() { return Boolean.FALSE }
}


/** Value is the same as input */
class Broadcaster extends ValueModule {
  boolean currentInput = false
  Broadcaster( String name ) { this.name = name }

  @Override
  def receiveSignal( Module sender ) { currentInput = sender.value }

  Boolean calculate() {
    value = inputs[0].getValue()
    return value
  }
}


/** Invert value if input is false */
class FlipFlop extends ValueModule {
  Boolean currentInput = false
  FlipFlop( String name ) { this.name = name }

  @Override
  def receiveSignal(Module sender) {
    currentInput = sender.value
  }

  Boolean calculate() {
    if( inputs.size() > 1 )
      throw new IllegalStateException( "FlipFlop can only have one input" )
    if( !currentInput ) {
      value = !value
      return value
    }
    else { // ignore high pulse - nothing happens
      log("Flipflop $name does nothing")
      return null
    }
  }
}

/** Value is true if all inputs are true, else false. */
class AndGate extends ValueModule {
  Map<String, Boolean> lastInputs = [:]
  AndGate( String name ) { this.name = name }

  @Override
  def receiveSignal(Module sender) {
    lastInputs[ sender.name ] = sender.value
  }

  def calculate() {
    value = !lastInputs.values().all { it == true }
  }
}