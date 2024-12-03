package mw.y2024.day03

class Mul {
  long a, b

  Mul( Long a, Long b ) { this.a = a; this.b = b }

  long execute() { a * b }

  static Mul parse( String s ) {
    def parts = s[4..-2].split(",").collect{ it.toLong() }
    return new Mul( parts[0], parts[1] )
  }
}
