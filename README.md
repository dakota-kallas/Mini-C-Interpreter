# Mini-C-Interpreter
An Interpreter for a Mini C language that is a subset of the C language.

### **Description:**
For this project, I wrote a type-checker and interpreter for a toy imperative language named MINI-C, a C-Like language with block scoping. The type-checker and
interpreter were written in Java and incorporate a pre-provided parser and code base. I completed both static functions `TypeChecker.isValid( Program p )` & 
`Interpreter.meaning( Program p )` for this project.       

The `TypeChecker.isValid( Program p )` function accepts a program and returns true if the program is valid and false otherise. The intent of 
this function is to ensure that the program is strongly and statically typed. Note that the input program must be syntactically correct in order to obtain a Program 
object on which to operate.    

The `Interpreter.meaning( Program p )` function accepts a single valid program object and returns the resulting state. The meaning of a program is given by the 
state that it produces.

# **Mini-C Language:**
Mini-C is an imperative language that supports only the boolean and int data types along with basic arithmetic, logic, and relational operators. There are no function
calls, arrays, strings, or complex data types.

## Semantics
This section informally defines the semantics of each program element. Since this specification is not formal, it should convey a reasonable specification of the expected meaning of each program element.

**PROGRAM**       
Execution of a program means execute the body in the context of the state imposed by the DECLARATIONS. The meaning of the program is the state that results from 
executing the body.

**BLOCK**       
Executing a block means execute each statement in the body of the block in the context of the state imposed by combining the enclosing state with modifications imposed 
by the DECLARATIONS. Each STATEMENT is executed in the order it occurs and the meaning of the BLOCK is the state produced by the final statement in the BLOCK. Note 
that blocks introduce a new variable scope such that variables from external scopes can be hidden (i.e. new variables with the same name can be declared in nested 
blocks).

**DECLARATIONS**        
Executing a DECLARATIONS means execute each DECLARATION in the order that they occur. The meaning of the DECLARATIONS is the state that results after execution of the 
last DECLARATION.

**DECLARATION**       
The meaning of a DECLARATION is the state that results from adding the declared variable to the state and assigning it a value that denotes the notion of not 
initialized. Note, that the scope of a single declaration extends only to the BODY of the associated BLOCK or PROGRAM.

**ASSIGN**        
Binds the value the expression to the named variable. The type of the variable and the type of the expression must be identical; otherwise there is an error.

**IF**        
The meaning is the meaning of the first statement if the conditional expression evaluates to true. Otherwise, the meaning is the meaning of the second statement if it 
is present. Otherwise, the meaning is the state in which the IF is executed.

**WHILE**       
Execution of a while first evaluates the expression and then executes the associated statement if the expression is true after which this process repeats.

**EXPRESSION**        
Evaluation of an expression produces the value of the expression.

**BINARY**        
A binary expression represents either a logical or arithmetic operation. See Figure 1 for details. Each operator is defined as that of the corresponding Java operator.

**UNARY**         
There are two unary expressions. See Figure 1 for details. Each operator is defined as that of the corresponding Java operator.

## Mini-C Abstract Syntax
The abstract syntax of ART-C is given below. The abstract syntax defines the objects that you must use when writing your type checker and interpreter. These grammatical 
classes correspond to actual classes in the provided code base.

`Program = Declarations decPart; Block body;`     
`Declarations = Declaration*;`        
`Declaration = Type t, Variable v;`     
`Statment = Skip | Block | Assignment | Conditional | Loop `      
`Skip = empty;`           
`Block = Declarations* declarations; Statement* statements;`        
`Assignment = Variable target; Expression source;`      
`Conditional = Expression test; Statement thenbranch; Statement elsebranch;`      
`Loop = Expression test; Statement body; `          
`Expression = Variable | Value | Binary | Unary;`         
`Binary = Operator op; Expression term2; Expression term2;`         
`Unary = Operator op; Expression term;`
