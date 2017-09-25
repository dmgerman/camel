begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|util
package|;
end_package

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
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|itest
operator|.
name|springboot
operator|.
name|util
operator|.
name|LocationUtils
operator|.
name|camelRoot
import|;
end_import

begin_comment
comment|/**  * Resolves the currently used version of a library. Useful to run unit tests directly from the IDE, without passing additional parameters.  * It resolves properties present in spring-boot and camel parent.  */
end_comment

begin_class
DECL|class|DependencyResolver
specifier|public
specifier|final
class|class
name|DependencyResolver
block|{
DECL|field|factory
specifier|private
specifier|static
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
DECL|field|xPathfactory
specifier|private
specifier|static
name|XPathFactory
name|xPathfactory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
DECL|method|DependencyResolver ()
specifier|private
name|DependencyResolver
parameter_list|()
block|{     }
comment|/**      * Retrieves a list of dependencies of the given scope      */
DECL|method|getDependencies (String pom, String scope)
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getDependencies
parameter_list|(
name|String
name|pom
parameter_list|,
name|String
name|scope
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|expression
init|=
literal|"/project/dependencies/dependency[scope='"
operator|+
name|scope
operator|+
literal|"']"
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|builder
operator|.
name|parse
argument_list|(
name|pom
argument_list|)
decl_stmt|;
name|XPath
name|xpath
init|=
name|xPathfactory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|XPathExpression
name|expr
init|=
name|xpath
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|dependencies
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|expr
operator|.
name|evaluate
argument_list|(
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
init|(
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
init|)
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|writer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|dependencies
operator|.
name|add
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dependencies
return|;
block|}
DECL|method|resolveModuleOrParentProperty (File modulePom, String property)
specifier|public
specifier|static
name|String
name|resolveModuleOrParentProperty
parameter_list|(
name|File
name|modulePom
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|property
operator|=
name|resolveProperty
argument_list|(
name|modulePom
argument_list|,
name|property
argument_list|,
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
operator|!
name|isResolved
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|property
operator|=
name|resolveSpringBootParentProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
operator|!
name|isResolved
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|property
operator|=
name|resolveCamelParentProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
operator|!
name|isResolved
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|property
operator|=
name|resolveCamelProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
return|return
name|property
return|;
block|}
DECL|method|resolveParentProperty (String property)
specifier|public
specifier|static
name|String
name|resolveParentProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
name|property
operator|=
name|resolveSpringBootParentProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
operator|&&
operator|!
name|isResolved
argument_list|(
name|property
argument_list|)
condition|)
block|{
name|property
operator|=
name|resolveCamelParentProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
return|return
name|property
return|;
block|}
DECL|method|resolveSpringBootParentProperty (String property)
specifier|public
specifier|static
name|String
name|resolveSpringBootParentProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|resolveProperty
argument_list|(
name|camelRoot
argument_list|(
literal|"platforms/spring-boot/spring-boot-dm/pom.xml"
argument_list|)
argument_list|,
name|property
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|resolveCamelParentProperty (String property)
specifier|public
specifier|static
name|String
name|resolveCamelParentProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|resolveProperty
argument_list|(
name|camelRoot
argument_list|(
literal|"parent/pom.xml"
argument_list|)
argument_list|,
name|property
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|resolveCamelProperty (String property)
specifier|public
specifier|static
name|String
name|resolveCamelProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|resolveProperty
argument_list|(
name|camelRoot
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|,
name|property
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|resolveProperty (File pom, String property, int depth)
specifier|private
specifier|static
name|String
name|resolveProperty
parameter_list|(
name|File
name|pom
parameter_list|,
name|String
name|property
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
try|try
block|{
name|property
operator|=
name|property
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|property
operator|.
name|startsWith
argument_list|(
literal|"${"
argument_list|)
operator|||
operator|!
name|property
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong property reference: "
operator|+
name|property
argument_list|)
throw|;
block|}
name|String
name|res
decl_stmt|;
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
literal|"${project.version}"
argument_list|)
condition|)
block|{
name|res
operator|=
name|getParentVersion
argument_list|(
name|pom
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|p
init|=
name|property
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|p
operator|=
name|p
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|res
operator|=
name|getPropertyFromPom
argument_list|(
name|pom
argument_list|,
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
return|return
name|property
return|;
block|}
block|}
if|if
condition|(
name|res
operator|!=
literal|null
operator|&&
operator|!
name|isResolved
argument_list|(
name|res
argument_list|)
operator|&&
name|depth
operator|<
literal|5
condition|)
block|{
name|res
operator|=
name|resolveProperty
argument_list|(
name|pom
argument_list|,
name|res
argument_list|,
name|depth
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
DECL|method|getPropertyFromPom (File pom, String property)
specifier|private
specifier|static
name|String
name|getPropertyFromPom
parameter_list|(
name|File
name|pom
parameter_list|,
name|String
name|property
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|xpath
argument_list|(
name|pom
argument_list|,
literal|"/project/properties/"
operator|+
name|property
operator|+
literal|"/text()"
argument_list|)
return|;
block|}
DECL|method|getParentVersion (File pom)
specifier|private
specifier|static
name|String
name|getParentVersion
parameter_list|(
name|File
name|pom
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|xpath
argument_list|(
name|pom
argument_list|,
literal|"/project/parent/version/text()"
argument_list|)
return|;
block|}
DECL|method|xpath (File pom, String expression)
specifier|private
specifier|static
name|String
name|xpath
parameter_list|(
name|File
name|pom
parameter_list|,
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|DocumentBuilder
name|builder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|builder
operator|.
name|parse
argument_list|(
name|pom
argument_list|)
decl_stmt|;
name|XPath
name|xpath
init|=
name|xPathfactory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|XPathExpression
name|expr
init|=
name|xpath
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|String
name|res
init|=
name|expr
operator|.
name|evaluate
argument_list|(
name|doc
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|!=
literal|null
operator|&&
name|res
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|res
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
DECL|method|isResolved (String value)
specifier|private
specifier|static
name|boolean
name|isResolved
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|startsWith
argument_list|(
literal|"$"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

