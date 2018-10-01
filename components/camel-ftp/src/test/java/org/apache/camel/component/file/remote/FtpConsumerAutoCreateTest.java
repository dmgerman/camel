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
name|java
operator|.
name|io
operator|.
name|File
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
name|GenericFileOperationFailedException
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_class
DECL|class|FtpConsumerAutoCreateTest
specifier|public
class|class
name|FtpConsumerAutoCreateTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|protected
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
literal|"///foo/bar/baz/xxx?password=admin&autoCreate=true"
return|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAutoCreate ()
specifier|public
name|void
name|testAutoCreate
parameter_list|()
throws|throws
name|Exception
block|{
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
name|endpoint
init|=
operator|(
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
operator|)
name|this
operator|.
name|getMandatoryEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|getExchanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/res/home/foo/bar/baz/xxx"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// producer should create necessary subdirs
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|,
literal|"sub1/sub2/hello.txt"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/res/home/foo/bar/baz/xxx/sub1/sub2"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// to see if another connect causes problems with autoCreate=true
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|getExchanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoAutoCreate ()
specifier|public
name|void
name|testNoAutoCreate
parameter_list|()
throws|throws
name|Exception
block|{
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
name|endpoint
init|=
operator|(
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
operator|)
name|this
operator|.
name|getMandatoryEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&autoCreate=false"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|endpoint
operator|.
name|getExchanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail with 550 No such directory."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getCode
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|550
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

