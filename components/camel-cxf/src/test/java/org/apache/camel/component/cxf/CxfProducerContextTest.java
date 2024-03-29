begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|endpoint
operator|.
name|Client
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|// We use context to change the producer's endpoint address here
end_comment

begin_class
DECL|class|CxfProducerContextTest
specifier|public
class|class
name|CxfProducerContextTest
extends|extends
name|CxfProducerTest
block|{
comment|// *** This class extends CxfProducerTest, so see that class for other tests
comment|// run by this code
DECL|field|TEST_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"sendSimpleMessage-test"
decl_stmt|;
DECL|field|TEST_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"exchange property value should get passed through request context"
decl_stmt|;
annotation|@
name|Test
DECL|method|testExchangePropertyPropagation ()
specifier|public
name|void
name|testExchangePropertyPropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendSimpleMessage
argument_list|()
decl_stmt|;
comment|// No direct access to native CXF Message but we can verify the
comment|// request context from the Camel exchange
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|actualValue
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|TEST_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"exchange property should get propagated to the request context"
argument_list|,
name|TEST_VALUE
argument_list|,
name|actualValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSimpleEndpointUri ()
specifier|protected
name|String
name|getSimpleEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort4
argument_list|()
operator|+
literal|"/CxfProducerContextTest/simple?serviceClass=org.apache.camel.component.cxf.HelloService"
return|;
block|}
annotation|@
name|Override
DECL|method|getJaxwsEndpointUri ()
specifier|protected
name|String
name|getJaxwsEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort4
argument_list|()
operator|+
literal|"/CxfProducerContextTest/jaxws?serviceClass=org.apache.hello_world_soap_http.Greeter"
return|;
block|}
annotation|@
name|Override
DECL|method|sendSimpleMessage ()
specifier|protected
name|Exchange
name|sendSimpleMessage
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|getSimpleServerAddress
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|ECHO_OPERATION
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"requestObject"
argument_list|,
operator|new
name|DefaultCxfBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|sendJaxWsMessage ()
specifier|protected
name|Exchange
name|sendJaxWsMessage
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|getJaxwsEndpointUri
argument_list|()
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|getJaxWsServerAddress
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|GREET_ME_OPERATION
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

