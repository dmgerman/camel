begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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
name|impl
operator|.
name|PollingConsumerSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ListPollingConsumer
specifier|public
class|class
name|ListPollingConsumer
extends|extends
name|PollingConsumerSupport
block|{
DECL|field|exchanges
specifier|private
specifier|final
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
decl_stmt|;
DECL|method|ListPollingConsumer (Endpoint endpoint, List<Exchange> exchanges)
specifier|public
name|ListPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchanges
operator|=
name|exchanges
expr_stmt|;
block|}
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
if|if
condition|(
name|exchanges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|exchanges
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

