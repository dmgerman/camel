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
name|apache
operator|.
name|camel
operator|.
name|blueprint
operator|.
name|BlueprintCamelContext
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
name|component
operator|.
name|seda
operator|.
name|SedaEndpoint
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
DECL|class|EndpointPropertyTest
specifier|public
class|class
name|EndpointPropertyTest
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
literal|"org/apache/camel/test/blueprint/EndpointPropertyTest.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testEndpointProperty ()
specifier|public
name|void
name|testEndpointProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"ref:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"ref:bar"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|BlueprintCamelContext
name|blue
init|=
name|context
argument_list|()
operator|.
name|adapt
argument_list|(
name|BlueprintCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|SedaEndpoint
name|foo
init|=
operator|(
name|SedaEndpoint
operator|)
name|blue
operator|.
name|getBlueprintContainer
argument_list|()
operator|.
name|getComponentInstance
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|foo
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|foo
operator|.
name|getPollTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|foo
operator|.
name|isBlockWhenFull
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda://foo?blockWhenFull=true&pollTimeout=5000&size=100"
argument_list|,
name|foo
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|SedaEndpoint
name|bar
init|=
operator|(
name|SedaEndpoint
operator|)
name|blue
operator|.
name|getBlueprintContainer
argument_list|()
operator|.
name|getComponentInstance
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|bar
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda://bar?size=200"
argument_list|,
name|bar
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

