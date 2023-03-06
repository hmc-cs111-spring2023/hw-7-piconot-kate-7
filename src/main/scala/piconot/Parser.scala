package piconot.parser

import picolib._
import scala.util.parsing.combinator._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers {
    // parsing interface
    def apply(s: String): ParseResult[Rule] = parseAll(rule, s)

    // rules - need cases for any number of directions and if or not the state changes, and for if you turn or brake
    def rule: Parser[Rule] =
        "while there is road ahead, stay" ~> dir ~ ("on route" ~> state) ^^ {
            case d ~ s => Rule(state, DriveStraight(d),d,state) } // direction same way that picodrive is moving, same state
        | "when the"  ~> dir  ~ ("road ends, turn off" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))) ^^ {
            case d_old ~ s_old ~  d ~  s_new => Rule(s_old, RoadEnds(d_old), d, s_new) } // changing state And direction because ahead direction is blocked
        | "when the" ~> dir ~ ("road ends, turn" ~> dir ~ ("to stay on route" ~> state)) ^^ {
            case d_old ~ d_new ~  s => Rule(s, RoadEnds(d_old), d_new, s) } // changing direction but not state
        | "when the" ~> dir ~ ("road ends," ~> dir ~ ("to stay on route" ~> state)) ^^ {
            case d_old ~ brake ~ s => Rule(s, RoadEnds(d_old), brake, s) } // not moving, nor changing state
        | "when the" ~> dir ~ ("road ends," ~> dir ~("to get off route" ~> state ~ ("and get on route" ~> state))) ^^ {
            case d_old ~ brake ~ s => Rule()
        }
    
    def dir: Parser[MoveDirection] =
        "North" ^^ {case "North" => North}
        | "East" ^^ {case "East" => East}
        | "West" ^^ {case "West" => West}
        | "South" ^^ {case "South" => South}
        | "brake" ^^ {case "brake" => StayHere}

    def state: Parser[State] =
        wholeNumber ^^ {case n => State(n)}
    
    
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
