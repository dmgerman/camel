begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
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
name|Exchange
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
name|Ignore
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
DECL|class|JcrAuthTokenWithLoginTest
specifier|public
class|class
name|JcrAuthTokenWithLoginTest
extends|extends
name|JcrAuthTestBase
block|{
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fails with some error"
argument_list|)
DECL|method|testCreateNodeWithAuthentication ()
specifier|public
name|void
name|testCreateNodeWithAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"<message>hello!</message>"
argument_list|)
decl_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|String
name|uuid
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Expected body to be null, found JCR node UUID"
argument_list|,
name|uuid
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: jcr
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_NAME
argument_list|,
name|constant
argument_list|(
literal|"node"
argument_list|)
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"my.contents.property"
argument_list|,
name|body
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcr://not-a-user:nonexisting-password@repository"
operator|+
name|BASE_REPO_PATH
argument_list|)
expr_stmt|;
comment|// END SNIPPET: jcr
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

