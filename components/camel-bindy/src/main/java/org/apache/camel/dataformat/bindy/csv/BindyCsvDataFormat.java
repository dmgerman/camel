begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyAbstractDataFormat
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyAbstractFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyCsvFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyKeyValuePairFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|util
operator|.
name|Converter
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|PackageScanClassResolver
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
name|ObjectHelper
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) using Bindy to marshal to and from CSV files  */
end_comment

begin_class
DECL|class|BindyCsvDataFormat
specifier|public
class|class
name|BindyCsvDataFormat
extends|extends
name|BindyAbstractDataFormat
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BindyCsvDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|BindyCsvDataFormat ()
specifier|public
name|BindyCsvDataFormat
parameter_list|()
block|{     }
DECL|method|BindyCsvDataFormat (String... packages)
specifier|public
name|BindyCsvDataFormat
parameter_list|(
name|String
modifier|...
name|packages
parameter_list|)
block|{
name|super
argument_list|(
name|packages
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|marshal (Exchange exchange, Object body, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|BindyCsvFactory
name|factory
init|=
operator|(
name|BindyCsvFactory
operator|)
name|getFactory
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getPackageScanClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factory
argument_list|,
literal|"not instantiated"
argument_list|)
expr_stmt|;
comment|// Get CRLF
name|byte
index|[]
name|bytesCRLF
init|=
name|Converter
operator|.
name|getByteReturn
argument_list|(
name|factory
operator|.
name|getCarriageReturn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|.
name|getGenerateHeaderColumnNames
argument_list|()
condition|)
block|{
name|String
name|result
init|=
name|factory
operator|.
name|generateHeader
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
comment|// Add a carriage return
name|outputStream
operator|.
name|write
argument_list|(
name|bytesCRLF
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
decl_stmt|;
comment|// the body is not a prepared list so help a bit here and create one for us
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|body
argument_list|)
operator|==
literal|null
condition|)
block|{
name|models
operator|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|body
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|model
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|model
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// cast to the expected type
name|models
operator|=
operator|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
operator|)
name|body
expr_stmt|;
block|}
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
range|:
name|models
control|)
block|{
name|String
name|result
init|=
name|factory
operator|.
name|unbind
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
comment|// Add a carriage return
name|outputStream
operator|.
name|write
argument_list|(
name|bytesCRLF
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|BindyCsvFactory
name|factory
init|=
operator|(
name|BindyCsvFactory
operator|)
name|getFactory
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getPackageScanClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factory
argument_list|,
literal|"not instantiated"
argument_list|)
expr_stmt|;
comment|// List of Pojos
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// Pojos of the model
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
decl_stmt|;
name|InputStreamReader
name|in
init|=
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
comment|// Scanner is used to read big file
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|in
argument_list|)
decl_stmt|;
comment|// Retrieve the separator defined to split the record
name|String
name|separator
init|=
name|factory
operator|.
name|getSeparator
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|separator
argument_list|,
literal|"The separator has not been defined in the annotation @CsvRecord or not instantiated during initModel."
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
try|try
block|{
comment|// If the first line of the CSV file contains columns name, then we
comment|// skip this line
if|if
condition|(
name|factory
operator|.
name|getSkipFirstLine
argument_list|()
condition|)
block|{
comment|// Check if scanner is empty
if|if
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
name|scanner
operator|.
name|nextLine
argument_list|()
expr_stmt|;
block|}
block|}
while|while
condition|(
name|scanner
operator|.
name|hasNextLine
argument_list|()
condition|)
block|{
comment|// Read the line
name|String
name|line
init|=
name|scanner
operator|.
name|nextLine
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|line
argument_list|)
condition|)
block|{
comment|// skip if line is empty
continue|continue;
block|}
comment|// Increment counter
name|count
operator|++
expr_stmt|;
comment|// Create POJO where CSV data will be stored
name|model
operator|=
name|factory
operator|.
name|factory
argument_list|()
expr_stmt|;
comment|// Added for camel- jira ticket
comment|// We will remove the first and last character  of the line
comment|// when the separator contains quotes, double quotes
comment|// e.g. ',' or "," ...
comment|// REMARK : We take the assumption that the data fields are
comment|// quoted or double quoted like that
comment|// e.g : "1 ", "street 1, NY", "USA"
if|if
condition|(
name|separator
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|String
name|tempLine
init|=
name|line
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|line
operator|=
name|tempLine
expr_stmt|;
block|}
comment|// Split the CSV record according to the separator defined in
comment|// annotated class @CSVRecord
name|String
index|[]
name|tokens
init|=
name|line
operator|.
name|split
argument_list|(
name|separator
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|tokens
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
argument_list|(
literal|"No records have been defined in the CSV !"
argument_list|)
throw|;
block|}
if|if
condition|(
name|result
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Size of the record splitted : "
operator|+
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Bind data from CSV record with model classes
name|factory
operator|.
name|bind
argument_list|(
name|result
argument_list|,
name|model
argument_list|,
name|count
argument_list|)
expr_stmt|;
comment|// Link objects together
name|factory
operator|.
name|link
argument_list|(
name|model
argument_list|)
expr_stmt|;
comment|// Add objects graph to the list
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Graph of objects created : "
operator|+
name|model
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Test if models list is empty or not
comment|// If this is the case (correspond to an empty stream, ...)
if|if
condition|(
name|models
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
argument_list|(
literal|"No records have been defined in the CSV !"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|models
return|;
block|}
block|}
finally|finally
block|{
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
literal|"in"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createModelFactory (PackageScanClassResolver resolver)
specifier|protected
name|BindyAbstractFactory
name|createModelFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|BindyCsvFactory
argument_list|(
name|resolver
argument_list|,
name|getPackages
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

