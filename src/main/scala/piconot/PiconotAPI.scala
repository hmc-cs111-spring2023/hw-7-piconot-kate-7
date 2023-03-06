import Console.{RED, RESET}

import picolib._
import picolib.maze._
import picolib.semantics._
import piconot.parser._
import picolib.display._
import java.io.File

//def simulateMaze(ast: List[Rule]) =
//  val emptyMaze = Maze("resources" + File.separator + "empty.txt")
//  TextSimulation(emptyMaze, ast)

// parse a 
//def parseAndEvalLine(input: String) =
//  PiconotParser(input) match
//    case PiconotParser.Success(ast, _) => println(simulateMaze(ast))
//    case e: PiconotParser.NoSuccess    => println(error(e.toString))

/** Parse a file and potentially evaluate it */
//def runFile(filename: String): Unit =
  //try {
  //  val input = io.Source.fromFile(filename).mkString
  //  parseAndEvalLine(input)
  //} catch {
  //  case e: java.io.FileNotFoundException => println(error(e.getMessage))
  //}

@main
def main(args: String*): Unit = {

  if (args.length != 2) {
    println(error(usage))
    sys.exit()
  }

  // parse the maze file
  val mazeFileName = args(0)
  val maze = Maze(getFileLines(mazeFileName))

  // parse the program file
  val programFilename = args(1)
  val program = PiconotParser(getFileContents(programFilename))

  // process the results of parsing
  program match {
    // Error handling: syntax errors
    case e: PiconotParser.NoSuccess => println(error(e.toString))

    // If parsing succeeded, create the bot and run it
    case PiconotParser.Success(t, _) => {
      object bot extends Picobot(maze, program.get) with TextDisplay
      bot.run()
    }
  }

}

/** A string that describes how to use the program * */
// def usage = "usage: sbt run <maze-file> <rules-file>" --- need to write
def usage = "you did something wrong here buddy"

/** Format an error message */
def error(message: String): String =
  s"${RESET}${RED}[Error] ${message}${RESET}"

/** Given a filename, get a list of the lines in the file */
def getFileLines(filename: String): List[String] =
  try {
    io.Source.fromFile(filename).getLines().toList
  } catch { // Error handling: non-existent file
    case e: java.io.FileNotFoundException => {
      println(error(e.getMessage())); sys.exit(1)
    }
  }

/** Given a filename, get the contents of the file */
def getFileContents(filename: String): String =
  try {
    io.Source.fromFile(filename).mkString
  } catch { // Error handling: non-existent file
    case e: java.io.FileNotFoundException => {
      println(error(e.getMessage())); sys.exit(1)
    }
  }