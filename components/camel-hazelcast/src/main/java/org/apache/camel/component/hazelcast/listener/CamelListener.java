begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.listener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|listener
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastComponentHelper
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|hazelcast
operator|.
name|HazelcastDefaultConsumer
import|;
end_import

begin_class
DECL|class|CamelListener
specifier|public
class|class
name|CamelListener
block|{
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|HazelcastDefaultConsumer
name|consumer
decl_stmt|;
DECL|method|CamelListener (HazelcastDefaultConsumer consumer, String cacheName)
specifier|public
name|CamelListener
parameter_list|(
name|HazelcastDefaultConsumer
name|consumer
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|sendExchange (String operation, String key, Object value)
specifier|protected
name|void
name|sendExchange
parameter_list|(
name|String
name|operation
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// set object to body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// set headers
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|HazelcastComponentHelper
operator|.
name|setListenerHeaders
argument_list|(
name|exchange
argument_list|,
name|HazelcastConstants
operator|.
name|CACHE_LISTENER
argument_list|,
name|operation
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
try|try
block|{
name|consumer
operator|.
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
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error processing exchange for hazelcast consumer on object '%s' in cache '%s'."
argument_list|,
name|key
argument_list|,
name|cacheName
argument_list|)
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|HazelcastDefaultConsumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
block|}
end_class

end_unit

