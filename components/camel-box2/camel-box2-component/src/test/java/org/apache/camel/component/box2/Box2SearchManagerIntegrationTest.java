begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box2
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|BoxFile
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
name|box2
operator|.
name|internal
operator|.
name|Box2ApiCollection
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
name|box2
operator|.
name|internal
operator|.
name|Box2SearchManagerApiMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
comment|/**  * Test class for {@link org.apache.camel.component.box2.api.Box2SearchManager}  * APIs.  */
end_comment

begin_class
DECL|class|Box2SearchManagerIntegrationTest
specifier|public
class|class
name|Box2SearchManagerIntegrationTest
extends|extends
name|AbstractBox2TestSupport
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
name|Box2SearchManagerIntegrationTest
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
name|Box2ApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|Box2SearchManagerApiMethod
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
DECL|field|testFile
specifier|private
name|BoxFile
name|testFile
decl_stmt|;
annotation|@
name|Test
DECL|method|testSearchFolder ()
specifier|public
name|void
name|testSearchFolder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox2.folderId"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox2.query"
argument_list|,
name|CAMEL_TEST_FILE_NAME
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|Collection
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://SEARCHFOLDER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"searchFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"searchFolder file found"
argument_list|,
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"searchFolder: "
operator|+
name|result
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
comment|// test route for searchFolder
name|from
argument_list|(
literal|"direct://SEARCHFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box2://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchFolder"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setupTest ()
specifier|public
name|void
name|setupTest
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|teardownTest ()
specifier|public
name|void
name|teardownTest
parameter_list|()
block|{
name|deleteTestFile
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|BoxAPIConnection
name|getConnection
parameter_list|()
block|{
name|Box2Endpoint
name|endpoint
init|=
operator|(
name|Box2Endpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"box2://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchFolder"
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
DECL|method|deleteTestFile ()
specifier|private
name|void
name|deleteTestFile
parameter_list|()
block|{
try|try
block|{
name|testFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{         }
name|testFile
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

