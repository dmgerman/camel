begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|ExpressionToPredicateAdapter
import|;
end_import

begin_comment
comment|/**  * For expressions and predicates using a body or header tokenizer.  *  * @see TokenizeLanguage  */
end_comment

begin_class
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

