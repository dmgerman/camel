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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfPayloadConsumerDuplicateNamespaceStreamCacheTest
specifier|public
class|class
name|CxfPayloadConsumerDuplicateNamespaceStreamCacheTest
extends|extends
name|CxfPayloadConsumerNamespaceOnEnvelopeStreamCacheTest
block|{
comment|/*      * The soap namespace prefix is already defined on the root tag of the      * payload. If this is set another time from the envelope, the result will      * be an invalid XML.      */
DECL|field|REQUEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
operator|+
literal|"<soap:Body><ns2:getToken xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns2=\"http://camel.apache.org/cxf/namespace\">"
operator|+
literal|"<arg0 xsi:type=\"xs:string\">Send</arg0></ns2:getToken></soap:Body></soap:Envelope>"
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testInvokeRouter ()
specifier|public
name|void
name|testInvokeRouter
parameter_list|()
block|{
name|Object
name|returnValue
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:router"
argument_list|,
name|REQUEST_MESSAGE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|returnValue
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|returnValue
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|returnValue
operator|)
operator|.
name|contains
argument_list|(
literal|"Return Value"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|returnValue
operator|)
operator|.
name|contains
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

