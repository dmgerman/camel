begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.mvel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|mvel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ExpressionEvaluationException
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
name|support
operator|.
name|ExpressionSupport
import|;
end_import

begin_class
DECL|class|MvelExpression
specifier|public
class|class
name|MvelExpression
extends|extends
name|ExpressionSupport
block|{
DECL|field|expressionString
specifier|private
specifier|final
name|String
name|expressionString
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|compiled
specifier|private
specifier|final
name|Serializable
name|compiled
decl_stmt|;
DECL|method|MvelExpression (MvelLanguage language, String expressionString, Class<?> type)
specifier|public
name|MvelExpression
parameter_list|(
name|MvelLanguage
name|language
parameter_list|,
name|String
name|expressionString
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|expressionString
operator|=
name|expressionString
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
try|try
block|{
name|this
operator|.
name|compiled
operator|=
name|org
operator|.
name|mvel2
operator|.
name|MVEL
operator|.
name|compileExpression
argument_list|(
name|expressionString
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|expressionString
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|mvel (String expression)
specifier|public
specifier|static
name|MvelExpression
name|mvel
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|MvelExpression
argument_list|(
operator|new
name|MvelLanguage
argument_list|()
argument_list|,
name|expression
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|evaluate (Exchange exchange, Class<T> tClass)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|)
block|{
try|try
block|{
name|Object
name|value
init|=
name|org
operator|.
name|mvel2
operator|.
name|MVEL
operator|.
name|executeExpression
argument_list|(
name|compiled
argument_list|,
operator|new
name|RootObject
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|tClass
argument_list|,
name|value
argument_list|)
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
name|ExpressionEvaluationException
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
return|return
name|org
operator|.
name|mvel2
operator|.
name|MVEL
operator|.
name|executeExpression
argument_list|(
name|compiled
argument_list|,
operator|new
name|RootObject
argument_list|(
name|exchange
argument_list|)
argument_list|)
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
name|ExpressionEvaluationException
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|expressionString
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
return|return
literal|"Mvel["
operator|+
name|expressionString
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

