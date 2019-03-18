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
name|List
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
name|ProducerTemplate
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
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
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
name|BadRecordLitFault
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
name|Assert
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfProducerSoapFaultTest
specifier|public
class|class
name|CxfProducerSoapFaultTest
extends|extends
name|Assert
block|{
DECL|field|JAXWS_SERVER_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|JAXWS_SERVER_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfProducerSoapFaultTest/test"
decl_stmt|;
DECL|field|JAXWS_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|JAXWS_ENDPOINT_URI
init|=
literal|"cxf://"
operator|+
name|JAXWS_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter"
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
throws|throws
name|Exception
block|{
name|GreeterImpl
name|greeterImpl
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JAXWS_SERVER_ADDRESS
argument_list|,
name|greeterImpl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncSoapFault ()
specifier|public
name|void
name|testAsyncSoapFault
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeSoapFault
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSyncSoapFault ()
specifier|public
name|void
name|testSyncSoapFault
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeSoapFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeSoapFault (boolean sync)
specifier|private
name|void
name|invokeSoapFault
parameter_list|(
name|boolean
name|sync
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|cxfEndpointURI
init|=
name|JAXWS_ENDPOINT_URI
decl_stmt|;
if|if
condition|(
name|sync
condition|)
block|{
name|cxfEndpointURI
operator|=
name|cxfEndpointURI
operator|+
literal|"&synchronous=true"
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|sendJaxWsMessage
argument_list|(
name|cxfEndpointURI
argument_list|,
literal|"BadRecordLitFault"
argument_list|,
literal|"testDocLitFault"
argument_list|)
decl_stmt|;
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
comment|// assert we got the exception first
name|assertNotNull
argument_list|(
literal|"except to get the exception"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong soap fault"
argument_list|,
name|exception
operator|instanceof
name|BadRecordLitFault
argument_list|)
expr_stmt|;
comment|// check out the message header which is copied from in message
name|String
name|fileName
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the file name from out message header"
argument_list|,
literal|"testFile"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
DECL|method|sendJaxWsMessage (final String uri, final String message, final String operation)
specifier|private
name|Exchange
name|sendJaxWsMessage
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|String
name|operation
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|uri
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
name|message
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
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|operation
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

