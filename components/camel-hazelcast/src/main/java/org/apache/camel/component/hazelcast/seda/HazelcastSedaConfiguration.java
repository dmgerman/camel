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
DECL|field|pollInterval
specifier|private
name|int
name|pollInterval
init|=
literal|1000
decl_stmt|;
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
DECL|method|HazelcastSedaConfiguration ()
specifier|public
name|HazelcastSedaConfiguration
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
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
DECL|method|getPollInterval ()
specifier|public
name|int
name|getPollInterval
parameter_list|()
block|{
return|return
name|pollInterval
return|;
block|}
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
name|pollInterval
operator|=
name|pollInterval
expr_stmt|;
block|}
block|}
end_class

end_unit

