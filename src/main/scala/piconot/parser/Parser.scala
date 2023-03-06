package piconot.parser

import picolib._
import scala.util.parsing.combinator._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers {
    // parsing interface
    def apply(s: String): ParseResult[List[Rule]] = parseAll(rules, s)

    def rules: Parser[List[Rule]] = rule*

    // rules - need cases for any number of directions and if or not the state changes, and for if you turn or brake
    def rule: Parser[Rule] =
        // only one piece of surroundings information
        ( "while there is road ahead, continue" ~> dir ~ ("on route" ~> state) ^^ {
            case d ~ s => Rule(s, DriveStraight(d),d,s) } // direction same way that picodrive is moving, same state
        | "while there is road ahead, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state)) ^^ {
            case s_old ~ (d ~ s_new) => Rule(s_old, DriveStraight(d), d, s_new) } // direction the same, but state changes
        | "when the"  ~> dir  ~ ("road ends, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))) ^^ {
            case d_old ~ (s_old ~  (d ~  s_new)) => Rule(s_old, RoadEnds(d_old), d, s_new) } // changing state And direction because ahead direction is blocked
        | "when the" ~> dir ~ ("road ends, turn" ~> dir ~ ("to continue on route" ~> state)) ^^ {
            case d_old ~ (d_new ~  s) => Rule(s, RoadEnds(d_old), d_new, s) } // changing direction but not state
        | "when the" ~> dir ~ ("road ends," ~> dir ~ ("to continue on route" ~> state)) ^^ {
            case d_old ~ (brake ~ s) => Rule(s, RoadEnds(d_old), brake, s) } // not moving, nor changing state
        | "when the" ~> dir ~ ("road ends," ~> dir ~("to get off route" ~> state ~ ("and get onto route" ~> state))) ^^ {
            case d_old ~ (brake ~ (s_old ~ s_new)) => Rule(s_old, RoadEnds(d_old), brake, s_new) } // not moving, but yes changing state
        
        // two pieces of surroundings information (given one closure, one either stays open or become closed)
        | "while there is road ahead, and the" ~> dir ~ ("road is closed, continue" ~> dir ~ ("on route" ~> state)) ^^ {
            case d_closed ~ (d ~ s) => Rule(s, RoadClosed(d, d_closed), d, s) } // not changing direction, no state change, one road closure
        | "while there is road ahead, and the" ~> dir ~ ("road is closed, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state))) ^^ {
            case d_closed ~ (s_old ~ (d ~ s_new)) => Rule(s_old, RoadClosed(d, d_closed), d, s_new) } // not changing direction, state change, one road closure
        | "when the"  ~> dir  ~ ("road ends, and the" ~> dir ~ ("road is closed, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state)))) ^^ {
            case d_old ~ (d_closed ~ (s_old ~  (d ~  s_new))) => Rule(s_old, TwoClosedOneOpen(d, d_old, d_closed), d, s_new) } // changing state And direction, two road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed, turn" ~> dir ~ ("to continue on route" ~> state))) ^^ {
            case d_old ~ (d_closed ~ (d_new ~  s)) => Rule(s, TwoClosedOneOpen(d_new, d_old, d_closed), d_new, s) } // changing direction but not state, two road closure
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed," ~> dir ~ ("to continue on route" ~> state))) ^^ {
            case d_old ~ (d_closed ~ (brake ~ s)) => Rule(s, TwoRoadsClosed(d_old, d_closed), brake, s) } // not moving, nor changing state, two road closures
        |  "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("road is closed," ~> dir ~ ("to get off route" ~> state ~ ("and get onto route" ~> state)))) ^^ {
            case d_old ~ (d_closed ~ (brake ~ (s_old ~ s_new))) => Rule(s_old, TwoRoadsClosed(d_old, d_closed), brake, s_new) } // not moving, but yes changing state
        
        // three pieces of surroundings information (given two closures, one either stays open or become closed)
        | "while there is road ahead, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed, continue" ~> dir ~ ("on route" ~> state))) ^^ {
            case d_closed ~ (d_closed_2 ~ (d ~ s)) => Rule(s, TwoClosedOneOpen(d, d_closed, d_closed_2), d, s) } // not changing direction, no state change, two road closures
        | "while there is road ahead, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed, get off route" ~> state ~ ("to continue" ~> dir ~ ("onto route" ~> state)))) ^^ {
            case d_closed ~ (d_closed_2 ~ (s_old ~ (d ~ s_new))) => Rule(s_old, TwoClosedOneOpen(d, d_closed, d_closed_2), d, s_new) } // not changing direction, state change, two road closures
        | "when the"  ~> dir  ~> ("road ends, and the" ~> dir ~> ("and" ~> dir ~> ("roads are closed, get off route" ~> state ~ ("and turn" ~> dir ~ ("onto route" ~> state))))) ^^ {
            case s_old ~  (d ~  s_new) => Rule(s_old, ThreeRoadsClosed(d), d, s_new) } // changing state And direction, three road closures
        | "when the" ~> dir ~> ("road ends, and the" ~> dir ~> ("and" ~> dir ~> ("roads are closed, turn" ~> dir ~ ("to continue on route" ~> state)))) ^^ {
            case d ~  s => Rule(s, ThreeRoadsClosed(d), d, s) } // changing direction but not state, three road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed," ~> dir ~ ("to continue on route" ~> state)))) ^^ {
            case d_old ~ (d_closed ~ (d_closed_2 ~ (brake ~ s))) => Rule(s, StayThreeRoads(d_old, d_closed, d_closed_2), brake, s) } // not moving, nor changing state, three road closures
        | "when the" ~> dir ~ ("road ends, and the" ~> dir ~ ("and" ~> dir ~ ("roads are closed," ~> dir ~ ("to get off route" ~> state ~ ("and get onto route" ~> state))))) ^^ {
            case d_old ~ (d_closed ~ (d_closed_2 ~ (brake ~ (s_old ~ s_new)))) => Rule(s_old, StayThreeRoads(d_old, d_closed, d_closed_2), brake, s_new) } )// not moving, but yes changing state

        
    
    def dir: Parser[MoveDirection] =
        "North" ^^ {case "North" => North}
        | "East" ^^ {case "East" => East}
        | "West" ^^ {case "West" => West}
        | "South" ^^ {case "South" => South}
        | "brake" ^^ {case "brake" => StayHere}

    def state: Parser[State] =
        wholeNumber ^^ {case n => State(n)}
    
    
}

