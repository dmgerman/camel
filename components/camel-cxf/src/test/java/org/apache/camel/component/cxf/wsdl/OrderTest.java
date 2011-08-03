begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wsdl
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
name|wsdl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
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
name|ExchangePattern
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
DECL|class|OrderTest
specifier|public
class|class
name|OrderTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|BeforeClass
DECL|method|loadTestSupport ()
specifier|public
specifier|static
name|void
name|loadTestSupport
parameter_list|()
block|{
comment|// Need to load the static class first
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/wsdl/camel-route.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testCamelWsdl ()
specifier|public
name|void
name|testCamelWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/camel-order/?wsdl"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkWsdl
argument_list|(
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfWsdl ()
specifier|public
name|void
name|testCxfWsdl
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/cxf-order/?wsdl"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkWsdl
argument_list|(
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|checkWsdl (InputStream in)
specifier|public
name|void
name|checkWsdl
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|containsOrderComplexType
init|=
literal|false
decl_stmt|;
name|LineNumberReader
name|reader
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
literal|"complexType name=\"order\""
argument_list|)
condition|)
block|{
name|containsOrderComplexType
operator|=
literal|true
expr_stmt|;
comment|// break;
block|}
block|}
if|if
condition|(
operator|!
name|containsOrderComplexType
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"WSDL does not contain complex type defintion for class Order"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

