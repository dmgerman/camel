begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.guava.eventbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|guava
operator|.
name|eventbus
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|Subscribe
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|util
operator|.
name|AsyncProcessorConverterHelper
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
name|ObjectHelper
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

begin_comment
comment|/**  * Class with public method marked with Guava @Subscribe annotation. Responsible for receiving events from the bus and  * sending them to the Camel infrastructure.  */
end_comment

begin_class
DECL|class|CamelEventHandler
specifier|public
class|class
name|CamelEventHandler
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelEventHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|eventBusEndpoint
specifier|private
specifier|final
name|GuavaEventBusEndpoint
name|eventBusEndpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|eventClass
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
decl_stmt|;
DECL|method|CamelEventHandler (GuavaEventBusEndpoint eventBusEndpoint, Processor processor, Class<?> eventClass)
specifier|public
name|CamelEventHandler
parameter_list|(
name|GuavaEventBusEndpoint
name|eventBusEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|eventBusEndpoint
argument_list|,
literal|"eventBusEndpoint"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventBusEndpoint
operator|=
name|eventBusEndpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventClass
operator|=
name|eventClass
expr_stmt|;
block|}
comment|/**      * Guava callback when an event was received      * @param event the event      * @throws Exception is thrown if error processing the even      */
annotation|@
name|Subscribe
DECL|method|eventReceived (Object event)
specifier|public
name|void
name|eventReceived
parameter_list|(
name|Object
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received event: {}"
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventClass
operator|==
literal|null
operator|||
name|eventClass
operator|.
name|isAssignableFrom
argument_list|(
name|event
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|eventBusEndpoint
operator|.
name|createExchange
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing event: {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
comment|// use async processor to support async routing engine
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot process event: {} as its class type: {} is not assignable with: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|event
block|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
block|,
name|eventClass
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

