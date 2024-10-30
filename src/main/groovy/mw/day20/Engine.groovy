package mw.day20

import groovy.transform.CompileStatic

@CompileStatic
class Engine {
  Button button
  List<Signal> activeSignals = []
  long lowSignalCount = 0
  long highSignalCount = 0
  Map<String, Boolean> watchValues = [:]
  long pushes = 0

  void addSignal( Signal signal ) {
    activeSignals << signal

    if( signal.value ) highSignalCount++
    else lowSignalCount++

    String key = signal.from.name
    if( watchValues.containsKey( key ) ) {
      if( watchValues[ key ] == signal.value ) {
        println( "Watch: ${key} is signaling ${signal.value ? "high" : "low"} to ${signal.to.name}. Pushes = $pushes" )
      }
    }
  }

  void removeSignal( Signal signal ) {
    activeSignals.remove( signal )
  }


  Engine( Button button ) {
    this.button = button
    println( "Engine created" )
  }

  List<Long> pushTheButton() {
    //println( "\nPushing the button..." )
    pushes++
    button.getOutputs(  ).each { Module m ->
      Signal s = new Signal( button, m, Boolean.FALSE )
      addSignal( s )
    }
    while( true ) {
      if( turn() == 0 ) {
        break
      }
    }
    return [ lowSignalCount, highSignalCount ]
  }


  int turn() {
    Set<Signal> toRemove = []
    Set<Signal> toAdd = []
    activeSignals.each { Signal signal ->
      def outSigs = signal.to.getOutputSignals( signal )
      outSigs.each { Signal newSignal ->
        toAdd << newSignal
      }
      toRemove << signal
    }

    toRemove.each {Signal s -> removeSignal( s ) }
    toAdd.each { Signal s -> addSignal( s ) }

    return activeSignals.size()
  }
}
