begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.mtom
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
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|Holder
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
name|soap
operator|.
name|SOAPBinding
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
name|mtom_feature
operator|.
name|Hello
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
name|mtom_feature
operator|.
name|HelloService
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * Unit test for exercising MTOM enabled end-to-end router in PAYLOAD mode  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfMtomRouterPayloadModeTest
specifier|public
class|class
name|CxfMtomRouterPayloadModeTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|port1
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|static
name|int
name|port2
init|=
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
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
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/jaxws-mtom/hello"
argument_list|,
name|getImpl
argument_list|()
argument_list|)
expr_stmt|;
name|SOAPBinding
name|binding
init|=
operator|(
name|SOAPBinding
operator|)
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
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
if|if
condition|(
name|MtomTestHelper
operator|.
name|isAwtHeadless
argument_list|(
name|logger
argument_list|,
literal|null
argument_list|)
condition|)
block|{
return|return;
block|}
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
init|=
operator|new
name|Holder
argument_list|<>
argument_list|(
name|getImage
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|Hello
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|SOAPBinding
name|binding
init|=
call|(
name|SOAPBinding
call|)
argument_list|(
operator|(
name|BindingProvider
operator|)
name|port
argument_list|)
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setMTOMEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|port
operator|.
name|detail
argument_list|(
name|photo
argument_list|,
name|image
argument_list|)
expr_stmt|;
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|RESP_PHOTO_DATA
argument_list|,
name|photo
operator|.
name|value
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|image
operator|.
name|value
operator|instanceof
name|BufferedImage
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|560
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image
operator|.
name|value
operator|)
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|300
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image
operator|.
name|value
operator|)
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getPort ()
specifier|protected
name|Hello
name|getPort
parameter_list|()
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/mtom.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|HelloService
name|service
init|=
operator|new
name|HelloService
argument_list|(
name|wsdl
argument_list|,
name|HelloService
operator|.
name|SERVICE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Hello
name|port
init|=
name|service
operator|.
name|getHelloPort
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/CxfMtomRouterPayloadModeTest/jaxws-mtom/hello"
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
DECL|method|getImage (String name)
specifier|private
name|Image
name|getImage
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ImageIO
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getImpl ()
specifier|protected
name|Object
name|getImpl
parameter_list|()
block|{
return|return
operator|new
name|HelloImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

