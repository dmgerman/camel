begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|ProcessingEnvironment
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|TypeElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|Diagnostic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|FileObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|JavaFileObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|StandardLocation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|model
operator|.
name|ComponentOption
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|model
operator|.
name|EndpointOption
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|AnnotationProcessorHelper
operator|.
name|dumpExceptionToErrorFile
import|;
end_import

begin_comment
comment|// TODO: ComponentPropertyConfigurerGenerator and EndpointPropertyConfigurerGenerator can be merged to one
end_comment

begin_comment
comment|// TODO: Add support for ignore case
end_comment

begin_class
DECL|class|ComponentPropertyConfigurerGenerator
specifier|public
specifier|final
class|class
name|ComponentPropertyConfigurerGenerator
block|{
DECL|method|ComponentPropertyConfigurerGenerator ()
specifier|private
name|ComponentPropertyConfigurerGenerator
parameter_list|()
block|{     }
DECL|method|generateExtendConfigurer (ProcessingEnvironment processingEnv, TypeElement parent, String pn, String cn, String fqn)
specifier|public
specifier|static
name|void
name|generateExtendConfigurer
parameter_list|(
name|ProcessingEnvironment
name|processingEnv
parameter_list|,
name|TypeElement
name|parent
parameter_list|,
name|String
name|pn
parameter_list|,
name|String
name|cn
parameter_list|,
name|String
name|fqn
parameter_list|)
block|{
name|Writer
name|w
init|=
literal|null
decl_stmt|;
try|try
block|{
name|JavaFileObject
name|src
init|=
name|processingEnv
operator|.
name|getFiler
argument_list|()
operator|.
name|createSourceFile
argument_list|(
name|fqn
argument_list|,
name|parent
argument_list|)
decl_stmt|;
name|w
operator|=
name|src
operator|.
name|openWriter
argument_list|()
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"/* Generated by org.apache.camel:apt */\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"package "
operator|+
name|pn
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import "
operator|+
name|parent
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"/**\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|" * Source code generated by org.apache.camel:apt\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|" */\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"public class "
operator|+
name|cn
operator|+
literal|" extends "
operator|+
name|parent
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|" {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"}\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|ERROR
argument_list|,
literal|"Unable to generate source code file: "
operator|+
name|fqn
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dumpExceptionToErrorFile
argument_list|(
literal|"camel-apt-error.log"
argument_list|,
literal|"Unable to generate source code file: "
operator|+
name|fqn
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|generatePropertyConfigurer (ProcessingEnvironment processingEnv, TypeElement parent, String pn, String cn, String fqn, String en, Set<ComponentOption> options)
specifier|public
specifier|static
name|void
name|generatePropertyConfigurer
parameter_list|(
name|ProcessingEnvironment
name|processingEnv
parameter_list|,
name|TypeElement
name|parent
parameter_list|,
name|String
name|pn
parameter_list|,
name|String
name|cn
parameter_list|,
name|String
name|fqn
parameter_list|,
name|String
name|en
parameter_list|,
name|Set
argument_list|<
name|ComponentOption
argument_list|>
name|options
parameter_list|)
block|{
name|Writer
name|w
init|=
literal|null
decl_stmt|;
try|try
block|{
name|JavaFileObject
name|src
init|=
name|processingEnv
operator|.
name|getFiler
argument_list|()
operator|.
name|createSourceFile
argument_list|(
name|fqn
argument_list|,
name|parent
argument_list|)
decl_stmt|;
name|w
operator|=
name|src
operator|.
name|openWriter
argument_list|()
expr_stmt|;
name|int
name|size
init|=
name|options
operator|.
name|size
argument_list|()
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"/* Generated by org.apache.camel:apt */\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"package "
operator|+
name|pn
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import java.util.HashMap;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import java.util.Map;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import org.apache.camel.CamelContext;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import org.apache.camel.spi.GeneratedPropertyConfigurer;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import org.apache.camel.support.component.PropertyConfigurerSupport;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"/**\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|" * Source code generated by org.apache.camel:apt\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|" */\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"@SuppressWarnings(\"unchecked\")\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"public class "
operator|+
name|cn
operator|+
literal|" extends PropertyConfigurerSupport implements GeneratedPropertyConfigurer {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    @Override\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    public boolean configure(CamelContext camelContext, Object component, String name, Object value, boolean ignoreCase) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        if (ignoreCase) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"            return doConfigureIgnoreCase(camelContext, component, name, value);\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        } else {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"            return doConfigure(camelContext, component, name, value);\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    private static boolean doConfigure(CamelContext camelContext, Object component, String name, Object value) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        switch (name) {\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|ComponentOption
name|option
range|:
name|options
control|)
block|{
name|String
name|getOrSet
init|=
name|option
operator|.
name|getName
argument_list|()
decl_stmt|;
name|getOrSet
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|getOrSet
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|getOrSet
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|setterLambda
init|=
name|setterLambda
argument_list|(
name|en
argument_list|,
name|getOrSet
argument_list|,
name|option
operator|.
name|getType
argument_list|()
argument_list|,
name|option
operator|.
name|getConfigurationField
argument_list|()
argument_list|)
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"        case \"%s\": %s; return true;\n"
argument_list|,
name|option
operator|.
name|getName
argument_list|()
argument_list|,
name|setterLambda
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|w
operator|.
name|write
argument_list|(
literal|"            default: return false;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    private static boolean doConfigureIgnoreCase(CamelContext camelContext, Object component, String name, Object value) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        switch (name.toLowerCase()) {\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|ComponentOption
name|option
range|:
name|options
control|)
block|{
name|String
name|getOrSet
init|=
name|option
operator|.
name|getName
argument_list|()
decl_stmt|;
name|getOrSet
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|getOrSet
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|getOrSet
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|setterLambda
init|=
name|setterLambda
argument_list|(
name|en
argument_list|,
name|getOrSet
argument_list|,
name|option
operator|.
name|getType
argument_list|()
argument_list|,
name|option
operator|.
name|getConfigurationField
argument_list|()
argument_list|)
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"        case \"%s\": %s; return true;\n"
argument_list|,
name|option
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|setterLambda
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|w
operator|.
name|write
argument_list|(
literal|"            default: return false;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    }\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"}\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|ERROR
argument_list|,
literal|"Unable to generate source code file: "
operator|+
name|fqn
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dumpExceptionToErrorFile
argument_list|(
literal|"camel-apt-error.log"
argument_list|,
literal|"Unable to generate source code file: "
operator|+
name|fqn
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setterLambda (String en, String getOrSet, String type, String configurationField)
specifier|private
specifier|static
name|String
name|setterLambda
parameter_list|(
name|String
name|en
parameter_list|,
name|String
name|getOrSet
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|configurationField
parameter_list|)
block|{
comment|// type may contain generics so remove those
if|if
condition|(
name|type
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurationField
operator|!=
literal|null
condition|)
block|{
name|getOrSet
operator|=
literal|"get"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|configurationField
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|configurationField
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|+
literal|"().set"
operator|+
name|getOrSet
expr_stmt|;
block|}
else|else
block|{
name|getOrSet
operator|=
literal|"set"
operator|+
name|getOrSet
expr_stmt|;
block|}
comment|// ((LogEndpoint) endpoint).setGroupSize(property(camelContext, java.lang.Integer.class, value))
return|return
name|String
operator|.
name|format
argument_list|(
literal|"((%s) component).%s(property(camelContext, %s.class, value))"
argument_list|,
name|en
argument_list|,
name|getOrSet
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|generateMetaInfConfigurer (ProcessingEnvironment processingEnv, String name, String fqn)
specifier|public
specifier|static
name|void
name|generateMetaInfConfigurer
parameter_list|(
name|ProcessingEnvironment
name|processingEnv
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|fqn
parameter_list|)
block|{
try|try
block|{
name|FileObject
name|resource
init|=
name|processingEnv
operator|.
name|getFiler
argument_list|()
operator|.
name|createResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
literal|""
argument_list|,
literal|"META-INF/services/org/apache/camel/configurer/"
operator|+
name|name
argument_list|)
decl_stmt|;
try|try
init|(
name|Writer
name|w
init|=
name|resource
operator|.
name|openWriter
argument_list|()
init|)
block|{
name|w
operator|.
name|append
argument_list|(
literal|"# Generated by camel annotation processor\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|append
argument_list|(
literal|"class="
argument_list|)
operator|.
name|append
argument_list|(
name|fqn
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

