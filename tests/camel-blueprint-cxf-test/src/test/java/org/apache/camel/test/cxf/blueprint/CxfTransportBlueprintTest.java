begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cxf
operator|.
name|blueprint
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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
DECL|class|CxfTransportBlueprintTest
specifier|public
class|class
name|CxfTransportBlueprintTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/cxf/blueprint/CxfTransportBeans.xml"
return|;
block|}
annotation|@
name|Override
comment|// camel-cxf blueprint schema doesn't publihsed yet
DECL|method|getBundleDirectives ()
specifier|protected
name|String
name|getBundleDirectives
parameter_list|()
block|{
return|return
literal|"blueprint.aries.xml-validation:=false"
return|;
block|}
annotation|@
name|Test
DECL|method|testPublishEndpointUrl ()
specifier|public
name|void
name|testPublishEndpointUrl
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|request
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body>"
operator|+
literal|"<ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">hello world</arg0>"
operator|+
literal|"</ns1:echo></soap:Body></soap:Envelope>"
decl_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:client"
argument_list|,
name|request
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get some response here"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong response."
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
literal|"hello world"
argument_list|)
operator|>
literal|0
operator|&&
name|response
operator|.
name|indexOf
argument_list|(
literal|"echoResponse"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

