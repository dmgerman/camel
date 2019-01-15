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
name|NamedNode
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
name|spi
operator|.
name|RoutePolicyFactory
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
name|PatternHelper
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.spi.RoutePolicyFactory} which executes for a duration and then triggers an action.  *<p/>  * This can be used to stop a set of routes (or CamelContext) after it has processed a number of messages, or has been running for N seconds.  */
end_comment

begin_class
DECL|class|DurationRoutePolicyFactory
specifier|public
class|class
name|DurationRoutePolicyFactory
implements|implements
name|RoutePolicyFactory
block|{
DECL|field|fromRouteId
specifier|private
name|String
name|fromRouteId
decl_stmt|;
DECL|field|maxMessages
specifier|private
name|int
name|maxMessages
decl_stmt|;
DECL|field|maxSeconds
specifier|private
name|int
name|maxSeconds
decl_stmt|;
DECL|field|action
specifier|private
name|DurationRoutePolicy
operator|.
name|Action
name|action
init|=
name|DurationRoutePolicy
operator|.
name|Action
operator|.
name|STOP_ROUTE
decl_stmt|;
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, NamedNode route)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|NamedNode
name|route
parameter_list|)
block|{
name|DurationRoutePolicy
name|policy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fromRouteId
operator|==
literal|null
operator|||
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|routeId
argument_list|,
name|fromRouteId
argument_list|)
condition|)
block|{
name|policy
operator|=
operator|new
name|DurationRoutePolicy
argument_list|(
name|camelContext
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setMaxMessages
argument_list|(
name|maxMessages
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setMaxSeconds
argument_list|(
name|maxSeconds
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
return|return
name|policy
return|;
block|}
DECL|method|getFromRouteId ()
specifier|public
name|String
name|getFromRouteId
parameter_list|()
block|{
return|return
name|fromRouteId
return|;
block|}
comment|/**      * Limit the route policy to the route which matches this pattern      *      * @see PatternHelper#matchPattern(String, String)      */
DECL|method|setFromRouteId (String fromRouteId)
specifier|public
name|void
name|setFromRouteId
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
block|{
name|this
operator|.
name|fromRouteId
operator|=
name|fromRouteId
expr_stmt|;
block|}
comment|/**      * Maximum number of messages to process before the action is triggered      */
DECL|method|setMaxMessages (int maxMessages)
specifier|public
name|void
name|setMaxMessages
parameter_list|(
name|int
name|maxMessages
parameter_list|)
block|{
name|this
operator|.
name|maxMessages
operator|=
name|maxMessages
expr_stmt|;
block|}
DECL|method|getMaxSeconds ()
specifier|public
name|int
name|getMaxSeconds
parameter_list|()
block|{
return|return
name|maxSeconds
return|;
block|}
comment|/**      * Maximum seconds Camel is running before the action is triggered      */
DECL|method|setMaxSeconds (int maxSeconds)
specifier|public
name|void
name|setMaxSeconds
parameter_list|(
name|int
name|maxSeconds
parameter_list|)
block|{
name|this
operator|.
name|maxSeconds
operator|=
name|maxSeconds
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|DurationRoutePolicy
operator|.
name|Action
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * What action to perform when maximum is triggered.      */
DECL|method|setAction (DurationRoutePolicy.Action action)
specifier|public
name|void
name|setAction
parameter_list|(
name|DurationRoutePolicy
operator|.
name|Action
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
block|}
end_class

end_unit

