begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RouteContext
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
name|XmlElement
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

begin_comment
comment|/**  * Represents an XML&lt;route/&gt; element  *  * @version $Revision: $  */
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
literal|"interceptors"
block|,
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
name|FIELD
argument_list|)
DECL|class|RouteType
specifier|public
class|class
name|RouteType
extends|extends
name|ProcessorType
implements|implements
name|CamelContextAware
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
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
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"interceptor"
argument_list|)
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|InterceptorRef
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptorRef
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
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
annotation|@
name|XmlElementRef
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
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
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
DECL|method|RouteType (Endpoint endpoint)
specifier|public
name|RouteType
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
literal|"Route[ "
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
comment|// TODO should we zap this and replace with next method?
DECL|method|addRoutes (CamelContext context)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
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
name|addRoutes
argument_list|(
name|context
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
DECL|method|addRoutes (CamelContext context, Collection<Route> routes)
specifier|public
name|void
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
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
for|for
control|(
name|FromType
name|fromType
range|:
name|inputs
control|)
block|{
name|addRoutes
argument_list|(
name|routes
argument_list|,
name|fromType
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
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
literal|"No CamelContext has been injected!"
argument_list|)
throw|;
block|}
name|Endpoint
name|answer
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Endpoint found for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|// Fluent API
comment|//-----------------------------------------------------------------------
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
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|InterceptorRef
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|setInterceptors (List<InterceptorRef> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|InterceptorRef
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|addRoutes (Collection<Route> routes, FromType fromType)
specifier|protected
name|void
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
name|RouteContext
argument_list|(
name|this
argument_list|,
name|fromType
argument_list|,
name|routes
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|routeContext
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
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
name|InterceptorRef
argument_list|>
name|list
init|=
name|output
operator|.
name|getInterceptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No interceptor collection: "
operator|+
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|list
operator|.
name|addAll
argument_list|(
name|getInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

