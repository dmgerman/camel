begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
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
name|component
operator|.
name|box
operator|.
name|api
operator|.
name|BoxEventsManager
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
name|box
operator|.
name|internal
operator|.
name|BoxApiCollection
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
name|box
operator|.
name|internal
operator|.
name|BoxEventsManagerApiMethod
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
name|box
operator|.
name|internal
operator|.
name|BoxFilesManagerApiMethod
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Test class for {@link BoxEventsManager}  * APIs. TODO Move the file to src/test/java, populate parameter values, and  * remove @Ignore annotations. The class source won't be generated again if the  * generator MOJO finds it under src/test/java.  */
end_comment

begin_class
DECL|class|BoxEventsManagerIntegrationTest
specifier|public
class|class
name|BoxEventsManagerIntegrationTest
extends|extends
name|AbstractBoxTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BoxEventsManagerIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|BoxApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|BoxEventsManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_FILE
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FILE
init|=
literal|"/CamelTestFile.txt"
decl_stmt|;
DECL|field|CAMEL_TEST_FILE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FILE_NAME
init|=
literal|"CamelTestFile.txt"
decl_stmt|;
annotation|@
name|Test
DECL|method|testListen ()
specifier|public
name|void
name|testListen
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// generate a file create event
name|createTestFile
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// generate a file delete event
name|deleteTestFile
argument_list|()
expr_stmt|;
block|}
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:boxEvents"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|setResultWaitTime
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"poll result"
argument_list|,
name|exchanges
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"poll result"
argument_list|,
name|exchanges
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"poll result: "
operator|+
name|exchanges
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
comment|// test route for events
name|from
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/listen?startingPosition=0"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:boxEvents"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getConnection ()
specifier|public
name|BoxAPIConnection
name|getConnection
parameter_list|()
block|{
name|BoxEndpoint
name|endpoint
init|=
operator|(
name|BoxEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/listen?startingPosition=0"
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
return|;
block|}
DECL|method|createTestFile ()
specifier|private
name|void
name|createTestFile
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
name|BoxFolder
name|rootFolder
init|=
name|BoxFolder
operator|.
name|getRootFolder
argument_list|(
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|stream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|CAMEL_TEST_FILE
argument_list|)
decl_stmt|;
name|testFile
operator|=
name|rootFolder
operator|.
name|uploadFile
argument_list|(
name|stream
argument_list|,
name|CAMEL_TEST_FILE_NAME
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

