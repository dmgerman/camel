begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|builder
operator|.
name|NotifyBuilder
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
name|impl
operator|.
name|JndiRegistry
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
comment|/**  * Memory repo test  */
end_comment

begin_class
DECL|class|FtpConsumerIdempotentMemoryRefTest
specifier|public
class|class
name|FtpConsumerIdempotentMemoryRefTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|repo
specifier|private
name|MemoryIdempotentRepository
name|repo
decl_stmt|;
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/idempotent?password=admin&binary=false&idempotent=true&idempotentRepository=#myRepo&idempotentKey=${file:onlyname}&delete=true"
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|repo
operator|=
operator|new
name|MemoryIdempotentRepository
argument_list|()
expr_stmt|;
name|repo
operator|.
name|setCacheSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myRepo"
argument_list|,
name|repo
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testIdempotent ()
specifier|public
name|void
name|testIdempotent
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello A"
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello B"
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello C"
argument_list|,
literal|"c.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello D"
argument_list|,
literal|"d.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello E"
argument_list|,
literal|"e.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|repo
operator|.
name|getCache
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"a.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"b.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"c.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"d.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"e.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|notify
operator|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|2
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
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
comment|// duplicate
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello A"
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello B"
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
comment|// new files
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello F"
argument_list|,
literal|"f.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello G"
argument_list|,
literal|"g.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|repo
operator|.
name|getCache
argument_list|()
operator|.
name|size
argument_list|()
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
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

