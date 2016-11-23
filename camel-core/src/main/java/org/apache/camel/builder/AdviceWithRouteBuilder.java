begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|impl
operator|.
name|InterceptSendToMockEndpointStrategy
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|RouteDefinition
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

begin_comment
comment|/**  * A {@link RouteBuilder} which has extended capabilities when using  * the<a href="http://camel.apache.org/advicewith.html">advice with</a> feature.  *<p/>  *<b>Important:</b> It is recommended to only advice a given route once (you can of course advice multiple routes).  * If you do it multiple times, then it may not work as expected, especially when any kind of error handling is involved.  * The Camel team plan for Camel 3.0 to support this as internal refactorings in the routing engine is needed to support this properly.  *  * @see org.apache.camel.model.RouteDefinition#adviceWith(org.apache.camel.CamelContext, RouteBuilder)  */
end_comment

begin_class
DECL|class|AdviceWithRouteBuilder
specifier|public
specifier|abstract
class|class
name|AdviceWithRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|originalRoute
specifier|private
name|RouteDefinition
name|originalRoute
decl_stmt|;
DECL|field|adviceWithTasks
specifier|private
specifier|final
name|List
argument_list|<
name|AdviceWithTask
argument_list|>
name|adviceWithTasks
init|=
operator|new
name|ArrayList
argument_list|<
name|AdviceWithTask
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Sets the original route which we advice.      *      * @param originalRoute the original route we advice.      */
DECL|method|setOriginalRoute (RouteDefinition originalRoute)
specifier|public
name|void
name|setOriginalRoute
parameter_list|(
name|RouteDefinition
name|originalRoute
parameter_list|)
block|{
name|this
operator|.
name|originalRoute
operator|=
name|originalRoute
expr_stmt|;
block|}
comment|/**      * Gets the original route we advice.      *      * @return the original route.      */
DECL|method|getOriginalRoute ()
specifier|public
name|RouteDefinition
name|getOriginalRoute
parameter_list|()
block|{
return|return
name|originalRoute
return|;
block|}
comment|/**      * Gets a list of additional tasks to execute after the {@link #configure()} method has been executed      * during the advice process.      *      * @return a list of additional {@link AdviceWithTask} tasks to be executed during the advice process.      */
DECL|method|getAdviceWithTasks ()
specifier|public
name|List
argument_list|<
name|AdviceWithTask
argument_list|>
name|getAdviceWithTasks
parameter_list|()
block|{
return|return
name|adviceWithTasks
return|;
block|}
comment|/**      * Mock all endpoints in the route.      *      * @throws Exception can be thrown if error occurred      */
DECL|method|mockEndpoints ()
specifier|public
name|void
name|mockEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock all endpoints matching the given pattern.      *      * @param pattern the pattern(s).      * @throws Exception can be thrown if error occurred      * @see org.apache.camel.util.EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String, String)      */
DECL|method|mockEndpoints (String... pattern)
specifier|public
name|void
name|mockEndpoints
parameter_list|(
name|String
modifier|...
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|s
range|:
name|pattern
control|)
block|{
name|getContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Mock all endpoints matching the given pattern, and<b>skips</b> sending to the original endpoint (detour messages).      *      * @param pattern the pattern(s).      * @throws Exception can be thrown if error occurred      * @see org.apache.camel.util.EndpointHelper#matchEndpoint(org.apache.camel.CamelContext, String, String)      */
DECL|method|mockEndpointsAndSkip (String... pattern)
specifier|public
name|void
name|mockEndpointsAndSkip
parameter_list|(
name|String
modifier|...
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|s
range|:
name|pattern
control|)
block|{
name|getContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|s
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Replaces the route from endpoint with a new uri      *      * @param uri uri of the new endpoint      */
DECL|method|replaceFromWith (String uri)
specifier|public
name|void
name|replaceFromWith
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceFromWith
argument_list|(
name|originalRoute
argument_list|,
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Replaces the route from endpoint with a new endpoint      *      * @param endpoint the new endpoint      */
DECL|method|replaceFromWith (Endpoint endpoint)
specifier|public
name|void
name|replaceFromWith
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceFrom
argument_list|(
name|originalRoute
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Weaves by matching id of the nodes in the route.      *<p/>      * Uses the {@link org.apache.camel.util.EndpointHelper#matchPattern(String, String)} matching algorithm.      *      * @param pattern the pattern      * @return the builder      * @see org.apache.camel.util.EndpointHelper#matchPattern(String, String)      */
DECL|method|weaveById (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveById
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching the to string representation of the nodes in the route.      *<p/>      * Uses the {@link org.apache.camel.util.EndpointHelper#matchPattern(String, String)} matching algorithm.      *      * @param pattern the pattern      * @return the builder      * @see org.apache.camel.util.EndpointHelper#matchPattern(String, String)      */
DECL|method|weaveByToString (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByToString
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching sending to endpoints with the given uri of the nodes in the route.      *<p/>      * Uses the {@link org.apache.camel.util.EndpointHelper#matchPattern(String, String)} matching algorithm.      *      * @param pattern the pattern      * @return the builder      * @see org.apache.camel.util.EndpointHelper#matchPattern(String, String)      */
DECL|method|weaveByToUri (String pattern)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByToUri
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Weaves by matching type of the nodes in the route.      *      * @param type the processor type      * @return the builder      */
DECL|method|weaveByType (Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|weaveByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Weaves by adding the nodes to the start of the route.      *      * @return the builder      */
DECL|method|weaveAddFirst ()
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|weaveAddFirst
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|"*"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|.
name|selectFirst
argument_list|()
operator|.
name|before
argument_list|()
return|;
block|}
comment|/**      * Weaves by adding the nodes to the end of the route.      *      * @return the builder      */
DECL|method|weaveAddLast ()
specifier|public
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|weaveAddLast
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|originalRoute
argument_list|,
literal|"originalRoute"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
literal|"*"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|.
name|maxDeep
argument_list|(
literal|1
argument_list|)
operator|.
name|selectLast
argument_list|()
operator|.
name|after
argument_list|()
return|;
block|}
block|}
end_class

end_unit

