begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Navigate
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
name|RouteAware
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
name|Service
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
name|SuspendableService
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * A {@link DefaultRoute} which starts with an  *<a href="http://camel.apache.org/event-driven-consumer.html">Event Driven Consumer</a>  *<p/>  * Use the API from {@link org.apache.camel.CamelContext} to control the lifecycle of a route,  * such as starting and stopping using the {@link org.apache.camel.CamelContext#startRoute(String)}  * and {@link org.apache.camel.CamelContext#stopRoute(String)} methods.  *  * @version   */
end_comment

begin_class
DECL|class|EventDrivenConsumerRoute
specifier|public
class|class
name|EventDrivenConsumerRoute
extends|extends
name|DefaultRoute
block|{
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|consumer
specifier|private
name|Consumer
name|consumer
decl_stmt|;
DECL|method|EventDrivenConsumerRoute (RouteContext routeContext, Endpoint endpoint, Processor processor)
specifier|public
name|EventDrivenConsumerRoute
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|routeContext
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
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
literal|"EventDrivenConsumerRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
comment|/**      * Factory method to lazily create the complete list of services required for this route      * such as adding the processor or consumer      */
annotation|@
name|Override
DECL|method|addServices (List<Service> services)
specifier|protected
name|void
name|addServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|consumer
operator|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|instanceof
name|RouteAware
condition|)
block|{
operator|(
operator|(
name|RouteAware
operator|)
name|consumer
operator|)
operator|.
name|setRoute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|Processor
name|processor
init|=
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|navigate ()
specifier|public
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|navigate
parameter_list|()
block|{
name|Processor
name|answer
init|=
name|getProcessor
argument_list|()
decl_stmt|;
comment|// we want navigating routes to be easy, so skip the initial channel
comment|// and navigate to its output where it all starts from end user point of view
if|if
condition|(
name|answer
operator|instanceof
name|Navigate
condition|)
block|{
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
init|=
operator|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
operator|)
name|answer
decl_stmt|;
if|if
condition|(
name|nav
operator|.
name|next
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Object
name|first
init|=
name|nav
operator|.
name|next
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|instanceof
name|Navigate
condition|)
block|{
return|return
operator|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
operator|)
name|first
return|;
block|}
block|}
return|return
operator|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
operator|)
name|answer
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|supportsSuspension ()
specifier|public
name|boolean
name|supportsSuspension
parameter_list|()
block|{
return|return
name|consumer
operator|instanceof
name|SuspendableService
return|;
block|}
block|}
end_class

end_unit

