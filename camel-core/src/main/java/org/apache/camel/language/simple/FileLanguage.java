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
name|builder
operator|.
name|FileExpressionBuilder
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
name|IllegalSyntaxException
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * File language is an extension to Simple language to add file specific expressions.  *  * Examples of supported file expressions are:  *<ul>  *<li><tt>file:name</tt> to access the file name (is relative, see note below))</li>  *<li><tt>file:name.noext</tt> to access the file name with no extension</li>  *<li><tt>file:ext</tt> to access the file extension</li>  *<li><tt>file:onlyname</tt> to access the file name (no paths)</li>  *<li><tt>file:onlyname.noext</tt> to access the file name (no paths) with no extension</li>  *<li><tt>file:parent</tt> to access the parent file name</li>  *<li><tt>file:path</tt> to access the file path name</li>  *<li><tt>file:absolute</tt> is the file regarded as absolute or relative</li>  *<li><tt>file:absolute.path</tt> to access the absolute file path name</li>  *<li><tt>file:length</tt> to access the file length as a Long type</li>  *<li><tt>file:modified</tt> to access the file last modified as a Date type</li>  *<li><tt>date:&lt;command&gt;:&lt;pattern&gt;</tt> for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Additional Supported commands are:<tt>file</tt> for the last modified timestamp of the file.  *     All the commands from {@link SimpleLanguage} is also avaiable.  *</li>  *</ul>  * The<b>relative</b> file is the filename with the starting directory clipped, as opposed to<b>path</b> that will  * return the full path including the starting directory.  *<br/>  * The<b>only</b> file is the filename only with all paths clipped.  *<br/>   * All the simple expression is also available so you can eg use<tt>${in.header.foo}</tt> to access the foo header.  *  * @see org.apache.camel.language.simple.SimpleLanguage  * @see org.apache.camel.language.bean.BeanLanguage  */
end_comment

begin_class
DECL|class|FileLanguage
specifier|public
class|class
name|FileLanguage
extends|extends
name|SimpleLanguageSupport
block|{
DECL|method|file (String expression)
specifier|public
specifier|static
name|Expression
name|file
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|FileLanguage
name|language
init|=
operator|new
name|FileLanguage
argument_list|()
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createSimpleExpression (String expression)
specifier|protected
name|Expression
name|createSimpleExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
comment|// file: prefix
name|String
name|remainder
init|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"file:"
argument_list|,
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name.noext"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileOnlyNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname.noext"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileOnlyNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"ext"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"parent"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileParentExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"path"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|filePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileAbsoluteExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute.path"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileAbsolutePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"length"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileSizeExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"modified"
argument_list|)
condition|)
block|{
return|return
name|FileExpressionBuilder
operator|.
name|fileLastModifiedExpression
argument_list|()
return|;
block|}
block|}
comment|// date: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"date:"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalSyntaxException
argument_list|(
name|this
argument_list|,
name|expression
operator|+
literal|" ${date:command:pattern} is the correct syntax."
argument_list|)
throw|;
block|}
name|String
name|command
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|pattern
init|=
name|parts
index|[
literal|1
index|]
decl_stmt|;
return|return
name|FileExpressionBuilder
operator|.
name|dateExpression
argument_list|(
name|command
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|// fallback to simple language if not file specific
return|return
name|FileExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

