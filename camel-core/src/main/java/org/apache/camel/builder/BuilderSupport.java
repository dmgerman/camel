begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * Base class for implementation inheritance  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|BuilderSupport
specifier|public
specifier|abstract
class|class
name|BuilderSupport
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
DECL|field|errorHandlerBuilder
specifier|private
name|ErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|errorHandlerBuilder
decl_stmt|;
DECL|method|BuilderSupport ()
specifier|protected
name|BuilderSupport
parameter_list|()
block|{     }
comment|// Builder methods
comment|//-------------------------------------------------------------------------
comment|/**      * Returns a predicate and value builder for headers on an exchange      */
DECL|method|header (String name)
specifier|public
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|body
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an exchange      */
DECL|method|outBody ()
specifier|public
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|outBody
parameter_list|()
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a specific type      */
DECL|method|outBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|outBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
init|=
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|BuilderSupport (BuilderSupport<E> parent)
specifier|protected
name|BuilderSupport
parameter_list|(
name|BuilderSupport
argument_list|<
name|E
argument_list|>
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|.
name|errorHandlerBuilder
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|parent
operator|.
name|errorHandlerBuilder
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getErrorHandlerBuilder ()
specifier|public
name|ErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|getErrorHandlerBuilder
parameter_list|()
block|{
if|if
condition|(
name|errorHandlerBuilder
operator|==
literal|null
condition|)
block|{
name|errorHandlerBuilder
operator|=
operator|new
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|errorHandlerBuilder
return|;
block|}
comment|/**      * Sets the error handler to use with processors created by this builder      */
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder<E> errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|errorHandlerBuilder
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|errorHandlerBuilder
expr_stmt|;
block|}
block|}
end_class

end_unit

