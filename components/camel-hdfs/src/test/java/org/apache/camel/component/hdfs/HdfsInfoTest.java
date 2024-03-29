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
name|notNullValue
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
name|*
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

begin_class
DECL|class|HdfsInfoTest
specifier|public
class|class
name|HdfsInfoTest
block|{
DECL|field|underTest
specifier|private
name|HdfsInfo
name|underTest
decl_stmt|;
annotation|@
name|Test
DECL|method|createHdfsInfo ()
specifier|public
name|void
name|createHdfsInfo
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
name|HdfsConfiguration
name|endpointConfig
init|=
name|mock
argument_list|(
name|HdfsConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// when
name|underTest
operator|=
operator|new
name|HdfsInfoFactory
argument_list|(
name|endpointConfig
argument_list|)
operator|.
name|newHdfsInfoWithoutAuth
argument_list|(
name|hdfsPath
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
name|assertThat
argument_list|(
name|underTest
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getFileSystem
argument_list|()
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|underTest
operator|.
name|getPath
argument_list|()
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

