begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.queue
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
name|queue
package|;
end_package

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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IQueue
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|HazelcastQueueProducer
specifier|public
class|class
name|HazelcastQueueProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|queue
specifier|private
name|IQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
DECL|field|helper
specifier|private
name|HazelcastComponentHelper
name|helper
init|=
operator|new
name|HazelcastComponentHelper
argument_list|()
decl_stmt|;
DECL|method|HazelcastQueueProducer (HazelcastInstance hazelcastInstance, Endpoint endpoint, String queueName)
specifier|public
name|HazelcastQueueProducer
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|queueName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|hazelcastInstance
operator|.
name|getQueue
argument_list|(
name|queueName
argument_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
comment|// get header parameters
name|int
name|operation
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
condition|)
block|{
if|if
condition|(
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
operator|instanceof
name|String
condition|)
block|{
name|operation
operator|=
name|helper
operator|.
name|lookupOperationNumber
argument_list|(
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|operation
operator|=
operator|(
name|Integer
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
expr_stmt|;
block|}
block|}
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
operator|-
literal|1
case|:
comment|//If no operation is specified use ADD.
case|case
name|HazelcastConstants
operator|.
name|ADD_OPERATION
case|:
name|this
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|PUT_OPERATION
case|:
name|this
operator|.
name|put
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|POLL_OPERATION
case|:
name|this
operator|.
name|poll
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|PEEK_OPERATION
case|:
name|this
operator|.
name|peek
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|OFFER_OPERATION
case|:
name|this
operator|.
name|offer
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|HazelcastConstants
operator|.
name|REMOVEVALUE_OPERATION
case|:
name|this
operator|.
name|remove
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' is not allowed for parameter '%s' on the QUEUE cache."
argument_list|,
name|operation
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|)
throw|;
block|}
comment|// finally copy headers
name|HazelcastComponentHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|add (Exchange exchange)
specifier|private
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|queue
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|put (Exchange exchange)
specifier|private
name|void
name|put
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|queue
operator|.
name|put
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|poll (Exchange exchange)
specifier|private
name|void
name|poll
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|queue
operator|.
name|poll
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|peek (Exchange exchange)
specifier|private
name|void
name|peek
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|offer (Exchange exchange)
specifier|private
name|void
name|offer
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|this
operator|.
name|queue
operator|.
name|offer
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (Exchange exchange)
specifier|private
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|queue
operator|.
name|remove
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|queue
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

