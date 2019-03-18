begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|KeyValueClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|option
operator|.
name|PutOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|option
operator|.
name|QueryOptions
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
name|InvokeOnHeader
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
name|component
operator|.
name|consul
operator|.
name|ConsulConfiguration
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
name|consul
operator|.
name|ConsulConstants
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
name|consul
operator|.
name|ConsulEndpoint
import|;
end_import

begin_class
DECL|class|ConsulKeyValueProducer
specifier|public
specifier|final
class|class
name|ConsulKeyValueProducer
extends|extends
name|AbstractConsulProducer
argument_list|<
name|KeyValueClient
argument_list|>
block|{
DECL|method|ConsulKeyValueProducer (ConsulEndpoint endpoint, ConsulConfiguration configuration)
specifier|public
name|ConsulKeyValueProducer
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|,
name|Consul
operator|::
name|keyValueClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|PUT
argument_list|)
DECL|method|put (Message message)
specifier|protected
name|void
name|put
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
name|getClient
argument_list|()
operator|.
name|putValue
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_FLAGS
argument_list|,
literal|0L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_OPTIONS
argument_list|,
name|PutOptions
operator|.
name|BLANK
argument_list|,
name|PutOptions
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|GET_VALUE
argument_list|)
DECL|method|getValue (Message message)
specifier|protected
name|void
name|getValue
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Boolean
name|asString
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_VALUE_AS_STRING
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isValueAsString
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|asString
condition|)
block|{
name|result
operator|=
name|getClient
argument_list|()
operator|.
name|getValueAsString
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|getClient
argument_list|()
operator|.
name|getValue
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_OPTIONS
argument_list|,
name|QueryOptions
operator|.
name|BLANK
argument_list|,
name|QueryOptions
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|GET_VALUES
argument_list|)
DECL|method|getValues (Message message)
specifier|protected
name|void
name|getValues
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Boolean
name|asString
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_VALUE_AS_STRING
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isValueAsString
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|asString
condition|)
block|{
name|result
operator|=
name|getClient
argument_list|()
operator|.
name|getValuesAsString
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|getClient
argument_list|()
operator|.
name|getValues
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_OPTIONS
argument_list|,
name|QueryOptions
operator|.
name|BLANK
argument_list|,
name|QueryOptions
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|GET_KEYS
argument_list|)
DECL|method|getKeys (Message message)
specifier|protected
name|void
name|getKeys
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getKeys
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|GET_SESSIONS
argument_list|)
DECL|method|getSessions (Message message)
specifier|protected
name|void
name|getSessions
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getSession
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|DELETE_KEY
argument_list|)
DECL|method|deleteKey (Message message)
specifier|protected
name|void
name|deleteKey
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|getClient
argument_list|()
operator|.
name|deleteKey
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|DELETE_KEYS
argument_list|)
DECL|method|deleteKeys (Message message)
specifier|protected
name|void
name|deleteKeys
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|getClient
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|LOCK
argument_list|)
DECL|method|lock (Message message)
specifier|protected
name|void
name|lock
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
name|getClient
argument_list|()
operator|.
name|acquireLock
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulKeyValueActions
operator|.
name|UNLOCK
argument_list|)
DECL|method|unlock (Message message)
specifier|protected
name|void
name|unlock
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
name|getClient
argument_list|()
operator|.
name|releaseLock
argument_list|(
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|getMandatoryHeader
argument_list|(
name|message
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

