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
DECL|class|FileProduceTempFileNameTest
specifier|public
class|class
name|FileProduceTempFileNameTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/tempandrename/?tempFileName=inprogress-${file:name.noext}.tmp"
decl_stmt|;
DECL|field|parentFileUrl
specifier|private
name|String
name|parentFileUrl
init|=
literal|"file://target/tempandrename/?tempFileName=../work/${file:name.noext}.tmp"
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
literal|"target/tempandrename"
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
argument_list|<
name|?
argument_list|>
name|producer
init|=
operator|(
name|GenericFileProducer
argument_list|<
name|?
argument_list|>
operator|)
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"claus.txt"
argument_list|)
expr_stmt|;
name|String
name|tempFileName
init|=
name|producer
operator|.
name|createTempFileName
argument_list|(
name|exchange
argument_list|,
literal|"target/tempandrename/claus.txt"
argument_list|)
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/tempandrename/inprogress-claus.tmp"
argument_list|,
name|tempFileName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
argument_list|<
name|?
argument_list|>
name|producer
init|=
operator|(
name|GenericFileProducer
argument_list|<
name|?
argument_list|>
operator|)
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"claus.txt"
argument_list|)
expr_stmt|;
name|String
name|tempFileName
init|=
name|producer
operator|.
name|createTempFileName
argument_list|(
name|exchange
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"inprogress-claus.tmp"
argument_list|,
name|tempFileName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTempFileName ()
specifier|public
name|void
name|testTempFileName
parameter_list|()
throws|throws
name|Exception
block|{
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
annotation|@
name|Test
DECL|method|testCreateParentTempFileName ()
specifier|public
name|void
name|testCreateParentTempFileName
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
name|parentFileUrl
argument_list|)
decl_stmt|;
name|GenericFileProducer
argument_list|<
name|?
argument_list|>
name|producer
init|=
operator|(
name|GenericFileProducer
argument_list|<
name|?
argument_list|>
operator|)
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"claus.txt"
argument_list|)
expr_stmt|;
name|String
name|tempFileName
init|=
name|producer
operator|.
name|createTempFileName
argument_list|(
name|exchange
argument_list|,
literal|"target/tempandrename/claus.txt"
argument_list|)
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/work/claus.tmp"
argument_list|,
name|tempFileName
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

