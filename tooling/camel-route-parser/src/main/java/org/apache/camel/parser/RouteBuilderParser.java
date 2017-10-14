begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|parser
operator|.
name|helper
operator|.
name|CamelJavaTreeParserHelper
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
name|parser
operator|.
name|helper
operator|.
name|CamelJavaParserHelper
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
name|parser
operator|.
name|model
operator|.
name|CamelEndpointDetails
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
name|parser
operator|.
name|model
operator|.
name|CamelNodeDetails
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
name|parser
operator|.
name|model
operator|.
name|CamelRouteDetails
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
name|parser
operator|.
name|model
operator|.
name|CamelSimpleExpressionDetails
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|_shade
operator|.
name|org
operator|.
name|eclipse
operator|.
name|jdt
operator|.
name|core
operator|.
name|dom
operator|.
name|ASTNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|_shade
operator|.
name|org
operator|.
name|eclipse
operator|.
name|jdt
operator|.
name|core
operator|.
name|dom
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|_shade
operator|.
name|org
operator|.
name|eclipse
operator|.
name|jdt
operator|.
name|core
operator|.
name|dom
operator|.
name|MemberValuePair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|_shade
operator|.
name|org
operator|.
name|eclipse
operator|.
name|jdt
operator|.
name|core
operator|.
name|dom
operator|.
name|NormalAnnotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|_shade
operator|.
name|org
operator|.
name|eclipse
operator|.
name|jdt
operator|.
name|core
operator|.
name|dom
operator|.
name|SingleMemberAnnotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|FieldSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaClassSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|MethodSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|util
operator|.
name|Strings
import|;
end_import

begin_comment
comment|/**  * A Camel RouteBuilder parser that parses Camel Java routes source code.  *<p/>  * This implementation is higher level details, and uses the lower level parser {@link CamelJavaParserHelper}.  */
end_comment

