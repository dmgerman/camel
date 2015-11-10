begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.springboot.commands.crsh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|springboot
operator|.
name|commands
operator|.
name|crsh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Route
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
name|commands
operator|.
name|AbstractCamelCommand
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
name|commands
operator|.
name|AbstractContextCommand
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
name|commands
operator|.
name|AbstractRouteCommand
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
name|commands
operator|.
name|LocalCamelController
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
name|commands
operator|.
name|StringEscape
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CamelCommandsFacade
specifier|public
class|class
name|CamelCommandsFacade
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelController
specifier|private
name|LocalCamelController
name|camelController
decl_stmt|;
DECL|field|stringEscape
specifier|private
name|StringEscape
name|stringEscape
init|=
operator|new
name|NoopStringEscape
argument_list|()
decl_stmt|;
DECL|method|CamelCommandsFacade (LocalCamelController controller)
name|CamelCommandsFacade
parameter_list|(
name|LocalCamelController
name|controller
parameter_list|)
block|{
name|this
operator|.
name|camelController
operator|=
name|controller
expr_stmt|;
block|}
DECL|method|getCamelController ()
name|LocalCamelController
name|getCamelController
parameter_list|()
block|{
return|return
name|this
operator|.
name|camelController
return|;
block|}
DECL|method|runCommand (Class<T> clazz, Object... commandArgs)
specifier|public
parameter_list|<
name|T
extends|extends
name|AbstractCamelCommand
parameter_list|>
name|String
name|runCommand
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Object
modifier|...
name|commandArgs
parameter_list|)
throws|throws
name|Exception
block|{
name|OutputBuffer
name|buffer
init|=
operator|new
name|OutputBuffer
argument_list|()
decl_stmt|;
name|PrintStream
name|ops
init|=
name|buffer
operator|.
name|getPrintStream
argument_list|()
decl_stmt|;
comment|// Trying to infer the camel context if not given
comment|// The order of the varargs for Route Command
comment|// [0] - route id
comment|// [1] - camel context
if|if
condition|(
name|AbstractRouteCommand
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|&&
literal|null
operator|==
name|commandArgs
index|[
literal|1
index|]
condition|)
block|{
name|commandArgs
index|[
literal|1
index|]
operator|=
name|getCamelContextForRoute
argument_list|(
operator|(
name|String
operator|)
name|commandArgs
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|ops
operator|.
name|println
argument_list|(
literal|"Automatically inferred context name : "
operator|+
name|commandArgs
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
comment|// The order of the varargs for Context Command
comment|// [0] - camel context
if|if
condition|(
name|AbstractContextCommand
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
operator|&&
literal|null
operator|==
name|commandArgs
index|[
literal|0
index|]
condition|)
block|{
name|commandArgs
index|[
literal|0
index|]
operator|=
name|getFirstCamelContextName
argument_list|()
expr_stmt|;
name|ops
operator|.
name|println
argument_list|(
literal|"Context name is not provided. Using the first : "
operator|+
name|commandArgs
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|// Finding the right constructor
name|Class
index|[]
name|types
init|=
operator|new
name|Class
index|[
name|commandArgs
operator|.
name|length
index|]
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
name|commandArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|commandArgs
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
expr_stmt|;
comment|// Commands require primitives
if|if
condition|(
name|types
index|[
name|i
index|]
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|boolean
operator|.
name|class
expr_stmt|;
block|}
if|if
condition|(
name|types
index|[
name|i
index|]
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|int
operator|.
name|class
expr_stmt|;
block|}
block|}
comment|// Instantiating an object
name|Constructor
argument_list|<
name|T
argument_list|>
name|constructor
init|=
name|clazz
operator|.
name|getConstructor
argument_list|(
name|types
argument_list|)
decl_stmt|;
name|T
name|command
init|=
name|constructor
operator|.
name|newInstance
argument_list|(
name|commandArgs
argument_list|)
decl_stmt|;
comment|// Some commands require StringEscape property to be set
try|try
block|{
name|Method
name|m
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"setStringEscape"
argument_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|StringEscape
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|command
argument_list|,
name|stringEscape
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
comment|// Executing
name|command
operator|.
name|execute
argument_list|(
name|camelController
argument_list|,
name|ops
argument_list|,
name|ops
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getCamelContextForRoute (String routeId)
specifier|private
name|String
name|getCamelContextForRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|contextNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelController
operator|.
name|getLocalCamelContexts
argument_list|()
control|)
block|{
for|for
control|(
name|Route
name|route
range|:
name|camelContext
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|routeId
operator|.
name|equals
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|contextNames
operator|.
name|add
argument_list|(
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|contextNames
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|StringBuffer
name|error
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|error
operator|.
name|append
argument_list|(
literal|"Cannot infer Camel Context. Please provide manually."
argument_list|)
expr_stmt|;
if|if
condition|(
name|contextNames
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|" Contexts : "
operator|+
name|contextNames
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|org
operator|.
name|crsh
operator|.
name|cli
operator|.
name|impl
operator|.
name|SyntaxException
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|contextNames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|getFirstCamelContextName ()
specifier|private
name|String
name|getFirstCamelContextName
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
literal|null
operator|==
name|camelController
operator|.
name|getLocalCamelContexts
argument_list|()
operator|||
name|camelController
operator|.
name|getLocalCamelContexts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|org
operator|.
name|crsh
operator|.
name|cli
operator|.
name|impl
operator|.
name|SyntaxException
argument_list|(
literal|"No camel contexts available"
argument_list|)
throw|;
block|}
return|return
name|camelController
operator|.
name|getLocalCamelContexts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

