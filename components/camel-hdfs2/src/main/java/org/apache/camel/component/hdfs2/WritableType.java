begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs2
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|io
operator|.
name|BooleanWritable
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
name|io
operator|.
name|ByteWritable
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
name|io
operator|.
name|BytesWritable
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
name|io
operator|.
name|DoubleWritable
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
name|io
operator|.
name|FloatWritable
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
name|io
operator|.
name|IntWritable
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
name|io
operator|.
name|LongWritable
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
name|io
operator|.
name|NullWritable
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
name|io
operator|.
name|Text
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
name|io
operator|.
name|WritableComparable
import|;
end_import

begin_enum
DECL|enum|WritableType
specifier|public
enum|enum
name|WritableType
block|{
DECL|enumConstant|NULL
name|NULL
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|NullWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|NullWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|BOOLEAN
name|BOOLEAN
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|BooleanWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|BooleanWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|BYTE
name|BYTE
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|ByteWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|ByteWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|INT
name|INT
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|IntWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|IntWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|FLOAT
name|FLOAT
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|FloatWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|FloatWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|LONG
name|LONG
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|LongWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|LongWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|DOUBLE
name|DOUBLE
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|DoubleWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|DoubleWritable
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|TEXT
name|TEXT
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|Text
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|Text
operator|.
name|class
return|;
block|}
block|}
block|,
DECL|enumConstant|BYTES
name|BYTES
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|BytesWritable
argument_list|>
name|getWritableClass
parameter_list|()
block|{
return|return
name|BytesWritable
operator|.
name|class
return|;
block|}
block|}
block|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|getWritableClass ()
specifier|public
specifier|abstract
name|Class
argument_list|<
name|?
extends|extends
name|WritableComparable
argument_list|>
name|getWritableClass
parameter_list|()
function_decl|;
block|}
end_enum

end_unit

