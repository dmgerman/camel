begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|idempotent
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
name|FileOutputStream
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
name|CamelContext
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
name|camel
operator|.
name|spi
operator|.
name|IdempotentRepository
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
name|util
operator|.
name|FileUtil
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
import|;
end_import

begin_class
DECL|class|FileConsumerIdempotentLoadStoreTest
specifier|public
class|class
name|FileConsumerIdempotentLoadStoreTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|repo
specifier|private
name|IdempotentRepository
name|repo
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/spring/processor/idempotent/fileConsumerIdempotentTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
literal|"target/fileidempotent"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/fileidempotent"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/fileidempotent/.filestore.dat"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// insert existing name to the file repo, so we should skip this file
name|String
name|name
init|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/fileidempotent/report.txt"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|name
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|LS
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// add a file to the repo
name|repo
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"fileStore"
argument_list|,
name|IdempotentRepository
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIdempotentLoad ()
specifier|public
name|void
name|testIdempotentLoad
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send two files (report.txt exists already in idempotent repo)
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/fileidempotent/"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/fileidempotent/"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report2.txt"
argument_list|)
expr_stmt|;
comment|// consume the file the first time
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// wait for the exchange to be done, as it only append to idempotent repo after success
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|String
name|name
init|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/fileidempotent/report.txt"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain file: "
operator|+
name|name
argument_list|,
name|repo
operator|.
name|contains
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|name2
init|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/fileidempotent/report2.txt"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain file: "
operator|+
name|name2
argument_list|,
name|repo
operator|.
name|contains
argument_list|(
name|name2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

