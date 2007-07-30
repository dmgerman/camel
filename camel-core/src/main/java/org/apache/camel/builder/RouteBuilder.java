begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultCamelContext
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
name|RoutesType
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
name|RouteType
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
name|DelegateProcessor
import|;
end_import

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

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/dsl.html">Java DSL</a>  * which is used to build {@link Route} instances in a @{link CamelContext} for smart routing.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilder
specifier|public
specifier|abstract
class|class
name|RouteBuilder
extends|extends
name|BuilderSupport
block|{
DECL|field|initalized
specifier|private
name|AtomicBoolean
name|initalized
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|routeCollection
specifier|private
name|RoutesType
name|routeCollection
init|=
operator|new
name|RoutesType
argument_list|()
decl_stmt|;
DECL|field|routes
specifier|private
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|RouteBuilder ()
specifier|protected
name|RouteBuilder
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteBuilder (CamelContext context)
specifier|protected
name|RouteBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called on initialization to to build the required destinationBuilders      */
DECL|method|configure ()
specifier|public
specifier|abstract
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|from (String uri)
specifier|public
name|RouteType
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|routeCollection
operator|.
name|from
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|from (Endpoint endpoint)
specifier|public
name|RouteType
name|from
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|routeCollection
operator|.
name|from
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
comment|/**      * Installs the given error handler builder      *      * @param errorHandlerBuilder the error handler to be used by default for all child routes      * @return the current builder with the error handler configured      */
DECL|method|errorHandler (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|RouteBuilder
name|errorHandler
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures whether or not the error handler is inherited by every processing node (or just the top most one)      *      * @param value the flag as to whether error handlers should be inherited or not      * @return the current builder      */
DECL|method|inheritErrorHandler (boolean value)
specifier|public
name|RouteBuilder
name|inheritErrorHandler
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|routeCollection
operator|.
name|setInheritErrorHandlerFlag
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|intercept (DelegateProcessor interceptor)
specifier|public
name|RouteBuilder
name|intercept
parameter_list|(
name|DelegateProcessor
name|interceptor
parameter_list|)
block|{
name|routeCollection
operator|.
name|intercept
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|createContainer
argument_list|()
expr_stmt|;
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
comment|/**      * Returns the routing map from inbound endpoints to processors      */
DECL|method|getRouteList ()
specifier|public
name|List
argument_list|<
name|Route
argument_list|>
name|getRouteList
parameter_list|()
throws|throws
name|Exception
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
return|return
name|routes
return|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|checkInitialized ()
specifier|protected
name|void
name|checkInitialized
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|initalized
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|configure
argument_list|()
expr_stmt|;
name|populateRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateRoutes (List<Route> routes)
specifier|protected
name|void
name|populateRoutes
parameter_list|(
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContext has been injected!"
argument_list|)
throw|;
block|}
name|routeCollection
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|routeCollection
operator|.
name|populateRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Factory method      */
DECL|method|createContainer ()
specifier|protected
name|CamelContext
name|createContainer
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
block|}
end_class

end_unit

