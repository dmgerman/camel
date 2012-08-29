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
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|DefaultHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
DECL|class|CxfConusmerNamespacePayLoadTest
specifier|public
class|class
name|CxfConusmerNamespacePayLoadTest
extends|extends
name|CxfConsumerPayloadTest
block|{
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
DECL|field|ECHO_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_REQUEST
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
operator|+
literal|"xmlns:ns1=\"http://cxf.component.camel.apache.org/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
operator|+
literal|"xmlns:ns2=\"http://cxf.component.camel.apache.org/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body>"
operator|+
literal|"<ns1:echo><ns2:arg0 xsi:type=\"xsd:string\">Hello World!</ns2:arg0></ns1:echo></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|method|checkRequest (String expect, String request)
specifier|protected
name|void
name|checkRequest
parameter_list|(
name|String
name|expect
parameter_list|,
name|String
name|request
parameter_list|)
block|{
if|if
condition|(
name|expect
operator|.
name|equals
argument_list|(
name|ECHO_REQUEST
argument_list|)
condition|)
block|{
comment|// just check the namespace of xsd
name|assertTrue
argument_list|(
literal|"Expect to find the namesapce"
argument_list|,
name|request
operator|.
name|indexOf
argument_list|(
literal|"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInvokingServiceFromClient ()
specifier|public
name|void
name|testInvokingServiceFromClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// just send a request which has all the namespace in the soap header
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
name|simpleEndpointAddress
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|ECHO_REQUEST
argument_list|,
name|ContentType
operator|.
name|create
argument_list|(
literal|"text/xml"
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
decl_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|responseBody
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
name|ECHO_RESPONSE
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|getConnectionManager
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

