begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ipfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ipfs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|io
operator|.
name|nessus
operator|.
name|utils
operator|.
name|StreamUtils
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
name|ProducerTemplate
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
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
DECL|class|SimpleIPFSTest
specifier|public
class|class
name|SimpleIPFSTest
block|{
annotation|@
name|Test
DECL|method|ipfsVersion ()
specifier|public
name|void
name|ipfsVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:startA"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:version"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startB"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:127.0.0.1/version"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startC"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:127.0.0.1:5001/version"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|String
name|resA
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:startA"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|resB
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:startB"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|resC
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:startC"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Arrays
operator|.
name|asList
argument_list|(
name|resA
argument_list|,
name|resB
argument_list|,
name|resC
argument_list|)
operator|.
name|forEach
argument_list|(
name|res
lambda|->
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Expecting 0.4 in: "
operator|+
name|resA
argument_list|,
name|resA
operator|.
name|startsWith
argument_list|(
literal|"0.4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|ipfsAddSingle ()
specifier|public
name|void
name|ipfsAddSingle
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hash
init|=
literal|"QmYgjSRbXFPdPYKqQSnUjmXLYLudVahEJQotMaAJKt6Lbd"
decl_stmt|;
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:add"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/html/index.html"
argument_list|)
decl_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|String
name|res
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|path
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|hash
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|ipfsAddRecursive ()
specifier|public
name|void
name|ipfsAddRecursive
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hash
init|=
literal|"Qme6hd6tYXTFb7bb7L3JZ5U6ygktpAHKxbaeffYyQN85mW"
decl_stmt|;
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:add"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/html"
argument_list|)
decl_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|res
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|path
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|res
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|hash
argument_list|,
name|res
operator|.
name|get
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|ipfsCat ()
specifier|public
name|void
name|ipfsCat
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hash
init|=
literal|"QmUD7uG5prAMHbcCfp4x1G1mMSpywcSMHTGpq62sbpDAg6"
decl_stmt|;
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:cat"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|InputStream
name|res
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|hash
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|verifyFileContent
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|ipfsGetSingle ()
specifier|public
name|void
name|ipfsGetSingle
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hash
init|=
literal|"QmUD7uG5prAMHbcCfp4x1G1mMSpywcSMHTGpq62sbpDAg6"
decl_stmt|;
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:get?outdir=target"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Path
name|res
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|hash
argument_list|,
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|,
name|hash
argument_list|)
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|verifyFileContent
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|res
operator|.
name|toFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|ipfsGetRecursive ()
specifier|public
name|void
name|ipfsGetRecursive
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|hash
init|=
literal|"Qme6hd6tYXTFb7bb7L3JZ5U6ygktpAHKxbaeffYyQN85mW"
decl_stmt|;
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ipfs:get?outdir=target"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assumeIPFS
argument_list|(
name|camelctx
argument_list|)
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Path
name|res
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|hash
argument_list|,
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target"
argument_list|,
name|hash
argument_list|)
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|res
operator|.
name|toFile
argument_list|()
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|res
operator|.
name|resolve
argument_list|(
literal|"index.html"
argument_list|)
operator|.
name|toFile
argument_list|()
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|verifyFileContent (InputStream ins)
specifier|private
name|void
name|verifyFileContent
parameter_list|(
name|InputStream
name|ins
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StreamUtils
operator|.
name|copyStream
argument_list|(
name|ins
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"The quick brown fox jumps over the lazy dog."
argument_list|,
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assumeIPFS (CamelContext camelctx)
specifier|private
name|void
name|assumeIPFS
parameter_list|(
name|CamelContext
name|camelctx
parameter_list|)
block|{
name|IPFSComponent
name|comp
init|=
name|camelctx
operator|.
name|getComponent
argument_list|(
literal|"ipfs"
argument_list|,
name|IPFSComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|comp
operator|.
name|getIPFSClient
argument_list|()
operator|.
name|hasConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

