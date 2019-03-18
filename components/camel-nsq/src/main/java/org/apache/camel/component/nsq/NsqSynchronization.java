begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|NSQMessage
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_class
DECL|class|NsqSynchronization
specifier|public
class|class
name|NsqSynchronization
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|nsqMessage
specifier|private
specifier|final
name|NSQMessage
name|nsqMessage
decl_stmt|;
DECL|field|requeueInterval
specifier|private
specifier|final
name|int
name|requeueInterval
decl_stmt|;
DECL|method|NsqSynchronization (NSQMessage nsqMessage, int requeueInterval)
specifier|public
name|NsqSynchronization
parameter_list|(
name|NSQMessage
name|nsqMessage
parameter_list|,
name|int
name|requeueInterval
parameter_list|)
block|{
name|this
operator|.
name|nsqMessage
operator|=
name|nsqMessage
expr_stmt|;
name|this
operator|.
name|requeueInterval
operator|=
name|requeueInterval
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|nsqMessage
operator|.
name|finished
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|nsqMessage
operator|.
name|requeue
argument_list|(
name|requeueInterval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowHandover ()
specifier|public
name|boolean
name|allowHandover
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

