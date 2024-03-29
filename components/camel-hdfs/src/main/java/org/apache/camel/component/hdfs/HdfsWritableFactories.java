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
name|ByteArrayOutputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|RuntimeCamelException
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
name|IOUtils
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
name|Writable
import|;
end_import

begin_class
DECL|class|HdfsWritableFactories
specifier|public
class|class
name|HdfsWritableFactories
block|{
DECL|interface|HdfsWritableFactory
interface|interface
name|HdfsWritableFactory
block|{
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
function_decl|;
DECL|method|read (Writable writable, Holder<Integer> size)
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
function_decl|;
block|}
DECL|class|HdfsNullWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsNullWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|NullWritable
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|class|HdfsByteWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsByteWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|1
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|ByteWritable
name|writable
init|=
operator|new
name|ByteWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Byte
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|ByteWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsBooleanWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsBooleanWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|1
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|BooleanWritable
name|writable
init|=
operator|new
name|BooleanWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|BooleanWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsBytesWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsBytesWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|BytesWritable
name|writable
init|=
operator|new
name|BytesWritable
argument_list|()
decl_stmt|;
name|ByteBuffer
name|bb
init|=
operator|(
name|ByteBuffer
operator|)
name|value
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|bb
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|bb
operator|.
name|array
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|size
operator|.
name|setValue
argument_list|(
name|bb
operator|.
name|array
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
operator|(
operator|(
name|BytesWritable
operator|)
name|writable
operator|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|size
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|bb
operator|.
name|put
argument_list|(
operator|(
operator|(
name|BytesWritable
operator|)
name|writable
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|size
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|bb
return|;
block|}
block|}
DECL|class|HdfsDoubleWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsDoubleWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|8
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|DoubleWritable
name|writable
init|=
operator|new
name|DoubleWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|DoubleWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsFloatWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsFloatWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|4
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|FloatWritable
name|writable
init|=
operator|new
name|FloatWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Float
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|FloatWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsIntWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsIntWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|4
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|IntWritable
name|writable
init|=
operator|new
name|IntWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|IntWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsLongWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsLongWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|8
decl_stmt|;
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|LongWritable
name|writable
init|=
operator|new
name|LongWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|LongWritable
operator|)
name|writable
operator|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsTextWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsTextWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|Text
name|writable
init|=
operator|new
name|Text
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|size
operator|.
name|setValue
argument_list|(
name|writable
operator|.
name|getBytes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
operator|(
operator|(
name|Text
operator|)
name|writable
operator|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|writable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|HdfsObjectWritableFactory
specifier|public
specifier|static
specifier|final
class|class
name|HdfsObjectWritableFactory
implements|implements
name|HdfsWritableFactory
block|{
annotation|@
name|Override
DECL|method|create (Object value, TypeConverter typeConverter, Holder<Integer> size)
specifier|public
name|Writable
name|create
parameter_list|(
name|Object
name|value
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|value
argument_list|)
init|)
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyBytes
argument_list|(
name|is
argument_list|,
name|bos
argument_list|,
name|HdfsConstants
operator|.
name|DEFAULT_BUFFERSIZE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|BytesWritable
name|writable
init|=
operator|new
name|BytesWritable
argument_list|()
decl_stmt|;
name|writable
operator|.
name|set
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|size
operator|.
name|setValue
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|writable
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|read (Writable writable, Holder<Integer> size)
specifier|public
name|Object
name|read
parameter_list|(
name|Writable
name|writable
parameter_list|,
name|Holder
argument_list|<
name|Integer
argument_list|>
name|size
parameter_list|)
block|{
name|size
operator|.
name|setValue
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

