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

begin_comment
comment|/**  * Unit test for file producer option tempPrefix  */
end_comment

begin_class
DECL|class|FileProduceTempPrefixTest
specifier|public
class|class
name|FileProduceTempPrefixTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/tempandrename/?tempPrefix=inprogress."
decl_stmt|;
DECL|method|testCreateTempFileName ()
specifier|public
name|void
name|testCreateTempFileName
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
name|fileUrl
argument_list|)
decl_stmt|;
name|GenericFileProducer
name|producer
init|=
operator|(
name|GenericFileProducer
operator|)
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|String
name|tempFileName
init|=
name|producer
operator|.
name|createTempFileName
argument_list|(
literal|"target/tempandrename/claus.txt"
argument_list|)
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/tempandrename/inprogress.claus.txt"
argument_list|,
name|tempFileName
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoPathCreateTempFileName ()
specifier|public
name|void
name|testNoPathCreateTempFileName
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
name|fileUrl
argument_list|)
decl_stmt|;
name|GenericFileProducer
name|producer
init|=
operator|(
name|GenericFileProducer
operator|)
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|String
name|tempFileName
init|=
name|producer
operator|.
name|createTempFileName
argument_list|(
literal|"claus.txt"
argument_list|)
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"inprogress.claus.txt"
argument_list|,
name|tempFileName
argument_list|)
expr_stmt|;
block|}
DECL|method|testTempPrefix ()
specifier|public
name|void
name|testTempPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/tempandrename"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/tempandrename/hello.txt"
argument_list|)
decl_stmt|;
comment|// use absolute file to let unittest pass on all platforms
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The generated file should exists: "
operator|+
name|file
argument_list|,
literal|true
argument_list|,
name|file
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
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
name|fileUrl
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

