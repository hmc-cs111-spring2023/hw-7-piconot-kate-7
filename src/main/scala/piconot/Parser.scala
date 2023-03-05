package piconot.parser

import picolib._
import scala.util.parsing.combinator._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers {
    // parsing interface
    def apply(s: String): ParseResult[Rule] = parseAll(rule, s)

    // rules
    def rule: Parser[Rule] =
        "while there is road ahead, stay " ~> dir ~ ("on route " ~> state) ^^ {
            case "while there is road ahead, drive " ~> d ~ ("on route " ~> s) => Rule(state, DriveStraight(d),d,state) }
        | "when the "  ~> dir  ~ ("road ends, turn off" ~> state ~ ("and turn " ~> dir ~ ("onto route " ~> state))) ^^ {
            case "when the " ~> d_old ("road ends, turn off" ~> s_old ~ ("and turn " ~> d ~ ("onto route " ~> s_new))) => Rule(s_old, RoadEnds(d_old), d, s_new) }
    
    def dir: Parser[MoveDirection] =
        // parser for returning a MoveDirection

    def state: Parser[State] =
         // parser for returning a State
    
    
}

def DriveStraight(dir: MoveDirection): Surroundings = dir match
    case North => Surroundings(Open, Anything, Anything, Anything)
    case East => Surroundings(Anything, Open, Anything, Anything)
    case West => Surroundings(Anything, Anything, Open, Anything)
    case South => Surroundings(Anything, Anything, Anything, Open)

def RoadEnds(dir: MoveDirection): Surroundings = dir match
    case North => Surroundings(Blocked, Anything, Anything, Anything)
    case East => Surroundings(Anything, Blocked, Anything, Anything)
    case West => Surroundings(Anything, Anything, Blocked, Anything)
    case South => Surroundings(Anything, Anything, Anything, Blocked)
