begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|LinkedHashMap
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
name|TreeMap
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
name|annotation
operator|.
name|CsvRecord
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
name|annotation
operator|.
name|DataField
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
name|annotation
operator|.
name|Link
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
name|annotation
operator|.
name|Section
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
comment|/**  * The BindyCsvFactory is the class who allows to : Generate a model associated  * to a CSV record, bind data from a record to the POJOs, export data of POJOs  * to a CSV record and format data into String, Date, Double, ... according to  * the format/pattern defined  */
end_comment

begin_class
DECL|class|BindyCsvFactory
specifier|public
class|class
name|BindyCsvFactory
extends|extends
name|BindyAbstractFactory
implements|implements
name|BindyFactory
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
name|BindyCsvFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataFields
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
name|dataFields
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|annotedFields
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Field
argument_list|>
name|annotedFields
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Integer
argument_list|,
name|Field
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|sections
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|sections
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|separator
specifier|private
name|String
name|separator
decl_stmt|;
DECL|field|skipFirstLine
specifier|private
name|boolean
name|skipFirstLine
decl_stmt|;
DECL|field|messageOrdered
specifier|private
name|boolean
name|messageOrdered
decl_stmt|;
DECL|method|BindyCsvFactory (PackageScanClassResolver resolver, String... packageNames)
specifier|public
name|BindyCsvFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|resolver
argument_list|,
name|packageNames
argument_list|)
expr_stmt|;
comment|// initialize specific parameters of the csv model
name|initCsvModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data. This process will scan for classes according to the package      * name provided, check the annotated classes and fields and retrieve the      * separator of the CSV record      *       * @throws Exception      */
DECL|method|initCsvModel ()
specifier|public
name|void
name|initCsvModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Find annotated Datafields declared in the Model classes
name|initAnnotedFields
argument_list|()
expr_stmt|;
comment|// initialize Csv parameter(s)
comment|// separator and skip first line from @CSVrecord annotation
name|initCsvRecordParameters
argument_list|()
expr_stmt|;
block|}
DECL|method|initAnnotedFields ()
specifier|public
name|void
name|initAnnotedFields
parameter_list|()
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
range|:
name|models
control|)
block|{
name|List
argument_list|<
name|Field
argument_list|>
name|linkFields
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|>
argument_list|()
decl_stmt|;
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
literal|"Class retrieved : "
operator|+
name|cl
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Field
name|field
range|:
name|cl
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|DataField
name|dataField
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|DataField
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataField
operator|!=
literal|null
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
literal|"Position defined in the class : "
operator|+
name|cl
operator|.
name|getName
argument_list|()
operator|+
literal|", position : "
operator|+
name|dataField
operator|.
name|pos
argument_list|()
operator|+
literal|", Field : "
operator|+
name|dataField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|dataFields
operator|.
name|put
argument_list|(
name|dataField
operator|.
name|pos
argument_list|()
argument_list|,
name|dataField
argument_list|)
expr_stmt|;
name|annotedFields
operator|.
name|put
argument_list|(
name|dataField
operator|.
name|pos
argument_list|()
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
name|Link
name|linkField
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Link
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|linkField
operator|!=
literal|null
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
literal|"Class linked  : "
operator|+
name|cl
operator|.
name|getName
argument_list|()
operator|+
literal|", Field"
operator|+
name|field
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|linkFields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|linkFields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|annotedLinkFields
operator|.
name|put
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|,
name|linkFields
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|bind (List<String> data, Map<String, Object> model)
specifier|public
name|void
name|bind
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|data
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|pos
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|pos
operator|<
name|data
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// Set the field with the data received
comment|// Only when no empty line is provided
comment|// Data is transformed according to the pattern defined or by
comment|// default the type of the field (int, double, String, ...)
if|if
condition|(
operator|!
name|data
operator|.
name|get
argument_list|(
name|pos
argument_list|)
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|DataField
name|dataField
init|=
name|dataFields
operator|.
name|get
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dataField
argument_list|,
literal|"No position defined for the field"
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|annotedFields
operator|.
name|get
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
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
literal|"Pos : "
operator|+
name|pos
operator|+
literal|", Data : "
operator|+
name|data
operator|.
name|get
argument_list|(
name|pos
argument_list|)
operator|+
literal|", Field type : "
operator|+
name|field
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Format
argument_list|<
name|?
argument_list|>
name|format
decl_stmt|;
comment|// Get pattern defined for the field
name|String
name|pattern
init|=
name|dataField
operator|.
name|pattern
argument_list|()
decl_stmt|;
comment|// Create format object to format the field
name|format
operator|=
name|FormatFactory
operator|.
name|getFormat
argument_list|(
name|field
operator|.
name|getType
argument_list|()
argument_list|,
name|pattern
argument_list|,
name|dataField
operator|.
name|precision
argument_list|()
argument_list|)
expr_stmt|;
comment|// field object to be set
name|Object
name|modelField
init|=
name|model
operator|.
name|get
argument_list|(
name|field
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// format the data received
name|Object
name|value
init|=
name|format
operator|.
name|parse
argument_list|(
name|data
operator|.
name|get
argument_list|(
name|pos
argument_list|)
argument_list|)
decl_stmt|;
name|field
operator|.
name|set
argument_list|(
name|modelField
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|pos
operator|++
expr_stmt|;
block|}
block|}
DECL|method|unbind (Map<String, Object> model)
specifier|public
name|String
name|unbind
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
name|dataFieldsSorted
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
argument_list|(
name|dataFields
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|it
init|=
name|dataFieldsSorted
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// Map containing the OUT position of the field
comment|// The key is double and is created using the position of the field and
comment|// location of the class in the message (using section)
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|positions
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// Check if separator exists
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|separator
argument_list|,
literal|"The separator has not been instantiated or property not defined in the @CsvRecord annotation"
argument_list|)
expr_stmt|;
name|char
name|separator
init|=
name|Converter
operator|.
name|getCharDelimitor
argument_list|(
name|this
operator|.
name|getSeparator
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"Separator converted : '0x"
operator|+
name|Integer
operator|.
name|toHexString
argument_list|(
name|separator
argument_list|)
operator|+
literal|"', from : "
operator|+
name|this
operator|.
name|getSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataField
name|dataField
init|=
name|dataFieldsSorted
operator|.
name|get
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
comment|// Retrieve the field
name|Field
name|field
init|=
name|annotedFields
operator|.
name|get
argument_list|(
name|dataField
operator|.
name|pos
argument_list|()
argument_list|)
decl_stmt|;
comment|// Change accessibility to allow to read protected/private fields
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Retrieve the format, pattern and precision associated to the type
name|Class
name|type
init|=
name|field
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|pattern
init|=
name|dataField
operator|.
name|pattern
argument_list|()
decl_stmt|;
name|int
name|precision
init|=
name|dataField
operator|.
name|precision
argument_list|()
decl_stmt|;
comment|// Create format
name|Format
name|format
init|=
name|FormatFactory
operator|.
name|getFormat
argument_list|(
name|type
argument_list|,
name|pattern
argument_list|,
name|precision
argument_list|)
decl_stmt|;
comment|// Get field from model
name|Object
name|modelField
init|=
name|model
operator|.
name|get
argument_list|(
name|field
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|modelField
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|isMessageOrdered
argument_list|()
condition|)
block|{
comment|// Generate a key using the number of the section
comment|// and the position of the field
name|Integer
name|key1
init|=
name|sections
operator|.
name|get
argument_list|(
name|modelField
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|key2
init|=
name|dataField
operator|.
name|position
argument_list|()
decl_stmt|;
name|Integer
name|keyGenerated
init|=
name|generateKey
argument_list|(
name|key1
argument_list|,
name|key2
argument_list|)
decl_stmt|;
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
literal|"Key generated : "
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|keyGenerated
argument_list|)
operator|+
literal|", for section : "
operator|+
name|key1
argument_list|)
expr_stmt|;
block|}
comment|// Get field value
name|Object
name|value
init|=
name|field
operator|.
name|get
argument_list|(
name|modelField
argument_list|)
decl_stmt|;
comment|// Add value to the list if not null
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// Format field value
name|String
name|valueFormated
init|=
name|format
operator|.
name|format
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Add the content to the TreeMap according to the position defined
name|positions
operator|.
name|put
argument_list|(
name|keyGenerated
argument_list|,
name|valueFormated
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
literal|"Positions size : "
operator|+
name|positions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// Get field value
name|Object
name|value
init|=
name|field
operator|.
name|get
argument_list|(
name|modelField
argument_list|)
decl_stmt|;
comment|// Add value to the list if not null
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// Format field value
name|String
name|valueFormated
init|=
name|format
operator|.
name|format
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|valueFormated
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// Iterate through the list to generate
comment|// the message according to the order/position
if|if
condition|(
name|this
operator|.
name|isMessageOrdered
argument_list|()
condition|)
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|posit
init|=
name|positions
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|posit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|value
init|=
name|positions
operator|.
name|get
argument_list|(
name|posit
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"Value added at the position ("
operator|+
name|posit
operator|+
literal|") : "
operator|+
name|value
operator|+
name|separator
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Find the separator used to delimit the CSV fields      */
DECL|method|getSeparator ()
specifier|public
name|String
name|getSeparator
parameter_list|()
block|{
return|return
name|separator
return|;
block|}
comment|/**      * Find the separator used to delimit the CSV fields      */
DECL|method|getSkipFirstLine ()
specifier|public
name|boolean
name|getSkipFirstLine
parameter_list|()
block|{
return|return
name|skipFirstLine
return|;
block|}
comment|/**      * Flag indicating if the message must be ordered      *       * @return boolean      */
DECL|method|isMessageOrdered ()
specifier|public
name|boolean
name|isMessageOrdered
parameter_list|()
block|{
return|return
name|messageOrdered
return|;
block|}
comment|/**      *       * Get paramaters defined in @Csvrecord annotation      *       */
DECL|method|initCsvRecordParameters ()
specifier|private
name|void
name|initCsvRecordParameters
parameter_list|()
block|{
if|if
condition|(
name|separator
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
range|:
name|models
control|)
block|{
comment|// Get annotation @CsvRecord from the class
name|CsvRecord
name|record
init|=
name|cl
operator|.
name|getAnnotation
argument_list|(
name|CsvRecord
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get annotation @Section from the class
name|Section
name|section
init|=
name|cl
operator|.
name|getAnnotation
argument_list|(
name|Section
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|record
operator|!=
literal|null
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
literal|"Csv record : "
operator|+
name|record
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Get skipFirstLine parameter
name|skipFirstLine
operator|=
name|record
operator|.
name|skipFirstLine
argument_list|()
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
literal|"Skip First Line parameter of the CSV : "
operator|+
name|skipFirstLine
argument_list|)
expr_stmt|;
block|}
comment|// Get Separator parameter
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|record
operator|.
name|separator
argument_list|()
argument_list|,
literal|"No separator has been defined in the @Record annotation !"
argument_list|)
expr_stmt|;
name|separator
operator|=
name|record
operator|.
name|separator
argument_list|()
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
literal|"Separator defined for the CSV : "
operator|+
name|separator
argument_list|)
expr_stmt|;
block|}
comment|// Get carriage return parameter
name|crlf
operator|=
name|record
operator|.
name|crlf
argument_list|()
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
literal|"Carriage return defined for the CSV : "
operator|+
name|crlf
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|section
operator|!=
literal|null
condition|)
block|{
comment|// Test if section number is not null
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|section
operator|.
name|number
argument_list|()
argument_list|,
literal|"No number has been defined for the section !"
argument_list|)
expr_stmt|;
comment|// Get section number and add it to the sections
name|sections
operator|.
name|put
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|,
name|section
operator|.
name|number
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