begin_class
DECL|class|RouteBuilderParser
specifier|public
specifier|final
class|class
name|RouteBuilderParser
block|{
DECL|method|RouteBuilderParser ()
specifier|private
name|RouteBuilderParser
parameter_list|()
block|{     }
comment|/**      * Parses the java source class and build a route model (tree) of the discovered routes in the java source class.      *      * @param clazz                   the java source class      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @return a list of route model (tree) of each discovered route      */
DECL|method|parseRouteBuilderTree (JavaClassSource clazz, String baseDir, String fullyQualifiedFileName, boolean includeInlinedRouteBuilders)
specifier|public
specifier|static
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|parseRouteBuilderTree
parameter_list|(
name|JavaClassSource
name|clazz
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|boolean
name|includeInlinedRouteBuilders
parameter_list|)
block|{
name|List
argument_list|<
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|method
init|=
name|CamelJavaParserHelper
operator|.
name|findConfigureMethod
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeInlinedRouteBuilders
condition|)
block|{
name|List
argument_list|<
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
argument_list|>
name|inlinedMethods
init|=
name|CamelJavaParserHelper
operator|.
name|findInlinedConfigureMethods
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|inlinedMethods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|methods
operator|.
name|addAll
argument_list|(
name|inlinedMethods
argument_list|)
expr_stmt|;
block|}
block|}
name|CamelJavaTreeParserHelper
name|parser
init|=
operator|new
name|CamelJavaTreeParserHelper
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|configureMethod
range|:
name|methods
control|)
block|{
comment|// there may be multiple route builder configure methods
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|details
init|=
name|parser
operator|.
name|parseCamelRouteTree
argument_list|(
name|clazz
argument_list|,
name|baseDir
argument_list|,
name|fullyQualifiedFileName
argument_list|,
name|configureMethod
argument_list|)
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|details
argument_list|)
expr_stmt|;
block|}
comment|// we end up parsing bottom->up so reverse list
name|Collections
operator|.
name|reverse
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * Parses the java source class to discover Camel endpoints.      *      * @param clazz                   the java source class      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @param endpoints               list to add discovered and parsed endpoints      */
DECL|method|parseRouteBuilderEndpoints (JavaClassSource clazz, String baseDir, String fullyQualifiedFileName, List<CamelEndpointDetails> endpoints)
specifier|public
specifier|static
name|void
name|parseRouteBuilderEndpoints
parameter_list|(
name|JavaClassSource
name|clazz
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelEndpointDetails
argument_list|>
name|endpoints
parameter_list|)
block|{
name|parseRouteBuilderEndpoints
argument_list|(
name|clazz
argument_list|,
name|baseDir
argument_list|,
name|fullyQualifiedFileName
argument_list|,
name|endpoints
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Parses the java source class to discover Camel endpoints.      *      * @param clazz                        the java source class      * @param baseDir                      the base of the source code      * @param fullyQualifiedFileName       the fully qualified source code file name      * @param endpoints                    list to add discovered and parsed endpoints      * @param unparsable                   list of unparsable nodes      * @param includeInlinedRouteBuilders  whether to include inlined route builders in the parsing      */
DECL|method|parseRouteBuilderEndpoints (JavaClassSource clazz, String baseDir, String fullyQualifiedFileName, List<CamelEndpointDetails> endpoints, List<String> unparsable, boolean includeInlinedRouteBuilders)
specifier|public
specifier|static
name|void
name|parseRouteBuilderEndpoints
parameter_list|(
name|JavaClassSource
name|clazz
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelEndpointDetails
argument_list|>
name|endpoints
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|unparsable
parameter_list|,
name|boolean
name|includeInlinedRouteBuilders
parameter_list|)
block|{
comment|// look for fields which are not used in the route
for|for
control|(
name|FieldSource
argument_list|<
name|JavaClassSource
argument_list|>
name|field
range|:
name|clazz
operator|.
name|getFields
argument_list|()
control|)
block|{
comment|// is the field annotated with a Camel endpoint
name|String
name|uri
init|=
literal|null
decl_stmt|;
name|Expression
name|exp
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Annotation
name|ann
range|:
name|field
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
name|boolean
name|valid
init|=
literal|"org.apache.camel.EndpointInject"
operator|.
name|equals
argument_list|(
name|ann
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
operator|||
literal|"org.apache.camel.cdi.Uri"
operator|.
name|equals
argument_list|(
name|ann
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|exp
operator|=
operator|(
name|Expression
operator|)
name|ann
operator|.
name|getInternal
argument_list|()
expr_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|SingleMemberAnnotation
condition|)
block|{
name|exp
operator|=
operator|(
operator|(
name|SingleMemberAnnotation
operator|)
name|exp
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exp
operator|instanceof
name|NormalAnnotation
condition|)
block|{
name|List
name|values
init|=
operator|(
operator|(
name|NormalAnnotation
operator|)
name|exp
operator|)
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|MemberValuePair
name|pair
init|=
operator|(
name|MemberValuePair
operator|)
name|value
decl_stmt|;
if|if
condition|(
literal|"uri"
operator|.
name|equals
argument_list|(
name|pair
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|exp
operator|=
name|pair
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
name|uri
operator|=
name|CamelJavaParserHelper
operator|.
name|getLiteralValue
argument_list|(
name|clazz
argument_list|,
literal|null
argument_list|,
name|exp
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we only want to add fields which are not used in the route
if|if
condition|(
operator|!
name|Strings
operator|.
name|isBlank
argument_list|(
name|uri
argument_list|)
operator|&&
name|findEndpointByUri
argument_list|(
name|endpoints
argument_list|,
name|uri
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// we only want the relative dir name from the
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|id
init|=
name|field
operator|.
name|getName
argument_list|()
decl_stmt|;
name|CamelEndpointDetails
name|detail
init|=
operator|new
name|CamelEndpointDetails
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointInstance
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointComponentName
argument_list|(
name|endpointComponentName
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
comment|// favor the position of the expression which had the actual uri
name|Object
name|internal
init|=
name|exp
operator|!=
literal|null
condition|?
name|exp
else|:
name|field
operator|.
name|getInternal
argument_list|()
decl_stmt|;
comment|// find position of field/expression
if|if
condition|(
name|internal
operator|instanceof
name|ASTNode
condition|)
block|{
name|int
name|pos
init|=
operator|(
operator|(
name|ASTNode
operator|)
name|internal
operator|)
operator|.
name|getStartPosition
argument_list|()
decl_stmt|;
name|int
name|line
init|=
name|findLineNumber
argument_list|(
name|fullyQualifiedFileName
argument_list|,
name|pos
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|>
operator|-
literal|1
condition|)
block|{
name|detail
operator|.
name|setLineNumber
argument_list|(
literal|""
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we do not know if this field is used as consumer or producer only, but we try
comment|// to find out by scanning the route in the configure method below
name|endpoints
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
comment|// find all the configure methods
name|List
argument_list|<
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|method
init|=
name|CamelJavaParserHelper
operator|.
name|findConfigureMethod
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeInlinedRouteBuilders
condition|)
block|{
name|List
argument_list|<
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
argument_list|>
name|inlinedMethods
init|=
name|CamelJavaParserHelper
operator|.
name|findInlinedConfigureMethods
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|inlinedMethods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|methods
operator|.
name|addAll
argument_list|(
name|inlinedMethods
argument_list|)
expr_stmt|;
block|}
block|}
comment|// look if any of these fields are used in the route only as consumer or producer, as then we can
comment|// determine this to ensure when we edit the endpoint we should only the options accordingly
for|for
control|(
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|configureMethod
range|:
name|methods
control|)
block|{
comment|// consumers only
name|List
argument_list|<
name|ParserResult
argument_list|>
name|uris
init|=
name|CamelJavaParserHelper
operator|.
name|parseCamelConsumerUris
argument_list|(
name|configureMethod
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|uris
control|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|isParsed
argument_list|()
condition|)
block|{
if|if
condition|(
name|unparsable
operator|!=
literal|null
condition|)
block|{
name|unparsable
operator|.
name|add
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|CamelEndpointDetails
name|detail
init|=
name|findEndpointByUri
argument_list|(
name|endpoints
argument_list|,
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|detail
operator|!=
literal|null
condition|)
block|{
comment|// its a consumer only
name|detail
operator|.
name|setConsumerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|detail
operator|=
operator|new
name|CamelEndpointDetails
argument_list|()
expr_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setMethodName
argument_list|(
name|configureMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointInstance
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointUri
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|line
init|=
name|findLineNumber
argument_list|(
name|fullyQualifiedFileName
argument_list|,
name|result
operator|.
name|getPosition
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|>
operator|-
literal|1
condition|)
block|{
name|detail
operator|.
name|setLineNumber
argument_list|(
literal|""
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setEndpointComponentName
argument_list|(
name|endpointComponentName
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setConsumerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setProducerOnly
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// producer only
name|uris
operator|=
name|CamelJavaParserHelper
operator|.
name|parseCamelProducerUris
argument_list|(
name|configureMethod
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|uris
control|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|isParsed
argument_list|()
condition|)
block|{
if|if
condition|(
name|unparsable
operator|!=
literal|null
condition|)
block|{
name|unparsable
operator|.
name|add
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|CamelEndpointDetails
name|detail
init|=
name|findEndpointByUri
argument_list|(
name|endpoints
argument_list|,
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|detail
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|detail
operator|.
name|isConsumerOnly
argument_list|()
condition|)
block|{
comment|// its both a consumer and producer
name|detail
operator|.
name|setConsumerOnly
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setProducerOnly
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// its a producer only
name|detail
operator|.
name|setProducerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// the same endpoint uri may be used in multiple places in the same route
comment|// so we should maybe add all of them
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|detail
operator|=
operator|new
name|CamelEndpointDetails
argument_list|()
expr_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setMethodName
argument_list|(
name|configureMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointInstance
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointUri
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|line
init|=
name|findLineNumber
argument_list|(
name|fullyQualifiedFileName
argument_list|,
name|result
operator|.
name|getPosition
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|>
operator|-
literal|1
condition|)
block|{
name|detail
operator|.
name|setLineNumber
argument_list|(
literal|""
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setEndpointComponentName
argument_list|(
name|endpointComponentName
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setConsumerOnly
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setProducerOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Parses the java source class to discover Camel simple expressions.      *      * @param clazz                   the java source class      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @param simpleExpressions       list to add discovered and parsed simple expressions      */
DECL|method|parseRouteBuilderSimpleExpressions (JavaClassSource clazz, String baseDir, String fullyQualifiedFileName, List<CamelSimpleExpressionDetails> simpleExpressions)
specifier|public
specifier|static
name|void
name|parseRouteBuilderSimpleExpressions
parameter_list|(
name|JavaClassSource
name|clazz
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelSimpleExpressionDetails
argument_list|>
name|simpleExpressions
parameter_list|)
block|{
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|method
init|=
name|CamelJavaParserHelper
operator|.
name|findConfigureMethod
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ParserResult
argument_list|>
name|expressions
init|=
name|CamelJavaParserHelper
operator|.
name|parseCamelSimpleExpressions
argument_list|(
name|method
argument_list|)
decl_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|expressions
control|)
block|{
if|if
condition|(
name|result
operator|.
name|isParsed
argument_list|()
condition|)
block|{
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|CamelSimpleExpressionDetails
name|detail
init|=
operator|new
name|CamelSimpleExpressionDetails
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setMethodName
argument_list|(
literal|"configure"
argument_list|)
expr_stmt|;
name|int
name|line
init|=
name|findLineNumber
argument_list|(
name|fullyQualifiedFileName
argument_list|,
name|result
operator|.
name|getPosition
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|>
operator|-
literal|1
condition|)
block|{
name|detail
operator|.
name|setLineNumber
argument_list|(
literal|""
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setSimple
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|predicate
init|=
name|result
operator|.
name|getPredicate
argument_list|()
operator|!=
literal|null
condition|?
name|result
operator|.
name|getPredicate
argument_list|()
else|:
literal|false
decl_stmt|;
name|boolean
name|expression
init|=
operator|!
name|predicate
decl_stmt|;
name|detail
operator|.
name|setPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|simpleExpressions
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Parses the java source class to discover Camel routes with id's assigned.      *      * @param clazz                   the java source class      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @param routes                  list to add discovered and parsed routes      */
DECL|method|parseRouteBuilderRouteIds (JavaClassSource clazz, String baseDir, String fullyQualifiedFileName, List<CamelRouteDetails> routes)
specifier|public
specifier|static
name|void
name|parseRouteBuilderRouteIds
parameter_list|(
name|JavaClassSource
name|clazz
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelRouteDetails
argument_list|>
name|routes
parameter_list|)
block|{
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|method
init|=
name|CamelJavaParserHelper
operator|.
name|findConfigureMethod
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ParserResult
argument_list|>
name|expressions
init|=
name|CamelJavaParserHelper
operator|.
name|parseCamelRouteIds
argument_list|(
name|method
argument_list|)
decl_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|expressions
control|)
block|{
comment|// route ids is assigned in java dsl using the routeId method
if|if
condition|(
name|result
operator|.
name|isParsed
argument_list|()
condition|)
block|{
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|CamelRouteDetails
name|detail
init|=
operator|new
name|CamelRouteDetails
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setMethodName
argument_list|(
literal|"configure"
argument_list|)
expr_stmt|;
name|int
name|line
init|=
name|findLineNumber
argument_list|(
name|fullyQualifiedFileName
argument_list|,
name|result
operator|.
name|getPosition
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|>
operator|-
literal|1
condition|)
block|{
name|detail
operator|.
name|setLineNumber
argument_list|(
literal|""
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
name|detail
operator|.
name|setRouteId
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|routes
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|findEndpointByUri (List<CamelEndpointDetails> endpoints, String uri)
specifier|private
specifier|static
name|CamelEndpointDetails
name|findEndpointByUri
parameter_list|(
name|List
argument_list|<
name|CamelEndpointDetails
argument_list|>
name|endpoints
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|CamelEndpointDetails
name|detail
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|detail
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|detail
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findLineNumber (String fullyQualifiedFileName, int position)
specifier|private
specifier|static
name|int
name|findLineNumber
parameter_list|(
name|String
name|fullyQualifiedFileName
parameter_list|,
name|int
name|position
parameter_list|)
block|{
name|int
name|lines
init|=
literal|0
decl_stmt|;
try|try
block|{
name|int
name|current
init|=
literal|0
decl_stmt|;
try|try
init|(
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
operator|new
name|File
argument_list|(
name|fullyQualifiedFileName
argument_list|)
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|lines
operator|++
expr_stmt|;
name|current
operator|+=
name|line
operator|.
name|length
argument_list|()
operator|+
literal|1
expr_stmt|;
comment|// add 1 for line feed
if|if
condition|(
name|current
operator|>=
name|position
condition|)
block|{
return|return
name|lines
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
return|return
operator|-
literal|1
return|;
block|}
return|return
name|lines
return|;
block|}
DECL|method|endpointComponentName (String uri)
specifier|private
specifier|static
name|String
name|endpointComponentName
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|int
name|idx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

