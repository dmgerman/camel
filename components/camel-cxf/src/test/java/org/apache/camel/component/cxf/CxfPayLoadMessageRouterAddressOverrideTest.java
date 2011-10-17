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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Exchange
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
name|RouteBuilder
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_comment
comment|/**  * A unit test for testing reading SOAP body with address override in PAYLOAD mode.  *   * @version   */
end_comment

begin_class
DECL|class|CxfPayLoadMessageRouterAddressOverrideTest
specifier|public
class|class
name|CxfPayLoadMessageRouterAddressOverrideTest
extends|extends
name|CxfPayLoadMessageRouterTest
block|{
DECL|field|routerEndpointURI
specifier|private
name|String
name|routerEndpointURI
init|=
literal|"cxf://"
operator|+
name|getRouterAddress
argument_list|()
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=PAYLOAD"
decl_stmt|;
DECL|field|serviceEndpointURI
specifier|private
name|String
name|serviceEndpointURI
init|=
literal|"cxf://http://localhost:9002/badAddress"
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=PAYLOAD"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|routerEndpointURI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|DESTINATION_OVERRIDE_URL
argument_list|,
name|getServiceAddress
argument_list|()
argument_list|)
expr_stmt|;
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Source
argument_list|>
name|elements
init|=
name|payload
operator|.
name|getBodySources
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the elements here"
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong elements size"
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Element
name|el
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMElement
argument_list|(
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong namespace URI"
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"http://cxf.component.camel.apache.org/"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|serviceEndpointURI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

