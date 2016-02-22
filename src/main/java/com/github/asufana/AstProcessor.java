package com.github.asufana;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;

import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.model.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;

//参考：http://fits.hatenablog.com/entry/2015/01/17/172651

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class AstProcessor extends AbstractProcessor {
    
    private Trees trees;
    private TreeMaker maker;
    private JavacElements elements;
    
    @Override
    public void init(final ProcessingEnvironment procEnv) {
        //ECJ(Eclipse Compiler for Java)では動作しない
        this.trees = Trees.instance(procEnv);
        
        final JavacProcessingEnvironment env = (JavacProcessingEnvironment) procEnv;
        this.maker = TreeMaker.instance(env.getContext());
        this.elements = JavacElements.instance(env.getContext());
    }
    
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {
        // コンパイル対象の全ソースを処理
        roundEnv.getRootElements().stream().map(this::toUnit).forEach(this::processUnit);
        
        return false;
    }
    
    private CompilationUnitTree toUnit(final Element el) {
        final TreePath path = this.trees.getPath(el);
        return path.getCompilationUnit();
    }
    
    private void processUnit(final CompilationUnitTree cu) {
        //Visitor パターンで AST を処理
        cu.accept(new VarVisitor(), null);
        //変換ソースを表示
        System.out.println(cu + "\n");
    }
    
    private class VarVisitor extends TreeScanner<Void, Void> {
        @Override
        public Void visitVariable(final VariableTree node, final Void p) {
            if (node instanceof JCVariableDecl) {
                final JCVariableDecl vd = (JCVariableDecl) node;
                
                if ("var".equals(vd.vartype.toString())) {
                    JCExpression ex = AstProcessor.this.maker.Ident(AstProcessor.this.elements.getName("java"));
                    ex = AstProcessor.this.maker.Select(ex,
                                                        AstProcessor.this.elements.getName("lang"));
                    ex = AstProcessor.this.maker.Select(ex,
                                                        AstProcessor.this.elements.getName("Object"));
                    vd.vartype = ex;
                }
            }
            return null;
        }
    }
}
