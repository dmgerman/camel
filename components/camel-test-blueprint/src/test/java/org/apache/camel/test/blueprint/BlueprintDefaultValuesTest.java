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
name|java
operator|.
name|util
operator|.
name|Properties
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

begin_comment
comment|/**  * A test showing that Blueprint XML property placeholders work correctly with  * placeholders containing colon in the key.  */
end_comment

begin_class
DECL|class|BlueprintDefaultValuesTest
specifier|public
class|class
name|BlueprintDefaultValuesTest
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
literal|"org/apache/camel/test/blueprint/default-values.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testPropertyResolutionFailure ()
specifier|public
name|void
name|testPropertyResolutionFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"dot=my.value colon=my:value"
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
annotation|@
name|Override
DECL|method|useOverridePropertiesWithPropertiesComponent ()
specifier|protected
name|Properties
name|useOverridePropertiesWithPropertiesComponent
parameter_list|()
block|{
name|Properties
name|extra
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|extra
operator|.
name|put
argument_list|(
literal|"my:key"
argument_list|,
literal|"my:value"
argument_list|)
expr_stmt|;
return|return
name|extra
return|;
block|}
block|}
end_class

end_unit

