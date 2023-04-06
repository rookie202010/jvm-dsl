package com.dongjiaqiang.jvm.dsl.calcite;

import com.dongjiaqiang.jvm.dsl.java.api.expression.CustomBlockExpressionTranslator;
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext;
import com.dongjiaqiang.jvm.dsl.java.core.JavaProgram;
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler;
import com.dongjiaqiang.jvm.dsl.java.core.compile.JavaCompiler;
import com.dongjiaqiang.jvm.dsl.java.core.translate.package$;
import com.typesafe.config.ConfigFactory;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

public class Example {


    public static Class<?> compile(String code,String packageName){
        JavaProgram javaProgram =  package$.MODULE$.ProgramTranslator(code).translate(packageName);
        JavaCompiler javaCompiler = new JaninoCompiler( Example.class.getClassLoader() );
        return javaCompiler.compile( javaProgram, packageName );
    }

    public static void main(String[] args) throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("caseSensitive", "false");
        properties.setProperty("unquotedCasing", "UNCHANGED");
        Connection connection = DriverManager.getConnection("jdbc:calcite:parserFactory=org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl#FACTORY",
                properties);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

        Class<?> clazz = compile("def eval(Array[String] input)=Int throws Exception{\n" +
                "       input.map(score=> { return score.toInt(); }).sum().toInt();\n" +
                "   }","com.example");

        calciteConnection.getRootSchema().add("customSum", ScalarFunctionImpl.create(clazz,"customSum"));
       // calciteConnection.execute("select 1 from 1;")
    }
}
