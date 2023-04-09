package com.dongjiaqiang.jvm.dsl.calcite;

import com.dongjiaqiang.jvm.dsl.java.core.JavaProgram;
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler;
import com.dongjiaqiang.jvm.dsl.java.core.compile.JavaCompiler;
import com.dongjiaqiang.jvm.dsl.java.core.translate.package$;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Example {

    public static class Emp {
        public String name;
        public long id;
        public String scores;
        public Emp(String name, long id, String scores) {
            this.name = name;
            this.id = id;
            this.scores = scores;
        }
    }

    public static class CustomCompilerFactory extends org.codehaus.janino.CompilerFactory {
        @Override
        public IClassBodyEvaluator newClassBodyEvaluator() {
            return new CustomClassBodyEvaluator();
        }
    }

    public static class JavaSchema {
        public final Emp[] emp = new Emp[]{
                new Emp("Joe", 1L, "1,11,33,42"),
                new Emp("Jim", 2L, "22,13,122,344"),
                new Emp("Leo", 3L, "1233,1212,34,111")
        };
    }

    public static ClassLoader compile(Map<String,String> codes, String packageName) {
        Set<JavaProgram> javaPrograms = codes.entrySet().stream().map(entry->
            package$.MODULE$.ProgramTranslator(entry.getValue()).translate(packageName,entry.getKey())
        ).collect(Collectors.toSet());
        JavaCompiler javaCompiler = new JaninoCompiler(Example.class.getClassLoader());
        return javaCompiler.compile(scala.collection.JavaConverters.asScalaSet(javaPrograms));
    }

    public static void main(String[] args) throws Exception {

        Example.class.getClassLoader().loadClass("com.dongjiaqiang.jvm.dsl.calcite.Example$CustomCompilerFactory");
        Class.forName("org.apache.calcite.jdbc.Driver");
        final Properties properties = new Properties();
        properties.setProperty("caseSensitive", "false");
        properties.setProperty("lex", "JAVA");
        Connection connection = DriverManager.getConnection("jdbc:calcite:",
                properties);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        Map<String,String> codes = new HashMap<>();
        codes.put("Foo","program{\n" +
                "  def eval(String name)=String{\n" +
                "    return name.toUpperCase();\n" +
                "  }\n" +
                "}");
        codes.put("Bar","program{\n" +
                " def eval(String input)=Int{\n" +
                "      try{\n" +
                "        return input.split(\",\").map(score=> { return score.toInt(); }).sum().toInt();\n" +
                "     }catch(Exception e){\n" +
                "        return 1;\n" +
                "     }\n" +
                " } \n" +
                "}");
        ClassLoader classLoader = compile(codes,"com.example.calcite");
        classLoader.loadClass("com.example.calcite.Foo");

        calciteConnection.getRootSchema().add("foo", ScalarFunctionImpl.create(classLoader.loadClass("com.example.calcite.Foo"), "eval"));
        calciteConnection.getRootSchema().add("bar",ScalarFunctionImpl.create(classLoader.loadClass("com.example.calcite.Bar"),"eval"));

        Schema schema = new ReflectiveSchema(new JavaSchema());
        calciteConnection.getRootSchema().add("sc", schema);
        Thread.currentThread().setContextClassLoader(classLoader);
        calciteConnection.getProperties().setProperty("compilerFactory", CustomCompilerFactory.class.getCanonicalName());

        ResultSet resultSet = calciteConnection.createStatement().executeQuery("select foo(name) as name,bar(scores) as score from sc.emp");
        while (resultSet.next()) {
            System.out.printf("%s %s\n",resultSet.getString("name"),resultSet.getInt("score"));
           // System.out.printf("name: %s id: %s scores: %s%n", resultSet.getString("name"), resultSet.getLong("id"), resultSet.getArray("scores"));
        }
    }
}