begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
name|commons
operator|.
name|csv
operator|.
name|CSVParser
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
DECL|class|CsvIterator
specifier|public
class|class
name|CsvIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|parser
specifier|private
specifier|final
name|CSVParser
name|parser
decl_stmt|;
DECL|field|reader
specifier|private
specifier|final
name|Reader
name|reader
decl_stmt|;
DECL|field|lineConverter
specifier|private
specifier|final
name|CsvLineConverter
argument_list|<
name|T
argument_list|>
name|lineConverter
decl_stmt|;
DECL|field|line
specifier|private
name|String
index|[]
name|line
decl_stmt|;
DECL|method|CsvIterator (CSVParser parser, Reader reader, CsvLineConverter<T> lineConverter)
specifier|public
name|CsvIterator
parameter_list|(
name|CSVParser
name|parser
parameter_list|,
name|Reader
name|reader
parameter_list|,
name|CsvLineConverter
argument_list|<
name|T
argument_list|>
name|lineConverter
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
name|this
operator|.
name|reader
operator|=
name|reader
expr_stmt|;
name|this
operator|.
name|lineConverter
operator|=
name|lineConverter
expr_stmt|;
name|line
operator|=
name|parser
operator|.
name|getLine
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|line
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|T
name|result
init|=
name|lineConverter
operator|.
name|convertLine
argument_list|(
name|line
argument_list|)
decl_stmt|;
try|try
block|{
name|line
operator|=
name|parser
operator|.
name|getLine
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|line
operator|=
literal|null
expr_stmt|;
name|close
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

