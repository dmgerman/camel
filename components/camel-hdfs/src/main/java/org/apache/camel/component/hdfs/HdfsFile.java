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
name|TypeConverter
import|;
end_import

begin_interface
DECL|interface|HdfsFile
interface|interface
name|HdfsFile
block|{
DECL|method|append (HdfsOutputStream hdfsostr, Object key, Object value, TypeConverter typeConverter)
name|long
name|append
parameter_list|(
name|HdfsOutputStream
name|hdfsostr
parameter_list|,
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
function_decl|;
DECL|method|next (HdfsInputStream hdfsInputStream, Holder<Object> key, Holder<Object> value)
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
function_decl|;
DECL|method|createOutputStream (String hdfsPath, HdfsConfiguration configuration)
name|Closeable
name|createOutputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|configuration
parameter_list|)
function_decl|;
DECL|method|createInputStream (String hdfsPath, HdfsConfiguration configuration)
name|Closeable
name|createInputStream
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|configuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

