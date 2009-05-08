begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

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
name|Exchange
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
name|ExpressionIllegalSyntaxException
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
name|impl
operator|.
name|ExpressionAdapter
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
name|spi
operator|.
name|Language
import|;
end_import

begin_comment
comment|/**  * A helper class for working with<a href="http://camel.apache.org/expression.html">expressions</a> based  * on files.  *<p/>  * This expression expects the headers from the {@link org.apache.camel.language.simple.FileLanguage} on the<b>IN</b> message.  *  * @see org.apache.camel.language.simple.FileLanguage  */
end_comment

begin_class
DECL|class|FileExpressionBuilder
specifier|public
specifier|final
class|class
name|FileExpressionBuilder
block|{
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
name|Expression
name|fileNameExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
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
literal|"file:name"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileOnlyNameExpression ()
specifier|public
specifier|static
name|Expression
name|fileOnlyNameExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|String
operator|.
name|class
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
literal|"file:onlyname"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileNameNoExtensionExpression ()
specifier|public
specifier|static
name|Expression
name|fileNameNoExtensionExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|int
name|pos
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
else|else
block|{
comment|// name does not have extension
return|return
name|name
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
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
literal|"file:name.noext"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileOnlyNameNoExtensionExpression ()
specifier|public
specifier|static
name|Expression
name|fileOnlyNameNoExtensionExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|int
name|pos
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
else|else
block|{
comment|// name does not have extension
return|return
name|name
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
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
literal|"file:onlyname.noext"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileExtensionExpression ()
specifier|public
specifier|static
name|Expression
name|fileExtensionExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
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
literal|"file:ext"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileParentExpression ()
specifier|public
specifier|static
name|Expression
name|fileParentExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileParent"
argument_list|,
name|String
operator|.
name|class
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
literal|"file:parent"
return|;
block|}
block|}
return|;
block|}
DECL|method|filePathExpression ()
specifier|public
specifier|static
name|Expression
name|filePathExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFilePath"
argument_list|,
name|String
operator|.
name|class
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
literal|"file:path"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileAbsolutePathExpression ()
specifier|public
specifier|static
name|Expression
name|fileAbsolutePathExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileAbsolutePath"
argument_list|,
name|String
operator|.
name|class
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
literal|"file:absolute.path"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileAbsoluteExpression ()
specifier|public
specifier|static
name|Expression
name|fileAbsoluteExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileAbsolute"
argument_list|,
name|Boolean
operator|.
name|class
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
literal|"file:absolute"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileSizeExpression ()
specifier|public
specifier|static
name|Expression
name|fileSizeExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileLength"
argument_list|,
name|Long
operator|.
name|class
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
literal|"file:length"
return|;
block|}
block|}
return|;
block|}
DECL|method|fileLastModifiedExpression ()
specifier|public
specifier|static
name|Expression
name|fileLastModifiedExpression
parameter_list|()
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileLastModified"
argument_list|,
name|Date
operator|.
name|class
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
literal|"file:modified"
return|;
block|}
block|}
return|;
block|}
DECL|method|dateExpression (final String command, final String pattern)
specifier|public
specifier|static
name|Expression
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
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
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
name|Date
name|date
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileLastModified"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
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
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|// must call evaluate to return the nested language evaluate when evaluating
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
argument_list|,
name|Object
operator|.
name|class
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
DECL|method|simpleExpression (final String expression)
specifier|public
specifier|static
name|Expression
name|simpleExpression
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// must call evaluate to return the nested language evaluate when evaluating
comment|// stacked expressions
name|Language
name|simple
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
return|return
name|simple
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
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
name|expression
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

