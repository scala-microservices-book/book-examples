package dataGen

import java.io._

import scala.util.Random


object DataGen extends App {
  val buff = new BufferedWriter(new FileWriter(new File("txt.sq")))

  val soMax = 2000
  val maxMostUsedPoints = 10000
  val maxAllUsedPoints = 2000
  val lessUsedProbability = 0.2

  val names = Set("Ali", "Paul", "Rachel", "Simon", "Angel", "Junior", "Philip", "Stephen", "Jennifer", "John", "Adam", "Rose", "Miller", "Neha", "Nancy", "", "Kapil", "Michelle", "Sophia", "Jackson", "Emma", "Aiden", "Olivia", "Lucas", "Ava Liam", "Mia Noah", "Isabella", "Ethan", "Riley Mason", "Aria", "Caden", "Zoe Oliver", " Charlotte Elijah", " Lily", "Grayson", " Layla Jacob", " Amelia", "Michael", " Emily Benjamin", " Madelyn Carter", " Aubrey", "James", "Adalyn", "Jayden", " Madison Logan", " Chloe Alexander", " Harper", "Caleb", " Abigail Ryan", " Aaliyah Luke", " Avery Daniel", " Evelyn", "Jack", " Kaylee", "William", " Ella", "Owen", " Ellie Gabriel", " Scarlett", "Matthew", " Arianna Connor", " Hailey", "Jayce", " Nora", "Isaac", " Addison Sebastian", " Brooklyn", "Henry", " Hannah", "Muhammad", " Mila", "Cameron", " Leah", "Wyatt", " Elizabeth Dylan", " Sarah Nathan", " Eliana", "Nicholas", " Mackenzie Julian", " Peyton", "Eli",
    "Maria", "Levi", " Grace", "Isaiah", " Adeline", " Landon", " Elena", " David", " Anna", "Christian", " Victoria", "Andrew", " Camilla", " Brayden", " Lillian", " John", " Natalie", " Lincoln"
  ).toList.map(_.trim)
  val aboutMe = Set("Toy apps or cute things like qsort in haskell really give the wrong idea.",
    "10 PRINT I am the best. 20 GOTO 10"
  ).toList
  val location = Set("london", "new york", "delhi", "singapore", "san jose", "san francisco", "sydney").toList


  for {
    soA <- 1 until soMax
    name = random(names)
    loc = random(location)
    about = random(aboutMe)
  } {
    write(s"insert into so_user_info (name, so_account_id, about_me, so_link, location) values ('$name',$soA,'$about','#','$loc');")
  }

  write("")
  write("")

  val mostUsed = List("scala", "C", "C#", "C++", "android", "haskell", "java", "swift", "go", "ruby", "perl", "python", "javascript", "objective-c", "clojure", "fortran", "lisp", "erlang", "lua").map(_.toLowerCase)
  val all = List("ABAP", "ABC", "ActionScript", "Ada", "Agilent VEE", "Algol", "Alice", "Angelscript", "Apex", "APL", "Applescript", "Arc", "AspectJ", "Assembly language", "ATLAS", "AutoIt", "AutoLISP", "Automator", "Avenue", "Awk", "Bash", "Basic", "BBC BASIC", "bc", "BCPL", "BETA", "BlitzMax", "Boo", "Bourne shell", "C shell", "C#", "C++", "C++/CLI", "C-Omega", "C", "Caml", "Ceylon", "CFML", "cg", "Ch", "CHILL", "CIL", "CL (OS/400)", "Clarion", "Clean", "Clipper", "Clojure", "CLU", "COBOL", "Cobra", "CoffeeScript", "COMAL", "Common Lisp", "Crystal", "cT", "Curl", "D", "Dart", "DCL", "Delphi/Object Pascal", "DiBOL", "Dylan", "E", "ECMAScript", "EGL", "Eiffel", "Elixir", "Elm", "Emacs Lisp", "Erlang", "Etoys", "Euphoria", "EXEC", "F#", "Factor", "Falcon", "Fantom", "Felix", "Forth", "Fortran", "Fortress", "Gambas", "GNU Octave", "Go", "Gosu", "Groovy", "Hack", "Haskell", "Haxe", "Heron", "HPL", "HyperTalk", "Icon", "IDL", "Inform", "Informix-4GL", "INTERCAL", "Io", "Ioke", "J#", "J", "JADE", "Java", "JavaFX Script", "JavaScript", "JScript", "JScript.NET", "Julia", "Korn shell", "Kotlin", "LabVIEW", "Ladder Logic", "Lasso", "Limbo", "Lingo", "Lisp", "LiveCode", "Logo", "LotusScript", "LPC", "Lua", "Lustre", "M4", "MAD", "Magic", "Magik", "Malbolge", "MANTIS", "Maple", "MATLAB", "Max/MSP", "MAXScript", "MDX", "MEL", "Mercury", "Miva", "ML", "Modula-2", "Modula-3", "Monkey", "MOO", "Moto", "MQL4", "MS-DOS batch", "MUMPS", "NATURAL", "Nemerle", "Nim", "NQC", "NSIS", "NXT-G", "Oberon", "Object Rexx", "Objective-C", "OCaml", "Occam", "OpenCL", "OpenEdge ABL", "OPL", "Oxygene", "Oz", "Paradox", "Pascal", "Perl", "PHP", "Pike", "PILOT", "PL/I", "PL/SQL", "Pliant", "PostScript", "POV-Ray", "PowerBasic", "PowerScript", "PowerShell", "Processing", "Prolog", "Pure Data", "PureBasic", "Python", "Q", "R", "Racket", "REBOL", "REXX", "Ring", "RPG (OS/400)", "Ruby", "Rust", "S-PLUS", "S", "SAS", "Sather", "Scala", "Scheme", "Scratch", "sed", "Seed7", "SIGNAL", "Simula", "Simulink", "Slate", "Smalltalk", "Smarty", "SPARK", "SPSS", "SQR", "Squeak", "Squirrel", "Standard ML", "Stata", "Suneido", "SuperCollider", "Swift", "TACL", "Tcl", "Tex", "thinBasic", "TOM", "Transact-SQL", "TypeScript", "Vala/Genie", "VBScript", "Verilog", "VHDL", "Visual Basic .NET", "Visual Basic", "WebDNA", "Whitespace", "Wolfram", "X10", "xBase", "XBase++", "Xen", "Xojo", "XPL", "XQuery", "XSLT", "Xtend", "yacc", "Yorick", "Z shell").map(_.toLowerCase())

  for {
    tag <- mostUsed ::: all.toSet.diff(mostUsed.toSet).toList
  } {
    write(s"insert into so_tag (name) values ('$tag');")
  }

  write("")
  write("")

  for {
    user <- 1 until soMax
    _ <- 1 to 5
    t = if (math.random > lessUsedProbability) (math.max(Random.nextInt(mostUsed.size),1),  math.max(Random.nextInt(maxMostUsedPoints),1))
    else (math.max(Random.nextInt(all.size),1), math.max(Random.nextInt(maxAllUsedPoints),1))
  } {
    write(s"insert into so_reputation (user, tag, points) values ($user, ${t._1},${t._2} );")
  }


  def random[T](ls: List[T]) = ls(Random.nextInt(ls.size))
  

  def write(str:String): Unit ={
    buff.write(str+"\n")
  }
  buff.close()

}
