// Bersilin C - CS20B013
// For CS6013 - Advanced Compiler Design or Modern Compilers
// Jan - May 2024

//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJDepthFirst<R,A> implements GJVisitor<R,A> 
{
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n, A argu) 
   {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) 
   {
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

   public R visit(NodeOptional n, A argu) 
   {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) 
   {
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

   /* Variables declared for usage */

   // Set to false if a variable is used before declaration
   public boolean varDeclared = true;

   // Symbol Table
   HashMap<String, ClassAttrNode> classMap = new HashMap<String, ClassAttrNode>();

   /** Checks if a variable is declared in the current method or not
    * 
    *  @param varName The name of the variable
    *  @param argu The current class and method and the defined variables
    */
   boolean isMethodVar(String varName, A argu)
   {
      ArguClass arg = (ArguClass)argu;
      ClassAttrNode classAttrNode = classMap.get(arg.currClass);
      MethodAttrNode methodAttrNode = classAttrNode.methodMap.get(arg.currMethod);

      if(methodAttrNode.methodVarMap.containsKey(varName)) return true;
      
      return false;
   }

   /**
    *  Copies the contents of the src set to the dest set (clears the dest set before copying)
    *  
    *  @param dest The destination set
    *  @param src The source set
    */
   void copySet(HashSet<String> dest, HashSet<String> src)
   {
      dest.clear();
      for(String string: src)
      {
         dest.add(string);
      }
   }

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n, A argu) 
   {      
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
      // We don't really care about the main class
      return null;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
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
      
      ClassAttrNode classAttrNode = new ClassAttrNode(className, null);
      classMap.put(className, classAttrNode);

      ArguClass arg = new ArguClass(className, null);

      n.f3.accept(this, (A)arg);
      n.f4.accept(this, (A)arg);
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

      ClassAttrNode classAttrNode = new ClassAttrNode(className, parentName);
      classMap.put(className, classAttrNode);

      ArguClass arg = new ArguClass(className, null);

      n.f5.accept(this, (A)arg);
      n.f6.accept(this, (A)arg);
      return null;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n, A argu) 
   {
      String typeName = (String)n.f0.accept(this, argu);
      String varName = (String)n.f1.accept(this, argu);

      ArguClass arg = (ArguClass)argu;

      ClassAttrNode classAttrNode = classMap.get(arg.currClass);
      if(arg.currMethod == null)
      {
         classAttrNode.addVar(varName, typeName);
      }
      else
      {
         MethodAttrNode methodAttrNode = classAttrNode.methodMap.get(arg.currMethod);
         methodAttrNode.addVar(varName, typeName);
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
      String returnType = (String)n.f1.accept(this, argu);
      String methodName = (String)n.f2.accept(this, argu);

      ArguClass arg = (ArguClass)argu;
      arg.currMethod = methodName;

      ClassAttrNode classAttrNode = classMap.get(arg.currClass);
      classAttrNode.addMethod(methodName, arg.currClass, returnType);
      
      n.f4.accept(this, (A)arg);
      
      arg.definedVariables.clear();
      
      n.f7.accept(this, (A)arg);
      n.f8.accept(this, (A)arg);

      n.f10.accept(this, (A)arg);

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
      String typeName = (String)n.f0.accept(this, argu);
      String paramName = (String)n.f1.accept(this, argu);

      ArguClass arg = (ArguClass)argu;
      MethodAttrNode methodAttrNode = classMap.get(arg.currClass).methodMap.get(arg.currMethod);
      methodAttrNode.addParam(paramName, typeName);
      return null;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n, A argu) 
   {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n, A argu) 
   {
      return (R)(new String("int[]"));
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n, A argu) 
   {
      return (R)(new String("boolean"));
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n, A argu) 
   {
      return (R)(new String("int"));
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
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n, A argu) 
   {
      String definedName = (String)n.f0.accept(this, argu);
      n.f2.accept(this, argu);

      if(isMethodVar(definedName, argu))
      {
         ArguClass arg = (ArguClass)argu;
         arg.definedVariables.add(definedName);
      }
      return null;
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
      String definedName = (String)n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      n.f5.accept(this, argu);

      if(isMethodVar(definedName, argu))
      {
         ArguClass arg = (ArguClass)argu;
         arg.definedVariables.add(definedName);
      }
      return null;
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
      n.f0.accept(this, argu);
      n.f4.accept(this, argu);
      return null;
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
      n.f2.accept(this, argu);

      ArguClass arg = (ArguClass)argu;
      HashSet<String> originalSet = new HashSet<String>(); // has the original copy for the next statement set(else)
      
      copySet(originalSet, arg.definedVariables);

      n.f4.accept(this, argu);
      
      HashSet<String> changed1 = new HashSet<String>();
      copySet(changed1, arg.definedVariables);
      copySet(arg.definedVariables, originalSet);

      n.f6.accept(this, argu);
      HashSet<String> temp = new HashSet<String>();
      copySet(temp, arg.definedVariables);

      for(String string : temp)
      {
         if(!changed1.contains(string))
         {
            arg.definedVariables.remove(string);
         }
      }
      return null;
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
      ArguClass arg = (ArguClass)argu;
      
      n.f2.accept(this, argu);

      HashSet<String> originalSet = new HashSet<String>();
      copySet(originalSet, arg.definedVariables);

      n.f4.accept(this, argu);
      copySet(arg.definedVariables, originalSet);

      return null;
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
    * f8 -> Identifier() // not initialised afrer the for loop
    * f9 -> "="
    * f10 -> Expression()
    * f11 -> ")"
    * f12 -> Statement()
    */
   public R visit(ForStatement n, A argu) 
   {
      String definedName = (String)n.f2.accept(this, argu);
      ArguClass arg = (ArguClass)argu;

      HashSet<String> tempSet = new HashSet<String>();
      copySet(tempSet, arg.definedVariables);

      n.f4.accept(this, argu);
      
      if(isMethodVar(definedName, argu))
      {
         arg.definedVariables.add(definedName);
         tempSet.add(definedName);
      }
      
      n.f6.accept(this, argu);
      n.f12.accept(this, argu); // This can add new declarations
      n.f10.accept(this, argu);

      copySet(arg.definedVariables, tempSet);
      return null;
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
      n.f2.accept(this, argu);
      return null;
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
      n.f0.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) 
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) 
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu)
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu)
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) 
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu)
   {
      n.f0.accept(this, argu);
      n.f2.accept(this, argu);
      return null;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) 
   {
      n.f0.accept(this, argu);
      return null;
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
      n.f0.accept(this, argu);
      n.f4.accept(this, argu);
      return null;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) 
   {
      R _ret=null;
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral() 0
    *       | TrueLiteral() 1
    *       | FalseLiteral() 2
    *       | Identifier() 3
    *       | ThisExpression() 4
    *       | ArrayAllocationExpression() 5
    *       | AllocationExpression() 6
    *       | NotExpression() 7
    *       | BracketExpression() 8
    */
   public R visit(PrimaryExpression n, A argu) 
   {
      if(n.f0.which == 3)
      {
         ArguClass arg = (ArguClass)argu;
         String usedName = (String)n.f0.accept(this, argu);

         if(isMethodVar(usedName, argu))
         {
            if(!arg.definedVariables.contains(usedName)) 
            {
               // System.out.println("Uninit: " + usedName);
               // System.out.println("Class: " + arg.currClass + " Method: " + arg.currMethod);
               // System.out.println("Defined Variables: ");
               // for(String string : arg.definedVariables)
               // {
               //    System.out.println(string);
               // }
               varDeclared = false;
            }
         }
      }
      else
      {
         n.f0.accept(this, argu);
      }
      return null;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) 
   {
      String identifier = n.f0.toString();
      return (R)identifier;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) 
   {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
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
      R _ret=null;
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) 
   {
      R _ret=null;
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> ( MessageSend() | PrimaryExpression() )
    */
   public R visit(NotExpression n, A argu) 
   {
      R _ret=null;
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) 
   {
      R _ret=null;
      n.f1.accept(this, argu);
      return _ret;
   }

   class ArguClass
   {
      String currClass;
      String currMethod;

      HashSet<String> definedVariables;

      ArguClass(String className, String methodName)
      {
         this.currClass = className;
         this.currMethod = methodName;
         this.definedVariables = new HashSet<String>();
      }
   }

   class ClassAttrNode
   {
      int methodCount;
      int varCount;
      HashMap<String, MethodAttrNode> methodMap;
      HashMap<String, VarAttrNode> varMap;

      String parent;
      String selfName;

      ClassAttrNode(String className, String parentName)
      {
         this.selfName = className;
         this.parent = parentName;
         this.methodMap = new HashMap<String, MethodAttrNode>();
         this.varMap = new HashMap<String, VarAttrNode>();
         this.methodCount = 0;
         this.varCount = 0;
      }

      void addVar(String varName, String typeName)
      {
         this.varCount++;
         this.varMap.put(varName, new VarAttrNode(varName, typeName));
      }

      void addMethod(String methodName, String className, String returnType)
      {
         MethodAttrNode methodAttrNode = new MethodAttrNode(methodName, className, returnType);
         this.methodMap.put(methodName, methodAttrNode);
         this.methodCount++;
      }
   }

   class MethodAttrNode
   {
      String methodName;
      String className;
      int methodVarCount;
      int paramCount;
      HashMap<String, ParamAttrNode> paramMap;
      HashMap<String, VarAttrNode> methodVarMap;
      String returnType;

      MethodAttrNode(String methodName, String className, String returnType)
      {
         this.methodName = methodName;
         this.className = className;
         this.returnType = returnType;

         this.paramMap = new HashMap<String, ParamAttrNode>();
         this.methodVarMap = new HashMap<String, VarAttrNode>();

         this.methodVarCount = 0;
         this.paramCount = 0;
      }

      void addParam(String paramName, String type)
      {
         this.paramCount++;
         this.paramMap.put(paramName, new ParamAttrNode(paramName, type));
      }

      void addVar(String varName, String type)
      {
         this.methodVarCount++;
         this.methodVarMap.put(varName, new VarAttrNode(varName, type));
      }
   }

   class VarAttrNode
   {
      String varName;
      String type;

      VarAttrNode(String varName, String type)
      {
         this.varName = varName;
         this.type = type;
      }
   }

   class ParamAttrNode 
   {
      String paramName;
      String type;

      ParamAttrNode(String paraString, String type)
      {
         this.paramName = paraString;
         this.type = type;
      }
   }
}