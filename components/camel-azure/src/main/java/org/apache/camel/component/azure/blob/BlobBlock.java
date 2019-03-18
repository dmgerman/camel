begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.blob
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|blob
package|;
end_package

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
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|blob
operator|.
name|BlockEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|blob
operator|.
name|BlockSearchMode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|core
operator|.
name|Base64
import|;
end_import

begin_class
DECL|class|BlobBlock
specifier|public
class|class
name|BlobBlock
block|{
DECL|field|blockStream
specifier|private
name|InputStream
name|blockStream
decl_stmt|;
DECL|field|blockEntry
specifier|private
name|BlockEntry
name|blockEntry
decl_stmt|;
DECL|method|BlobBlock (InputStream blockStream)
specifier|public
name|BlobBlock
parameter_list|(
name|InputStream
name|blockStream
parameter_list|)
block|{
name|this
argument_list|(
name|Base64
operator|.
name|encode
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|blockStream
argument_list|)
expr_stmt|;
block|}
DECL|method|BlobBlock (String blockId, InputStream blockStream)
specifier|public
name|BlobBlock
parameter_list|(
name|String
name|blockId
parameter_list|,
name|InputStream
name|blockStream
parameter_list|)
block|{
name|this
argument_list|(
name|blockId
argument_list|,
name|BlockSearchMode
operator|.
name|LATEST
argument_list|,
name|blockStream
argument_list|)
expr_stmt|;
block|}
DECL|method|BlobBlock (String blockId, BlockSearchMode searchMode, InputStream blockStream)
specifier|public
name|BlobBlock
parameter_list|(
name|String
name|blockId
parameter_list|,
name|BlockSearchMode
name|searchMode
parameter_list|,
name|InputStream
name|blockStream
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|BlockEntry
argument_list|(
name|blockId
argument_list|,
name|searchMode
argument_list|)
argument_list|,
name|blockStream
argument_list|)
expr_stmt|;
block|}
DECL|method|BlobBlock (BlockEntry blockEntry, InputStream blockStream)
specifier|public
name|BlobBlock
parameter_list|(
name|BlockEntry
name|blockEntry
parameter_list|,
name|InputStream
name|blockStream
parameter_list|)
block|{
name|this
operator|.
name|blockStream
operator|=
name|blockStream
expr_stmt|;
name|this
operator|.
name|blockEntry
operator|=
name|blockEntry
expr_stmt|;
block|}
DECL|method|getBlockStream ()
specifier|public
name|InputStream
name|getBlockStream
parameter_list|()
block|{
return|return
name|blockStream
return|;
block|}
DECL|method|getBlockEntry ()
specifier|public
name|BlockEntry
name|getBlockEntry
parameter_list|()
block|{
return|return
name|blockEntry
return|;
block|}
block|}
end_class

end_unit

