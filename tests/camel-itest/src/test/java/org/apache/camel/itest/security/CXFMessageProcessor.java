begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|SOAPMessage
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|CXFMessageProcessor
specifier|public
class|class
name|CXFMessageProcessor
implements|implements
name|Processor
block|{
DECL|field|RESPONSE
specifier|static
specifier|final
name|String
name|RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><greetMeResponse xmlns=\"http://apache.org/hello_world_soap_http/types\">"
operator|+
literal|"<responseType> Hello CXF</responseType></greetMeResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|method|process (Exchange exchange)
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
comment|// just print out the request message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// just make sure the request is greetme
name|assertTrue
argument_list|(
literal|"It should be GreetMe request."
argument_list|,
name|request
operator|.
name|indexOf
argument_list|(
literal|"<greetMe"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|RESPONSE
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|SOAPMessage
name|message
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

