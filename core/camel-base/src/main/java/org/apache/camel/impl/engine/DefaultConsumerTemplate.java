begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

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
name|ConsumerTemplate
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
name|spi
operator|.
name|ConsumerCache
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
name|support
operator|.
name|CamelContextHelper
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
name|UnitOfWorkHelper
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
name|service
operator|.
name|ServiceHelper
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
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link ConsumerTemplate}.  */
end_comment

begin_class
DECL|class|DefaultConsumerTemplate
specifier|public
class|class
name|DefaultConsumerTemplate
extends|extends
name|ServiceSupport
implements|implements
name|ConsumerTemplate
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|consumerCache
specifier|private
name|ConsumerCache
name|consumerCache
decl_stmt|;
DECL|field|maximumCacheSize
specifier|private
name|int
name|maximumCacheSize
decl_stmt|;
DECL|method|DefaultConsumerTemplate (CamelContext camelContext)
specifier|public
name|DefaultConsumerTemplate
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
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|maximumCacheSize
return|;
block|}
DECL|method|setMaximumCacheSize (int maximumCacheSize)
specifier|public
name|void
name|setMaximumCacheSize
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
block|{
name|this
operator|.
name|maximumCacheSize
operator|=
name|maximumCacheSize
expr_stmt|;
block|}
DECL|method|getCurrentCacheSize ()
specifier|public
name|int
name|getCurrentCacheSize
parameter_list|()
block|{
if|if
condition|(
name|consumerCache
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|consumerCache
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
if|if
condition|(
name|consumerCache
operator|!=
literal|null
condition|)
block|{
name|consumerCache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
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
DECL|method|receive (String endpointUri)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|getConsumerCache
argument_list|()
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|receive (Endpoint endpoint)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receive
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receive (String endpointUri, long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|getConsumerCache
argument_list|()
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receive (Endpoint endpoint, long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receive
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receiveNoWait (String endpointUri)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
return|return
name|getConsumerCache
argument_list|()
operator|.
name|receiveNoWait
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|receiveNoWait (Endpoint endpoint)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|receiveBody (String endpointUri, long timeout)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, long timeout)
specifier|public
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|receiveBodyNoWait (String endpointUri)
specifier|public
name|Object
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receiveNoWait
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|receiveBodyNoWait (Endpoint endpoint)
specifier|public
name|Object
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|receiveBodyNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|receiveBody (String endpointUri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|answer
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|receiveBody (String endpointUri, long timeout, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|endpointUri
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|answer
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
DECL|method|receiveBody (Endpoint endpoint, long timeout, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBody
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|receiveBodyNoWait (String endpointUri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Exchange
name|exchange
init|=
name|receiveNoWait
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|extractResultBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|answer
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|answer
return|;
block|}
DECL|method|receiveBodyNoWait (Endpoint endpoint, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|receiveBodyNoWait
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|doneUoW (Exchange exchange)
specifier|public
name|void
name|doneUoW
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
comment|// The receiveBody method will get a null exchange
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// handover completions and done them manually to ensure they are being executed
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
init|=
name|exchange
operator|.
name|handoverCompletions
argument_list|()
decl_stmt|;
name|UnitOfWorkHelper
operator|.
name|doneSynchronizations
argument_list|(
name|exchange
argument_list|,
name|synchronizations
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// done the unit of work
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|done
argument_list|(
name|exchange
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
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during done UnitOfWork for Exchange: "
operator|+
name|exchange
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveMandatoryEndpoint (String endpointUri)
specifier|protected
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|endpointUri
argument_list|)
return|;
block|}
comment|/**      * Extracts the body from the given result.      *<p/>      * If the exchange pattern is provided it will try to honor it and retrieve the body      * from either IN or OUT according to the pattern.      *      * @param result   the result      * @return  the result, can be<tt>null</tt>.      */
DECL|method|extractResultBody (Exchange result)
specifier|protected
name|Object
name|extractResultBody
parameter_list|(
name|Exchange
name|result
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
comment|// rethrow if there was an exception
if|if
condition|(
name|result
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|result
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
comment|// okay no fault then return the response
if|if
condition|(
name|result
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// use OUT as the response
name|answer
operator|=
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// use IN as the response
name|answer
operator|=
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getConsumerCache ()
specifier|private
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ConsumerCache
name|getConsumerCache
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ConsumerTemplate has not been started"
argument_list|)
throw|;
block|}
return|return
name|consumerCache
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|consumerCache
operator|==
literal|null
condition|)
block|{
name|consumerCache
operator|=
operator|new
name|DefaultConsumerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|,
name|maximumCacheSize
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should shutdown the services as this is our intention, to not re-use the services anymore
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|consumerCache
argument_list|)
expr_stmt|;
name|consumerCache
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

