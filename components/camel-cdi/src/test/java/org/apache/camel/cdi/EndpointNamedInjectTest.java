begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Endpoint
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
comment|/**  * Test endpoint injection by @Named  */
end_comment

begin_class
DECL|class|EndpointNamedInjectTest
specifier|public
class|class
name|EndpointNamedInjectTest
extends|extends
name|CdiTestSupport
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"myNamedEndpoint"
argument_list|)
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldInjectEndpoint ()
specifier|public
name|void
name|shouldInjectEndpoint
parameter_list|()
block|{
name|assertNotNull
argument_list|(
literal|"Could not find injected endpoint!"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Endpoint URI"
argument_list|,
literal|"mock:nameInjected"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

