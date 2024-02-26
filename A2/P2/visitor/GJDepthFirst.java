//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;
import attributes.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJDepthFirst<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * Symbol Table for the BuritoJava program
    */
   HashMap<String, ClassAttrNode> symbolTable = new HashMap<String, ClassAttrNode>();
   HashSet<String> usedIdentifiers = new HashSet<String>();

   boolean firstPass = true;
   
   int tempCount = -1;
   String getNextTemp()
   {
      tempCount++;
      String newTemp = "__t" + tempCount;
      while(usedIdentifiers.contains(newTemp))
      {
         tempCount++;
         newTemp = "__t" + tempCount;
      }
      return newTemp;
   }

   boolean debugVar = false;

   void debug(String s)
   {
      if(debugVar) System.err.println(s);
   }

   void Out(String s)
   {
      System.out.println(s);
   }

   String getType(String identifier, ArguClass argu)
   {
      String currClass = argu.currClass;
      String currMethod = argu.currMethod;

      if(symbolTable.get(currClass).methodMap.get(currMethod).methodVarMap.containsKey(identifier))
      {
         return symbolTable.get(currClass).methodMap.get(currMethod).methodVarMap.get(identifier).type;
      }
      else if(symbolTable.get(currClass).methodMap.get(currMethod).paramMap.containsKey(identifier))
      {
         return symbolTable.get(currClass).methodMap.get(currMethod).paramMap.get(identifier).type;
      }
      else if(symbolTable.get(currClass).varMap.containsKey(identifier))
      {
         return symbolTable.get(currClass).varMap.get(identifier).type;
      }
      else
      {
         String className = currClass;
         while(symbolTable.get(className).parent != null)
         {
            String parentClass = symbolTable.get(className).parent;
            if(symbolTable.get(parentClass).varMap.containsKey(identifier))
            {
               return symbolTable.get(parentClass).varMap.get(identifier).type;
            }
            className = parentClass;
         }
      }

      assert(false); // Code should never reach here
      return null;
   }

   String getMethodReturnType(String className, String methodName)
   {
      if(symbolTable.get(className).methodMap.containsKey(methodName))
      {
         return symbolTable.get(className).methodMap.get(methodName).returnType;
      }
      else
      {
         while(symbolTable.get(className).parent != null)
         {
            className = symbolTable.get(className).parent;
            if(symbolTable.get(className).methodMap.containsKey(methodName))
            {
               return symbolTable.get(className).methodMap.get(methodName).returnType;
            }
         }
      }

      assert(false); // Code should never reach here
      return null;
   }

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n, A argu) 
   {
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);

      firstPass = false;

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return null;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n, A argu) 
   {
      String className = (String)n.f1.accept(this, argu);
      String paramName = (String)n.f11.accept(this, argu);

      if(firstPass)
      {
         usedIdentifiers.add(className);
         usedIdentifiers.add(paramName);

         ClassAttrNode classAttrNode = new ClassAttrNode(className, null);
         symbolTable.put(className, classAttrNode);

         symbolTable.get(className).addMethod("main", className, "void");
         symbolTable.get(className).methodMap.get("main").addParam(paramName, "String[]");
      }
      else
      {
         String s1 = "class " + className;
         String s2 = "public static void main(String[] " + paramName + ")";

         ArguClass arguClass = new ArguClass();
         arguClass.currClass = className;
         arguClass.currMethod = "main";

         StmtReturn stmtReturn = (StmtReturn)n.f14.accept(this, (A)arguClass);

         String varDecls = "";
         for(String varName : stmtReturn.usedTemps.keySet())
         {
            varDecls += stmtReturn.usedTemps.get(varName) + " " + varName + ";\n";
         }

         Out(s1 + "\n{\n" + s2 + "\n{\n" + varDecls + "\n" + stmtReturn.printString + "}\n}\n");
      }
      return null;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n, A argu) 
   {
      n.f0.accept(this, argu);
      return null;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n, A argu) 
   {
      String className = (String)n.f1.accept(this, argu);

      ArguClass arguClass = new ArguClass();
      arguClass.currClass = className;
      arguClass.currMethod = null;

      if(firstPass)
      {
         usedIdentifiers.add(className);
   
         ClassAttrNode classAttrNode = new ClassAttrNode(className, null);
         symbolTable.put(className, classAttrNode);

         n.f3.accept(this, (A)arguClass);
         n.f4.accept(this, (A)arguClass);
      }
      else
      {
         String s1 = "class " + className;

         String varDecls = "";
         for(String varName : symbolTable.get(className).varMap.keySet())
         {
            VarAttrNode varAttrNode = symbolTable.get(className).varMap.get(varName);
            varDecls += varAttrNode.type + " " + varName + ";\n";
         }

         Out(s1 + "\n{\n" + varDecls + "\n");

         n.f4.accept(this, (A)arguClass);

         Out("\n}\n");
      }
      return null;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n, A argu) 
   {
      String className = (String)n.f1.accept(this, argu);
      String parentName = (String)n.f3.accept(this, argu);

      ArguClass arguClass = new ArguClass();
      arguClass.currClass = className;
      arguClass.currMethod = null;

      if(firstPass)
      {
         usedIdentifiers.add(className);
         usedIdentifiers.add(parentName);

         ClassAttrNode classAttrNode = new ClassAttrNode(className, parentName);
         symbolTable.put(className, classAttrNode);

         n.f5.accept(this, (A)arguClass);
         n.f6.accept(this, (A)arguClass);
      }
      else
      {
         String s1 = "class " + className + " extends " + parentName;

         String varDecls = "";
         for(String varName : symbolTable.get(className).varMap.keySet())
         {
            VarAttrNode varAttrNode = symbolTable.get(className).varMap.get(varName);
            varDecls += varAttrNode.type + " " + varName + ";\n";
         }

         Out(s1 + "\n{\n" + varDecls + "\n");

         n.f6.accept(this, (A)arguClass);

         Out("\n}\n");
      }
      return null;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n, A argu) 
   {
      ArguClass arguClass = (ArguClass)argu;
      String varType = (String)n.f0.accept(this, argu);
      String varName = (String)n.f1.accept(this, argu);

      usedIdentifiers.add(varName);

      if (arguClass.currMethod == null)
      {
         symbolTable.get(arguClass.currClass).addVar(varName, varType);
      }
      else
      {
         symbolTable.get(arguClass.currClass).methodMap.get(arguClass.currMethod).addVar(varName, varType);
      }
      return null;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n, A argu) 
   {
      ArguClass arguClass = (ArguClass)argu;
      String returnType = (String)n.f1.accept(this, argu);
      String methodName = (String)n.f2.accept(this, argu);

      arguClass.currMethod = methodName;

      if(firstPass)
      {
         usedIdentifiers.add(methodName);

         symbolTable.get(arguClass.currClass).addMethod(methodName, arguClass.currClass, returnType);

         n.f4.accept(this, (A)arguClass);
         n.f7.accept(this, (A)arguClass);
      }
      else
      {
         /* Printing Paramlist in the same order (ensured by LinkedHashMap) */
         String paramList = "";
         for(String paramName : symbolTable.get(arguClass.currClass).methodMap.get(methodName).paramMap.keySet())
         {
            ParamAttrNode paramAttrNode = symbolTable.get(arguClass.currClass).methodMap.get(methodName).paramMap.get(paramName);
            paramList += paramAttrNode.type + " " + paramName + ", ";
         }

         // Removes the comma and space at the end of the string
         if(paramList.length() > 0) paramList = paramList.substring(0, paramList.length() - 2);
         /* ParamList Printing ends */
         
         HashMap<String, String> usedTemps = new HashMap<String, String>();
         String stmtString = "";

         for (int i = 0; i < n.f8.size(); i++)
         {
            StmtReturn stmtReturn = (StmtReturn)n.f8.elementAt(i).accept(this, (A)arguClass);
            stmtString += stmtReturn.printString + "\n\n";
            usedTemps.putAll(stmtReturn.usedTemps);
         }
         
         ExprReturn exprReturn = (ExprReturn)n.f10.accept(this, (A)arguClass);
         
         /* Printing all variable declarations */

         String varDecls = "";
         for(String varName: symbolTable.get(arguClass.currClass).methodMap.get(methodName).methodVarMap.keySet())
         {
            VarAttrNode varAttrNode = symbolTable.get(arguClass.currClass).methodMap.get(methodName).methodVarMap.get(varName);
            varDecls += varAttrNode.type + " " + varName + ";\n";
         }
         for(String varName : usedTemps.keySet())
            varDecls += usedTemps.get(varName) + " " + varName + ";\n";
         
         for(String varName : exprReturn.usedTemps.keySet())
            varDecls += exprReturn.usedTemps.get(varName) + " " + varName + ";\n";

         /* Variable declaration printing ends */

         stmtString += exprReturn.printString + "\n";

         Out("public " + returnType + " " + methodName + "(" + paramList + ")\n");
         Out("{\n" + varDecls + stmtString);

         Out("return " + exprReturn.returnTemp + ";\n}\n");
      }

      return null;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n, A argu) 
   {
      ArguClass arguClass = (ArguClass)argu;
      String paramType = (String)n.f0.accept(this, argu);
      String paramName = (String)n.f1.accept(this, argu);

      usedIdentifiers.add(paramName);

      symbolTable.get(arguClass.currClass).methodMap.get(arguClass.currMethod).addParam(paramName, paramType);
      return null;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n, A argu) 
   {
      n.f1.accept(this, argu);
      return null;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n, A argu) 
   {
      return (R)n.f0.accept(this, argu);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n, A argu) 
   {
      return (R)(n.f0.tokenImage + n.f1.tokenImage + n.f2.tokenImage);
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n, A argu) 
   {
      return (R)n.f0.tokenImage;
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n, A argu) 
   {
      return (R)n.f0.tokenImage;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | FieldAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | ForStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n, A argu) 
   {
      return (R)n.f0.accept(this, argu);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n, A argu) 
   {
      StmtReturn newStmtReturn = new StmtReturn();
      for (int i = 0; i < n.f1.size(); i++)
      {
         StmtReturn stmtReturn = (StmtReturn)n.f1.elementAt(i).accept(this, argu);
         newStmtReturn.addPrintString(stmtReturn.printString);
         newStmtReturn.addUsedTempMultiple(stmtReturn.usedTemps);
      }

      return (R)newStmtReturn;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n, A argu) 
   {
      String id = (String)n.f0.accept(this, argu);
      ExprReturn exprReturn = (ExprReturn)n.f2.accept(this, argu);

      StmtReturn stmtReturn = new StmtReturn();
      stmtReturn.addUsedTempMultiple(exprReturn.usedTemps);
      stmtReturn.addPrintString(exprReturn.printString);

      stmtReturn.addPrintString(id + " = " + exprReturn.returnTemp + ";\n");

      return (R)stmtReturn;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n, A argu) 
   {
      String id = (String)n.f0.accept(this, argu);
      ExprReturn exprReturn1 = (ExprReturn)n.f2.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f5.accept(this, argu);

      StmtReturn stmtReturn = new StmtReturn();
      stmtReturn.addUsedTempMultiple(exprReturn1.usedTemps);
      stmtReturn.addUsedTempMultiple(exprReturn2.usedTemps);

      stmtReturn.addPrintString(exprReturn1.printString);
      stmtReturn.addPrintString(exprReturn2.printString);

      stmtReturn.addPrintString(id + " [ " + exprReturn1.returnTemp + " ] = " + exprReturn2.returnTemp + ";\n");

      return (R)stmtReturn;
   }

   /**
    * f0 -> Expression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "="
    * f4 -> Expression()
    * f5 -> ";"
    */
   public R visit(FieldAssignmentStatement n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      String fieldName = (String)n.f2.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f4.accept(this, argu);

      StmtReturn stmtReturn = new StmtReturn();
      stmtReturn.addUsedTempMultiple(exprReturn1.usedTemps);
      stmtReturn.addUsedTempMultiple(exprReturn2.usedTemps);

      stmtReturn.addPrintString(exprReturn1.printString);
      stmtReturn.addPrintString(exprReturn2.printString);

      stmtReturn.addPrintString(exprReturn1.returnTemp + " . " + fieldName + " = " + exprReturn2.returnTemp + ";\n");

      return (R)stmtReturn;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfStatement n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f2.accept(this, argu);
      StmtReturn stmtReturn1 = (StmtReturn)n.f4.accept(this, argu);
      StmtReturn stmtReturn2 = (StmtReturn)n.f6.accept(this, argu);

      StmtReturn newStmtReturn = new StmtReturn();
      newStmtReturn.addUsedTempMultiple(exprReturn.usedTemps);
      newStmtReturn.addUsedTempMultiple(stmtReturn1.usedTemps);
      newStmtReturn.addUsedTempMultiple(stmtReturn2.usedTemps);

      newStmtReturn.addPrintString(exprReturn.printString);
      newStmtReturn.addPrintString("if( " + exprReturn.returnTemp + " )\n{\n" + stmtReturn1.printString + "\n}\nelse\n{\n" + stmtReturn2.printString + "\n}\n");

      return (R)newStmtReturn;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f2.accept(this, argu);
      StmtReturn stmtReturn = (StmtReturn)n.f4.accept(this, argu);

      StmtReturn newStmtReturn = new StmtReturn();
      newStmtReturn.addUsedTempMultiple(exprReturn.usedTemps);
      newStmtReturn.addUsedTempMultiple(stmtReturn.usedTemps);

      newStmtReturn.addPrintString(exprReturn.printString); // Condition

      newStmtReturn.addPrintString("while( " + exprReturn.returnTemp + " )\n{\n" + stmtReturn.printString + "\n"); // Body
      newStmtReturn.addPrintString(exprReturn.printString + "}\n"); // Condition change

      return (R)newStmtReturn;
   }

   /**
    * f0 -> "for"
    * f1 -> "("
    * f2 -> Identifier()
    * f3 -> "="
    * f4 -> Expression()
    * f5 -> ";"
    * f6 -> Expression()
    * f7 -> ";"
    * f8 -> Identifier()
    * f9 -> "="
    * f10 -> Expression()
    * f11 -> ")"
    * f12 -> Statement()
    */
   public R visit(ForStatement n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f4.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f6.accept(this, argu);
      ExprReturn exprReturn3 = (ExprReturn)n.f10.accept(this, argu);
      StmtReturn stmtReturn = (StmtReturn)n.f12.accept(this, argu);
      String iterator = (String)n.f2.accept(this, argu);
      String id_2 = (String)n.f8.accept(this, argu);

      StmtReturn newStmtReturn = new StmtReturn();
      newStmtReturn.addUsedTempMultiple(exprReturn1.usedTemps);
      newStmtReturn.addUsedTempMultiple(exprReturn2.usedTemps);
      newStmtReturn.addUsedTempMultiple(exprReturn3.usedTemps);
      newStmtReturn.addUsedTempMultiple(stmtReturn.usedTemps);

      newStmtReturn.addPrintString(exprReturn1.printString); // Initialize iterator
      newStmtReturn.addPrintString(iterator + " = " + exprReturn1.returnTemp + ";\n");

      newStmtReturn.addPrintString(exprReturn2.printString); // Condition

      newStmtReturn.addPrintString("while( " + exprReturn2.returnTemp + " )\n{\n" + stmtReturn.printString + "\n"); // Body
      newStmtReturn.addPrintString(exprReturn3.printString); // Increment iterator

      newStmtReturn.addPrintString(id_2 + " = " + exprReturn3.returnTemp + ";\n");
      newStmtReturn.addPrintString(exprReturn2.printString + "}\n"); // Condition change

      return (R)newStmtReturn;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f2.accept(this, argu);

      StmtReturn stmtReturn = new StmtReturn();
      stmtReturn.addPrintString(exprReturn.printString);
      stmtReturn.addUsedTempMultiple(exprReturn.usedTemps);

      stmtReturn.addPrintString("System.out.println(" + exprReturn.returnTemp + ");\n");

      return (R)stmtReturn;
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n, A argu) 
   {
      return (R)n.f0.accept(this, argu);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "boolean");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " & " + exprReturn2.returnTemp + ";\n");
      exprReturn1.setNewReturnTemp(newTemp, "boolean");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "boolean");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " < " + exprReturn2.returnTemp + ";\n");
      exprReturn1.setNewReturnTemp(newTemp, "boolean");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "int");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " + " + exprReturn2.returnTemp + ";\n");
      exprReturn1.setNewReturnTemp(newTemp, "int");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "int");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " - " + exprReturn2.returnTemp + ";\n");
      exprReturn1.setNewReturnTemp(newTemp, "int");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "int");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " * " + exprReturn2.returnTemp + ";\n");
      exprReturn1.setNewReturnTemp(newTemp, "int");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu) 
   {
      ExprReturn exprReturn1 = (ExprReturn)n.f0.accept(this, argu);
      ExprReturn exprReturn2 = (ExprReturn)n.f2.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn1.addUsedTemp(newTemp, "int");
      exprReturn1.addUsedTempMultiple(exprReturn2.usedTemps);

      exprReturn1.addPrintString(exprReturn2.printString);
      exprReturn1.addPrintString(newTemp + " = " + exprReturn1.returnTemp + " [ " + exprReturn2.returnTemp + " ];\n");
      exprReturn1.setNewReturnTemp(newTemp, "int");

      return (R)exprReturn1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f0.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn.addUsedTemp(newTemp, "int");

      exprReturn.addPrintString(newTemp + " = " + exprReturn.returnTemp + " . length;\n");
      exprReturn.setNewReturnTemp(newTemp, "int");

      return (R)exprReturn;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f0.accept(this, argu);
      String methodName = (String)n.f2.accept(this, argu); // method to be called

      String className = exprReturn.returnType; // class of the object

      String methodReturnType = getMethodReturnType(className, methodName); // returns method type (accounted for inheritance)

      ArrayList<ExprReturn> exprList = (ArrayList<ExprReturn>)n.f4.accept(this, argu);

      if(exprList == null) exprList = new ArrayList<ExprReturn>();
      for(int i=0; i<exprList.size(); i++) exprReturn.addPrintString(exprList.get(i).printString);
      for(int i=0; i<exprList.size(); i++) exprReturn.addUsedTempMultiple(exprList.get(i).usedTemps);

      String paramList = "";
      for(int i=0; i<exprList.size(); i++)
      {
         paramList += exprList.get(i).returnTemp;
         if(i != exprList.size() - 1) paramList += ", ";
      }
      
      String newTemp = getNextTemp();
      exprReturn.addUsedTemp(newTemp, methodReturnType);

      exprReturn.addPrintString(newTemp + " = " + exprReturn.returnTemp + " . " + methodName + " ( " + paramList + " );\n");
      exprReturn.setNewReturnTemp(newTemp, methodReturnType);

      return (R)exprReturn;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) 
   {
      ArrayList<ExprReturn> exprList = new ArrayList<ExprReturn>();
      exprList.add((ExprReturn)(n.f0.accept(this, argu)));

      for(int i=0; i<n.f1.size(); i++) 
         exprList.add((ExprReturn)n.f1.elementAt(i).accept(this, argu));

      return (R)(exprList);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) 
   {
      return (R)n.f1.accept(this, argu);
   }

   /**
    * f0 -> IntegerLiteral()  0
    *       | TrueLiteral() 1
    *       | FalseLiteral() 2
    *       | Identifier() 3
    *       | ThisExpression() 4
    *
    *       // All the ones below return ExprReturn
    *       | ArrayAllocationExpression() 5
    *       | AllocationExpression() 6
    *       | NotExpression() 7
    *       | BracketExpression() 8
    */
   public R visit(PrimaryExpression n, A argu) 
   {
      // Should always return an ExprReturn
      ExprReturn exprReturn;

      if(n.f0.which > 4) exprReturn = (ExprReturn)n.f0.accept(this, argu);
      else
      {
         exprReturn = new ExprReturn(null, null);
         String newTemp = getNextTemp();

         if(n.f0.which == 0)
         {  
            exprReturn.addUsedTemp(newTemp, "int");
            exprReturn.addPrintString(newTemp + " = " + (String)n.f0.accept(this, argu) + ";\n");
            exprReturn.setNewReturnTemp(newTemp, "int");
         }
         else if(n.f0.which == 1 || n.f0.which == 2)
         {
            exprReturn.addUsedTemp(newTemp, "boolean");
            exprReturn.addPrintString(newTemp + " = " + (String)n.f0.accept(this, argu) + ";\n");
            exprReturn.setNewReturnTemp(newTemp, "boolean");
         }
         else if(n.f0.which == 3)
         {
            String identifier = (String)n.f0.accept(this, argu);
            String type = getType(identifier, (ArguClass)argu);
            exprReturn.addUsedTemp(newTemp, type);
            exprReturn.addPrintString(newTemp + " = " + identifier + ";\n");
            exprReturn.setNewReturnTemp(newTemp, type);
         }
         else if(n.f0.which == 4)
         {
            String className = ((ArguClass)argu).currClass;
            exprReturn.addUsedTemp(newTemp, className);
            exprReturn.addPrintString(newTemp + " = this;\n");
            exprReturn.setNewReturnTemp(newTemp, className);
         }
      }

      return (R)exprReturn;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) 
   {
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) 
   {
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) 
   {
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) 
   {
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) 
   {
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f3.accept(this, argu);

      String newTemp = getNextTemp();
      exprReturn.addUsedTemp(newTemp, "int[]");

      exprReturn.addPrintString(newTemp + " = new int [ " + exprReturn.returnTemp + " ];\n");
      exprReturn.setNewReturnTemp(newTemp, "int[]");

      return (R)exprReturn;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) 
   {
      String type = (String)n.f1.accept(this, argu);
      
      String newTemp = getNextTemp();
      ExprReturn exprReturn = new ExprReturn(newTemp, type);
      exprReturn.addUsedTemp(newTemp, type);

      exprReturn.addPrintString(newTemp + " = new " + type + "();\n");
      exprReturn.setNewReturnTemp(newTemp, type);

      return (R)exprReturn;
   }

   /**
    * f0 -> "!"
    * f1 -> MessageSend() 
    *    |  PrimaryExpression()
    */
   public R visit(NotExpression n, A argu) 
   {
      ExprReturn exprReturn = (ExprReturn)n.f1.accept(this, argu);
      
      String newTemp = getNextTemp();
      exprReturn.addUsedTemp(newTemp, "boolean");

      exprReturn.addPrintString(newTemp + " = ! " + exprReturn.returnTemp + ";\n");
      exprReturn.setNewReturnTemp(newTemp, "boolean");

      return (R)exprReturn;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) 
   {
      return (R)n.f1.accept(this, argu); // No need to change anything here
   }
}
