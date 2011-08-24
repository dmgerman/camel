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
name|spi
operator|.
name|RoutePolicy
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
name|support
operator|.
name|SynchronizationAdapter
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
comment|/**  * {@link Processor} which instruments the {@link RoutePolicy}.  *  * @version   */
end_comment

begin_class
DECL|class|RoutePolicyProcessor
specifier|public
class|class
name|RoutePolicyProcessor
extends|extends
name|DelegateAsyncProcessor
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
name|RoutePolicyProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|routePolicies
specifier|private
specifier|final
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicies
decl_stmt|;
DECL|field|route
specifier|private
name|Route
name|route
decl_stmt|;
DECL|method|RoutePolicyProcessor (Processor processor, List<RoutePolicy> routePolicies)
specifier|public
name|RoutePolicyProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicies
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|routePolicies
operator|=
name|routePolicies
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
name|routePolicies
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// invoke begin
for|for
control|(
name|RoutePolicy
name|policy
range|:
name|routePolicies
control|)
block|{
try|try
block|{
if|if
condition|(
name|isRoutePolicyRunAllowed
argument_list|(
name|policy
argument_list|)
condition|)
block|{
name|policy
operator|.
name|onExchangeBegin
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
literal|"Error occurred during onExchangeBegin on RoutePolicy: "
operator|+
name|policy
operator|+
literal|". This exception will be ignored"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
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
for|for
control|(
name|RoutePolicy
name|policy
range|:
name|routePolicies
control|)
block|{
try|try
block|{
if|if
condition|(
name|isRoutePolicyRunAllowed
argument_list|(
name|policy
argument_list|)
condition|)
block|{
name|policy
operator|.
name|onExchangeDone
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
literal|"Error occurred during onExchangeDone on RoutePolicy: "
operator|+
name|policy
operator|+
literal|". This exception will be ignored"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RoutePolicyOnCompletion"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/**      * Sets the route this policy applies.      *      * @param route the route      */
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
comment|/**      * Strategy to determine if this policy is allowed to run      *      * @param policy the policy      * @return<tt>true</tt> to run      */
DECL|method|isRoutePolicyRunAllowed (RoutePolicy policy)
specifier|protected
name|boolean
name|isRoutePolicyRunAllowed
parameter_list|(
name|RoutePolicy
name|policy
parameter_list|)
block|{
if|if
condition|(
name|policy
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
name|policy
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
block|}
end_class

end_unit

