package piconot.parser

import picolib._
import scala.util.parsing.combinator._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers {
    // parsing interface
    def apply(s: String): ParseResult[Rule] = parseAll(rule, s)

    // rules
    def rule: Parser[Rule] =
        "while there is road ahead, drive " ~> dir ~ ("on route " ~> state) ^^ {
            case "while there is road ahead, drive " ~> d ~ ("on route " ~> s) => Rule(Surroundings())
        }
    
    def dir: Parser[MoveDirection] =
        // parser for returning a MoveDirection

    def state: Parser[State] =
         // parser for returning a State
    
    
}