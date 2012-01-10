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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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

begin_comment
comment|/**  * A unit test for java only CXF in payload mode  *   * @version   */
end_comment

begin_class
DECL|class|CxfJavaOnlyPayloadModeTest
specifier|public
class|class
name|CxfJavaOnlyPayloadModeTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"cxf://http://localhost:"
operator|+
name|port1
operator|+
literal|"/CxfJavaOnlyPayloadModeTest/helloworld"
operator|+
literal|"?wsdlURL=classpath:person.wsdl"
operator|+
literal|"&serviceName={http://camel.apache.org/wsdl-first}PersonService"
operator|+
literal|"&portName={http://camel.apache.org/wsdl-first}soap"
operator|+
literal|"&dataFormat=PAYLOAD"
operator|+
literal|"&properties.exceptionMessageCauseEnabled=true&properties.faultStackTraceEnabled=true"
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
name|Test
DECL|method|testCxfJavaOnly ()
specifier|public
name|void
name|testCxfJavaOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|"<GetPerson xmlns=\"http://camel.apache.org/wsdl-first/types\"><personId>123</personId></GetPerson>"
decl_stmt|;
name|Document
name|xml
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|Object
name|output
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|url
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
comment|// using CxfPayload in payload mode
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|payload
init|=
operator|(
name|CxfPayload
argument_list|<
name|?
argument_list|>
operator|)
name|output
decl_stmt|;
comment|// convert the payload body to string
name|String
name|reply
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|payload
operator|.
name|getBody
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|contains
argument_list|(
literal|"<personId>123</personId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|contains
argument_list|(
literal|"<ssn>456</ssn"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|contains
argument_list|(
literal|"<name>Donald Duck</name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|url
argument_list|)
operator|.
name|process
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
name|String
name|s
init|=
literal|"<GetPersonResponse xmlns=\"http://camel.apache.org/wsdl-first/types\">"
operator|+
literal|"<personId>123</personId><ssn>456</ssn><name>Donald Duck</name>"
operator|+
literal|"</GetPersonResponse>"
decl_stmt|;
name|Document
name|xml
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|xml
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
block|}
end_class

end_unit

