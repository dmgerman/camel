begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
name|AfterPropertiesConfigured
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
name|ExpressionAdapter
import|;
end_import

begin_class
DECL|class|JsonPathExpression
specifier|public
class|class
name|JsonPathExpression
extends|extends
name|ExpressionAdapter
implements|implements
name|AfterPropertiesConfigured
block|{
DECL|field|expression
specifier|private
specifier|final
name|String
name|expression
decl_stmt|;
DECL|field|engine
specifier|private
name|JsonPathEngine
name|engine
decl_stmt|;
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
DECL|field|suppressExceptions
specifier|private
name|boolean
name|suppressExceptions
decl_stmt|;
DECL|field|allowSimple
specifier|private
name|boolean
name|allowSimple
init|=
literal|true
decl_stmt|;
DECL|field|options
specifier|private
name|Option
index|[]
name|options
decl_stmt|;
DECL|method|JsonPathExpression (String expression)
specifier|public
name|JsonPathExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
comment|/**      * To configure the result type to use      */
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
DECL|method|isSuppressExceptions ()
specifier|public
name|boolean
name|isSuppressExceptions
parameter_list|()
block|{
return|return
name|suppressExceptions
return|;
block|}
comment|/**      * Whether to suppress exceptions such as PathNotFoundException      */
DECL|method|setSuppressExceptions (boolean suppressExceptions)
specifier|public
name|void
name|setSuppressExceptions
parameter_list|(
name|boolean
name|suppressExceptions
parameter_list|)
block|{
name|this
operator|.
name|suppressExceptions
operator|=
name|suppressExceptions
expr_stmt|;
block|}
DECL|method|isAllowSimple ()
specifier|public
name|boolean
name|isAllowSimple
parameter_list|()
block|{
return|return
name|allowSimple
return|;
block|}
comment|/**      * Whether to allow in inlined simple exceptions in the json path expression      */
DECL|method|setAllowSimple (boolean allowSimple)
specifier|public
name|void
name|setAllowSimple
parameter_list|(
name|boolean
name|allowSimple
parameter_list|)
block|{
name|this
operator|.
name|allowSimple
operator|=
name|allowSimple
expr_stmt|;
block|}
DECL|method|getOptions ()
specifier|public
name|Option
index|[]
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
comment|/**      * To configure the json path options to use      */
DECL|method|setOptions (Option[] options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|Option
index|[]
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Object
name|result
init|=
name|evaluateJsonPath
argument_list|(
name|exchange
argument_list|,
name|engine
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
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
name|resultType
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
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
annotation|@
name|Override
DECL|method|afterPropertiesConfigured (CamelContext camelContext)
specifier|public
name|void
name|afterPropertiesConfigured
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
try|try
block|{
name|engine
operator|=
operator|new
name|JsonPathEngine
argument_list|(
name|expression
argument_list|,
name|suppressExceptions
argument_list|,
name|allowSimple
argument_list|,
name|options
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
name|expression
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
literal|"jsonpath["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|evaluateJsonPath (Exchange exchange, JsonPathEngine engine)
specifier|private
name|Object
name|evaluateJsonPath
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JsonPathEngine
name|engine
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|engine
operator|.
name|read
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

