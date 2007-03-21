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
name|Endpoint
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
name|processor
operator|.
name|LoggingLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|errorHandlerBuilder
specifier|private
name|ErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|errorHandlerBuilder
decl_stmt|;
DECL|method|BuilderSupport (CamelContext context)
specifier|protected
name|BuilderSupport
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
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
name|this
operator|.
name|context
operator|=
name|parent
operator|.
name|getContext
argument_list|()
expr_stmt|;
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
comment|// Helper methods
comment|//-------------------------------------------------------------------------
comment|/**      * Resolves the given URI to an endpoint      */
DECL|method|endpoint (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|getContext
argument_list|()
operator|.
name|resolveEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Resolves the list of URIs into a list of {@link Endpoint} instances      */
DECL|method|endpoints (String... uris)
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpoints
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
name|endpoints
operator|.
name|add
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoints
return|;
block|}
comment|/**      * Helper method to create a list of {@link Endpoint} instances      */
DECL|method|endpoints (Endpoint<E>.... endpoints)
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpoints
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
modifier|...
name|endpoints
parameter_list|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
range|:
name|endpoints
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
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
comment|/**      * Creates a disabled error handler for removing the default error handler      */
DECL|method|noErrorHandler ()
specifier|public
name|NoErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|noErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|NoErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Creates an error handler which just logs errors      */
DECL|method|loggingErrorHandler ()
specifier|public
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|loggingErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**      * Creates an error handler which just logs errors      */
DECL|method|loggingErrorHandler (String log)
specifier|public
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|loggingErrorHandler
parameter_list|(
name|String
name|log
parameter_list|)
block|{
return|return
name|loggingErrorHandler
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|log
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates an error handler which just logs errors      */
DECL|method|loggingErrorHandler (Log log)
specifier|public
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|loggingErrorHandler
parameter_list|(
name|Log
name|log
parameter_list|)
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|log
argument_list|)
return|;
block|}
comment|/**      * Creates an error handler which just logs errors      */
DECL|method|loggingErrorHandler (Log log, LoggingLevel level)
specifier|public
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
name|loggingErrorHandler
parameter_list|(
name|Log
name|log
parameter_list|,
name|LoggingLevel
name|level
parameter_list|)
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|log
argument_list|,
name|level
argument_list|)
return|;
block|}
DECL|method|deadLetterChannel ()
specifier|public
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
name|deadLetterChannel
parameter_list|()
block|{
return|return
operator|new
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
DECL|method|deadLetterChannel (String deadLetterUri)
specifier|public
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
name|deadLetterChannel
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
return|return
name|deadLetterChannel
argument_list|(
name|endpoint
argument_list|(
name|deadLetterUri
argument_list|)
argument_list|)
return|;
block|}
DECL|method|deadLetterChannel (Endpoint<E> deadLetterEndpoint)
specifier|public
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
name|deadLetterChannel
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|deadLetterEndpoint
parameter_list|)
block|{
return|return
operator|new
name|DeadLetterChannelBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|deadLetterEndpoint
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setContext (CamelContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
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

