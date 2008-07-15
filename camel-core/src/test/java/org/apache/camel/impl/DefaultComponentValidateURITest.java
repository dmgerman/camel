begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Endpoint
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
name|ResolveEndpointFailedException
import|;
end_import

begin_comment
comment|/**  * Unit test for URI validation when creating an endpoint  */
end_comment

begin_class
DECL|class|DefaultComponentValidateURITest
specifier|public
class|class
name|DefaultComponentValidateURITest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNoParameters ()
specifier|public
name|void
name|testNoParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer://foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created an endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoQuestionMarker ()
specifier|public
name|void
name|testNoQuestionMarker
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer://foo&fixedRate=true&delay=0&period=500"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
DECL|method|testUnknownParameter ()
specifier|public
name|void
name|testUnknownParameter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer://foo?delay=250&unknown=1&period=500"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
DECL|method|testDoubleAmpersand ()
specifier|public
name|void
name|testDoubleAmpersand
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer://foo?delay=250&&period=500"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
block|}
end_class

end_unit

