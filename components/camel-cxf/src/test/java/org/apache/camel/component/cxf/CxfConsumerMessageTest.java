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

begin_class
DECL|class|CxfConsumerMessageTest
specifier|public
class|class
name|CxfConsumerMessageTest
extends|extends
name|CxfConsumerTest
block|{
DECL|field|ECHO_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_METHOD
init|=
literal|"ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\""
decl_stmt|;
DECL|field|ECHO_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">echo Hello World!</return>"
operator|+
literal|"</ns1:echoResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoBooleanResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">true</return>"
operator|+
literal|"</ns1:echoBooleanResponse></soap:Body></soap:Envelope>"
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
name|SIMPLE_ENDPOINT_URI
operator|+
literal|"&dataFormat=MESSAGE"
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
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Get the request message
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
comment|// Send the response message back
if|if
condition|(
name|request
operator|.
name|indexOf
argument_list|(
name|ECHO_METHOD
argument_list|)
operator|>
literal|0
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_RESPONSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// echoBoolean call
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_BOOLEAN_RESPONSE
argument_list|)
expr_stmt|;
block|}
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

