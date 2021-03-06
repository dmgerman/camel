begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.javabody
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|javabody
package|;
end_package

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
name|component
operator|.
name|ahc
operator|.
name|AhcComponent
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
name|ahc
operator|.
name|AhcConstants
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
name|ahc
operator|.
name|BaseAhcTest
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
name|jetty
operator|.
name|JettyHttpComponent
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
name|http
operator|.
name|common
operator|.
name|HttpCommonComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|AhcProduceJavaBodyTest
specifier|public
class|class
name|AhcProduceJavaBodyTest
extends|extends
name|BaseAhcTest
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testHttpSendJavaBodyAndReceiveString ()
specifier|public
name|void
name|testHttpSendJavaBodyAndReceiveString
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpCommonComponent
name|jetty
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|HttpCommonComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AhcComponent
name|ahc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ahc
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|getTestServerEndpointUri
argument_list|()
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
name|MyCoolBean
name|cool
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MyCoolBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cool
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|cool
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|cool
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// we send back plain test
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyCoolBean
name|cool
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|123
argument_list|,
literal|"Camel"
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|getAhcEndpointUri
argument_list|()
argument_list|,
name|cool
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpSendJavaBodyAndReceiveJavaBody ()
specifier|public
name|void
name|testHttpSendJavaBodyAndReceiveJavaBody
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpCommonComponent
name|jetty
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|HttpCommonComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AhcComponent
name|ahc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ahc
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|getTestServerEndpointUri
argument_list|()
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
name|MyCoolBean
name|cool
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MyCoolBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cool
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|cool
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|cool
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|MyCoolBean
name|reply
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|456
argument_list|,
literal|"Camel rocks"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyCoolBean
name|cool
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|123
argument_list|,
literal|"Camel"
argument_list|)
decl_stmt|;
name|MyCoolBean
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|getAhcEndpointUri
argument_list|()
argument_list|,
name|cool
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|,
name|MyCoolBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|reply
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel rocks"
argument_list|,
name|reply
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpSendStringAndReceiveJavaBody ()
specifier|public
name|void
name|testHttpSendStringAndReceiveJavaBody
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpCommonComponent
name|jetty
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|HttpCommonComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AhcComponent
name|ahc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ahc
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|getTestServerEndpointUri
argument_list|()
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|MyCoolBean
name|reply
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|456
argument_list|,
literal|"Camel rocks"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyCoolBean
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|getAhcEndpointUri
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|,
name|MyCoolBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|reply
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel rocks"
argument_list|,
name|reply
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotAllowedReceive ()
specifier|public
name|void
name|testNotAllowedReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpCommonComponent
name|jetty
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|HttpCommonComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|AhcComponent
name|ahc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ahc
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|getTestServerEndpointUri
argument_list|()
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|MyCoolBean
name|reply
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|456
argument_list|,
literal|"Camel rocks"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Object
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|getAhcEndpointUri
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|MyCoolBean
name|bean
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|MyCoolBean
operator|.
name|class
argument_list|,
name|reply
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotAllowed ()
specifier|public
name|void
name|testNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyHttpComponent
name|jetty
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|JettyHttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|AhcComponent
name|ahc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ahc
operator|.
name|setAllowJavaSerializedObject
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|getTestServerEndpointUri
argument_list|()
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|MyCoolBean
name|reply
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|456
argument_list|,
literal|"Camel rocks"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyCoolBean
name|cool
init|=
operator|new
name|MyCoolBean
argument_list|(
literal|123
argument_list|,
literal|"Camel"
argument_list|)
decl_stmt|;
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|getAhcEndpointUri
argument_list|()
argument_list|,
name|cool
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|,
name|MyCoolBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Content-type application/x-java-serialized-object is not allowed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

