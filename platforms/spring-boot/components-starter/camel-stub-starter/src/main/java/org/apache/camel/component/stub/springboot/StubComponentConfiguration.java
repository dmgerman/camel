begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stub.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stub
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * The stub component provides a simple way to stub out any physical endpoints  * while in development or testing.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.stub"
argument_list|)
DECL|class|StubComponentConfiguration
specifier|public
class|class
name|StubComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the stub component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Sets the default maximum capacity of the SEDA queue (i.e., the number of      * messages it can hold).      */
DECL|field|queueSize
specifier|private
name|Integer
name|queueSize
init|=
literal|1000
decl_stmt|;
comment|/**      * Sets the default number of concurrent threads processing exchanges.      */
DECL|field|concurrentConsumers
specifier|private
name|Integer
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
comment|/**      * Sets the default queue factory. The option is a      * org.apache.camel.component.seda.BlockingQueueFactory<org.apache.camel.Exchange> type.      */
DECL|field|defaultQueueFactory
specifier|private
name|String
name|defaultQueueFactory
decl_stmt|;
comment|/**      * Whether a thread that sends messages to a full SEDA queue will block      * until the queue's capacity is no longer exhausted. By default, an      * exception will be thrown stating that the queue is full. By enabling this      * option, the calling thread will instead block and wait until the message      * can be accepted.      */
DECL|field|defaultBlockWhenFull
specifier|private
name|Boolean
name|defaultBlockWhenFull
init|=
literal|false
decl_stmt|;
comment|/**      * Whether a thread that sends messages to a full SEDA queue will be      * discarded. By default, an exception will be thrown stating that the queue      * is full. By enabling this option, the calling thread will give up sending      * and continue, meaning that the message was not sent to the SEDA queue.      */
DECL|field|defaultDiscardWhenFull
specifier|private
name|Boolean
name|defaultDiscardWhenFull
init|=
literal|false
decl_stmt|;
comment|/**      * Whether a thread that sends messages to a full SEDA queue will block      * until the queue's capacity is no longer exhausted. By default, an      * exception will be thrown stating that the queue is full. By enabling this      * option, where a configured timeout can be added to the block case.      * Utilizing the .offer(timeout) method of the underlining java queue      */
DECL|field|defaultOfferTimeout
specifier|private
name|Long
name|defaultOfferTimeout
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the producer should be started lazy (on the first message). By      * starting lazy you can use this to allow CamelContext and routes to      * startup in situations where a producer may otherwise fail during starting      * and cause the route to fail being started. By deferring this startup to      * be lazy then the startup failure can be handled during routing messages      * via Camel's routing error handlers. Beware that when the first message is      * processed then creating and starting the producer may take a little time      * and prolong the total processing time of the processing.      */
DECL|field|lazyStartProducer
specifier|private
name|Boolean
name|lazyStartProducer
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler,      * which mean any exceptions occurred while the consumer is trying to pickup      * incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler. By default the consumer will use      * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that      * will be logged at WARN or ERROR level and ignored.      */
DECL|field|bridgeErrorHandler
specifier|private
name|Boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
DECL|method|getQueueSize ()
specifier|public
name|Integer
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
DECL|method|setQueueSize (Integer queueSize)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|Integer
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
DECL|method|getConcurrentConsumers ()
specifier|public
name|Integer
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (Integer concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|Integer
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
DECL|method|getDefaultQueueFactory ()
specifier|public
name|String
name|getDefaultQueueFactory
parameter_list|()
block|{
return|return
name|defaultQueueFactory
return|;
block|}
DECL|method|setDefaultQueueFactory (String defaultQueueFactory)
specifier|public
name|void
name|setDefaultQueueFactory
parameter_list|(
name|String
name|defaultQueueFactory
parameter_list|)
block|{
name|this
operator|.
name|defaultQueueFactory
operator|=
name|defaultQueueFactory
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
DECL|method|getDefaultDiscardWhenFull ()
specifier|public
name|Boolean
name|getDefaultDiscardWhenFull
parameter_list|()
block|{
return|return
name|defaultDiscardWhenFull
return|;
block|}
DECL|method|setDefaultDiscardWhenFull (Boolean defaultDiscardWhenFull)
specifier|public
name|void
name|setDefaultDiscardWhenFull
parameter_list|(
name|Boolean
name|defaultDiscardWhenFull
parameter_list|)
block|{
name|this
operator|.
name|defaultDiscardWhenFull
operator|=
name|defaultDiscardWhenFull
expr_stmt|;
block|}
DECL|method|getDefaultOfferTimeout ()
specifier|public
name|Long
name|getDefaultOfferTimeout
parameter_list|()
block|{
return|return
name|defaultOfferTimeout
return|;
block|}
DECL|method|setDefaultOfferTimeout (Long defaultOfferTimeout)
specifier|public
name|void
name|setDefaultOfferTimeout
parameter_list|(
name|Long
name|defaultOfferTimeout
parameter_list|)
block|{
name|this
operator|.
name|defaultOfferTimeout
operator|=
name|defaultOfferTimeout
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|getLazyStartProducer ()
specifier|public
name|Boolean
name|getLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
DECL|method|setLazyStartProducer (Boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|Boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|getBridgeErrorHandler ()
specifier|public
name|Boolean
name|getBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
DECL|method|setBridgeErrorHandler (Boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|Boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
block|}
end_class

end_unit

