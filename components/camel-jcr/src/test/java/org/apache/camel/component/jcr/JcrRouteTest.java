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
name|javax
operator|.
name|jcr
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|SimpleCredentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|apache
operator|.
name|jackrabbit
operator|.
name|core
operator|.
name|TransientRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|JcrRouteTest
specifier|public
class|class
name|JcrRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/repository"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJcrRoute ()
specifier|public
name|void
name|testJcrRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"<hello>world!</hello>"
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
name|Session
name|session
init|=
name|repository
operator|.
name|login
argument_list|(
operator|new
name|SimpleCredentials
argument_list|(
literal|"user"
argument_list|,
literal|"pass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Node
name|node
init|=
name|session
operator|.
name|getNodeByIdentifier
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/home/test/node"
argument_list|,
name|node
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<hello>world!</hello>"
argument_list|,
name|node
operator|.
name|getProperty
argument_list|(
literal|"my.contents.property"
argument_list|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|isLive
argument_list|()
condition|)
block|{
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
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
literal|"jcr://user:pass@repository/home/test"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: jcr
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|repository
operator|=
operator|new
name|TransientRepository
argument_list|(
literal|"target/repository.xml"
argument_list|,
literal|"target/repository"
argument_list|)
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"repository"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

