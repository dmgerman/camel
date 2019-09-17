begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
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
name|util
operator|.
name|LinkedList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|Scanner
import|;
end_import

begin_comment
comment|/**  * A DataSet that reads payloads from a file that are used to create each message exchange  * along with using a pluggable transformer to customize the messages.  The file contents may optionally  * be split using a supplied token.  */
end_comment

begin_class
DECL|class|FileDataSet
specifier|public
class|class
name|FileDataSet
extends|extends
name|ListDataSet
block|{
DECL|field|sourceFile
specifier|private
name|File
name|sourceFile
decl_stmt|;
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
DECL|field|defaultBodies
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|defaultBodies
decl_stmt|;
DECL|method|FileDataSet (String sourceFileName)
specifier|public
name|FileDataSet
parameter_list|(
name|String
name|sourceFileName
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|sourceFileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|FileDataSet (File sourceFile)
specifier|public
name|FileDataSet
parameter_list|(
name|File
name|sourceFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|sourceFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|FileDataSet (String sourceFileName, String delimiter)
specifier|public
name|FileDataSet
parameter_list|(
name|String
name|sourceFileName
parameter_list|,
name|String
name|delimiter
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|sourceFileName
argument_list|)
argument_list|,
name|delimiter
argument_list|)
expr_stmt|;
block|}
DECL|method|FileDataSet (File sourceFile, String delimiter)
specifier|public
name|FileDataSet
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|String
name|delimiter
parameter_list|)
throws|throws
name|IOException
block|{
name|setSourceFile
argument_list|(
name|sourceFile
argument_list|,
name|delimiter
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getSourceFile ()
specifier|public
name|File
name|getSourceFile
parameter_list|()
block|{
return|return
name|sourceFile
return|;
block|}
DECL|method|setSourceFile (File sourceFile)
specifier|public
name|void
name|setSourceFile
parameter_list|(
name|File
name|sourceFile
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|sourceFile
operator|=
name|sourceFile
expr_stmt|;
name|readSourceFile
argument_list|()
expr_stmt|;
block|}
DECL|method|setSourceFile (File sourceFile, String delimiter)
specifier|public
name|void
name|setSourceFile
parameter_list|(
name|File
name|sourceFile
parameter_list|,
name|String
name|delimiter
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|sourceFile
operator|=
name|sourceFile
expr_stmt|;
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
name|readSourceFile
argument_list|()
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|readSourceFile ()
specifier|private
name|void
name|readSourceFile
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|bodies
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|delimiter
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|sourceFile
argument_list|,
literal|null
argument_list|,
name|delimiter
argument_list|)
init|)
block|{
while|while
condition|(
name|scanner
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|nextPayload
init|=
name|scanner
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|nextPayload
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|nextPayload
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
name|bodies
operator|.
name|add
argument_list|(
name|nextPayload
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|Object
name|data
init|=
name|IOHelper
operator|.
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|sourceFile
argument_list|)
argument_list|)
decl_stmt|;
name|bodies
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|setDefaultBodies
argument_list|(
name|bodies
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

