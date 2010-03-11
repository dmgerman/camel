begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|CamelContext
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
name|Route
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
name|impl
operator|.
name|SynchronizationAdapter
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
name|RoutePolicy
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RoutePolicyProcessor
specifier|public
class|class
name|RoutePolicyProcessor
extends|extends
name|DelegateProcessor
block|{
DECL|field|routePolicy
specifier|private
specifier|final
name|RoutePolicy
name|routePolicy
decl_stmt|;
DECL|field|route
specifier|private
name|Route
name|route
decl_stmt|;
DECL|method|RoutePolicyProcessor (Processor processor, RoutePolicy routePolicy)
specifier|public
name|RoutePolicyProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|RoutePolicy
name|routePolicy
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|routePolicy
operator|=
name|routePolicy
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
literal|"RoutePolicy["
operator|+
name|routePolicy
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|processNext (Exchange exchange)
specifier|protected
name|void
name|processNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// check whether the policy is enabled
if|if
condition|(
name|isRoutePolicyRunAllowed
argument_list|()
condition|)
block|{
comment|// invoke begin
name|routePolicy
operator|.
name|onExchangeBegin
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// add on completion that invokes the policy callback on complete
comment|// as the Exchange can be routed async and thus we need the callback to
comment|// invoke when the route is completed
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// do not invoke it if Camel is stopping as we don't want
comment|// the policy to start a consumer during Camel is stopping
if|if
condition|(
name|isCamelStopping
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|routePolicy
operator|.
name|onExchangeDone
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RoutePolicy"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setRoute (Route route)
specifier|public
name|void
name|setRoute
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
block|}
DECL|method|isCamelStopping (CamelContext context)
specifier|private
specifier|static
name|boolean
name|isCamelStopping
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|ServiceSupport
name|ss
init|=
operator|(
name|ServiceSupport
operator|)
name|context
decl_stmt|;
return|return
name|ss
operator|.
name|isStopping
argument_list|()
operator|||
name|ss
operator|.
name|isStopped
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|isRoutePolicyRunAllowed ()
specifier|private
name|boolean
name|isRoutePolicyRunAllowed
parameter_list|()
block|{
if|if
condition|(
name|routePolicy
operator|instanceof
name|ServiceSupport
condition|)
block|{
name|ServiceSupport
name|ss
init|=
operator|(
name|ServiceSupport
operator|)
name|routePolicy
decl_stmt|;
return|return
name|ss
operator|.
name|isRunAllowed
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

