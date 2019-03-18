begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|language
operator|.
name|tokenizer
operator|.
name|TokenizeLanguage
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
name|Metadata
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

begin_comment
comment|/**  * To use Camel message body or header with a tokenizer in Camel expressions or predicates.  *  * @see TokenizeLanguage  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.0.0"
argument_list|,
name|label
operator|=
literal|"language,core"
argument_list|,
name|title
operator|=
literal|"Tokenize"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"tokenize"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TokenizerExpression
specifier|public
class|class
name|TokenizerExpression
extends|extends
name|ExpressionDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|endToken
specifier|private
name|String
name|endToken
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|inheritNamespaceTagName
specifier|private
name|String
name|inheritNamespaceTagName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|regex
specifier|private
name|Boolean
name|regex
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|xml
specifier|private
name|Boolean
name|xml
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|includeTokens
specifier|private
name|Boolean
name|includeTokens
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipFirst
specifier|private
name|Boolean
name|skipFirst
decl_stmt|;
DECL|method|TokenizerExpression ()
specifier|public
name|TokenizerExpression
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"tokenize"
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
comment|/**      * The (start) token to use as tokenizer, for example you can use the new line token.      * You can use simple language as the token to support dynamic tokens.      */
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
comment|/**      * The end token to use as tokenizer if using start/end token pairs.      * You can use simple language as the token to support dynamic tokens.      */
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
comment|/**      * Name of header to tokenize instead of using the message body.      */
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
comment|/**      * If the token is a regular expression pattern.      *<p/>      * The default value is false      */
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
DECL|method|getRegex ()
specifier|public
name|Boolean
name|getRegex
parameter_list|()
block|{
return|return
name|regex
return|;
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
comment|/**      * To inherit namespaces from a root/parent tag name when using XML      * You can use simple language as the tag name to support dynamic names.      */
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
DECL|method|getXml ()
specifier|public
name|Boolean
name|getXml
parameter_list|()
block|{
return|return
name|xml
return|;
block|}
comment|/**      * Whether the input is XML messages.      * This option must be set to true if working with XML payloads.      */
DECL|method|setXml (Boolean xml)
specifier|public
name|void
name|setXml
parameter_list|(
name|Boolean
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
DECL|method|getIncludeTokens ()
specifier|public
name|Boolean
name|getIncludeTokens
parameter_list|()
block|{
return|return
name|includeTokens
return|;
block|}
comment|/**      * Whether to include the tokens in the parts when using pairs      *<p/>      * The default value is false      */
DECL|method|setIncludeTokens (Boolean includeTokens)
specifier|public
name|void
name|setIncludeTokens
parameter_list|(
name|Boolean
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
comment|/**      * To group N parts together, for example to split big files into chunks of 1000 lines.      * You can use simple language as the group to support dynamic group sizes.      */
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
DECL|method|getSkipFirst ()
specifier|public
name|Boolean
name|getSkipFirst
parameter_list|()
block|{
return|return
name|skipFirst
return|;
block|}
comment|/**      * To skip the very first element      */
DECL|method|setSkipFirst (Boolean skipFirst)
specifier|public
name|void
name|setSkipFirst
parameter_list|(
name|Boolean
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
DECL|method|createExpression (CamelContext camelContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// special for new line tokens, if defined from XML then its 2 characters, so we replace that back to a single char
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"\\n"
argument_list|)
condition|)
block|{
name|token
operator|=
literal|'\n'
operator|+
name|token
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
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
name|setEndToken
argument_list|(
name|endToken
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
name|setHeaderName
argument_list|(
name|headerName
argument_list|)
expr_stmt|;
if|if
condition|(
name|regex
operator|!=
literal|null
condition|)
block|{
name|language
operator|.
name|setRegex
argument_list|(
name|regex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
name|language
operator|.
name|setXml
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeTokens
operator|!=
literal|null
condition|)
block|{
name|language
operator|.
name|setIncludeTokens
argument_list|(
name|includeTokens
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|group
operator|!=
literal|null
operator|&&
operator|!
literal|"0"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
name|language
operator|.
name|setGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipFirst
operator|!=
literal|null
condition|)
block|{
name|language
operator|.
name|setSkipFirst
argument_list|(
name|skipFirst
argument_list|)
expr_stmt|;
block|}
return|return
name|language
operator|.
name|createExpression
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (CamelContext camelContext)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Expression
name|exp
init|=
name|createExpression
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
return|return
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|exp
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|endToken
operator|!=
literal|null
condition|)
block|{
return|return
literal|"tokenize{body() using tokens: "
operator|+
name|token
operator|+
literal|"..."
operator|+
name|endToken
operator|+
literal|"}"
return|;
block|}
else|else
block|{
return|return
literal|"tokenize{"
operator|+
operator|(
name|headerName
operator|!=
literal|null
condition|?
literal|"header: "
operator|+
name|headerName
else|:
literal|"body()"
operator|)
operator|+
literal|" using token: "
operator|+
name|token
operator|+
literal|"}"
return|;
block|}
block|}
block|}
end_class

end_unit

