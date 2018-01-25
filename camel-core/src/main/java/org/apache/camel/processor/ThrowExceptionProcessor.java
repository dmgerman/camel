begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|CamelContextAware
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
name|CamelExchangeException
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
name|Traceable
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
name|IdAware
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
name|ServiceSupport
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
name|AsyncProcessorHelper
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
comment|/**  * The processor which sets an {@link Exception} on the {@link Exchange}  */
end_comment

begin_class
DECL|class|ThrowExceptionProcessor
specifier|public
class|class
name|ThrowExceptionProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
implements|,
name|IdAware
implements|,
name|CamelContextAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|simple
specifier|private
name|Expression
name|simple
decl_stmt|;
DECL|field|exception
specifier|private
specifier|final
name|Exception
name|exception
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|type
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|method|ThrowExceptionProcessor (Exception exception)
specifier|public
name|ThrowExceptionProcessor
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|this
argument_list|(
name|exception
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ThrowExceptionProcessor (Exception exception, Class<? extends Exception> type, String message)
specifier|public
name|ThrowExceptionProcessor
parameter_list|(
name|Exception
name|exception
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|type
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Exception
name|cause
init|=
name|exception
decl_stmt|;
try|try
block|{
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|type
operator|!=
literal|null
condition|)
block|{
comment|// create the message using simple language so it can be dynamic
name|String
name|text
init|=
name|simple
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// create a new exception of that type, and provide the message as
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|type
operator|.
name|getDeclaredConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|cause
operator|=
operator|(
name|Exception
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cause
operator|==
literal|null
operator|&&
name|type
operator|!=
literal|null
condition|)
block|{
comment|// create a new exception of that type using its default constructor
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|type
operator|.
name|getDeclaredConstructor
argument_list|()
decl_stmt|;
name|cause
operator|=
operator|(
name|Exception
operator|)
name|constructor
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error creating new instance of "
operator|+
name|exception
operator|.
name|getClass
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
name|String
name|className
init|=
name|this
operator|.
name|exception
operator|==
literal|null
condition|?
name|this
operator|.
name|type
operator|.
name|getSimpleName
argument_list|()
else|:
name|this
operator|.
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
return|return
literal|"throwException["
operator|+
name|className
operator|+
literal|"]"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getException ()
specifier|public
name|Exception
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ThrowException"
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|simple
operator|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

