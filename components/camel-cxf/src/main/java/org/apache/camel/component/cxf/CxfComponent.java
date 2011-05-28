begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|component
operator|.
name|cxf
operator|.
name|spring
operator|.
name|CxfEndpointBean
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
name|HeaderFilterStrategyComponent
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
name|camel
operator|.
name|util
operator|.
name|IntrospectionSupport
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
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/cxf.html">CXF Component</a>   *   * @version   */
end_comment

begin_class
DECL|class|CxfComponent
specifier|public
class|class
name|CxfComponent
extends|extends
name|HeaderFilterStrategyComponent
block|{
DECL|method|CxfComponent ()
specifier|public
name|CxfComponent
parameter_list|()
block|{     }
DECL|method|CxfComponent (CamelContext context)
specifier|public
name|CxfComponent
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
comment|/**      * Create a {@link CxfEndpoint} which, can be a Spring bean endpoint having       * URI format cxf:bean:<i>beanId</i> or transport address endpoint having URI format      * cxf://<i>transportAddress</i>.       */
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
argument_list|)
condition|)
block|{
comment|// Get the bean from the Spring context
name|String
name|beanId
init|=
name|remaining
operator|.
name|substring
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|beanId
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|beanId
operator|=
name|beanId
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|CxfEndpointBean
name|bean
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|beanId
argument_list|,
name|CxfEndpointBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|=
operator|new
name|CxfSpringEndpoint
argument_list|(
name|this
argument_list|,
name|bean
argument_list|)
expr_stmt|;
comment|// Apply Spring bean properties (including # notation referenced bean).  Note that the
comment|// Spring bean properties values can be overridden by property defined in URI query.
comment|// The super class (DefaultComponent) will invoke "setProperties" after this method
comment|// with to apply properties defined by URI query.
if|if
condition|(
name|bean
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|copy
operator|.
name|putAll
argument_list|(
name|bean
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMtomEnabled
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|copy
operator|.
name|get
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// endpoint URI does not specify a bean
name|result
operator|=
operator|new
name|CxfEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|setEndpointHeaderFilterStrategy
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// extract the properties.xxx and set them as properties
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"properties."
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// set the properties of MTOM
name|result
operator|.
name|setMtomEnabled
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfiguration (String uri, String remaining, Endpoint endpoint, Map<String, Object> parameters)
specifier|protected
name|void
name|afterConfiguration
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|cxfEndpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|endpoint
decl_stmt|;
name|cxfEndpoint
operator|.
name|updateEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

