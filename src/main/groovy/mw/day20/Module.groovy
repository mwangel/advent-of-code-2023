package mw.day20

import groovy.transform.CompileStatic

@CompileStatic
interface Module {
  boolean getValue()
  void setValue( boolean value )
  String getName()
  void setName( String name )
  void addInputConnection( Module sender )
  void addOutputConnection( Module receiver )
  //def receiveSignal( Signal signal )

  /** Returns true or false for a pulse, or NULL to give no output */
  List<Signal> getOutputSignals( Signal pulse )
}


@CompileStatic
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

  void log( s ) { println s }

  void debug( s ) {
    //println s
  }

  void logSendSignal( Module receiver ) {
    //log( "$name -${value ? "high" : "low"}-> ${receiver.name}" )
  }
}


/** Constant value = false */
@CompileStatic
class Button extends ValueModule {
  Button( String name ) { this.name = "Button" }

  @Override
  List<Signal> getOutputSignals( Signal pulse ) {
    return outputs.collect{ Module receiver ->
      logSendSignal( receiver )
      new Signal( this, receiver, value )
    } as List<Signal>
  }
}


@CompileStatic
class UntypedModule extends ValueModule {
  boolean currentInput = false

  UntypedModule( String name ) { this.name = name }

  @Override
  List<Signal> getOutputSignals( Signal pulse ) {
    if( !pulse.value && name == "rx" )
      throw new RuntimeException( "rx received a low signal" )
    currentInput = pulse.value
    return []
  }
}

/** Value is the same as input */
@CompileStatic
class Broadcaster extends ValueModule {
  boolean currentInput = false
  Broadcaster( String name ) { this.name = name }

  @Override
  List<Signal> getOutputSignals( Signal pulse ) {
    List<Signal> signalsSent = []
    value = pulse.value
    outputs.each { Module receiver ->
      logSendSignal( receiver )
      signalsSent << new Signal( this, receiver, value )
    }
    return signalsSent
  }
}


/** Invert value if input is false */
@CompileStatic
class FlipFlop extends ValueModule {
  Boolean currentInput = false
  FlipFlop( String name ) { this.name = name }

  List<Signal> getOutputSignals( Signal pulse ) {
    currentInput = pulse.value
    if( currentInput == false ) { // low pulse - invert value
      value = !value
      List<Signal> signalsSent = []
      outputs.each { Module receiver ->
        logSendSignal( receiver )
        signalsSent << new Signal( this, receiver, value )
      }
      return signalsSent
    }
    else { // ignore high pulse - nothing happens
      debug("Flipflop $name does nothing")
      return []
    }
  }
}

/** Output signal is True if any input is False.
 *  Output signal is False if all inputs are True */
@CompileStatic
class AndGate extends ValueModule {
  Map<String, Boolean> lastInputs = [:]
  AndGate( String name ) { this.name = name }

  void addInputConnection( Module sender ) {
    inputs << sender
    lastInputs[sender.name] = false
  }


  List<Signal> getOutputSignals( Signal pulse ) {
    lastInputs[pulse.from.name] = pulse.value
    value = lastInputs.values().any { !it }
    List<Signal>  signalsSent = []
    outputs.each { Module receiver ->
      logSendSignal( receiver )
      signalsSent << new Signal( this, receiver, value )
    }
    return signalsSent
  }
}