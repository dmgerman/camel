begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|builder
operator|.
name|ProcessorFactory
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

begin_comment
comment|/**  * Represents an XML&lt;route/&gt; element  *  * @version $Revision: $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"root"
argument_list|)
DECL|class|RouteType
specifier|public
class|class
name|RouteType
extends|extends
name|OutputType
implements|implements
name|CamelContextAware
implements|,
name|ProcessorFactory
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|from
specifier|private
name|List
argument_list|<
name|FromType
argument_list|>
name|from
init|=
operator|new
name|ArrayList
argument_list|<
name|FromType
argument_list|>
argument_list|()
decl_stmt|;
comment|/*     public Route createRoute() throws Exception {         return new EventDrivenConsumerRoute(getEndpoint(), createProcessor());     } */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Route[from: "
operator|+
name|from
operator|+
literal|" processor: "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
comment|// Properties
comment|//-----------------------------------------------------------------------
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|method|getFrom ()
specifier|public
name|List
argument_list|<
name|FromType
argument_list|>
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (List<FromType> from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|List
argument_list|<
name|FromType
argument_list|>
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
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
DECL|method|createProcessor ()
specifier|public
name|Processor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
comment|// TODO
block|}
DECL|method|resolveEndpoint (String uri)
specifier|protected
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
name|FromType
name|from
init|=
operator|new
name|FromType
argument_list|()
decl_stmt|;
name|from
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|getFrom
argument_list|()
operator|.
name|add
argument_list|(
name|from
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

