import picolib._
import picolib.maze._
import picolib.semantics._
import piconot.parser._
import java.io.File

// basic error message
def error(message: String): String =
  message

// 
def simulateMaze(ast: List[Rule]) =
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")
  TextSimulation(emptyMaze, ast)

// parse a 
def parseAndEvalLine(input: String) =
  PiconotParser(input) match
    case PiconotParser.Success(ast, _) => println(simulateMaze(ast))
    case e: PiconotParser.NoSuccess    => println(error(e.toString))

/** Parse a file and potentially evaluate it */
def runFile(filename: String): Unit =
  try {
    val input = io.Source.fromFile(filename).mkString
    parseAndEvalLine(input)
  } catch {
    case e: java.io.FileNotFoundException => println(error(e.getMessage))
  }


@main
def main(args: String*): Unit =
  runFile(args(0))
