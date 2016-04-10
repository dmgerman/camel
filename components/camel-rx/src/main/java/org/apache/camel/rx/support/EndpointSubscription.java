begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
operator|.
name|support
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
name|atomic
operator|.
name|AtomicBoolean
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
name|Consumer
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
name|Processor
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
name|processor
operator|.
name|CamelInternalProcessor
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
name|ServiceSupport
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observer
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscription
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_comment
comment|/**  * An RX {@link Subscription} on a Camel {@link Endpoint}  */
end_comment

begin_class
DECL|class|EndpointSubscription
specifier|public
class|class
name|EndpointSubscription
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|Subscription
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EndpointSubscription
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|observer
specifier|private
specifier|final
name|Observer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|observer
decl_stmt|;
DECL|field|consumer
specifier|private
name|Consumer
name|consumer
decl_stmt|;
DECL|field|unsubscribed
specifier|private
specifier|final
name|AtomicBoolean
name|unsubscribed
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|EndpointSubscription (Endpoint endpoint, final Observer<? super T> observer, final Func1<Exchange, T> func)
specifier|public
name|EndpointSubscription
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Observer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|observer
parameter_list|,
specifier|final
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|func
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|observer
operator|=
name|observer
expr_stmt|;
comment|// lets create the consumer
name|Processor
name|processor
init|=
operator|new
name|ProcessorToObserver
argument_list|<
name|T
argument_list|>
argument_list|(
name|func
argument_list|,
name|observer
argument_list|)
decl_stmt|;
comment|// must ensure the consumer is being executed in an unit of work so synchronization callbacks etc is invoked
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// need to start endpoint before we create producer
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|internal
argument_list|)
expr_stmt|;
comment|// add as service so we ensure it gets stopped when CamelContext stops
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|consumer
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|observer
operator|.
name|onError
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EndpointSubscription["
operator|+
name|endpoint
operator|+
literal|" observer: "
operator|+
name|observer
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|unsubscribe ()
specifier|public
name|void
name|unsubscribe
parameter_list|()
block|{
if|if
condition|(
name|unsubscribed
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error stopping consumer: "
operator|+
name|consumer
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isUnsubscribed ()
specifier|public
name|boolean
name|isUnsubscribed
parameter_list|()
block|{
return|return
name|unsubscribed
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|getObserver ()
specifier|public
name|Observer
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getObserver
parameter_list|()
block|{
return|return
name|observer
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|unsubscribed
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|unsubscribed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

