begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
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
name|BindToRegistry
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
name|builder
operator|.
name|RouteBuilder
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
comment|/**  * Test class for com.google.api.services.drive.Drive$Files APIs.  */
end_comment

begin_class
DECL|class|CustomClientFactoryTest
specifier|public
class|class
name|CustomClientFactoryTest
extends|extends
name|AbstractGoogleDriveTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myAuth"
argument_list|)
DECL|field|cf
specifier|private
name|MyClientFactory
name|cf
init|=
operator|new
name|MyClientFactory
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testClientFactoryUpdated ()
specifier|public
name|void
name|testClientFactoryUpdated
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
literal|"google-drive://drive-files/list?clientFactory=#myAuth"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|GoogleDriveEndpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|GoogleDriveEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getClientFactory
argument_list|()
operator|instanceof
name|MyClientFactory
argument_list|)
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
name|from
argument_list|(
literal|"google-drive://drive-files/list?clientFactory=#myAuth"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

