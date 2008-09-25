begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|Expression
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
name|RuntimeCamelException
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
name|builder
operator|.
name|ExpressionBuilder
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
name|component
operator|.
name|file
operator|.
name|FileExchange
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
name|language
operator|.
name|bean
operator|.
name|BeanLanguage
import|;
end_import

begin_comment
comment|/**  * A helper class for working with<a href="http://activemq.apache.org/camel/expression.html">expressions</a> based  * on FileExchange.  */
end_comment

begin_class
DECL|class|FileExpressionBuilder
specifier|public
specifier|final
class|class
name|FileExpressionBuilder
block|{
comment|// TODO: All the file stuff should just be added as
DECL|method|FileExpressionBuilder ()
specifier|private
name|FileExpressionBuilder
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|fileNameExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|fileNameExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:name"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileNameNoExtensionExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|fileNameNoExtensionExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|name
init|=
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:name.noext"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileParentExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|fileParentExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:parent"
return|;
block|}
block|}
return|;
block|}
DECL|method|filePathExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|filePathExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:path"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileAbsoluteExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|fileAbsoluteExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:absolute"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileCanoicalPathExpression ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|fileCanoicalPathExpression
parameter_list|()
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Could not get the canonical path for file: "
operator|+
name|exchange
operator|.
name|getFile
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file:canonical.path"
return|;
block|}
block|}
return|;
block|}
DECL|method|dateExpression (final String command, final String pattern)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|dateExpression
parameter_list|(
specifier|final
name|String
name|command
parameter_list|,
specifier|final
name|String
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|command
argument_list|)
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Date
name|date
init|=
operator|new
name|Date
argument_list|(
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|lastModified
argument_list|()
argument_list|)
decl_stmt|;
name|SimpleDateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
return|return
name|df
operator|.
name|format
argument_list|(
name|date
argument_list|)
return|;
block|}
comment|// must call evalute to return the nested langauge evaluate when evaluating
comment|// stacked expressions
return|return
name|ExpressionBuilder
operator|.
name|dateExpression
argument_list|(
name|command
argument_list|,
name|pattern
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"date("
operator|+
name|command
operator|+
literal|":"
operator|+
name|pattern
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
DECL|method|simpleExpression (final String simple)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|simpleExpression
parameter_list|(
specifier|final
name|String
name|simple
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
comment|// must call evalute to return the nested langauge evaluate when evaluating
comment|// stacked expressions
return|return
name|SimpleLanguage
operator|.
name|simple
argument_list|(
name|simple
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"simple("
operator|+
name|simple
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
DECL|method|beanExpression (final String bean)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|FileExchange
parameter_list|>
name|Expression
argument_list|<
name|E
argument_list|>
name|beanExpression
parameter_list|(
specifier|final
name|String
name|bean
parameter_list|)
block|{
return|return
operator|new
name|Expression
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
comment|// must call evalute to return the nested langauge evaluate when evaluating
comment|// stacked expressions
return|return
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|bean
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"bean("
operator|+
name|bean
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

