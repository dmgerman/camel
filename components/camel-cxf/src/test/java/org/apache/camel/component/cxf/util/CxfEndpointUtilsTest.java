begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
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
name|util
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|component
operator|.
name|cxf
operator|.
name|CxfComponent
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
name|CxfEndpoint
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
name|DataFormat
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
name|Test
import|;
end_import

begin_class
DECL|class|CxfEndpointUtilsTest
specifier|public
class|class
name|CxfEndpointUtilsTest
extends|extends
name|Assert
block|{
comment|// set up the port name and service name
DECL|field|SERVICE_NAME
specifier|protected
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.example.com/test"
argument_list|,
literal|"ServiceName"
argument_list|)
decl_stmt|;
DECL|field|PORT_NAME
specifier|protected
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://www.example.com/test"
argument_list|,
literal|"PortName"
argument_list|)
decl_stmt|;
DECL|field|CXF_BASE_URI
specifier|private
specifier|static
specifier|final
name|String
name|CXF_BASE_URI
init|=
literal|"cxf://http://www.example.com/testaddress"
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
operator|+
literal|"&portName={http://www.example.com/test}PortName"
operator|+
literal|"&serviceName={http://www.example.com/test}ServiceName"
operator|+
literal|"&defaultBus=true"
decl_stmt|;
DECL|field|NO_SERVICE_CLASS_URI
specifier|private
specifier|static
specifier|final
name|String
name|NO_SERVICE_CLASS_URI
init|=
literal|"cxf://http://www.example.com/testaddress"
operator|+
literal|"?portName={http://www.example.com/test}PortName"
operator|+
literal|"&serviceName={http://www.example.com/test}ServiceName"
decl_stmt|;
DECL|method|getEndpointURI ()
specifier|protected
name|String
name|getEndpointURI
parameter_list|()
block|{
return|return
name|CXF_BASE_URI
return|;
block|}
DECL|method|getNoServiceClassURI ()
specifier|protected
name|String
name|getNoServiceClassURI
parameter_list|()
block|{
return|return
name|NO_SERVICE_CLASS_URI
return|;
block|}
DECL|method|getCamelContext ()
specifier|protected
name|CamelContext
name|getCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|createEndpoint (String uri)
specifier|protected
name|CxfEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
return|return
operator|(
name|CxfEndpoint
operator|)
operator|new
name|CxfComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testGetProperties ()
specifier|public
name|void
name|testGetProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
argument_list|)
decl_stmt|;
name|QName
name|service
init|=
name|endpoint
operator|.
name|getServiceName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right service name"
argument_list|,
name|service
argument_list|,
name|SERVICE_NAME
argument_list|)
expr_stmt|;
block|}
DECL|method|sepChar ()
specifier|public
name|char
name|sepChar
parameter_list|()
block|{
return|return
literal|'&'
return|;
block|}
annotation|@
name|Test
DECL|method|testGetDataFormatCXF ()
specifier|public
name|void
name|testGetDataFormatCXF
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
operator|+
name|sepChar
argument_list|()
operator|+
literal|"dataFormat=CXF_MESSAGE"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the Message DataFormat"
argument_list|,
name|DataFormat
operator|.
name|CXF_MESSAGE
argument_list|,
name|endpoint
operator|.
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetDataFormatRAW ()
specifier|public
name|void
name|testGetDataFormatRAW
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getEndpointURI
argument_list|()
operator|+
name|sepChar
argument_list|()
operator|+
literal|"dataFormat=RAW"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the Message DataFormat"
argument_list|,
name|DataFormat
operator|.
name|RAW
argument_list|,
name|endpoint
operator|.
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCheckServiceClassWithTheEndpoint ()
specifier|public
name|void
name|testCheckServiceClassWithTheEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getNoServiceClassURI
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCheckServiceClassProcedure ()
specifier|public
name|void
name|testCheckServiceClassProcedure
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getNoServiceClassURI
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|createProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCheckServiceClassConsumer ()
specifier|public
name|void
name|testCheckServiceClassConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|getNoServiceClassURI
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|exception
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Should get a CamelException here"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"serviceClass must be specified"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

