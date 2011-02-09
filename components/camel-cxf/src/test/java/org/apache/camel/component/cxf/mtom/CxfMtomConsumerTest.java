begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URL
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
name|Message
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfMtomConsumerTest
specifier|public
class|class
name|CxfMtomConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|MTOM_ENDPOINT_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|MTOM_ENDPOINT_ADDRESS
init|=
literal|"http://localhost:9091/jaxws-mtom/hello"
decl_stmt|;
DECL|field|MTOM_ENDPOINT_URI
specifier|protected
specifier|static
specifier|final
name|String
name|MTOM_ENDPOINT_URI
init|=
literal|"cxf://"
operator|+
name|MTOM_ENDPOINT_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloImpl"
decl_stmt|;
DECL|field|serviceName
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/camel/cxf/mtom_feature"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
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
name|MTOM_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should not get any attachements here."
argument_list|,
literal|0
argument_list|,
name|in
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get the parameter list
name|List
argument_list|<
name|?
argument_list|>
name|parameter
init|=
name|in
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get the operation name
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
init|=
operator|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
operator|)
name|parameter
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The photo should not be null"
argument_list|,
name|photo
operator|.
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the right request"
argument_list|,
operator|new
name|String
argument_list|(
name|photo
operator|.
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|,
literal|"RequestFromCXF"
argument_list|)
expr_stmt|;
name|photo
operator|.
name|value
operator|=
literal|"ResponseFromCamel"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
init|=
operator|(
name|Holder
argument_list|<
name|Image
argument_list|>
operator|)
name|parameter
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the image here"
argument_list|,
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
comment|// set the holder message back
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|null
block|,
name|photo
block|,
name|image
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getPort ()
specifier|private
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
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
return|return
name|service
operator|.
name|getHelloPort
argument_list|()
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
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"java.awt.headless"
argument_list|)
operator|||
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Mac OS"
argument_list|)
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"cruise"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running headless. Skipping test as Images may not work."
argument_list|)
expr_stmt|;
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
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|"RequestFromCXF"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
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
argument_list|<
name|Image
argument_list|>
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
name|assertEquals
argument_list|(
literal|"ResponseFromCamel"
argument_list|,
operator|new
name|String
argument_list|(
name|photo
operator|.
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

