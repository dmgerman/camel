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
name|Predicate
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
name|Routes
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
name|ChoiceType
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
name|ExceptionType
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
name|InterceptType
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
name|ProcessorType
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
name|processor
operator|.
name|DelegateProcessor
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
name|interceptor
operator|.
name|StreamCachingInterceptor
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/dsl.html">Java DSL</a> which is  * used to build {@link Route} instances in a {@link CamelContext} for smart routing.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteBuilder
specifier|public
specifier|abstract
class|class
name|RouteBuilder
extends|extends
name|BuilderSupport
implements|implements
name|Routes
block|{
DECL|field|initialized
specifier|private
name|AtomicBoolean
name|initialized
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
specifier|public
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
specifier|public
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|routeCollection
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      *<b>Called on initialization to build the routes using the fluent builder syntax.</b>      *<p/>      * This is a central method for RouteBuilder implementations to implement      * the routes using the Java fluent builder syntax.      *      * @throws Exception can be thrown during configuration      */
DECL|method|configure ()
specifier|public
specifier|abstract
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new route from the given URI input      *      * @param uri  the from uri      * @return the builder      */
DECL|method|from (String uri)
specifier|public
name|RouteType
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|RouteType
name|answer
init|=
name|routeCollection
operator|.
name|from
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new route from the given endpoint      *      * @param endpoint  the from endpoint      * @return the builder      */
DECL|method|from (Endpoint endpoint)
specifier|public
name|RouteType
name|from
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|RouteType
name|answer
init|=
name|routeCollection
operator|.
name|from
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|configureRoute
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Installs the given<a href="http://activemq.apache.org/camel/error-handler.html">error handler</a> builder      *      * @param errorHandlerBuilder  the error handler to be used by default for all child routes      * @return the current builder with the error handler configured      */
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
comment|/**      * Configures whether or not the<a href="http://activemq.apache.org/camel/error-handler.html">error handler</a>      * is inherited by every processing node (or just the top most one)      *      * @param inherit  whether error handlers should be inherited or not      * @return the current builder      */
DECL|method|inheritErrorHandler (boolean inherit)
specifier|public
name|RouteBuilder
name|inheritErrorHandler
parameter_list|(
name|boolean
name|inherit
parameter_list|)
block|{
name|routeCollection
operator|.
name|setInheritErrorHandlerFlag
argument_list|(
name|inherit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds the given interceptor to this route      *      * @param interceptor  the interceptor      * @return the builder      */
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
comment|/**      * Adds a route for an interceptor; use the {@link ProcessorType#proceed()} method      * to continue processing the underlying route being intercepted.      * @return the builder      */
DECL|method|intercept ()
specifier|public
name|InterceptType
name|intercept
parameter_list|()
block|{
return|return
name|routeCollection
operator|.
name|intercept
argument_list|()
return|;
block|}
comment|/**      * Applies a route for an interceptor if the given predicate is true      * otherwise the interceptor route is not applied      *      * @param predicate  the predicate      * @return the builder      */
DECL|method|intercept (Predicate predicate)
specifier|public
name|ChoiceType
name|intercept
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
return|return
name|routeCollection
operator|.
name|intercept
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/**      *<a href="http://activemq.apache.org/camel/exception-clause.html">Exception clause</a>      * for catching certain exceptions and handling them.      *      * @param exception exception to catch      * @return the builder      */
DECL|method|onException (Class exception)
specifier|public
name|ExceptionType
name|onException
parameter_list|(
name|Class
name|exception
parameter_list|)
block|{
return|return
name|routeCollection
operator|.
name|onException
argument_list|(
name|exception
argument_list|)
return|;
block|}
comment|/**      *<a href="http://activemq.apache.org/camel/exception-clause.html">Exception clause</a>      * for catching certain exceptions and handling them.      *      * @param exceptions list of exceptions to catch      * @return the builder      */
DECL|method|onException (Class... exceptions)
specifier|public
name|ExceptionType
name|onException
parameter_list|(
name|Class
modifier|...
name|exceptions
parameter_list|)
block|{
name|ExceptionType
name|last
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Class
name|ex
range|:
name|exceptions
control|)
block|{
name|last
operator|=
name|last
operator|==
literal|null
condition|?
name|onException
argument_list|(
name|ex
argument_list|)
else|:
name|last
operator|.
name|onException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|last
operator|!=
literal|null
condition|?
name|last
else|:
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
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
comment|/**      * Uses {@link org.apache.camel.CamelContext#getRoutes()} to return the routes in the context.      */
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
annotation|@
name|Override
DECL|method|setInheritErrorHandler (boolean inheritErrorHandler)
specifier|public
name|void
name|setInheritErrorHandler
parameter_list|(
name|boolean
name|inheritErrorHandler
parameter_list|)
block|{
name|super
operator|.
name|setInheritErrorHandler
argument_list|(
name|inheritErrorHandler
argument_list|)
expr_stmt|;
name|routeCollection
operator|.
name|setInheritErrorHandlerFlag
argument_list|(
name|inheritErrorHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|super
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
name|routeCollection
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
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
name|initialized
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// Set the CamelContext ErrorHandler here
name|CamelContext
name|camelContext
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|.
name|getErrorHandlerBuilder
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setErrorHandlerBuilder
argument_list|(
name|camelContext
operator|.
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"CamelContext has not been injected!"
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
name|camelContext
operator|.
name|addRouteDefinitions
argument_list|(
name|routeCollection
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setRouteCollection (RoutesType routeCollection)
specifier|public
name|void
name|setRouteCollection
parameter_list|(
name|RoutesType
name|routeCollection
parameter_list|)
block|{
name|this
operator|.
name|routeCollection
operator|=
name|routeCollection
expr_stmt|;
block|}
DECL|method|getRouteCollection ()
specifier|public
name|RoutesType
name|getRouteCollection
parameter_list|()
block|{
return|return
name|this
operator|.
name|routeCollection
return|;
block|}
comment|/**      * Completely disable stream caching for all routes being defined in the same RouteBuilder after this.      */
DECL|method|noStreamCaching ()
specifier|public
name|void
name|noStreamCaching
parameter_list|()
block|{
name|StreamCachingInterceptor
operator|.
name|noStreamCaching
argument_list|(
name|routeCollection
operator|.
name|getInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enable stream caching for all routes being defined in the same RouteBuilder after this call.      */
DECL|method|streamCaching ()
specifier|public
name|void
name|streamCaching
parameter_list|()
block|{
name|routeCollection
operator|.
name|intercept
argument_list|(
operator|new
name|StreamCachingInterceptor
argument_list|()
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
DECL|method|configureRoute (RouteType route)
specifier|protected
name|void
name|configureRoute
parameter_list|(
name|RouteType
name|route
parameter_list|)
block|{
name|route
operator|.
name|setGroup
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a collection of routes to this context      *      * @throws Exception if the routes could not be created for whatever reason      */
DECL|method|addRoutes (Routes routes)
specifier|protected
name|void
name|addRoutes
parameter_list|(
name|Routes
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

