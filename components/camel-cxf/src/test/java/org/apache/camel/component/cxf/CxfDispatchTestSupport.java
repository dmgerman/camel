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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Base class for testing arbitrary payload  */
end_comment

begin_class
DECL|class|CxfDispatchTestSupport
specifier|public
specifier|abstract
class|class
name|CxfDispatchTestSupport
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|DISPATCH_NS
specifier|protected
specifier|static
specifier|final
name|String
name|DISPATCH_NS
init|=
literal|"http://camel.apache.org/cxf/jaxws/dispatch"
decl_stmt|;
DECL|field|INVOKE_NAME
specifier|protected
specifier|static
specifier|final
name|String
name|INVOKE_NAME
init|=
literal|"Invoke"
decl_stmt|;
DECL|field|INVOKE_ONEWAY_NAME
specifier|protected
specifier|static
specifier|final
name|String
name|INVOKE_ONEWAY_NAME
init|=
literal|"InvokeOneWay"
decl_stmt|;
DECL|field|PAYLOAD_TEMPLATE
specifier|protected
specifier|static
specifier|final
name|String
name|PAYLOAD_TEMPLATE
init|=
literal|"<ns1:greetMe xmlns:ns1=\"http://apache.org/hello_world_soap_http/types\"><ns1:requestType>%s</ns1:requestType></ns1:greetMe>"
decl_stmt|;
DECL|field|PAYLOAD_ONEWAY_TEMPLATE
specifier|protected
specifier|static
specifier|final
name|String
name|PAYLOAD_ONEWAY_TEMPLATE
init|=
literal|"<ns1:greetMeOneWay xmlns:ns1=\"http://apache.org/hello_world_soap_http/types\"><ns1:requestType>%s</ns1:requestType></ns1:greetMeOneWay>"
decl_stmt|;
DECL|field|MESSAGE_TEMPLATE
specifier|protected
specifier|static
specifier|final
name|String
name|MESSAGE_TEMPLATE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>"
operator|+
name|PAYLOAD_TEMPLATE
operator|+
literal|"</soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|MESSAGE_ONEWAY_TEMPLATE
specifier|protected
specifier|static
specifier|final
name|String
name|MESSAGE_ONEWAY_TEMPLATE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>"
operator|+
name|PAYLOAD_ONEWAY_TEMPLATE
operator|+
literal|"</soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|documentBuilderFactory
specifier|private
specifier|static
name|DocumentBuilderFactory
name|documentBuilderFactory
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Before
DECL|method|startService ()
specifier|public
name|void
name|startService
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopService ()
specifier|public
name|void
name|stopService
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getResponseType (Element node)
specifier|protected
specifier|static
name|String
name|getResponseType
parameter_list|(
name|Element
name|node
parameter_list|)
block|{
name|NodeList
name|nodes
init|=
name|node
operator|.
name|getElementsByTagNameNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"responseType"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|!=
literal|null
operator|&&
name|nodes
operator|.
name|getLength
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Node
name|c
init|=
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
operator|.
name|getNodeValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getDocumentBuilderFactory ()
specifier|protected
specifier|static
specifier|synchronized
name|DocumentBuilderFactory
name|getDocumentBuilderFactory
parameter_list|()
block|{
if|if
condition|(
name|documentBuilderFactory
operator|==
literal|null
condition|)
block|{
name|documentBuilderFactory
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|documentBuilderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|documentBuilderFactory
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|documentBuilderFactory
operator|.
name|setIgnoringComments
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|documentBuilderFactory
return|;
block|}
block|}
end_class

end_unit

