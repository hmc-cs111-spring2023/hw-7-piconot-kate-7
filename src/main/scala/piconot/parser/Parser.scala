package piconot.parser

import picolib._
import scala.util.parsing.combinator._
import picolib.semantics._
import picodrive.semantics._

object PiconotParser extends JavaTokenParsers {
    // parsing interface
    def apply(s: String): ParseResult[List[Rule]] = parseAll(rules, s)

    def rules: Parser[List[Rule]] = rule*

    // rules - need cases for any number of directions and if or not the state changes, and for if you turn or stop
    def rule: Parser[Rule] =
        // only one piece of surroundings information
        // direction same way that picodrive is moving, same state
        ( "while there is road ahead, continue" ~> dir ~ ("on route" ~> state) ^^ {
            case d ~ s => Rule(s, DriveStraight(d),d,s) } 
        // direction the same, but state changes
        | "while there is road ahead, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state)) ^^ {
            case s_old ~ (d ~ s_new) => Rule(s_old, DriveStraight(d), d, s_new) } 
        // changing state And direction because ahead direction is blocked
        | "when the"  ~> dir  ~ ("road ends, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))) ^^ {
            case d_old ~ (s_old ~  (d_new ~  s_new)) => Rule(s_old, RoadClosed(d_new, d_old), d_new, s_new) } 
        // changing direction (one road is closed) but not state
        | "when the" ~> dir ~ ("road ends, turn" ~> dir ~ ("to continue on route" ~> state)) ^^ {
            case d_old ~ (d_new ~  s) => Rule(s, RoadClosed(d_new, d_old), d_new, s) } 
        // not moving, nor changing state
        | "when the" ~> dir ~ ("road ends," ~> dir ~ ("to continue on route" ~> state)) ^^ {
            case d_old ~ (stop ~ s) => Rule(s, RoadEnds(d_old), stop, s) } 
        // not moving, but yes changing state
        | "when the" ~> dir ~ ("road ends," ~> dir ~("to get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state)))) ^^ {
            case d_old ~ (stop ~ (s_old ~ (d_new ~ s_new))) => Rule(s_old, RoadClosed(d_new, d_old), stop, s_new) }
        
        // two pieces of surroundings information (given one closure, one either stays open or become closed)
        // not changing direction, no state change, one road closure
        | "while there is road ahead, and the" ~> dir ~ ("road is closed, continue" ~> dir ~ ("on route" ~> state)) ^^ {
            case d_closed ~ (d ~ s) => Rule(s, RoadClosed(d, d_closed), d, s) } 
        // not changing direction, state change, one road closure
        | "while there is road ahead, and the" ~> dir ~ ("road is closed, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state))) ^^ {
            case d_closed ~ (s_old ~ (d ~ s_new)) => Rule(s_old, RoadClosed(d, d_closed), d, s_new) }
        // changing state And direction, two road closures
        | "when the"  ~> dir  ~ ("road ends, and the" ~> dir ~ ("road is closed, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state)))) ^^ {
            case d_old ~ (d_closed ~ (s_old ~  (d ~  s_new))) => Rule(s_old, TwoClosedOneOpen(d, d_old, d_closed), d, s_new) } 
        // changing direction but not state, two road closure
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed, turn" ~> dir ~ ("to continue on route" ~> state))) ^^ {
            case d_old ~ (d_closed ~ (d_new ~  s)) => Rule(s, TwoClosedOneOpen(d_new, d_old, d_closed), d_new, s) } 
        // not moving, nor changing state, two road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed," ~> dir ~ ("to continue on route" ~> state))) ^^ {
            case d_old ~ (d_closed ~ (stop ~ s)) => Rule(s, TwoRoadsClosed(d_old, d_closed), stop, s) } 
        // not moving, but yes changing state
        |  "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed," ~> dir ~ ("to get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))))) ^^ {
            case d_old ~ (d_closed ~ (stop ~ (s_old ~ (d_new ~ s_new)))) => Rule(s_old, TwoClosedOneOpen(d_new, d_old, d_closed), stop, s_new) } 
        
        // three pieces of surroundings information (given two closures, one either stays open or become closed)
        // not changing direction, no state change, two road closures
        | "while there is road ahead, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed, continue" ~> dir ~ ("on route" ~> state))) ^^ {
            case d_closed ~ (d_closed_2 ~ (d ~ s)) => Rule(s, TwoClosedOneOpen(d, d_closed, d_closed_2), d, s) } 
        // not changing direction, state change, two road closures
        | "while there is road ahead, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state)))) ^^ {
            case d_closed ~ (d_closed_2 ~ (s_old ~ (d ~ s_new))) => Rule(s_old, TwoClosedOneOpen(d, d_closed, d_closed_2), d, s_new) } 
        // changing state And direction, three road closures
        | "when the"  ~> dir  ~> ("road ends, and the" ~> dir ~> ("and" ~> dir ~> ("roads are closed, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))))) ^^ {
            case s_old ~  (d ~  s_new) => Rule(s_old, ThreeRoadsClosed(d), d, s_new) } 
        // changing direction but not state, three road closures
        | "when the" ~> dir ~> ("road ends, and the" ~> dir ~> ("and" ~> dir ~> ("roads are closed, turn" ~> dir ~ ("to continue on route" ~> state)))) ^^ {
            case d ~  s => Rule(s, ThreeRoadsClosed(d), d, s) }
        // not moving, nor changing state, three road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed," ~> dir ~ ("to continue on route" ~> state)))) ^^ {
            case d_old ~ (d_closed ~ (d_closed_2 ~ (stop ~ s))) => Rule(s, StayThreeRoads(d_old, d_closed, d_closed_2), stop, s) } 
        // not moving, but yes changing state, three road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed," ~> dir ~ ("to get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state)))))) ^^ {
            case d_old ~ (d_closed ~ (d_closed_2 ~ (stop ~ (s_old ~ (d_new ~ s_new))))) => Rule(s_old, StayThreeRoads(d_old, d_closed, d_closed_2), stop, s_new) } )

        
    
    def dir: Parser[MoveDirection] =
        "North" ^^ {case "North" => North}
        | "East" ^^ {case "East" => East}
        | "West" ^^ {case "West" => West}
        | "South" ^^ {case "South" => South}
        | "stop" ^^ {case "stop" => StayHere}

    def state: Parser[State] =
        wholeNumber ^^ {case n => State(n)}
    
    
}

