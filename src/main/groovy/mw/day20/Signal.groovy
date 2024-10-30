package mw.day20

import groovy.transform.CompileStatic

@CompileStatic
class Signal {
  Module from
  Module to
  Boolean value

  Signal( Module from, Module to, Boolean value ) {
    this.from = from
    this.to = to
    this.value = value
  }

  String toString() {
    return "${from.name} -${value ? "high" : "low"}-> ${to.name}"
  }
}
