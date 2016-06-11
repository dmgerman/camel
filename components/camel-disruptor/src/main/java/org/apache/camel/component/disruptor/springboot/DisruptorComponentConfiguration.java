begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
operator|.
name|springboot
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
name|component
operator|.
name|disruptor
operator|.
name|DisruptorProducerType
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
name|disruptor
operator|.
name|DisruptorWaitStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|DeprecatedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The disruptor component provides asynchronous SEDA behavior using LMAX  * Disruptor.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.disruptor"
argument_list|)
DECL|class|DisruptorComponentConfiguration
specifier|public
class|class
name|DisruptorComponentConfiguration
block|{
comment|/**      * To configure the default number of concurrent consumers      */
DECL|field|defaultConcurrentConsumers
specifier|private
name|int
name|defaultConcurrentConsumers
decl_stmt|;
comment|/**      * To configure the default value for multiple consumers      */
DECL|field|defaultMultipleConsumers
specifier|private
name|Boolean
name|defaultMultipleConsumers
init|=
literal|false
decl_stmt|;
comment|/**      * To configure the default value for DisruptorProducerType The default      * value is Multi.      */
DECL|field|defaultProducerType
specifier|private
name|DisruptorProducerType
name|defaultProducerType
decl_stmt|;
comment|/**      * To configure the default value for DisruptorWaitStrategy The default      * value is Blocking.      */
DECL|field|defaultWaitStrategy
specifier|private
name|DisruptorWaitStrategy
name|defaultWaitStrategy
decl_stmt|;
comment|/**      * To configure the default value for block when full The default value is      * true.      */
DECL|field|defaultBlockWhenFull
specifier|private
name|Boolean
name|defaultBlockWhenFull
init|=
literal|false
decl_stmt|;
comment|/**      * To configure the ring buffer size      */
annotation|@
name|Deprecated
DECL|field|queueSize
specifier|private
name|int
name|queueSize
decl_stmt|;
comment|/**      * To configure the ring buffer size      */
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
decl_stmt|;
DECL|method|getDefaultConcurrentConsumers ()
specifier|public
name|int
name|getDefaultConcurrentConsumers
parameter_list|()
block|{
return|return
name|defaultConcurrentConsumers
return|;
block|}
DECL|method|setDefaultConcurrentConsumers (int defaultConcurrentConsumers)
specifier|public
name|void
name|setDefaultConcurrentConsumers
parameter_list|(
name|int
name|defaultConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|defaultConcurrentConsumers
operator|=
name|defaultConcurrentConsumers
expr_stmt|;
block|}
DECL|method|getDefaultMultipleConsumers ()
specifier|public
name|Boolean
name|getDefaultMultipleConsumers
parameter_list|()
block|{
return|return
name|defaultMultipleConsumers
return|;
block|}
DECL|method|setDefaultMultipleConsumers (Boolean defaultMultipleConsumers)
specifier|public
name|void
name|setDefaultMultipleConsumers
parameter_list|(
name|Boolean
name|defaultMultipleConsumers
parameter_list|)
block|{
name|this
operator|.
name|defaultMultipleConsumers
operator|=
name|defaultMultipleConsumers
expr_stmt|;
block|}
DECL|method|getDefaultProducerType ()
specifier|public
name|DisruptorProducerType
name|getDefaultProducerType
parameter_list|()
block|{
return|return
name|defaultProducerType
return|;
block|}
DECL|method|setDefaultProducerType (DisruptorProducerType defaultProducerType)
specifier|public
name|void
name|setDefaultProducerType
parameter_list|(
name|DisruptorProducerType
name|defaultProducerType
parameter_list|)
block|{
name|this
operator|.
name|defaultProducerType
operator|=
name|defaultProducerType
expr_stmt|;
block|}
DECL|method|getDefaultWaitStrategy ()
specifier|public
name|DisruptorWaitStrategy
name|getDefaultWaitStrategy
parameter_list|()
block|{
return|return
name|defaultWaitStrategy
return|;
block|}
DECL|method|setDefaultWaitStrategy (DisruptorWaitStrategy defaultWaitStrategy)
specifier|public
name|void
name|setDefaultWaitStrategy
parameter_list|(
name|DisruptorWaitStrategy
name|defaultWaitStrategy
parameter_list|)
block|{
name|this
operator|.
name|defaultWaitStrategy
operator|=
name|defaultWaitStrategy
expr_stmt|;
block|}
DECL|method|getDefaultBlockWhenFull ()
specifier|public
name|Boolean
name|getDefaultBlockWhenFull
parameter_list|()
block|{
return|return
name|defaultBlockWhenFull
return|;
block|}
DECL|method|setDefaultBlockWhenFull (Boolean defaultBlockWhenFull)
specifier|public
name|void
name|setDefaultBlockWhenFull
parameter_list|(
name|Boolean
name|defaultBlockWhenFull
parameter_list|)
block|{
name|this
operator|.
name|defaultBlockWhenFull
operator|=
name|defaultBlockWhenFull
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getQueueSize ()
specifier|public
name|int
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setQueueSize (int queueSize)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|int
name|queueSize
parameter_list|)
block|{
name|this
operator|.
name|queueSize
operator|=
name|queueSize
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
block|}
end_class

end_unit

