begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.multipart
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
name|multipart
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
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
name|component
operator|.
name|cxf
operator|.
name|CXFTestSupport
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
name|cxf
operator|.
name|multipart
operator|.
name|MultiPartInvoke
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
name|cxf
operator|.
name|multipart
operator|.
name|types
operator|.
name|InE
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
name|cxf
operator|.
name|multipart
operator|.
name|types
operator|.
name|ObjectFactory
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
name|spring
operator|.
name|SpringCamelContext
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
name|CamelTestSupport
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
name|IOHelper
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|CXFMultiPartTest
specifier|public
class|class
name|CXFMultiPartTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|SERVICE_NAME
specifier|public
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/cxf/multipart"
argument_list|,
literal|"MultiPartInvokeService"
argument_list|)
decl_stmt|;
DECL|field|ROUTE_PORT_NAME
specifier|public
specifier|static
specifier|final
name|QName
name|ROUTE_PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/cxf/multipart"
argument_list|,
literal|"MultiPartInvokePort"
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|static
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
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
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
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
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|MultiPartInvokeImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CXFMultiPartTest/SoapContext/SoapPort"
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
name|AfterClass
DECL|method|stopService ()
specifier|public
specifier|static
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
annotation|@
name|Test
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|invokeMultiPartService
argument_list|(
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort3
argument_list|()
operator|+
literal|"/CXFMultiPartTest/CamelContext/RouterPort"
argument_list|,
literal|"in0"
argument_list|,
literal|"in1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|equals
argument_list|(
literal|"in0 in1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|equals
argument_list|(
literal|"in0 in1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeMultiPartService (String address, String in0, String in1)
specifier|private
name|String
name|invokeMultiPartService
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|in0
parameter_list|,
name|String
name|in1
parameter_list|)
block|{
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|ROUTE_PORT_NAME
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|MultiPartInvoke
name|multiPartClient
init|=
name|service
operator|.
name|getPort
argument_list|(
name|ROUTE_PORT_NAME
argument_list|,
name|MultiPartInvoke
operator|.
name|class
argument_list|)
decl_stmt|;
name|InE
name|e0
init|=
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createInE
argument_list|()
decl_stmt|;
name|InE
name|e1
init|=
operator|new
name|ObjectFactory
argument_list|()
operator|.
name|createInE
argument_list|()
decl_stmt|;
name|e0
operator|.
name|setV
argument_list|(
name|in0
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setV
argument_list|(
name|in1
argument_list|)
expr_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<
name|InE
argument_list|>
name|h
init|=
operator|new
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<
name|InE
argument_list|>
name|h1
init|=
operator|new
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|multiPartClient
operator|.
name|foo
argument_list|(
name|e0
argument_list|,
name|e1
argument_list|,
name|h
argument_list|,
name|h1
argument_list|)
expr_stmt|;
return|return
name|h
operator|.
name|value
operator|.
name|getV
argument_list|()
operator|+
literal|" "
operator|+
name|h1
operator|.
name|value
operator|.
name|getV
argument_list|()
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/multipart/MultiPartTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

