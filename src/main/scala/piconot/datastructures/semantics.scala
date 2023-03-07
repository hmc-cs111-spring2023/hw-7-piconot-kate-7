package picodrive.semantics

import picolib._
import picolib.semantics._
// this file defines intermediary data structures between my language (picodrive) and picobot
// these functions essentially map directions into the correct surroundings for the picobot AST

// given one direction to be open, assumes the other directions are anything
def DriveStraight(dir: MoveDirection): Surroundings = dir match
    case North => Surroundings(Open, Anything, Anything, Anything)
    case East => Surroundings(Anything, Open, Anything, Anything)
    case West => Surroundings(Anything, Anything, Open, Anything)
    case South => Surroundings(Anything, Anything, Anything, Open)

// given the direction ahead is closed, and assuming that the other directions are anything
def RoadEnds(dir: MoveDirection): Surroundings = dir match
    case North => Surroundings(Blocked, Anything, Anything, Anything)
    case East => Surroundings(Anything, Blocked, Anything, Anything)
    case West => Surroundings(Anything, Anything, Blocked, Anything)
    case South => Surroundings(Anything, Anything, Anything, Blocked)

// given both an open direction and a closed direction, assumes the others are anything
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

// given two roads that are closed, assumes the other two are anything
def TwoRoadsClosed(dir1: MoveDirection, dir2: MoveDirection): Surroundings = (dir1, dir2) match
    case (North, East) | (East, North) => Surroundings(Blocked, Blocked, Anything, Anything)
    case (North, West) | (West, North) => Surroundings(Blocked, Anything, Blocked, Anything)
    case (North, South) | (South, North) => Surroundings(Blocked, Anything, Anything, Blocked)
    case (East, West) | (West, East) => Surroundings(Anything, Blocked, Blocked, Anything)
    case (East, South) | (South, East) => Surroundings(Anything, Blocked, Anything, Blocked)
    case (West, South) | (South, West) => Surroundings(Anything, Anything, Blocked, Blocked)

// given an open direction, as well as two closed roads
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



