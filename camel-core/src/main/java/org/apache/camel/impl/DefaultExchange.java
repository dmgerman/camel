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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
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
name|MessageHistory
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
name|EndpointHelper
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

begin_comment
comment|/**  * A default implementation of {@link Exchange}  *  * @version   */
end_comment

begin_class
DECL|class|DefaultExchange
specifier|public
specifier|final
class|class
name|DefaultExchange
implements|implements
name|Exchange
block|{
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
DECL|field|fromRouteId
specifier|private
name|String
name|fromRouteId
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
name|fromEndpoint
operator|=
name|parent
operator|.
name|getFromEndpoint
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromRouteId
operator|=
name|parent
operator|.
name|getFromRouteId
argument_list|()
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
argument_list|(
name|fromEndpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|fromEndpoint
operator|=
name|fromEndpoint
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
comment|// do not output information about the message as it may contain sensitive information
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Exchange[%s]"
argument_list|,
name|exchangeId
operator|==
literal|null
condition|?
literal|""
else|:
name|exchangeId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCreated ()
specifier|public
name|Date
name|getCreated
parameter_list|()
block|{
if|if
condition|(
name|hasProperties
argument_list|()
condition|)
block|{
return|return
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CREATED_TIMESTAMP
argument_list|,
name|Date
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|copy ()
specifier|public
name|Exchange
name|copy
parameter_list|()
block|{
comment|// to be backwards compatible as today
return|return
name|copy
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|copy (boolean safeCopy)
specifier|public
name|Exchange
name|copy
parameter_list|(
name|boolean
name|safeCopy
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|safeCopy
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setFault
argument_list|(
name|getIn
argument_list|()
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getIn
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|safeCopyHeaders
argument_list|(
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// just copy the attachments here
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|copyAttachments
argument_list|(
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getOut
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|safeCopyHeaders
argument_list|(
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Just copy the attachments here
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyAttachments
argument_list|(
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// old way of doing copy which is @deprecated
comment|// TODO: remove this in Camel 3.0, and always do a safe copy
name|exchange
operator|.
name|setIn
argument_list|(
name|getIn
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setOut
argument_list|(
name|getOut
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|getException
argument_list|()
argument_list|)
expr_stmt|;
comment|// copy properties after body as body may trigger lazy init
if|if
condition|(
name|hasProperties
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperties
argument_list|(
name|safeCopyProperties
argument_list|(
name|getProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|safeCopyHeaders (Map<String, Object> headers)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeCopyHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|context
operator|.
name|getHeadersMapFactory
argument_list|()
operator|.
name|newMap
argument_list|(
name|headers
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|safeCopyProperties (Map<String, Object> properties)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|safeCopyProperties
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
name|createProperties
argument_list|(
name|properties
argument_list|)
decl_stmt|;
comment|// safe copy message history using a defensive copy
name|List
argument_list|<
name|MessageHistory
argument_list|>
name|history
init|=
operator|(
name|List
argument_list|<
name|MessageHistory
argument_list|>
operator|)
name|answer
operator|.
name|remove
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|)
decl_stmt|;
if|if
condition|(
name|history
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|history
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
DECL|method|getProperty (String name, Object defaultValue)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
block|{
name|Object
name|answer
init|=
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
name|defaultValue
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|value
return|;
block|}
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getProperty (String name, Object defaultValue, Class<T> type)
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
name|Object
name|defaultValue
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
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// lets avoid NullPointerException when converting to boolean for null values
if|if
condition|(
name|boolean
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|value
return|;
block|}
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
if|if
condition|(
operator|!
name|hasProperties
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
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
DECL|method|removeProperties (String pattern)
specifier|public
name|boolean
name|removeProperties
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
name|removeProperties
argument_list|(
name|pattern
argument_list|,
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
return|;
block|}
DECL|method|removeProperties (String pattern, String... excludePatterns)
specifier|public
name|boolean
name|removeProperties
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasProperties
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// store keys to be removed as we cannot loop and remove at the same time in implementations such as HashMap
name|Set
argument_list|<
name|String
argument_list|>
name|toBeRemoved
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|matches
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|properties
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|key
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
if|if
condition|(
name|excludePatterns
operator|!=
literal|null
operator|&&
name|isExcludePatternMatch
argument_list|(
name|key
argument_list|,
name|excludePatterns
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|matches
operator|=
literal|true
expr_stmt|;
name|toBeRemoved
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|toBeRemoved
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|toBeRemoved
operator|.
name|size
argument_list|()
operator|==
name|properties
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// special optimization when all should be removed
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|toBeRemoved
operator|.
name|forEach
argument_list|(
name|k
lambda|->
name|properties
operator|.
name|remove
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|matches
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
name|createProperties
argument_list|()
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
DECL|method|hasProperties ()
specifier|public
name|boolean
name|hasProperties
parameter_list|()
block|{
return|return
name|properties
operator|!=
literal|null
operator|&&
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
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
operator|new
name|DefaultMessage
argument_list|(
name|getContext
argument_list|()
argument_list|)
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
DECL|method|getIn (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getIn
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Message
name|in
init|=
name|getIn
argument_list|()
decl_stmt|;
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|in
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|// fallback to use type converter
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|this
argument_list|,
name|in
argument_list|)
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
comment|// lazy create
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
name|out
operator|=
operator|(
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
operator|new
name|DefaultMessage
argument_list|(
name|getContext
argument_list|()
argument_list|)
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
DECL|method|getOut (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getOut
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
operator|!
name|hasOut
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Message
name|out
init|=
name|getOut
argument_list|()
decl_stmt|;
comment|// eager same instance type test to avoid the overhead of invoking the type converter
comment|// if already same type
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|out
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|// fallback to use type converter
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|this
argument_list|,
name|out
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
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|hasOut
argument_list|()
condition|?
name|getOut
argument_list|()
else|:
name|getIn
argument_list|()
return|;
block|}
DECL|method|getMessage (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getMessage
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|hasOut
argument_list|()
condition|?
name|getOut
argument_list|(
name|type
argument_list|)
else|:
name|getIn
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|setMessage (Message message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|hasOut
argument_list|()
condition|)
block|{
name|setOut
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
return|return
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|type
argument_list|,
name|exception
argument_list|)
return|;
block|}
DECL|method|setException (Throwable t)
specifier|public
name|void
name|setException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|exception
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|t
operator|instanceof
name|Exception
condition|)
block|{
name|this
operator|.
name|exception
operator|=
operator|(
name|Exception
operator|)
name|t
expr_stmt|;
block|}
else|else
block|{
comment|// wrap throwable into an exception
name|this
operator|.
name|exception
operator|=
name|ObjectHelper
operator|.
name|wrapCamelExecutionException
argument_list|(
name|this
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|t
operator|instanceof
name|InterruptedException
condition|)
block|{
comment|// mark the exchange as interrupted due to the interrupt exception
name|setProperty
argument_list|(
name|Exchange
operator|.
name|INTERRUPTED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getFromRouteId ()
specifier|public
name|String
name|getFromRouteId
parameter_list|()
block|{
return|return
name|fromRouteId
return|;
block|}
DECL|method|setFromRouteId (String fromRouteId)
specifier|public
name|void
name|setFromRouteId
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
block|{
name|this
operator|.
name|fromRouteId
operator|=
name|fromRouteId
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
name|exception
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|hasOut
argument_list|()
condition|?
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
else|:
name|getIn
argument_list|()
operator|.
name|isFault
argument_list|()
return|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
name|UnitOfWork
name|uow
init|=
name|getUnitOfWork
argument_list|()
decl_stmt|;
if|if
condition|(
name|uow
operator|!=
literal|null
condition|)
block|{
return|return
name|uow
operator|.
name|isTransacted
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|isExternalRedelivered ()
specifier|public
name|Boolean
name|isExternalRedelivered
parameter_list|()
block|{
name|Boolean
name|answer
init|=
literal|null
decl_stmt|;
comment|// check property first, as the implementation details to know if the message
comment|// was externally redelivered is message specific, and thus the message implementation
comment|// could potentially change during routing, and therefore later we may not know if the
comment|// original message was externally redelivered or not, therefore we store this detail
comment|// as a exchange property to keep it around for the lifecycle of the exchange
if|if
condition|(
name|hasProperties
argument_list|()
condition|)
block|{
name|answer
operator|=
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXTERNAL_REDELIVERED
argument_list|,
literal|null
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// lets avoid adding methods to the Message API, so we use the
comment|// DefaultMessage to allow component specific messages to extend
comment|// and implement the isExternalRedelivered method.
name|Message
name|msg
init|=
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|instanceof
name|DefaultMessage
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|DefaultMessage
operator|)
name|msg
operator|)
operator|.
name|isTransactedRedelivered
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|isRollbackOnly ()
specifier|public
name|boolean
name|isRollbackOnly
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY
argument_list|)
argument_list|)
operator|||
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY_LAST
argument_list|)
argument_list|)
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
name|unitOfWork
operator|!=
literal|null
operator|&&
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
name|onCompletions
control|)
block|{
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
name|onCompletions
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|unitOfWork
operator|==
literal|null
condition|)
block|{
comment|// unit of work not yet registered so we store the on completion temporary
comment|// until the unit of work is assigned to this exchange by the unit of work
if|if
condition|(
name|onCompletions
operator|==
literal|null
condition|)
block|{
name|onCompletions
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
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
DECL|method|containsOnCompletion (Synchronization onCompletion)
specifier|public
name|boolean
name|containsOnCompletion
parameter_list|(
name|Synchronization
name|onCompletion
parameter_list|)
block|{
if|if
condition|(
name|unitOfWork
operator|!=
literal|null
condition|)
block|{
comment|// if there is an unit of work then the completions is moved there
return|return
name|unitOfWork
operator|.
name|containsSynchronization
argument_list|(
name|onCompletion
argument_list|)
return|;
block|}
else|else
block|{
comment|// check temporary completions if no unit of work yet
return|return
name|onCompletions
operator|!=
literal|null
operator|&&
name|onCompletions
operator|.
name|contains
argument_list|(
name|onCompletion
argument_list|)
return|;
block|}
block|}
DECL|method|handoverCompletions (Exchange target)
specifier|public
name|void
name|handoverCompletions
parameter_list|(
name|Exchange
name|target
parameter_list|)
block|{
if|if
condition|(
name|onCompletions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Synchronization
name|onCompletion
range|:
name|onCompletions
control|)
block|{
name|target
operator|.
name|addOnCompletion
argument_list|(
name|onCompletion
argument_list|)
expr_stmt|;
block|}
comment|// cleanup the temporary on completion list as they have been handed over
name|onCompletions
operator|.
name|clear
argument_list|()
expr_stmt|;
name|onCompletions
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|unitOfWork
operator|!=
literal|null
condition|)
block|{
comment|// let unit of work handover
name|unitOfWork
operator|.
name|handoverSynchronization
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handoverCompletions ()
specifier|public
name|List
argument_list|<
name|Synchronization
argument_list|>
name|handoverCompletions
parameter_list|()
block|{
name|List
argument_list|<
name|Synchronization
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|onCompletions
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|onCompletions
argument_list|)
expr_stmt|;
name|onCompletions
operator|.
name|clear
argument_list|()
expr_stmt|;
name|onCompletions
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|answer
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
name|messageSupport
operator|.
name|setCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
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
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createProperties ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createProperties
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|<>
argument_list|()
return|;
block|}
DECL|method|createProperties (Map<String, Object> properties)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createProperties
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
return|return
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|properties
argument_list|)
return|;
block|}
DECL|method|isExcludePatternMatch (String key, String... excludePatterns)
specifier|private
specifier|static
name|boolean
name|isExcludePatternMatch
parameter_list|(
name|String
name|key
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|excludePatterns
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|key
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

