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
name|annotation
operator|.
name|processing
operator|.
name|RoundEnvironment
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
name|JavaFileObject
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

begin_class
DECL|class|PropertyPlaceholderGenerator
specifier|public
class|class
name|PropertyPlaceholderGenerator
block|{
DECL|method|generatePropertyPlaceholderProviderSource (ProcessingEnvironment processingEnv, TypeElement parent, String def, String fqnDef, String cn, String fqn, Set<CoreEipAnnotationProcessorHelper.EipOption> options)
specifier|public
specifier|static
name|void
name|generatePropertyPlaceholderProviderSource
parameter_list|(
name|ProcessingEnvironment
name|processingEnv
parameter_list|,
name|TypeElement
name|parent
parameter_list|,
name|String
name|def
parameter_list|,
name|String
name|fqnDef
parameter_list|,
name|String
name|cn
parameter_list|,
name|String
name|fqn
parameter_list|,
name|Set
argument_list|<
name|CoreEipAnnotationProcessorHelper
operator|.
name|EipOption
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
literal|"package org.apache.camel.model.placeholder;\n"
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
literal|"import java.util.function.Consumer;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import java.util.function.Supplier;\n"
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
literal|"import "
operator|+
name|fqnDef
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import org.apache.camel.spi.PropertyPlaceholderConfigurer;\n"
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
literal|" implements PropertyPlaceholderConfigurer {\n"
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
literal|"    private final Map<String, Supplier<String>> readPlaceholders = new HashMap<>();\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    private final Map<String, Consumer<String>> writePlaceholders = new HashMap<>();\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
comment|// add constructor
name|w
operator|.
name|write
argument_list|(
literal|"    public "
operator|+
name|cn
operator|+
literal|"(Object obj) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        "
operator|+
name|def
operator|+
literal|" definition = ("
operator|+
name|def
operator|+
literal|") obj;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
comment|// only include string types as they are the only ones we can use for property placeholders
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|CoreEipAnnotationProcessorHelper
operator|.
name|EipOption
name|option
range|:
name|options
control|)
block|{
if|if
condition|(
literal|"java.lang.String"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|String
name|getOrSet
init|=
name|sanitizePropertyPlaceholderOptionName
argument_list|(
name|def
argument_list|,
name|option
argument_list|)
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
name|w
operator|.
name|write
argument_list|(
literal|"        readPlaceholders.put(\""
operator|+
name|option
operator|.
name|getName
argument_list|()
operator|+
literal|"\", definition::get"
operator|+
name|getOrSet
operator|+
literal|");\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        writePlaceholders.put(\""
operator|+
name|option
operator|.
name|getName
argument_list|()
operator|+
literal|"\", definition::set"
operator|+
name|getOrSet
operator|+
literal|");\n"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|w
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
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
literal|"    @Override\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    public Map<String, Supplier<String>> getReadPropertyPlaceholderOptions(CamelContext camelContext) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        return readPlaceholders;\n"
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
literal|"    @Override\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    public Map<String, Consumer<String>> getWritePropertyPlaceholderOptions(CamelContext camelContext) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        return writePlaceholders;\n"
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
DECL|method|generatePropertyPlaceholderDefinitionsHelper (ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, Set<String> propertyPlaceholderDefinitions)
specifier|public
specifier|static
name|void
name|generatePropertyPlaceholderDefinitionsHelper
parameter_list|(
name|ProcessingEnvironment
name|processingEnv
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|propertyPlaceholderDefinitions
parameter_list|)
block|{
name|String
name|fqn
init|=
literal|"org.apache.camel.model.placeholder.DefinitionPropertiesPlaceholderProviderHelper"
decl_stmt|;
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
literal|"/* Generated by camel-apt */\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"package org.apache.camel.model.placeholder;\n"
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
literal|"import java.util.Optional;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import java.util.function.Function;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"import java.util.function.Supplier;\n"
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
literal|"import org.apache.camel.spi.PropertyPlaceholderConfigurer;\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|def
range|:
name|propertyPlaceholderDefinitions
control|)
block|{
name|w
operator|.
name|write
argument_list|(
literal|"import "
operator|+
name|def
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
block|}
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
literal|"public class DefinitionPropertiesPlaceholderProviderHelper {\n"
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
literal|"    private static final Map<Class, Function<Object, PropertyPlaceholderConfigurer>> MAP;\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"    static {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        Map<Class, Function<Object, PropertyPlaceholderConfigurer>> map = new HashMap<>("
operator|+
name|propertyPlaceholderDefinitions
operator|.
name|size
argument_list|()
operator|+
literal|");\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|def
range|:
name|propertyPlaceholderDefinitions
control|)
block|{
name|String
name|cn
init|=
name|def
operator|.
name|substring
argument_list|(
name|def
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        map.put("
operator|+
name|cn
operator|+
literal|".class, "
operator|+
name|cn
operator|+
literal|"PropertyPlaceholderProvider::new);\n"
argument_list|)
expr_stmt|;
block|}
name|w
operator|.
name|write
argument_list|(
literal|"        MAP = map;\n"
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
literal|"    public static Optional<PropertyPlaceholderConfigurer> provider(Object definition) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        Function<Object, PropertyPlaceholderConfigurer> func = MAP.get(definition.getClass());\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"        if (func != null) {\n"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"            return Optional.of(func.apply(definition));\n"
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
literal|"        return Optional.empty();\n"
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
comment|/**      * Some models have different setter/getter names vs the xml name (eg as defined in @XmlAttribute).      * So we need to correct this using this method.      */
DECL|method|sanitizePropertyPlaceholderOptionName (String def, CoreEipAnnotationProcessorHelper.EipOption option)
specifier|private
specifier|static
name|String
name|sanitizePropertyPlaceholderOptionName
parameter_list|(
name|String
name|def
parameter_list|,
name|CoreEipAnnotationProcessorHelper
operator|.
name|EipOption
name|option
parameter_list|)
block|{
if|if
condition|(
literal|"SimpleExpression"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
operator|||
literal|"JsonPathExpression"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"resultType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"resultTypeName"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"EnrichDefinition"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
operator|||
literal|"PollEnrichDefinition"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
operator|||
literal|"ClaimCheckDefinition"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"strategyRef"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"aggregationStrategyRef"
return|;
block|}
elseif|else
if|if
condition|(
literal|"strategyMethodName"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"aggregationStrategyMethodName"
return|;
block|}
elseif|else
if|if
condition|(
literal|"strategyMethodAllowNull"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"aggregationStrategyMethodAllowNull"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"MethodCallExpression"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"beanType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"beanTypeName"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"XPathExpression"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"documentType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"documentTypeName"
return|;
block|}
elseif|else
if|if
condition|(
literal|"resultType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"resultTypeName"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"WireTapDefinition"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"processorRef"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"newExchangeProcessorRef"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"TidyMarkupDataFormat"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"dataObjectType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"dataObjectTypeName"
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"BindyDataFormat"
operator|.
name|equals
argument_list|(
name|def
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"classType"
operator|.
name|equals
argument_list|(
name|option
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"classTypeAsString"
return|;
block|}
block|}
return|return
name|option
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

