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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
import|;
end_import

begin_class
DECL|class|CxfConsumerPayloadTest
specifier|public
class|class
name|CxfConsumerPayloadTest
extends|extends
name|CxfConsumerTest
block|{
DECL|field|ECHO_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_RESPONSE
init|=
literal|"<ns1:echoResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">echo Hello World!</return>"
operator|+
literal|"</ns1:echoResponse>"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_RESPONSE
init|=
literal|"<ns1:echoBooleanResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">true</return>"
operator|+
literal|"</ns1:echoBooleanResponse>"
decl_stmt|;
DECL|field|ECHO_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_REQUEST
init|=
literal|"<ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">Hello World!</arg0></ns1:echo>"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_REQUEST
init|=
literal|"<ns1:echoBoolean xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">true</arg0></ns1:echoBoolean>"
decl_stmt|;
comment|// START SNIPPET: payload
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
name|SIMPLE_ENDPOINT_URI
operator|+
literal|"&dataFormat=PAYLOAD"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
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
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|requestPayload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|inElements
init|=
name|requestPayload
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|outElements
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
comment|// You can use a customer toStringConverter to turn a CxfPayLoad message into String as you want
name|String
name|request
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
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|String
name|documentString
init|=
name|ECHO_RESPONSE
decl_stmt|;
if|if
condition|(
name|inElements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"echoBoolean"
argument_list|)
condition|)
block|{
name|documentString
operator|=
name|ECHO_BOOLEAN_RESPONSE
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong request"
argument_list|,
name|ECHO_BOOLEAN_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"Get a wrong request"
argument_list|,
name|ECHO_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
name|Document
name|outDocument
init|=
name|converter
operator|.
name|toDOMDocument
argument_list|(
name|documentString
argument_list|)
decl_stmt|;
name|outElements
operator|.
name|add
argument_list|(
name|outDocument
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
comment|// set the payload header with null
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|responsePayload
init|=
operator|new
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
argument_list|(
literal|null
argument_list|,
name|outElements
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|responsePayload
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
comment|// END SNIPPET: payload
block|}
end_class

end_unit

