begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
name|ContextTestSupport
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

begin_comment
comment|/**  * Checks that body of type {@link java.io.File} is simply moved avoiding  * copying using IO streams.  */
end_comment

begin_class
DECL|class|FileProducerFileBodyGetsMoved
specifier|public
class|class
name|FileProducerFileBodyGetsMoved
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/filemove"
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
DECL|method|testStoreFileExchangeBodyIsFile ()
specifier|public
name|void
name|testStoreFileExchangeBodyIsFile
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedFileExists
argument_list|(
literal|"target/filemove/testStoreFile"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|temporaryFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"camel"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
name|temporaryFile
argument_list|,
name|Exchange
operator|.
name|FILE_LOCAL_WORK_PATH
argument_list|,
name|temporaryFile
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Temporary body file should have been moved, not copied"
argument_list|,
name|temporaryFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStoreFileExchangeBodyIsWrappedFile ()
specifier|public
name|void
name|testStoreFileExchangeBodyIsWrappedFile
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedFileExists
argument_list|(
literal|"target/filemove/testStoreFile"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|temporaryFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"camel"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|body
init|=
operator|new
name|GenericFile
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|body
operator|.
name|setFile
argument_list|(
name|temporaryFile
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
name|temporaryFile
argument_list|,
name|Exchange
operator|.
name|FILE_LOCAL_WORK_PATH
argument_list|,
name|temporaryFile
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Temporary body file should have been moved, not copied"
argument_list|,
name|temporaryFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/filemove/?fileName=testStoreFile"
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

