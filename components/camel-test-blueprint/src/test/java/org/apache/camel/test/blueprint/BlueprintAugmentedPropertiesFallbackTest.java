begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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

begin_comment
comment|/**  * A test showing that Blueprint XML property placeholders work correctly with  * augmented property names and fallback behavior.  */
end_comment

begin_class
DECL|class|BlueprintAugmentedPropertiesFallbackTest
specifier|public
class|class
name|BlueprintAugmentedPropertiesFallbackTest
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
literal|"org/apache/camel/test/blueprint/augmented-properties-fallback.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|loadConfigAdminConfigurationFile ()
specifier|protected
name|String
index|[]
name|loadConfigAdminConfigurationFile
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/etc/augmented.fallback.cfg"
block|,
literal|"augmented.fallback"
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testFallbackToUnaugmentedProperty ()
specifier|public
name|void
name|testFallbackToUnaugmentedProperty
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*          * mock:result only gets the expected body if BlueprintPropertiesParser          * correctly fell back from "TEST.destination" to "destination"          */
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"fallbackToUnaugmentedProperty is working"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

