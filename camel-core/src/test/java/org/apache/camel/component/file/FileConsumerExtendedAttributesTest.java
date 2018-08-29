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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
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
name|Map
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for consuming a batch of files (multiple files in one consume)  */
end_comment

begin_class
DECL|class|FileConsumerExtendedAttributesTest
specifier|public
class|class
name|FileConsumerExtendedAttributesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ROOT
specifier|private
specifier|static
specifier|final
name|String
name|ROOT
init|=
literal|"target/extended-attributes"
decl_stmt|;
DECL|field|FILE
specifier|private
specifier|static
specifier|final
name|String
name|FILE
init|=
literal|"attributes.txt"
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
name|ROOT
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|ROOT
argument_list|,
literal|"basic"
argument_list|,
name|FILE
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|ROOT
argument_list|,
literal|"basic-as-default"
argument_list|,
name|FILE
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|ROOT
argument_list|,
literal|"basic-as-default-with-filter"
argument_list|,
name|FILE
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createFile
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|ROOT
argument_list|,
literal|"posix"
argument_list|,
name|FILE
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
name|fromF
argument_list|(
literal|"file://%s/basic?initialDelay=0&delay=10&extendedAttributes=basic:*"
argument_list|,
name|ROOT
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
literal|"mock:basic"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"file://%s/basic-as-default?initialDelay=0&delay=10&extendedAttributes=*"
argument_list|,
name|ROOT
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
literal|"mock:basic-as-default"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"file://%s/basic-as-default-with-filter?initialDelay=0&delay=10&extendedAttributes=size,lastModifiedTime,lastAccessTime"
argument_list|,
name|ROOT
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
literal|"mock:basic-as-default-with-filter"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"file://%s/posix?initialDelay=0&delay=10&extendedAttributes=posix:*"
argument_list|,
name|ROOT
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
literal|"mock:posix"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testBasicAttributes ()
specifier|public
name|void
name|testBasicAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|testAttributes
argument_list|(
literal|"mock:basic"
argument_list|,
literal|"basic:"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBasicAttributesAsDefault ()
specifier|public
name|void
name|testBasicAttributesAsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|testAttributes
argument_list|(
literal|"mock:basic-as-default"
argument_list|,
literal|"basic:"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBasicAttributesAsDefaultWithFilter ()
specifier|public
name|void
name|testBasicAttributesAsDefaultWithFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|testAttributes
argument_list|(
literal|"mock:basic-as-default"
argument_list|,
literal|"basic:"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPosixAttributes ()
specifier|public
name|void
name|testPosixAttributes
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|FileUtil
operator|.
name|isWindows
argument_list|()
condition|)
block|{
return|return;
block|}
name|testAttributes
argument_list|(
literal|"mock:posix"
argument_list|,
literal|"posix:"
argument_list|)
expr_stmt|;
block|}
DECL|method|testAttributes (String mockEndpoint, String prefix)
specifier|private
name|void
name|testAttributes
parameter_list|(
name|String
name|mockEndpoint
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
name|mockEndpoint
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
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelFileExtendedAttributes"
argument_list|)
operator|.
name|isNotNull
argument_list|()
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
literal|"CamelFileExtendedAttributes"
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
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
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFileExtendedAttributes"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|attributes
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

