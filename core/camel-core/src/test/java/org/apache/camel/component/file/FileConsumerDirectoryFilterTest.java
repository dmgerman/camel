begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|ArrayList
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
comment|/**  * Unit test for the file filter option using directories  */
end_comment

begin_class
DECL|class|FileConsumerDirectoryFilterTest
specifier|public
class|class
name|FileConsumerDirectoryFilterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
specifier|final
name|String
name|fileUrl
init|=
literal|"file://target/data/directoryfilter/?recursive=true&filter=#myFilter&initialDelay=0&delay=10"
decl_stmt|;
DECL|field|names
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myFilter"
argument_list|,
operator|new
name|MyDirectoryFilter
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
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
name|deleteDirectory
argument_list|(
literal|"target/data/directoryfilter"
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
DECL|method|testFilterFilesWithARegularFile ()
specifier|public
name|void
name|testFilterFilesWithARegularFile
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/directoryfilter/skipDir/"
argument_list|,
literal|"This is a file to be filtered"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"skipme.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/directoryfilter/skipDir2/"
argument_list|,
literal|"This is a file to be filtered"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"skipme.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/directoryfilter/okDir/"
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// check names
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// copy to list so its easier to index
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|names
argument_list|)
decl_stmt|;
name|list
operator|.
name|sort
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"okDir"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// windows or unix paths
name|assertTrue
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
literal|"okDir/hello.txt"
argument_list|)
operator|||
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
literal|"okDir\\hello.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"skipDir"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"skipDir2"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|3
argument_list|)
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|fileUrl
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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
comment|// START SNIPPET: e1
DECL|class|MyDirectoryFilter
specifier|public
class|class
name|MyDirectoryFilter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericFileFilter
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|accept (GenericFile<T> file)
specifier|public
name|boolean
name|accept
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
comment|// remember the name due unit testing (should not be needed in
comment|// regular use-cases)
name|names
operator|.
name|add
argument_list|(
name|file
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
comment|// we dont accept any files within directory starting with skip in
comment|// the name
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
operator|&&
name|file
operator|.
name|getFileName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"skip"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

