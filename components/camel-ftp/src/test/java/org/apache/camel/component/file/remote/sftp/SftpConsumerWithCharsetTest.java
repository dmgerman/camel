begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.sftp
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
operator|.
name|sftp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|file
operator|.
name|FileComponent
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
name|file
operator|.
name|remote
operator|.
name|RemoteFile
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
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|SftpConsumerWithCharsetTest
specifier|public
class|class
name|SftpConsumerWithCharsetTest
extends|extends
name|SftpServerTestSupport
block|{
DECL|field|SAMPLE_FILE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_NAME
init|=
name|String
operator|.
name|format
argument_list|(
literal|"sample-%s.txt"
argument_list|,
name|SftpConsumerWithCharsetTest
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|SAMPLE_FILE_CHARSET
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_CHARSET
init|=
literal|"iso-8859-1"
decl_stmt|;
DECL|field|SAMPLE_FILE_PAYLOAD
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_PAYLOAD
init|=
literal|"\u00e6\u00f8\u00e5 \u00a9"
decl_stmt|;
comment|// danish ae oe aa and (c) sign
annotation|@
name|Test
DECL|method|testConsumeWithCharset ()
specifier|public
name|void
name|testConsumeWithCharset
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// prepare sample file to be consumed by SFTP consumer
name|createSampleFile
argument_list|()
expr_stmt|;
comment|// Prepare expectations
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|SAMPLE_FILE_PAYLOAD
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|// Check that expectations are satisfied
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// Check that the proper charset was set in the internal object
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|RemoteFile
argument_list|<
name|?
argument_list|>
name|file
init|=
operator|(
name|RemoteFile
argument_list|<
name|?
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAMPLE_FILE_CHARSET
argument_list|,
name|file
operator|.
name|getCharset
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&password=admin&charset="
operator|+
name|SAMPLE_FILE_CHARSET
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
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
DECL|method|createSampleFile ()
specifier|private
name|void
name|createSampleFile
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/"
operator|+
name|SAMPLE_FILE_NAME
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|write
argument_list|(
name|file
argument_list|,
name|SAMPLE_FILE_PAYLOAD
argument_list|,
name|SAMPLE_FILE_CHARSET
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

