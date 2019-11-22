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
name|Closeable
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

begin_enum
DECL|enum|HdfsFileType
specifier|public
enum|enum
name|HdfsFileType
block|{
DECL|enumConstant|NORMAL_FILE
name|NORMAL_FILE
argument_list|(
operator|new
name|HdfsNormalFileHandler
argument_list|()
argument_list|)
block|,
DECL|enumConstant|SEQUENCE_FILE
name|SEQUENCE_FILE
argument_list|(
operator|new
name|HdfsSequenceFileHandler
argument_list|()
argument_list|)
block|,
DECL|enumConstant|MAP_FILE
name|MAP_FILE
argument_list|(
operator|new
name|HdfsMapFileHandler
argument_list|()
argument_list|)
block|,
DECL|enumConstant|BLOOMMAP_FILE
name|BLOOMMAP_FILE
argument_list|(
operator|new
name|HdfsBloommapFileHandler
argument_list|()
argument_list|)
block|,
DECL|enumConstant|ARRAY_FILE
name|ARRAY_FILE
argument_list|(
operator|new
name|HdfsArrayFileTypeHandler
argument_list|()
argument_list|)
block|;
DECL|field|file
specifier|private
specifier|final
name|HdfsFile
name|file
decl_stmt|;
DECL|method|HdfsFileType (HdfsFile file)
name|HdfsFileType
parameter_list|(
name|HdfsFile
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|createOutputStream (String hdfsPath, HdfsInfoFactory hdfsInfoFactory)
specifier|public
name|Closeable
name|createOutputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|)
block|{
return|return
name|this
operator|.
name|file
operator|.
name|createOutputStream
argument_list|(
name|hdfsPath
argument_list|,
name|hdfsInfoFactory
argument_list|)
return|;
block|}
DECL|method|append (HdfsOutputStream hdfsOutputStream, Object key, Object value, Exchange exchange)
specifier|public
name|long
name|append
parameter_list|(
name|HdfsOutputStream
name|hdfsOutputStream
parameter_list|,
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|this
operator|.
name|file
operator|.
name|append
argument_list|(
name|hdfsOutputStream
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|createInputStream (String hdfsPath, HdfsInfoFactory hdfsInfoFactory)
specifier|public
name|Closeable
name|createInputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsInfoFactory
name|hdfsInfoFactory
parameter_list|)
block|{
return|return
name|this
operator|.
name|file
operator|.
name|createInputStream
argument_list|(
name|hdfsPath
argument_list|,
name|hdfsInfoFactory
argument_list|)
return|;
block|}
DECL|method|next (HdfsInputStream hdfsInputStream, Holder<Object> key, Holder<Object> value)
specifier|public
name|long
name|next
parameter_list|(
name|HdfsInputStream
name|hdfsInputStream
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|key
parameter_list|,
name|Holder
argument_list|<
name|Object
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|this
operator|.
name|file
operator|.
name|next
argument_list|(
name|hdfsInputStream
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

