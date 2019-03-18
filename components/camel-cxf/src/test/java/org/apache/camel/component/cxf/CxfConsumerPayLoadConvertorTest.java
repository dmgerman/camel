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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
DECL|class|CxfConsumerPayLoadConvertorTest
specifier|public
class|class
name|CxfConsumerPayLoadConvertorTest
extends|extends
name|CxfConsumerPayloadTest
block|{
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
name|simpleEndpointURI
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
name|Source
argument_list|>
name|inElements
init|=
name|requestPayload
operator|.
name|getBodySources
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
name|String
name|documentString
init|=
name|ECHO_RESPONSE
decl_stmt|;
name|Element
name|in
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMElement
argument_list|(
name|inElements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
comment|// Just check the element namespace
if|if
condition|(
operator|!
name|in
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|ELEMENT_NAMESPACE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong element namespace"
argument_list|)
throw|;
block|}
if|if
condition|(
name|in
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
name|checkRequest
argument_list|(
literal|"ECHO_BOOLEAN_REQUEST"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|documentString
operator|=
name|ECHO_RESPONSE
expr_stmt|;
name|checkRequest
argument_list|(
literal|"ECHO_REQUEST"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
comment|// just set the documentString into to the message body
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|documentString
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

