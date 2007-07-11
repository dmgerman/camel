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
name|EventDrivenConsumerRoute
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
name|CompositeProcessor
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
DECL|class|RouteType
specifier|public
class|class
name|RouteType
extends|extends
name|ProcessorType
implements|implements
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
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
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
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
for|for
control|(
name|FromType
name|fromType
range|:
name|inputs
control|)
block|{
name|routes
operator|.
name|add
argument_list|(
name|createRoute
argument_list|(
name|fromType
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
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
DECL|method|createProcessor (List<ProcessorType> processors)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|processors
parameter_list|)
block|{
name|List
argument_list|<
name|Processor
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|processors
control|)
block|{
name|Processor
name|processor
init|=
name|output
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Processor
name|processor
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|processor
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processor
operator|=
operator|new
name|CompositeProcessor
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
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
annotation|@
name|XmlElementRef
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
annotation|@
name|XmlElementRef
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
block|}
annotation|@
name|XmlTransient
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
DECL|method|createRoute (FromType fromType)
specifier|protected
name|Route
name|createRoute
parameter_list|(
name|FromType
name|fromType
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|EventDrivenConsumerRoute
argument_list|(
name|resolveEndpoint
argument_list|(
name|fromType
operator|.
name|getUri
argument_list|()
argument_list|)
argument_list|,
name|createProcessor
argument_list|(
name|outputs
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

