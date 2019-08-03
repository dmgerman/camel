begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.tokenizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|tokenizer
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
name|IsSingleton
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
name|Predicate
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ExpressionToPredicateAdapter
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
name|support
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A language for tokenizer expressions.  *<p/>  * This tokenizer language can operator in the following modes:  *<ul>  *<li>default - using a single tokenizer</li>  *<li>pair - using both start and end tokens</li>  *<li>xml - using both start and end tokens in XML mode, support inheriting namespaces</li>  *</ul>  * The default mode supports the<tt>headerName</tt> and<tt>regex</tt> options.  * Where as the pair mode only supports<tt>token</tt> and<tt>endToken</tt>.  * And the<tt>xml</tt> mode supports the<tt>inheritNamespaceTagName</tt> option.  */
end_comment

begin_class
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|annotations
operator|.
name|Language
argument_list|(
literal|"tokenize"
argument_list|)
DECL|class|TokenizeLanguage
specifier|public
class|class
name|TokenizeLanguage
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
DECL|field|endToken
specifier|private
name|String
name|endToken
decl_stmt|;
DECL|field|inheritNamespaceTagName
specifier|private
name|String
name|inheritNamespaceTagName
decl_stmt|;
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|field|regex
specifier|private
name|boolean
name|regex
decl_stmt|;
DECL|field|xml
specifier|private
name|boolean
name|xml
decl_stmt|;
DECL|field|includeTokens
specifier|private
name|boolean
name|includeTokens
decl_stmt|;
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|field|groupDelimiter
specifier|private
name|String
name|groupDelimiter
decl_stmt|;
DECL|field|skipFirst
specifier|private
name|boolean
name|skipFirst
decl_stmt|;
DECL|method|tokenize (String token)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|)
block|{
return|return
name|tokenize
argument_list|(
name|token
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|tokenize (String token, boolean regex)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|token
parameter_list|,
name|boolean
name|regex
parameter_list|)
block|{
name|TokenizeLanguage
name|language
init|=
operator|new
name|TokenizeLanguage
argument_list|()
decl_stmt|;
name|language
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|language
operator|.
name|setRegex
argument_list|(
name|regex
argument_list|)
expr_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|tokenize (String headerName, String token)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|token
parameter_list|)
block|{
return|return
name|tokenize
argument_list|(
name|headerName
argument_list|,
name|token
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|tokenize (String headerName, String token, boolean regex)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|token
parameter_list|,
name|boolean
name|regex
parameter_list|)
block|{
name|TokenizeLanguage
name|language
init|=
operator|new
name|TokenizeLanguage
argument_list|()
decl_stmt|;
name|language
operator|.
name|setHeaderName
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
name|language
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|language
operator|.
name|setRegex
argument_list|(
name|regex
argument_list|)
expr_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|tokenizePair (String startToken, String endToken, boolean includeTokens)
specifier|public
specifier|static
name|Expression
name|tokenizePair
parameter_list|(
name|String
name|startToken
parameter_list|,
name|String
name|endToken
parameter_list|,
name|boolean
name|includeTokens
parameter_list|)
block|{
name|TokenizeLanguage
name|language
init|=
operator|new
name|TokenizeLanguage
argument_list|()
decl_stmt|;
name|language
operator|.
name|setToken
argument_list|(
name|startToken
argument_list|)
expr_stmt|;
name|language
operator|.
name|setEndToken
argument_list|(
name|endToken
argument_list|)
expr_stmt|;
name|language
operator|.
name|setIncludeTokens
argument_list|(
name|includeTokens
argument_list|)
expr_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|tokenizeXML (String tagName, String inheritNamespaceTagName)
specifier|public
specifier|static
name|Expression
name|tokenizeXML
parameter_list|(
name|String
name|tagName
parameter_list|,
name|String
name|inheritNamespaceTagName
parameter_list|)
block|{
name|TokenizeLanguage
name|language
init|=
operator|new
name|TokenizeLanguage
argument_list|()
decl_stmt|;
name|language
operator|.
name|setToken
argument_list|(
name|tagName
argument_list|)
expr_stmt|;
name|language
operator|.
name|setInheritNamespaceTagName
argument_list|(
name|inheritNamespaceTagName
argument_list|)
expr_stmt|;
name|language
operator|.
name|setXml
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|createExpression
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a tokenize expression.      */
DECL|method|createExpression ()
specifier|public
name|Expression
name|createExpression
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|token
argument_list|,
literal|"token"
argument_list|)
expr_stmt|;
comment|// validate some invalid combinations
if|if
condition|(
name|endToken
operator|!=
literal|null
operator|&&
name|inheritNamespaceTagName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both xml and pair tokenizer enabled."
argument_list|)
throw|;
block|}
if|if
condition|(
name|isXml
argument_list|()
operator|&&
operator|(
name|endToken
operator|!=
literal|null
operator|||
name|includeTokens
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both xml and pair tokenizer enabled."
argument_list|)
throw|;
block|}
name|Expression
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isXml
argument_list|()
condition|)
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|tokenizeXMLExpression
argument_list|(
name|token
argument_list|,
name|inheritNamespaceTagName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endToken
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|tokenizePairExpression
argument_list|(
name|token
argument_list|,
name|endToken
argument_list|,
name|includeTokens
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// use the regular tokenizer
name|Expression
name|exp
init|=
name|headerName
operator|==
literal|null
condition|?
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
else|:
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|regex
condition|)
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|regexTokenizeExpression
argument_list|(
name|exp
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|tokenizeExpression
argument_list|(
name|exp
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|==
literal|null
operator|&&
name|skipFirst
condition|)
block|{
comment|// wrap in skip first (if group then it has its own skip first logic)
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|skipFirstExpression
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if group then wrap answer in group expression
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isXml
argument_list|()
condition|)
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|groupXmlIteratorExpression
argument_list|(
name|answer
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|delim
init|=
name|groupDelimiter
operator|!=
literal|null
condition|?
name|groupDelimiter
else|:
name|token
decl_stmt|;
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|groupIteratorExpression
argument_list|(
name|answer
argument_list|,
name|delim
argument_list|,
name|group
argument_list|,
name|skipFirst
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|expression
argument_list|)
condition|)
block|{
name|this
operator|.
name|token
operator|=
name|expression
expr_stmt|;
block|}
return|return
name|createExpression
argument_list|()
return|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
DECL|method|setToken (String token)
specifier|public
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
DECL|method|getEndToken ()
specifier|public
name|String
name|getEndToken
parameter_list|()
block|{
return|return
name|endToken
return|;
block|}
DECL|method|setEndToken (String endToken)
specifier|public
name|void
name|setEndToken
parameter_list|(
name|String
name|endToken
parameter_list|)
block|{
name|this
operator|.
name|endToken
operator|=
name|endToken
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|isRegex ()
specifier|public
name|boolean
name|isRegex
parameter_list|()
block|{
return|return
name|regex
return|;
block|}
DECL|method|setRegex (boolean regex)
specifier|public
name|void
name|setRegex
parameter_list|(
name|boolean
name|regex
parameter_list|)
block|{
name|this
operator|.
name|regex
operator|=
name|regex
expr_stmt|;
block|}
DECL|method|getInheritNamespaceTagName ()
specifier|public
name|String
name|getInheritNamespaceTagName
parameter_list|()
block|{
return|return
name|inheritNamespaceTagName
return|;
block|}
DECL|method|setInheritNamespaceTagName (String inheritNamespaceTagName)
specifier|public
name|void
name|setInheritNamespaceTagName
parameter_list|(
name|String
name|inheritNamespaceTagName
parameter_list|)
block|{
name|this
operator|.
name|inheritNamespaceTagName
operator|=
name|inheritNamespaceTagName
expr_stmt|;
block|}
DECL|method|isXml ()
specifier|public
name|boolean
name|isXml
parameter_list|()
block|{
return|return
name|xml
return|;
block|}
DECL|method|setXml (boolean xml)
specifier|public
name|void
name|setXml
parameter_list|(
name|boolean
name|xml
parameter_list|)
block|{
name|this
operator|.
name|xml
operator|=
name|xml
expr_stmt|;
block|}
DECL|method|isIncludeTokens ()
specifier|public
name|boolean
name|isIncludeTokens
parameter_list|()
block|{
return|return
name|includeTokens
return|;
block|}
DECL|method|setIncludeTokens (boolean includeTokens)
specifier|public
name|void
name|setIncludeTokens
parameter_list|(
name|boolean
name|includeTokens
parameter_list|)
block|{
name|this
operator|.
name|includeTokens
operator|=
name|includeTokens
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
DECL|method|getGroupDelimiter ()
specifier|public
name|String
name|getGroupDelimiter
parameter_list|()
block|{
return|return
name|groupDelimiter
return|;
block|}
DECL|method|setGroupDelimiter (String groupDelimiter)
specifier|public
name|void
name|setGroupDelimiter
parameter_list|(
name|String
name|groupDelimiter
parameter_list|)
block|{
name|this
operator|.
name|groupDelimiter
operator|=
name|groupDelimiter
expr_stmt|;
block|}
DECL|method|isSkipFirst ()
specifier|public
name|boolean
name|isSkipFirst
parameter_list|()
block|{
return|return
name|skipFirst
return|;
block|}
DECL|method|setSkipFirst (boolean skipFirst)
specifier|public
name|void
name|setSkipFirst
parameter_list|(
name|boolean
name|skipFirst
parameter_list|)
block|{
name|this
operator|.
name|skipFirst
operator|=
name|skipFirst
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

