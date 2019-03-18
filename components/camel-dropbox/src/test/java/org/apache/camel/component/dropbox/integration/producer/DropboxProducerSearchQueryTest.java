begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.integration.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
operator|.
name|integration
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|dropbox
operator|.
name|integration
operator|.
name|DropboxTestSupport
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxConstants
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxResultHeader
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
DECL|class|DropboxProducerSearchQueryTest
specifier|public
class|class
name|DropboxProducerSearchQueryTest
extends|extends
name|DropboxTestSupport
block|{
DECL|field|FILE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|FILE_NAME
init|=
literal|"myTestFile.txt"
decl_stmt|;
annotation|@
name|Before
DECL|method|createFile ()
specifier|public
name|void
name|createFile
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|content
init|=
literal|"Hi camels"
decl_stmt|;
name|createFile
argument_list|(
name|FILE_NAME
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelDropbox ()
specifier|public
name|void
name|testCamelDropbox
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelDropboxWithOptionInHeader ()
specifier|public
name|void
name|testCamelDropboxWithOptionInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"direct:start2"
argument_list|)
expr_stmt|;
block|}
DECL|method|test (String endpoint)
specifier|private
name|void
name|test
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|DropboxResultHeader
operator|.
name|FOUND_FILES
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s"
argument_list|,
name|workdir
argument_list|,
name|FILE_NAME
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"dropbox://search?accessToken={{accessToken}}&remotePath=%s&query=%s"
argument_list|,
name|workdir
argument_list|,
name|FILE_NAME
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|DropboxConstants
operator|.
name|HEADER_REMOTE_PATH
argument_list|,
name|constant
argument_list|(
name|workdir
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|DropboxConstants
operator|.
name|HEADER_QUERY
argument_list|,
name|constant
argument_list|(
name|FILE_NAME
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"dropbox://search?accessToken={{accessToken}}"
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

