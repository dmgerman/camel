begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|generated
operator|.
name|SSPTParserConstants
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|generated
operator|.
name|Token
import|;
end_import

begin_class
DECL|class|InParameter
specifier|public
class|class
name|InParameter
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|typeName
specifier|private
specifier|final
name|String
name|typeName
decl_stmt|;
DECL|field|sqlType
specifier|private
specifier|final
name|int
name|sqlType
decl_stmt|;
DECL|field|scale
specifier|private
specifier|final
name|Integer
name|scale
decl_stmt|;
DECL|field|valueExtractor
specifier|private
name|ValueExtractor
name|valueExtractor
decl_stmt|;
DECL|method|InParameter (String name, int sqlType, Token valueSrcToken, Integer scale, String typeName)
specifier|public
name|InParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|Token
name|valueSrcToken
parameter_list|,
name|Integer
name|scale
parameter_list|,
name|String
name|typeName
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|sqlType
operator|=
name|sqlType
expr_stmt|;
name|parseValueExpression
argument_list|(
name|valueSrcToken
argument_list|)
expr_stmt|;
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
name|this
operator|.
name|typeName
operator|=
name|typeName
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|scale
operator|!=
literal|null
operator|&&
name|this
operator|.
name|typeName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseRuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Both scale=%s and typeName=%s cannot be set"
argument_list|,
name|this
operator|.
name|scale
argument_list|,
name|this
operator|.
name|typeName
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|parseValueExpression (Token valueSrcToken)
specifier|private
name|void
name|parseValueExpression
parameter_list|(
name|Token
name|valueSrcToken
parameter_list|)
block|{
if|if
condition|(
name|SSPTParserConstants
operator|.
name|SIMPLE_EXP_TOKEN
operator|==
name|valueSrcToken
operator|.
name|kind
condition|)
block|{
name|this
operator|.
name|valueExtractor
operator|=
parameter_list|(
name|exchange
parameter_list|,
name|container
parameter_list|)
lambda|->
block|{
name|Expression
name|exp
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
operator|.
name|createExpression
argument_list|(
name|valueSrcToken
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|exp
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
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SSPTParserConstants
operator|.
name|PARAMETER_POS_TOKEN
operator|==
name|valueSrcToken
operator|.
name|kind
condition|)
block|{
comment|//remove leading :#
specifier|final
name|String
name|mapKey
init|=
name|valueSrcToken
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|this
operator|.
name|valueExtractor
operator|=
parameter_list|(
name|exchange
parameter_list|,
name|container
parameter_list|)
lambda|->
operator|(
operator|(
name|Map
operator|)
name|container
operator|)
operator|.
name|get
argument_list|(
name|mapKey
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getScale ()
specifier|public
name|Integer
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getTypeName ()
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
name|typeName
return|;
block|}
DECL|method|getSqlType ()
specifier|public
name|int
name|getSqlType
parameter_list|()
block|{
return|return
name|sqlType
return|;
block|}
DECL|method|getValueExtractor ()
specifier|public
name|ValueExtractor
name|getValueExtractor
parameter_list|()
block|{
return|return
name|valueExtractor
return|;
block|}
block|}
end_class

end_unit

