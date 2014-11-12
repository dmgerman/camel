begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|language
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
name|CamelContext
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
name|ComponentConfiguration
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
name|ContextTestSupport
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
name|EndpointConfiguration
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
name|impl
operator|.
name|DefaultCamelContext
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
DECL|class|LanguageComponentConfigurationAndDocumentationTest
specifier|public
class|class
name|LanguageComponentConfigurationAndDocumentationTest
extends|extends
name|ContextTestSupport
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
DECL|method|testComponentConfiguration ()
specifier|public
name|void
name|testComponentConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|LanguageComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"language"
argument_list|,
name|LanguageComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointConfiguration
name|conf
init|=
name|comp
operator|.
name|createConfiguration
argument_list|(
literal|"language:simple:foo?transform=false"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|conf
operator|.
name|getParameter
argument_list|(
literal|"transform"
argument_list|)
argument_list|)
expr_stmt|;
name|ComponentConfiguration
name|compConf
init|=
name|comp
operator|.
name|createComponentConfiguration
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|compConf
operator|.
name|createParameterJsonSchema
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"script\": { \"kind\": \"parameter\", \"type\": \"string\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"cacheScript\": { \"kind\": \"parameter\", \"type\": \"boolean\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentDocumentation ()
specifier|public
name|void
name|testComponentDocumentation
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|String
name|html
init|=
name|context
operator|.
name|getComponentDocumentation
argument_list|(
literal|"language"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found some auto-generated HTML"
argument_list|,
name|html
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

