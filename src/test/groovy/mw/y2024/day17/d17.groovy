package mw.y2024.day17

import groovy.transform.CompileStatic
import spock.lang.Specification

class d17 extends Specification {

  def "Day 17 - computer"() {
    given:
//      def (a,b,c, program, p1) = [729, 0, 0, decode([0,1,5,4,3,0]), "4,6,3,5,6,3,5,2,1,0"]
      def (a,b,c, program, p1) = [35200350, 0, 0, decode([2,4,1,2,7,5,4,7,1,3,5,5,0,3,3,0]), "2,7,4,7,2,1,7,5,1"]
    when:
      def result = run( a, b, c, program )
    then:
      result.join(",") == p1
  }


  def run( long a, long b, long c, List program ) {
    List<String> output = []
    int pc = 0 // program counter - index to the next instruction to execute
    while( pc < program.size() ) {
      Instruction instruction = program[pc]
      (a,b,c,pc) = execute( instruction, a, b, c, pc, output )
    }

    println( "-------------")
    println( output )
    return output
  }

  @CompileStatic
  def execute( Instruction instruction, long a, long b, long c, long pc, List<String> output ) {
    long comboValue = instruction.data
    switch( instruction.data ) {
      case 4: comboValue = a; break
      case 5: comboValue = b; break
      case 6: comboValue = c; break
    }
    long literalValue = instruction.data

    switch( instruction.opcode ) {
      case 0: // adv (a = a / 2^comboValue)
        a = a >> comboValue
        pc++
        break
      case 1: // bxl (b = b xor data)
        b = b ^ literalValue
        pc++
        break
      case 2: // b = lowest 3 bits of comboValue (also know as "modulo 8")
        b = comboValue & 0b111
        pc++
        break
      case 3: // jnz (if a != 0 then pc = literalValue, else pc++)
        if( a != 0 ) pc = literalValue
        else pc++
        break
      case 4: // bxc (b = b xor c)
        b = b ^ c
        pc++
        break
      case 5: // out (output comboValue mod 8)
        output << (comboValue % 8).toString()
        pc++
        break
      case 6: // bdv (b = a / 2^comboValue)
        b = a >> comboValue
        pc++
        break
      case 7: // cdv (c = a / 2^comboValue)
        c = a >> comboValue
        pc++
        break
      default:
        throw new RuntimeException( "Unknown Instruction: $Instruction" )
    }

    return [a, b, c, pc]
  }

  class Instruction {
    long opcode
    long data

    Instruction( long opcode, long data ) {
      this.opcode = opcode
      this.data = data
    }

    String toString() {
      return "$opcode $data"
    }
  }


  def decode( List<Long> input ) {
    List<Instruction> program = []

    for( int i = 0; i < input.size(); i+=2 ) {
      long opcode = input[i]
      long operand = input[i+1]
      program << new Instruction( opcode, operand )
    }

    return program
  }
}
