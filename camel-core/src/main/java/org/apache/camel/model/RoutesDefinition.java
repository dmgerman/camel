begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|builder
operator|.
name|ErrorHandlerBuilder
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

begin_comment
comment|/**  * Represents a collection of routes  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"routes"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RoutesDefinition
specifier|public
class|class
name|RoutesDefinition
extends|extends
name|OptionalIdentifiedType
argument_list|<
name|RoutesDefinition
argument_list|>
implements|implements
name|RouteContainer
block|{
annotation|@
name|XmlElementRef
DECL|field|routes
specifier|private
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|AbstractInterceptorDefinition
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractInterceptorDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|intercepts
specifier|private
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|exceptions
specifier|private
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|errorHandlerBuilder
specifier|private
name|ErrorHandlerBuilder
name|errorHandlerBuilder
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Routes: "
operator|+
name|routes
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"routes"
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<RouteDefinition> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|AbstractInterceptorDefinition
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|setInterceptors (List<AbstractInterceptorDefinition> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|AbstractInterceptorDefinition
argument_list|>
name|interceptors
parameter_list|)
block|{
name|this
operator|.
name|interceptors
operator|=
name|interceptors
expr_stmt|;
block|}
DECL|method|getIntercepts ()
specifier|public
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|getIntercepts
parameter_list|()
block|{
return|return
name|intercepts
return|;
block|}
DECL|method|setIntercepts (List<InterceptDefinition> intercepts)
specifier|public
name|void
name|setIntercepts
parameter_list|(
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
parameter_list|)
block|{
name|this
operator|.
name|intercepts
operator|=
name|intercepts
expr_stmt|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
DECL|method|setExceptions (List<OnExceptionDefinition> exceptions)
specifier|public
name|void
name|setExceptions
parameter_list|(
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
operator|.
name|exceptions
operator|=
name|exceptions
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getErrorHandlerBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|getErrorHandlerBuilder
parameter_list|()
block|{
return|return
name|errorHandlerBuilder
return|;
block|}
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|errorHandlerBuilder
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a new route      *      * @return the builder      */
DECL|method|route ()
specifier|public
name|RouteDefinition
name|route
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given URI input      *      * @param uri  the from uri      * @return the builder      */
DECL|method|from (String uri)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given endpoint      *      * @param endpoint  the from endpoint      * @return the builder      */
DECL|method|from (Endpoint endpoint)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given URI inputs      *      * @param uris  the from uri      * @return the builder      */
DECL|method|from (String... uris)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|uris
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route from the given endpoints      *      * @param endpoints  the from endpoints      * @return the builder      */
DECL|method|from (Endpoint... endpoints)
specifier|public
name|RouteDefinition
name|from
parameter_list|(
name|Endpoint
modifier|...
name|endpoints
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|createRoute
argument_list|()
decl_stmt|;
name|route
operator|.
name|from
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|route
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * Creates a new route using the given route      *      * @param route the route      * @return the builder      */
DECL|method|route (RouteDefinition route)
specifier|public
name|RouteDefinition
name|route
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
comment|// lets configure the route
name|route
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AbstractInterceptorDefinition
argument_list|>
name|list
init|=
name|getInterceptors
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractInterceptorDefinition
name|interceptorType
range|:
name|list
control|)
block|{
name|route
operator|.
name|addInterceptor
argument_list|(
name|interceptorType
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|InterceptDefinition
argument_list|>
name|intercepts
init|=
name|getIntercepts
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptDefinition
name|intercept
range|:
name|intercepts
control|)
block|{
comment|// need to create a proxy for this one and use the
comment|// proceed of the proxy which will be local to this route
name|InterceptDefinition
name|proxy
init|=
name|intercept
operator|.
name|createProxy
argument_list|()
decl_stmt|;
name|route
operator|.
name|addOutput
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|route
operator|.
name|pushBlock
argument_list|(
name|proxy
operator|.
name|getProceed
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|getExceptions
argument_list|()
argument_list|)
expr_stmt|;
name|getRoutes
argument_list|()
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|route
return|;
block|}
comment|/**      * Adds an interceptor      *      * @param interceptor  the interceptor      * @return the builder      */
DECL|method|intercept (DelegateProcessor interceptor)
specifier|public
name|RoutesDefinition
name|intercept
parameter_list|(
name|DelegateProcessor
name|interceptor
parameter_list|)
block|{
name|getInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|InterceptorDefinition
argument_list|(
name|interceptor
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates and adds an interceptor      *      * @return the interceptor builder to configure      */
DECL|method|intercept ()
specifier|public
name|InterceptDefinition
name|intercept
parameter_list|()
block|{
name|InterceptDefinition
name|answer
init|=
operator|new
name|InterceptDefinition
argument_list|()
decl_stmt|;
name|getIntercepts
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates and adds an interceptor that is attached with a predicate      *      * @param predicate  the predicate      * @return the builder      */
DECL|method|intercept (Predicate predicate)
specifier|public
name|ChoiceDefinition
name|intercept
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|InterceptDefinition
name|answer
init|=
operator|new
name|InterceptDefinition
argument_list|()
decl_stmt|;
name|getIntercepts
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
operator|.
name|when
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/**      * Adds an on exception      *       * @param exception  the exception      * @return the builder      */
DECL|method|onException (Class exception)
specifier|public
name|OnExceptionDefinition
name|onException
parameter_list|(
name|Class
name|exception
parameter_list|)
block|{
name|OnExceptionDefinition
name|answer
init|=
operator|new
name|OnExceptionDefinition
argument_list|(
name|exception
argument_list|)
decl_stmt|;
name|getExceptions
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createRoute ()
specifier|protected
name|RouteDefinition
name|createRoute
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
operator|new
name|RouteDefinition
argument_list|()
decl_stmt|;
name|ErrorHandlerBuilder
name|handler
init|=
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|route
operator|.
name|setErrorHandlerBuilderIfNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
return|return
name|route
return|;
block|}
block|}
end_class

end_unit

