begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.watch
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
name|watch
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
name|nio
operator|.
name|file
operator|.
name|StandardOpenOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringFileWatcherTest
specifier|public
class|class
name|SpringFileWatcherTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|springTestFile
specifier|private
name|File
name|springTestFile
decl_stmt|;
DECL|field|springTestCustomHasherFile
specifier|private
name|File
name|springTestCustomHasherFile
decl_stmt|;
annotation|@
name|Before
DECL|method|createTestFiles ()
specifier|public
name|void
name|createTestFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/fileWatchSpringTest"
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/fileWatchSpringTestCustomHasher"
argument_list|)
argument_list|)
expr_stmt|;
name|springTestFile
operator|=
operator|new
name|File
argument_list|(
literal|"target/fileWatchSpringTest"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|springTestCustomHasherFile
operator|=
operator|new
name|File
argument_list|(
literal|"target/fileWatchSpringTestCustomHasher"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|springTestFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|springTestCustomHasherFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultConfig ()
specifier|public
name|void
name|testDefaultConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Files
operator|.
name|write
argument_list|(
name|springTestFile
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"modification"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|StandardOpenOption
operator|.
name|SYNC
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|springTestFile
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"modification 2"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|StandardOpenOption
operator|.
name|SYNC
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:springTest"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// two MODIFY events
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomHasher ()
specifier|public
name|void
name|testCustomHasher
parameter_list|()
throws|throws
name|Exception
block|{
name|Files
operator|.
name|write
argument_list|(
name|springTestCustomHasherFile
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"first modification"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|StandardOpenOption
operator|.
name|SYNC
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|springTestCustomHasherFile
operator|.
name|toPath
argument_list|()
argument_list|,
literal|"second modification"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|StandardOpenOption
operator|.
name|SYNC
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:springTestCustomHasher"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// We passed dummy TestHasher which returns constant hashcode. This should cause, that second MODIFY event is discarded
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|1000
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
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/file/watch/SpringFileWatchComponentTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

