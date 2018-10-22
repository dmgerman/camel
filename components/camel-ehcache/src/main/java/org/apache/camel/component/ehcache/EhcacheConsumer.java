begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
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
name|Processor
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
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|CacheEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|CacheEventListener
import|;
end_import

begin_class
DECL|class|EhcacheConsumer
specifier|public
class|class
name|EhcacheConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|CacheEventListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|configuration
specifier|private
specifier|final
name|EhcacheConfiguration
name|configuration
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|EhcacheManager
name|manager
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|Cache
name|cache
decl_stmt|;
DECL|method|EhcacheConsumer (EhcacheEndpoint endpoint, String cacheName, EhcacheConfiguration configuration, Processor processor)
specifier|public
name|EhcacheConsumer
parameter_list|(
name|EhcacheEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|EhcacheConfiguration
name|configuration
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|endpoint
operator|.
name|getManager
argument_list|()
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|manager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|,
name|configuration
operator|.
name|getKeyType
argument_list|()
argument_list|,
name|configuration
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|getRuntimeConfiguration
argument_list|()
operator|.
name|registerCacheEventListener
argument_list|(
name|this
argument_list|,
name|configuration
operator|.
name|getEventOrdering
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEventFiring
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
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
name|cache
operator|.
name|getRuntimeConfiguration
argument_list|()
operator|.
name|deregisterCacheEventListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onEvent (CacheEvent<?, ?> event)
specifier|public
name|void
name|onEvent
parameter_list|(
name|CacheEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|EhcacheConstants
operator|.
name|KEY
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|EhcacheConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|event
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|EhcacheConstants
operator|.
name|OLD_VALUE
argument_list|,
name|event
operator|.
name|getOldValue
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|event
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

