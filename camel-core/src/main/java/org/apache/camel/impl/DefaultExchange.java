begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ExchangePattern
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
name|Message
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
name|Synchronization
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
name|UnitOfWork
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
name|ExchangeHelper
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
name|UuidGenerator
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link Exchange}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultExchange
specifier|public
class|class
name|DefaultExchange
implements|implements
name|Exchange
block|{
DECL|field|DEFAULT_ID_GENERATOR
specifier|private
specifier|static
specifier|final
name|UuidGenerator
name|DEFAULT_ID_GENERATOR
init|=
operator|new
name|UuidGenerator
argument_list|()
decl_stmt|;
DECL|field|context
specifier|protected
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
DECL|field|in
specifier|private
name|Message
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|Message
name|out
decl_stmt|;
DECL|field|fault
specifier|private
name|Message
name|fault
decl_stmt|;
DECL|field|exception
specifier|private
name|Exception
name|exception
decl_stmt|;
DECL|field|exchangeId
specifier|private
name|String
name|exchangeId
decl_stmt|;
DECL|field|unitOfWork
specifier|private
name|UnitOfWork
name|unitOfWork
decl_stmt|;
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|field|fromEndpoint
specifier|private
name|Endpoint
name|fromEndpoint
decl_stmt|;
DECL|field|onCompletions
specifier|private
name|List
argument_list|<
name|Synchronization
argument_list|>
name|onCompletions
decl_stmt|;
DECL|method|DefaultExchange (CamelContext context)
specifier|public
name|DefaultExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultExchange (CamelContext context, ExchangePattern pattern)
specifier|public
name|DefaultExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|DefaultExchange (Exchange parent)
specifier|public
name|DefaultExchange
parameter_list|(
name|Exchange
name|parent
parameter_list|)
block|{
name|this
argument_list|(
name|parent
operator|.
name|getContext
argument_list|()
argument_list|,
name|parent
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|unitOfWork
operator|=
name|parent
operator|.
name|getUnitOfWork
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromEndpoint
operator|=
name|parent
operator|.
name|getFromEndpoint
argument_list|()
expr_stmt|;
block|}
DECL|method|DefaultExchange (Endpoint fromEndpoint)
specifier|public
name|DefaultExchange
parameter_list|(
name|Endpoint
name|fromEndpoint
parameter_list|)
block|{
name|this
argument_list|(
name|fromEndpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultExchange (Endpoint fromEndpoint, ExchangePattern pattern)
specifier|public
name|DefaultExchange
parameter_list|(
name|Endpoint
name|fromEndpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|fromEndpoint
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromEndpoint
operator|=
name|fromEndpoint
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
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
literal|"Exchange["
operator|+
name|in
operator|+
literal|"]"
return|;
block|}
DECL|method|copy ()
specifier|public
name|Exchange
name|copy
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|newInstance
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|copyFrom
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|newCopy (boolean handoverOnCompletion)
specifier|public
name|Exchange
name|newCopy
parameter_list|(
name|boolean
name|handoverOnCompletion
parameter_list|)
block|{
name|Exchange
name|copy
init|=
name|copy
argument_list|()
decl_stmt|;
comment|// do not share the unit of work
name|copy
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// handover on completeion to the copy if we got any
if|if
condition|(
name|handoverOnCompletion
operator|&&
name|unitOfWork
operator|!=
literal|null
condition|)
block|{
name|unitOfWork
operator|.
name|handoverSynchronization
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
comment|// set a correlation id so we can track back the original exchange
name|copy
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CORRELATION_ID
argument_list|,
name|this
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|copyFrom (Exchange exchange)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|==
name|this
condition|)
block|{
return|return;
block|}
name|setProperties
argument_list|(
name|safeCopy
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// this can cause strangeness if we copy, say, a FileMessage onto an FtpExchange with overloaded getExchange() methods etc.
name|safeCopy
argument_list|(
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|safeCopy
argument_list|(
name|getOut
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|hasFault
argument_list|()
condition|)
block|{
name|safeCopy
argument_list|(
name|getFault
argument_list|()
argument_list|,
name|exchange
operator|.
name|getFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|unitOfWork
operator|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
expr_stmt|;
name|pattern
operator|=
name|exchange
operator|.
name|getPattern
argument_list|()
expr_stmt|;
name|setFromEndpoint
argument_list|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|safeCopy (Message message, Message that)
specifier|private
specifier|static
name|void
name|safeCopy
parameter_list|(
name|Message
name|message
parameter_list|,
name|Message
name|that
parameter_list|)
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|copyFrom
argument_list|(
name|that
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|safeCopy (Map<String, Object> properties)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeCopy
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|properties
argument_list|)
return|;
block|}
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|)
return|;
block|}
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
DECL|method|getProperty (String name)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getProperty (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|this
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|setProperty (String name, Object value)
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// avoid the NullPointException
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if the value is null, we just remove the key from the map
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|removeProperty (String name)
specifier|public
name|Object
name|removeProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Map<String, Object> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getIn ()
specifier|public
name|Message
name|getIn
parameter_list|()
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
name|createInMessage
argument_list|()
expr_stmt|;
name|configureMessage
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
DECL|method|setIn (Message in)
specifier|public
name|void
name|setIn
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|configureMessage
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|getOut ()
specifier|public
name|Message
name|getOut
parameter_list|()
block|{
return|return
name|getOut
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|hasOut ()
specifier|public
name|boolean
name|hasOut
parameter_list|()
block|{
return|return
name|out
operator|!=
literal|null
return|;
block|}
DECL|method|getOut (boolean lazyCreate)
specifier|public
name|Message
name|getOut
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
if|if
condition|(
name|out
operator|==
literal|null
operator|&&
name|lazyCreate
condition|)
block|{
name|out
operator|=
operator|(
name|in
operator|!=
literal|null
operator|&&
name|in
operator|instanceof
name|MessageSupport
operator|)
condition|?
operator|(
operator|(
name|MessageSupport
operator|)
name|in
operator|)
operator|.
name|newInstance
argument_list|()
else|:
name|createOutMessage
argument_list|()
expr_stmt|;
name|configureMessage
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|method|setOut (Message out)
specifier|public
name|void
name|setOut
parameter_list|(
name|Message
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|configureMessage
argument_list|(
name|out
argument_list|)
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
DECL|method|getException (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getException
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|exception
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createExceptionIterator
argument_list|(
name|exception
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Throwable
name|e
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|e
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
DECL|method|setException (Exception exception)
specifier|public
name|void
name|setException
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|getFromEndpoint ()
specifier|public
name|Endpoint
name|getFromEndpoint
parameter_list|()
block|{
return|return
name|fromEndpoint
return|;
block|}
DECL|method|setFromEndpoint (Endpoint fromEndpoint)
specifier|public
name|void
name|setFromEndpoint
parameter_list|(
name|Endpoint
name|fromEndpoint
parameter_list|)
block|{
name|this
operator|.
name|fromEndpoint
operator|=
name|fromEndpoint
expr_stmt|;
block|}
DECL|method|hasFault ()
specifier|public
name|boolean
name|hasFault
parameter_list|()
block|{
return|return
name|fault
operator|!=
literal|null
return|;
block|}
DECL|method|getFault ()
specifier|public
name|Message
name|getFault
parameter_list|()
block|{
return|return
name|getFault
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|getFault (boolean lazyCreate)
specifier|public
name|Message
name|getFault
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
if|if
condition|(
name|fault
operator|==
literal|null
operator|&&
name|lazyCreate
condition|)
block|{
name|fault
operator|=
operator|(
name|in
operator|!=
literal|null
operator|&&
name|in
operator|instanceof
name|MessageSupport
operator|)
condition|?
operator|(
operator|(
name|MessageSupport
operator|)
name|in
operator|)
operator|.
name|newInstance
argument_list|()
else|:
name|createFaultMessage
argument_list|()
expr_stmt|;
name|configureMessage
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
return|return
name|fault
return|;
block|}
DECL|method|setFault (Message fault)
specifier|public
name|void
name|setFault
parameter_list|(
name|Message
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
name|configureMessage
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
DECL|method|removeFault ()
specifier|public
name|void
name|removeFault
parameter_list|()
block|{
name|this
operator|.
name|fault
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|getExchangeId ()
specifier|public
name|String
name|getExchangeId
parameter_list|()
block|{
if|if
condition|(
name|exchangeId
operator|==
literal|null
condition|)
block|{
name|exchangeId
operator|=
name|createExchangeId
argument_list|()
expr_stmt|;
block|}
return|return
name|exchangeId
return|;
block|}
DECL|method|setExchangeId (String id)
specifier|public
name|void
name|setExchangeId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|exchangeId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|isFailed ()
specifier|public
name|boolean
name|isFailed
parameter_list|()
block|{
if|if
condition|(
name|hasFault
argument_list|()
condition|)
block|{
name|Object
name|faultBody
init|=
name|getFault
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|faultBody
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|getException
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
name|Boolean
name|transacted
init|=
name|getProperty
argument_list|(
name|TRANSACTED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|transacted
operator|!=
literal|null
operator|&&
name|transacted
return|;
block|}
DECL|method|isRollbackOnly ()
specifier|public
name|boolean
name|isRollbackOnly
parameter_list|()
block|{
name|Boolean
name|rollback
init|=
name|getProperty
argument_list|(
name|ROLLBACK_ONLY
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|rollback
operator|!=
literal|null
operator|&&
name|rollback
return|;
block|}
DECL|method|getUnitOfWork ()
specifier|public
name|UnitOfWork
name|getUnitOfWork
parameter_list|()
block|{
return|return
name|unitOfWork
return|;
block|}
DECL|method|setUnitOfWork (UnitOfWork unitOfWork)
specifier|public
name|void
name|setUnitOfWork
parameter_list|(
name|UnitOfWork
name|unitOfWork
parameter_list|)
block|{
name|this
operator|.
name|unitOfWork
operator|=
name|unitOfWork
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|onCompletions
operator|!=
literal|null
condition|)
block|{
comment|// now an unit of work has been assigned so add the on completions
comment|// we might have registered already
for|for
control|(
name|Synchronization
name|onCompletion
range|:
name|this
operator|.
name|onCompletions
control|)
block|{
name|this
operator|.
name|unitOfWork
operator|.
name|addSynchronization
argument_list|(
name|onCompletion
argument_list|)
expr_stmt|;
block|}
comment|// cleanup the temporary on completion list as they now have been registered
comment|// on the unit of work
name|this
operator|.
name|onCompletions
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|onCompletions
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|addOnCompletion (Synchronization onCompletion)
specifier|public
name|void
name|addOnCompletion
parameter_list|(
name|Synchronization
name|onCompletion
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|unitOfWork
operator|==
literal|null
condition|)
block|{
comment|// unit of work not yet registered so we store the on completion temporary
comment|// until the unit of work is assigned to this exchange by the UnitOfWorkProcessor
if|if
condition|(
name|this
operator|.
name|onCompletions
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|onCompletions
operator|=
operator|new
name|ArrayList
argument_list|<
name|Synchronization
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|onCompletions
operator|.
name|add
argument_list|(
name|onCompletion
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
name|onCompletion
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Factory method used to lazily create the IN message      */
DECL|method|createInMessage ()
specifier|protected
name|Message
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessage
argument_list|()
return|;
block|}
comment|/**      * Factory method to lazily create the OUT message      */
DECL|method|createOutMessage ()
specifier|protected
name|Message
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessage
argument_list|()
return|;
block|}
comment|/**      * Factory method to lazily create the FAULT message      */
DECL|method|createFaultMessage ()
specifier|protected
name|Message
name|createFaultMessage
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessage
argument_list|()
return|;
block|}
comment|/**      * Configures the message after it has been set on the exchange      */
DECL|method|configureMessage (Message message)
specifier|protected
name|void
name|configureMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|instanceof
name|MessageSupport
condition|)
block|{
name|MessageSupport
name|messageSupport
init|=
operator|(
name|MessageSupport
operator|)
name|message
decl_stmt|;
name|messageSupport
operator|.
name|setExchange
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createExchangeId ()
specifier|protected
name|String
name|createExchangeId
parameter_list|()
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|in
operator|.
name|createExchangeId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|DefaultExchange
operator|.
name|DEFAULT_ID_GENERATOR
operator|.
name|generateId
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

