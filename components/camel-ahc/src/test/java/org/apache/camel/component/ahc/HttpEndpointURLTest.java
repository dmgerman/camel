begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|junit5
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|HttpEndpointURLTest
specifier|public
class|class
name|HttpEndpointURLTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testHttpEndpointURLWithIPv6 ()
specifier|public
name|void
name|testHttpEndpointURLWithIPv6
parameter_list|()
block|{
name|AhcEndpoint
name|endpoint
init|=
operator|(
name|AhcEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ahc://http://[2a00:8a00:6000:40::1413]:30300/test?test=true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://[2a00:8a00:6000:40::1413]:30300/test?test=true"
argument_list|,
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

