begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
package|;
end_package

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|MemcachedClient
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
name|impl
operator|.
name|DefaultProducer
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

begin_comment
comment|/**  * Camel producer for communication with a kestrel based queue.  */
end_comment

begin_class
DECL|class|KestrelProducer
specifier|public
class|class
name|KestrelProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|KestrelEndpoint
name|endpoint
decl_stmt|;
DECL|field|memcachedClient
specifier|private
specifier|final
name|MemcachedClient
name|memcachedClient
decl_stmt|;
DECL|method|KestrelProducer (final KestrelEndpoint endpoint, final MemcachedClient memcachedClient)
specifier|public
name|KestrelProducer
parameter_list|(
specifier|final
name|KestrelEndpoint
name|endpoint
parameter_list|,
specifier|final
name|MemcachedClient
name|memcachedClient
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|memcachedClient
operator|=
name|memcachedClient
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
name|String
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|queue
init|=
name|endpoint
operator|.
name|getQueue
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to: "
operator|+
name|queue
operator|+
literal|" message: "
operator|+
name|msg
argument_list|)
expr_stmt|;
block|}
name|memcachedClient
operator|.
name|set
argument_list|(
name|queue
argument_list|,
literal|0
argument_list|,
name|msg
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
name|CamelExchangeException
argument_list|(
literal|"Error sending to: "
operator|+
name|queue
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No message body to send to: "
operator|+
name|queue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

