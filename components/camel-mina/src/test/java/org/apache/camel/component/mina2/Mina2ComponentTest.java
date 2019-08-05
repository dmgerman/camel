begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|ResolveEndpointFailedException
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
name|test
operator|.
name|junit4
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
name|Test
import|;
end_import

begin_comment
comment|/**  * For testing various minor holes that hasn't been covered by other unit tests.  */
end_comment

begin_class
DECL|class|Mina2ComponentTest
specifier|public
class|class
name|Mina2ComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testUnknownProtocol ()
specifier|public
name|void
name|testUnknownProtocol
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:xxx://localhost:8080"
argument_list|,
literal|"mina:xxx://localhost:8080"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should be an IAE exception"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unrecognised MINA protocol: xxx for uri: mina://xxx://localhost:8080"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMistypedProtocol ()
specifier|public
name|void
name|testMistypedProtocol
parameter_list|()
block|{
try|try
block|{
comment|// the protocol is mistyped as a colon is missing after tcp
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:tcp//localhost:8080"
argument_list|,
literal|"mina:tcp//localhost:8080"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should be an IAE exception"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unrecognised MINA protocol: null for uri: mina://tcp//localhost:8080"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

