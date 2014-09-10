begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|support
operator|.
name|LanguageSupport
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A language for tokenizer expressions.  *<p/>  * This xmltokenizer language can operate in the following modes:  *<ul>  *<li>inject - injecting the contextual namespace bindings into the extracted token</li>  *<li>wrap - wrapping the extracted token in its ancestor context</li>  *<li>unwrap - unwrapping the extracted token to its child content</li>  *</ul>  */
end_comment

begin_class
DECL|class|XMLTokenizeLanguage
specifier|public
class|class
name|XMLTokenizeLanguage
extends|extends
name|LanguageSupport
block|{
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|field|mode
specifier|private
name|char
name|mode
decl_stmt|;
DECL|field|group
specifier|private
name|int
name|group
decl_stmt|;
DECL|method|tokenize (String path)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|tokenize
argument_list|(
name|path
argument_list|,
literal|'i'
argument_list|)
return|;
block|}
DECL|method|tokenize (String path, char mode)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|path
parameter_list|,
name|char
name|mode
parameter_list|)
block|{
name|XMLTokenizeLanguage
name|language
init|=
operator|new
name|XMLTokenizeLanguage
argument_list|()
decl_stmt|;
name|language
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|language
operator|.
name|setMode
argument_list|(
name|mode
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
DECL|method|tokenize (String headerName, String path)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|tokenize
argument_list|(
name|headerName
argument_list|,
name|path
argument_list|,
literal|'i'
argument_list|)
return|;
block|}
DECL|method|tokenize (String headerName, String path, char mode)
specifier|public
specifier|static
name|Expression
name|tokenize
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|path
parameter_list|,
name|char
name|mode
parameter_list|)
block|{
name|XMLTokenizeLanguage
name|language
init|=
operator|new
name|XMLTokenizeLanguage
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
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|language
operator|.
name|setMode
argument_list|(
name|mode
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
name|path
argument_list|,
literal|"token"
argument_list|)
expr_stmt|;
name|Expression
name|answer
init|=
name|ExpressionBuilder
operator|.
name|tokenizeXMLAwareExpression
argument_list|(
name|path
argument_list|,
name|mode
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
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
name|path
operator|=
name|expression
expr_stmt|;
block|}
return|return
name|createExpression
argument_list|()
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
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
DECL|method|getMode ()
specifier|public
name|char
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|setMode (char mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|char
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|int
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
DECL|method|setGroup (int group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|int
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