// helper functions to map surroundings from direction - these might end up having to be parsers

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

def RoadClosed(openDir: MoveDirection, closedDir: MoveDirection): Surroundings = (openDir, closedDir) match
    case (North, East) => Surroundings(Open, Blocked, Anything, Anything)
    case (North, West) => Surroundings(Open, Anything, Blocked, Anything)
    case (North, South) => Surroundings(Open, Anything, Anything, Blocked)
    case (East, North) => Surroundings(Blocked, Open, Anything, Anything)
    case (East, West) => Surroundings(Anything, Open, Blocked, Anything)
    case (East, South) => Surroundings(Anything, Open, Anything, Blocked)
    case (West, North) => Surroundings(Blocked, Anything, Open, Anything)
    case (West, East) => Surroundings(Anything, Blocked, Open, Anything)
    case (West, South) => Surroundings(Anything, Anything, Open, Blocked)
    case (South, North) => Surroundings(Blocked, Anything, Anything, Open)
    case (South, East) => Surroundings(Anything, Blocked, Anything, Open)
    case (South, West) => Surroundings(Anything, Anything, Blocked, Open)

def TwoRoadsClosed(dir1: MoveDirection, dir2: MoveDirection): Surroundings = (dir1, dir2) match
    case (North, East) | (East, North) => Surroundings(Blocked, Blocked, Anything, Anything)
    case (North, West) | (West, North) => Surroundings(Blocked, Anything, Blocked, Anything)
    case (North, South) | (South, North) => Surroundings(Blocked, Anything, Anything, Blocked)
    case (East, West) | (West, East) => Surroundings(Anything, Blocked, Blocked, Anything)
    case (East, South) | (South, East) => Surroundings(Anything, Blocked, Anything, Blocked)
    case (West, South) | (South, West) => Surroundings(Anything, Anything, Blocked, Blocked)


def TwoClosedOneOpen(openDir: MoveDirection, dir1: MoveDirection, dir2: MoveDirection): Surroundings = (openDir, dir1, dir2) match
    case (North, West, East) | (North, East, West)=> Surroundings(Open, Blocked, Blocked, Anything)
    case (North, East, South) | (North, South, East) => Surroundings(Open, Blocked, Anything, Blocked)
    case (North, West, South) | (North, South, West) => Surroundings(Open, Anything, Blocked, Blocked)
    case (East, North, West) | (East, West, North) => Surroundings(Blocked, Open, Blocked, Anything)
    case (East, North, South) | (East, South, North) => Surroundings(Blocked, Open, Anything, Blocked)
    case (East, West, South) | (East, South, West) => Surroundings(Anything, Open, Blocked, Blocked)
    case (West, North, East) | (West, East, North) => Surroundings(Blocked, Blocked, Open, Anything)
    case (West, North, South) | (West, South, North) => Surroundings(Blocked, Anything, Open, Blocked)
    case (West, East, South) | (West, South, East) => Surroundings(Anything, Blocked, Open, Blocked)
    case (South, North, East) | (South, East, North) => Surroundings(Blocked, Blocked, Anything, Open)
    case (South, North, West) | (South, West, North) => Surroundings(Blocked, Anything, Blocked, Open)
    case (South, East, West) | (South, West, East) => Surroundings(Anything, Blocked, Blocked, Open)


def ThreeRoadsClosed(openDir: MoveDirection): Surroundings = openDir match
    case North => Surroundings(Open, Blocked, Blocked, Blocked)
    case East => Surroundings(Blocked, Open, Blocked, Blocked)
    case West => Surroundings(Blocked, Blocked, Open, Blocked)
    case South => Surroundings(Blocked, Blocked, Blocked, Open)

def StayThreeRoads(dir1: MoveDirection, dir2: MoveDirection, dir3: MoveDirection) = (dir1, dir2, dir3) match
    case (East, West, South) | (East, South, West) | (West, South, East) | (West, East, South) | (South, East, West) | (South, West, East) 
        => Surroundings(Open, Blocked, Blocked, Blocked)
    case (North, West, South) | (North, South, West) | (West, North, South) | (West, South, North) | (South, North, West) | (South, West, North) 
        => Surroundings(Blocked, Open, Blocked, Blocked)
    case (North, East, South) | (North, South, East) | (East, North, South) | (East, South, North) | (South, East, North) | (South, North, East)
        => Surroundings(Blocked, Blocked, Open, Blocked)
    case (North, East, West) | (North, West, East) | (East, North, West) | (East, West, North) | (West, North, East) | (West, East, North)
        => Surroundings(Blocked, Blocked, Blocked, Open)



