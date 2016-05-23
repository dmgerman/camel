begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|EventClient
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
DECL|class|ConsulEventWatchTest
specifier|public
class|class
name|ConsulEventWatchTest
extends|extends
name|ConsulTestSupport
block|{
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|field|client
specifier|private
name|EventClient
name|client
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|public
name|void
name|doPreSetup
parameter_list|()
block|{
name|key
operator|=
name|generateRandomString
argument_list|()
expr_stmt|;
name|client
operator|=
name|getConsul
argument_list|()
operator|.
name|eventClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchEvent ()
specifier|public
name|void
name|testWatchEvent
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|generateRandomListOfStrings
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:event-watch"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|values
operator|.
name|forEach
argument_list|(
name|v
lambda|->
name|client
operator|.
name|fireEvent
argument_list|(
name|key
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"consul:event?key=%s"
argument_list|,
name|key
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.consul?level=INFO&showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:event-watch"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

