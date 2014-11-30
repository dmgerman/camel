begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.seda
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
name|seda
package|;
end_package

begin_comment
comment|/**  * Hazelcast SEDA Component configuration.  */
end_comment

begin_class
DECL|class|HazelcastSedaConfiguration
specifier|public
class|class
name|HazelcastSedaConfiguration
block|{
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|field|pollTimeout
specifier|private
name|int
name|pollTimeout
init|=
literal|1000
decl_stmt|;
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
DECL|field|transferExchange
specifier|private
name|boolean
name|transferExchange
decl_stmt|;
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
DECL|method|HazelcastSedaConfiguration ()
specifier|public
name|HazelcastSedaConfiguration
parameter_list|()
block|{     }
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (final int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
specifier|final
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getQueueName ()
specifier|public
name|String
name|getQueueName
parameter_list|()
block|{
return|return
name|queueName
return|;
block|}
DECL|method|setQueueName (final String queueName)
specifier|public
name|void
name|setQueueName
parameter_list|(
specifier|final
name|String
name|queueName
parameter_list|)
block|{
name|this
operator|.
name|queueName
operator|=
name|queueName
expr_stmt|;
block|}
comment|/**      * @deprecated use pollTimeout instead      */
annotation|@
name|Deprecated
DECL|method|getPollInterval ()
specifier|public
name|int
name|getPollInterval
parameter_list|()
block|{
return|return
name|pollTimeout
return|;
block|}
comment|/**      * @deprecated use pollTimeout instead      */
annotation|@
name|Deprecated
DECL|method|setPollInterval (int pollInterval)
specifier|public
name|void
name|setPollInterval
parameter_list|(
name|int
name|pollInterval
parameter_list|)
block|{
name|this
operator|.
name|pollTimeout
operator|=
name|pollInterval
expr_stmt|;
block|}
DECL|method|getPollTimeout ()
specifier|public
name|int
name|getPollTimeout
parameter_list|()
block|{
return|return
name|pollTimeout
return|;
block|}
DECL|method|setPollTimeout (int pollTimeout)
specifier|public
name|void
name|setPollTimeout
parameter_list|(
name|int
name|pollTimeout
parameter_list|)
block|{
name|this
operator|.
name|pollTimeout
operator|=
name|pollTimeout
expr_stmt|;
block|}
DECL|method|isTransferExchange ()
specifier|public
name|boolean
name|isTransferExchange
parameter_list|()
block|{
return|return
name|transferExchange
return|;
block|}
DECL|method|setTransferExchange (boolean transferExchange)
specifier|public
name|void
name|setTransferExchange
parameter_list|(
name|boolean
name|transferExchange
parameter_list|)
block|{
name|this
operator|.
name|transferExchange
operator|=
name|transferExchange
expr_stmt|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
block|}
end_class

end_unit

