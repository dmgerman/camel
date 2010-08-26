begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|reply
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|DefaultTimeoutMap
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CorrelationMap
specifier|public
class|class
name|CorrelationMap
extends|extends
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|ReplyHandler
argument_list|>
block|{
DECL|method|CorrelationMap (ScheduledExecutorService executor, long requestMapPollTimeMillis)
specifier|public
name|CorrelationMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|,
name|long
name|requestMapPollTimeMillis
parameter_list|)
block|{
name|super
argument_list|(
name|executor
argument_list|,
name|requestMapPollTimeMillis
argument_list|)
expr_stmt|;
block|}
DECL|method|onEviction (String key, ReplyHandler value)
specifier|public
name|boolean
name|onEviction
parameter_list|(
name|String
name|key
parameter_list|,
name|ReplyHandler
name|value
parameter_list|)
block|{
comment|// trigger timeout
name|value
operator|.
name|onTimeout
argument_list|(
name|key
argument_list|)
expr_stmt|;
comment|// return true to remove the element
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|put (String key, ReplyHandler value, long timeoutMillis)
specifier|public
name|void
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|ReplyHandler
name|value
parameter_list|,
name|long
name|timeoutMillis
parameter_list|)
block|{
if|if
condition|(
name|timeoutMillis
operator|<=
literal|0
condition|)
block|{
comment|// no timeout (must use Integer.MAX_VALUE)
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|timeoutMillis
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

