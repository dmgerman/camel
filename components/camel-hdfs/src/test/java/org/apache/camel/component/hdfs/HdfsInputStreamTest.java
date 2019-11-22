begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
package|;
end_package

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
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FSDataInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|Path
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
name|mockito
operator|.
name|ArgumentCaptor
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
name|is
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
name|notNullValue
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
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|HdfsInputStreamTest
specifier|public
class|class
name|HdfsInputStreamTest
block|{
DECL|field|hdfsInfoFactory
specifier|private
name|HdfsInfoFactory
name|hdfsInfoFactory
decl_stmt|;
DECL|field|endpointConfig
specifier|private
name|HdfsConfiguration
name|endpointConfig
decl_stmt|;
DECL|field|fileSystem
specifier|private
name|FileSystem
name|fileSystem
decl_stmt|;
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|underTest
specifier|private
name|HdfsInputStream
name|underTest
decl_stmt|;
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
name|hdfsInfoFactory
operator|=
name|mock
argument_list|(
name|HdfsInfoFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|HdfsInfo
name|hdfsInfo
init|=
name|mock
argument_list|(
name|HdfsInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpointConfig
operator|=
name|mock
argument_list|(
name|HdfsConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|fileSystem
operator|=
name|mock
argument_list|(
name|FileSystem
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|mock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
name|mock
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|hdfsInfoFactory
operator|.
name|newHdfsInfo
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hdfsInfo
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hdfsInfoFactory
operator|.
name|newHdfsInfoWithoutAuth
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hdfsInfo
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hdfsInfoFactory
operator|.
name|getEndpointConfig
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|endpointConfig
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hdfsInfo
operator|.
name|getFileSystem
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fileSystem
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hdfsInfo
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hdfsInfo
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createInputStreamForLocalNormalFile ()
specifier|public
name|void
name|createInputStreamForLocalNormalFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|String
name|hdfsPath
init|=
literal|"hdfs://localhost/target/test/multiple-consumers"
decl_stmt|;
name|FSDataInputStream
name|fsDataInputStream
init|=
name|mock
argument_list|(
name|FSDataInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|endpointConfig
operator|.
name|getFileType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|HdfsFileType
operator|.
name|NORMAL_FILE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpointConfig
operator|.
name|getFileSystemType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|HdfsFileSystemType
operator|.
name|LOCAL
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fileSystem
operator|.
name|rename
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fileSystem
operator|.
name|open
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fsDataInputStream
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|Path
argument_list|>
name|pathCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// when
name|underTest
operator|=
name|HdfsInputStream
operator|.
name|createInputStream
argument_list|(
name|hdfsPath
argument_list|,
name|hdfsInfoFactory
argument_list|)
expr_stmt|;
comment|// then
name|assertThat
argument_list|(
name|underTest
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|fileSystem
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|rename
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|,
name|pathCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|pathCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"hdfs://localhost/target/test/multiple-consumers.null"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getNumOfReadBytes
argument_list|()
argument_list|,
name|is
argument_list|(
literal|0L
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getNumOfReadMessages
argument_list|()
argument_list|,
name|is
argument_list|(
literal|0L
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getActualPath
argument_list|()
argument_list|,
name|is
argument_list|(
name|hdfsPath
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getChunkSize
argument_list|()
argument_list|,
name|is
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|isOpened
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createInputStreamForMissingNormalFile ()
specifier|public
name|void
name|createInputStreamForMissingNormalFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|String
name|hdfsPath
init|=
literal|"hdfs://localhost/target/test/multiple-consumers"
decl_stmt|;
name|FSDataInputStream
name|fsDataInputStream
init|=
name|mock
argument_list|(
name|FSDataInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|endpointConfig
operator|.
name|getFileType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|HdfsFileType
operator|.
name|NORMAL_FILE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpointConfig
operator|.
name|getFileSystemType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|HdfsFileSystemType
operator|.
name|LOCAL
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fileSystem
operator|.
name|rename
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fileSystem
operator|.
name|open
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fsDataInputStream
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|Path
argument_list|>
name|pathCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// when
name|underTest
operator|=
name|HdfsInputStream
operator|.
name|createInputStream
argument_list|(
name|hdfsPath
argument_list|,
name|hdfsInfoFactory
argument_list|)
expr_stmt|;
comment|// then
name|assertThat
argument_list|(
name|underTest
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|fileSystem
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|rename
argument_list|(
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

