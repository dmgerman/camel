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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
DECL|class|GenericFileMessageTest
specifier|public
class|class
name|GenericFileMessageTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testGenericMessageToStringConversion ()
specifier|public
name|void
name|testGenericMessageToStringConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|GenericFileMessage
argument_list|<
name|File
argument_list|>
name|message
init|=
operator|new
name|GenericFileMessage
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|assertStringContains
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|,
literal|"org.apache.camel.component.file.GenericFileMessage@"
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
init|=
operator|new
name|GenericFile
argument_list|<
name|File
argument_list|>
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|file
operator|.
name|setFileName
argument_list|(
literal|"target/test.txt"
argument_list|)
expr_stmt|;
name|file
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/test.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|GenericFileMessage
argument_list|<
name|File
argument_list|>
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FileUtil
operator|.
name|isWindows
argument_list|()
condition|?
literal|"target\\test.txt"
else|:
literal|"target/test.txt"
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGenericFileContentType ()
specifier|public
name|void
name|testGenericFileContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
init|=
operator|new
name|GenericFile
argument_list|<
name|File
argument_list|>
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|file
operator|.
name|setEndpointPath
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|file
operator|.
name|setFileName
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|file
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/camel-core-test.log"
argument_list|)
argument_list|)
expr_stmt|;
name|GenericFileMessage
argument_list|<
name|File
argument_list|>
name|message
init|=
operator|new
name|GenericFileMessage
argument_list|<
name|File
argument_list|>
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|file
operator|.
name|populateHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong file content type"
argument_list|,
literal|"txt"
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

