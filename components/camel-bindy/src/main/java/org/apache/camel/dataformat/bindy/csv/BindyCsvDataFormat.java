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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link DataFormat}) using Bindy to marshal to and from CSV files  */
end_comment

begin_class
DECL|class|BindyCsvDataFormat
specifier|public
class|class
name|BindyCsvDataFormat
implements|implements
name|DataFormat
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
DECL|field|packageName
specifier|private
name|String
name|packageName
decl_stmt|;
DECL|field|modelFactory
specifier|private
name|BindyCsvFactory
name|modelFactory
decl_stmt|;
DECL|method|BindyCsvDataFormat ()
specifier|public
name|BindyCsvDataFormat
parameter_list|()
block|{     }
DECL|method|BindyCsvDataFormat (String packageName)
specifier|public
name|BindyCsvDataFormat
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|packageName
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
operator|(
name|ArrayList
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
decl_stmt|;
name|byte
index|[]
name|bytesCRLF
decl_stmt|;
comment|// Get CRLF
name|bytesCRLF
operator|=
name|Converter
operator|.
name|getByteReturn
argument_list|(
name|factory
operator|.
name|getCarriageReturn
argument_list|()
argument_list|)
expr_stmt|;
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
name|notEmpty
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
comment|// If the first line of the CSV file contains columns name, then we skip this line
if|if
condition|(
name|factory
operator|.
name|getSkipFirstLine
argument_list|()
condition|)
block|{
name|scanner
operator|.
name|nextLine
argument_list|()
expr_stmt|;
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
literal|"Counter "
operator|+
name|count
operator|++
operator|+
literal|" : content : "
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
comment|// Create POJO where CSV data will be stored
name|model
operator|=
name|factory
operator|.
name|factory
argument_list|()
expr_stmt|;
comment|// Split the CSV record according to the separator defined in
comment|// annotated class @CSVRecord
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
name|line
operator|.
name|split
argument_list|(
name|separator
argument_list|)
argument_list|)
decl_stmt|;
comment|// Bind data from CSV record with model classes
name|factory
operator|.
name|bind
argument_list|(
name|result
argument_list|,
name|model
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
return|return
name|models
return|;
block|}
finally|finally
block|{
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
name|ObjectHelper
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
comment|/**      * Method used to create the singleton of the BindyCsvFactory      */
DECL|method|getFactory (PackageScanClassResolver resolver)
specifier|public
name|BindyCsvFactory
name|getFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|modelFactory
operator|==
literal|null
condition|)
block|{
name|modelFactory
operator|=
operator|new
name|BindyCsvFactory
argument_list|(
name|resolver
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
block|}
return|return
name|modelFactory
return|;
block|}
DECL|method|setModelFactory (BindyCsvFactory modelFactory)
specifier|public
name|void
name|setModelFactory
parameter_list|(
name|BindyCsvFactory
name|modelFactory
parameter_list|)
block|{
name|this
operator|.
name|modelFactory
operator|=
name|modelFactory
expr_stmt|;
block|}
DECL|method|getPackageName ()
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|packageName
return|;
block|}
DECL|method|setPackageName (String packageName)
specifier|public
name|void
name|setPackageName
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|packageName
expr_stmt|;
block|}
block|}
end_class

end_unit

