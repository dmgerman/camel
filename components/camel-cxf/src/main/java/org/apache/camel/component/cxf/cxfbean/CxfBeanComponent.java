begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
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
operator|.
name|cxfbean
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
name|List
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
name|HeaderFilterStrategyComponent
import|;
end_import

begin_comment
comment|/**  * CXF Bean component creates {@link CxfBeanEndpoint} which represents a  * bean.<b>Currently, only JAXRS annotated beans are supported.  In the  * future, JAXwS annotated beans and POJO can be supported</b>.  *   * @version   */
end_comment

begin_class
DECL|class|CxfBeanComponent
specifier|public
class|class
name|CxfBeanComponent
extends|extends
name|HeaderFilterStrategyComponent
block|{
DECL|field|endpoints
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CxfBeanEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|CxfBeanEndpoint
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
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
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Extract the comma separated list of providers in advance of the auto-extraction
comment|// that a DefaultEndpoint will perform (as the default one does not understand lists).
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
literal|"providers"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfBeanEndpoint
name|answer
init|=
operator|new
name|CxfBeanEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
block|}
name|setEndpointHeaderFilterStrategy
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// add to the endpoints map before calling the endpoint's init() method to
comment|// make sure the the CxfBeanDestination activate() method can find the endpoint
comment|// from the map.
name|endpoints
operator|.
name|put
argument_list|(
name|answer
operator|.
name|createEndpointUri
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
comment|// we invoke setProperties ourselves so the bus is set for CxfBeanEndpoint.init()
return|return
literal|false
return|;
block|}
DECL|method|getEndpoint (String endpointUri)
specifier|public
name|CxfBeanEndpoint
name|getEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
return|return
name|endpoints
operator|.
name|get
argument_list|(
name|endpointUri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

