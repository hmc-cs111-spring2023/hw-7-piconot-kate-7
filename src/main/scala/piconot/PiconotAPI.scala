import picolib._
import picolib.maze._
import picolib.semantics._
import piconot.parser._
import java.io.File

// basic error message
def error(message: String): String =
  s"${RESET}${RED}[Error] ${message}${RESET}"

// 
def simulateMaze(ast: Maze): String =
  try {
    eval(ast).toString
  } catch {
    case e: ArithmeticException => error(e.getMessage)
  }

// parse a 
def parseAndEvalLine(input: String) =
  CalcParser(input) match
    case CalcParser.Success(ast, _) => println(simulateMaze(ast))
    case e: CalcParser.NoSuccess    => println(error(e.toString))

/** Parse a file and potentially evaluate it */
def runFile(filename: String): Unit =
  try {
    val input = Source.fromFile(filename).mkString
    parseAndEvalLine(input)
  } catch {
    case e: java.io.FileNotFoundException => println(error(e.getMessage))
  }


@main
def main(args: String*): Unit =
  runFile(args(0))
