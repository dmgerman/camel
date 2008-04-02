begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.provider
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|provider
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
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPBody
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPBodyElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPEnvelope
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPPart
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

begin_comment
comment|/**  * A simple bean demonstrating the processing of a SOAPMessage routed by Camel  *  */
end_comment

begin_comment
comment|//START SNIPPET: e1
end_comment

begin_class
DECL|class|TesterBean
specifier|public
class|class
name|TesterBean
block|{
DECL|method|processSOAP (Exchange exchange)
specifier|public
name|SOAPMessage
name|processSOAP
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Since the Camel-CXF endpoint uses a list to store the parameters
comment|// and bean component uses the bodyAs expression to get the value
comment|// we'll need to deal with the parameters ourself
name|SOAPMessage
name|soapMessage
init|=
operator|(
name|SOAPMessage
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapMessage
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Incoming null message detected..."
argument_list|)
expr_stmt|;
return|return
name|createDefaultSoapMessage
argument_list|(
literal|"Greetings from Apache Camel!!!!"
argument_list|,
literal|"null"
argument_list|)
return|;
block|}
try|try
block|{
name|SOAPPart
name|sp
init|=
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|SOAPEnvelope
name|se
init|=
name|sp
operator|.
name|getEnvelope
argument_list|()
decl_stmt|;
name|SOAPBody
name|sb
init|=
name|se
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|requestText
init|=
name|sb
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|requestText
argument_list|)
expr_stmt|;
return|return
name|createDefaultSoapMessage
argument_list|(
literal|"Greetings from Apache Camel!!!!"
argument_list|,
name|requestText
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
name|createDefaultSoapMessage
argument_list|(
literal|"Greetings from Apache Camel!!!!"
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|createDefaultSoapMessage (String responseMessage, String requestMessage)
specifier|public
specifier|static
name|SOAPMessage
name|createDefaultSoapMessage
parameter_list|(
name|String
name|responseMessage
parameter_list|,
name|String
name|requestMessage
parameter_list|)
block|{
try|try
block|{
name|SOAPMessage
name|soapMessage
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|SOAPBody
name|body
init|=
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|QName
name|payloadName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
literal|"greetMeResponse"
argument_list|,
literal|"ns1"
argument_list|)
decl_stmt|;
name|SOAPBodyElement
name|payload
init|=
name|body
operator|.
name|addBodyElement
argument_list|(
name|payloadName
argument_list|)
decl_stmt|;
name|SOAPElement
name|message
init|=
name|payload
operator|.
name|addChildElement
argument_list|(
literal|"responseType"
argument_list|)
decl_stmt|;
name|message
operator|.
name|addTextNode
argument_list|(
name|responseMessage
operator|+
literal|" Request was  "
operator|+
name|requestMessage
argument_list|)
expr_stmt|;
return|return
name|soapMessage
return|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: e1
end_comment

end_unit

