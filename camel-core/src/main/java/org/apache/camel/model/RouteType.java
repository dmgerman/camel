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
name|Collection
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
name|XmlAttribute
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|CamelContextAware
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
name|NoSuchEndpointException
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
name|ServiceStatus
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
name|impl
operator|.
name|DefaultRouteContext
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
name|CamelContextHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;route/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"route"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|propOrder
operator|=
block|{
literal|"inputs"
block|,
literal|"outputs"
block|}
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|PROPERTY
argument_list|)
DECL|class|RouteType
specifier|public
class|class
name|RouteType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
argument_list|>
implements|implements
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RouteType
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptorType
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|inputs
specifier|private
name|List
argument_list|<
name|FromType
argument_list|>
name|inputs
init|=
operator|new
name|ArrayList
argument_list|<
name|FromType
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|streamCaching
specifier|private
name|Boolean
name|streamCaching
decl_stmt|;
DECL|method|RouteType ()
specifier|public
name|RouteType
parameter_list|()
block|{     }
DECL|method|RouteType (String uri)
specifier|public
name|RouteType
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|from
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteType (Endpoint endpoint)
specifier|public
name|RouteType
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|from
argument_list|(
name|endpoint
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
literal|"Route["
operator|+
name|inputs
operator|+
literal|" -> "
operator|+
name|outputs
operator|+
literal|"]"
return|;
block|}
comment|/**      * Returns the status of the route if it has been registered with a {@link CamelContext}      */
DECL|method|getStatus ()
specifier|public
name|ServiceStatus
name|getStatus
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
return|return
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
name|this
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|addRoutes (CamelContext context, Collection<Route> routes)
specifier|public
name|List
argument_list|<
name|RouteContext
argument_list|>
name|addRoutes
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteContext
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteContext
argument_list|>
argument_list|()
decl_stmt|;
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|ErrorHandlerBuilder
name|handler
init|=
name|context
operator|.
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
name|setErrorHandlerBuilderIfNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|FromType
name|fromType
range|:
name|inputs
control|)
block|{
name|RouteContext
name|routeContext
init|=
name|addRoutes
argument_list|(
name|routes
argument_list|,
name|fromType
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
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
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|// Fluent API
comment|// -----------------------------------------------------------------------
comment|/**      * Creates an input to the route      *      * @param uri  the from uri      * @return the builder      */
DECL|method|from (String uri)
specifier|public
name|RouteType
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|getInputs
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|FromType
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates an input to the route      *      * @param endpoint  the from endpoint      * @return the builder      */
DECL|method|from (Endpoint endpoint)
specifier|public
name|RouteType
name|from
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|getInputs
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|FromType
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates inputs to the route      *      * @param uris  the from uris      * @return the builder      */
DECL|method|from (String... uris)
specifier|public
name|RouteType
name|from
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
name|getInputs
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|FromType
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Creates inputs to the route      *      * @param endpoints  the from endpoints      * @return the builder      */
DECL|method|from (Endpoint... endpoints)
specifier|public
name|RouteType
name|from
parameter_list|(
name|Endpoint
modifier|...
name|endpoints
parameter_list|)
block|{
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|getInputs
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|FromType
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Set the group name for this route      *      * @param name  the group name      * @return the builder      */
DECL|method|group (String name)
specifier|public
name|RouteType
name|group
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setGroup
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Disable stream caching for this route      *      * @return the builder      */
DECL|method|noStreamCaching ()
specifier|public
name|RouteType
name|noStreamCaching
parameter_list|()
block|{
name|StreamCachingInterceptor
operator|.
name|noStreamCaching
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enable stream caching for this route      *      * @return the builder      */
DECL|method|streamCaching ()
specifier|public
name|RouteType
name|streamCaching
parameter_list|()
block|{
name|addInterceptor
argument_list|(
operator|new
name|StreamCachingInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
annotation|@
name|XmlTransient
DECL|method|setInterceptors (List<InterceptorType> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|InterceptorType
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
DECL|method|getInputs ()
specifier|public
name|List
argument_list|<
name|FromType
argument_list|>
name|getInputs
parameter_list|()
block|{
return|return
name|inputs
return|;
block|}
annotation|@
name|XmlElementRef
DECL|method|setInputs (List<FromType> inputs)
specifier|public
name|void
name|setInputs
parameter_list|(
name|List
argument_list|<
name|FromType
argument_list|>
name|inputs
parameter_list|)
block|{
name|this
operator|.
name|inputs
operator|=
name|inputs
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
annotation|@
name|XmlElementRef
DECL|method|setOutputs (List<ProcessorType> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
comment|// TODO I don't think this is called when using JAXB!
if|if
condition|(
name|outputs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
control|)
block|{
name|configureChild
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
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
annotation|@
name|XmlTransient
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
comment|/**      * The group that this route belongs to; could be the name of the RouteBuilder class      * or be explicitly configured in the XML.      *      * May be null.      */
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
annotation|@
name|XmlAttribute
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
DECL|method|getStreamCaching ()
specifier|public
name|Boolean
name|getStreamCaching
parameter_list|()
block|{
return|return
name|streamCaching
return|;
block|}
comment|/**      * Enable stream caching on this route      * @param streamCaching<code>true</code> for enabling stream caching      */
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|method|setStreamCaching (Boolean streamCaching)
specifier|public
name|void
name|setStreamCaching
parameter_list|(
name|Boolean
name|streamCaching
parameter_list|)
block|{
name|this
operator|.
name|streamCaching
operator|=
name|streamCaching
expr_stmt|;
if|if
condition|(
name|streamCaching
operator|!=
literal|null
operator|&&
name|streamCaching
condition|)
block|{
name|streamCaching
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|noStreamCaching
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|addRoutes (Collection<Route> routes, FromType fromType)
specifier|protected
name|RouteContext
name|addRoutes
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|,
name|FromType
name|fromType
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteContext
name|routeContext
init|=
operator|new
name|DefaultRouteContext
argument_list|(
name|this
argument_list|,
name|fromType
argument_list|,
name|routes
argument_list|)
decl_stmt|;
name|routeContext
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
comment|// force endpoint resolution
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getLifecycleStrategy
argument_list|()
operator|.
name|onRouteContextCreate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|>
argument_list|(
name|outputs
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|list
control|)
block|{
name|output
operator|.
name|addRoutes
argument_list|(
name|routeContext
argument_list|,
name|routes
argument_list|)
expr_stmt|;
block|}
name|routeContext
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|routeContext
return|;
block|}
annotation|@
name|Override
DECL|method|configureChild (ProcessorType output)
specifier|protected
name|void
name|configureChild
parameter_list|(
name|ProcessorType
name|output
parameter_list|)
block|{
name|super
operator|.
name|configureChild
argument_list|(
name|output
argument_list|)
expr_stmt|;
if|if
condition|(
name|isInheritErrorHandler
argument_list|()
condition|)
block|{
name|output
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
init|=
name|getInterceptors
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptorType
name|interceptor
range|:
name|interceptors
control|)
block|{
name|output
operator|.
name|addInterceptor
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|addInterceptor (InterceptorType interceptor)
specifier|public
name|void
name|addInterceptor
parameter_list|(
name|InterceptorType
name|interceptor
parameter_list|)
block|{
name|getInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

