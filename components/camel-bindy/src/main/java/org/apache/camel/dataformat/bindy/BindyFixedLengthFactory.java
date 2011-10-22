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
name|Collection
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
name|Map
operator|.
name|Entry
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
name|FixedLengthRecord
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
name|format
operator|.
name|FormatException
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The BindyCsvFactory is the class who allows to : Generate a model associated  * to a fixed length record, bind data from a record to the POJOs, export data of POJOs  * to a fixed length record and format data into String, Date, Double, ... according to  * the format/pattern defined  */
end_comment

begin_class
DECL|class|BindyFixedLengthFactory
specifier|public
class|class
name|BindyFixedLengthFactory
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BindyFixedLengthFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|isOneToMany
name|boolean
name|isOneToMany
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
DECL|field|annotatedFields
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Field
argument_list|>
name|annotatedFields
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
DECL|field|numberOptionalFields
specifier|private
name|int
name|numberOptionalFields
decl_stmt|;
DECL|field|numberMandatoryFields
specifier|private
name|int
name|numberMandatoryFields
decl_stmt|;
DECL|field|totalFields
specifier|private
name|int
name|totalFields
decl_stmt|;
DECL|field|hasHeader
specifier|private
name|boolean
name|hasHeader
decl_stmt|;
DECL|field|hasFooter
specifier|private
name|boolean
name|hasFooter
decl_stmt|;
DECL|field|paddingChar
specifier|private
name|char
name|paddingChar
decl_stmt|;
DECL|field|recordLength
specifier|private
name|int
name|recordLength
decl_stmt|;
DECL|method|BindyFixedLengthFactory (PackageScanClassResolver resolver, String... packageNames)
specifier|public
name|BindyFixedLengthFactory
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
comment|// initialize specific parameters of the fixed length model
name|initFixedLengthModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data. This process will scan for classes according to the      * package name provided, check the annotated classes and fields      */
DECL|method|initFixedLengthModel ()
specifier|public
name|void
name|initFixedLengthModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Find annotated fields declared in the Model classes
name|initAnnotatedFields
argument_list|()
expr_stmt|;
comment|// initialize Fixed length parameter(s)
comment|// from @FixedLengthrecord annotation
name|initFixedLengthRecordParameters
argument_list|()
expr_stmt|;
block|}
DECL|method|initAnnotatedFields ()
specifier|public
name|void
name|initAnnotatedFields
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
literal|"Class retrieved: {}"
argument_list|,
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
literal|"Position defined in the class: {}, position: {}, Field: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|cl
operator|.
name|getName
argument_list|()
block|,
name|dataField
operator|.
name|pos
argument_list|()
block|,
name|dataField
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataField
operator|.
name|required
argument_list|()
condition|)
block|{
operator|++
name|numberMandatoryFields
expr_stmt|;
block|}
else|else
block|{
operator|++
name|numberOptionalFields
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
name|annotatedFields
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
literal|"Class linked: {}, Field: {}"
argument_list|,
name|cl
operator|.
name|getName
argument_list|()
argument_list|,
name|field
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
name|annotatedLinkFields
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
name|totalFields
operator|=
name|numberMandatoryFields
operator|+
name|numberOptionalFields
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
literal|"Number of optional fields: {}"
argument_list|,
name|numberOptionalFields
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Number of mandatory fields: {}"
argument_list|,
name|numberMandatoryFields
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Total: {}"
argument_list|,
name|totalFields
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Will not be used in the case of a Fixed Length record
comment|// as we provide the content of the record and
comment|// we don't split it as this is the case for a CSV record
annotation|@
name|Override
DECL|method|bind (List<String> data, Map<String, Object> model, int line)
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
parameter_list|,
name|int
name|line
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|bind (String record, Map<String, Object> model, int line)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|record
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
parameter_list|,
name|int
name|line
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|pos
init|=
literal|1
decl_stmt|;
name|int
name|counterMandatoryFields
init|=
literal|0
decl_stmt|;
name|DataField
name|dataField
decl_stmt|;
name|String
name|token
decl_stmt|;
name|int
name|offset
decl_stmt|;
name|int
name|length
decl_stmt|;
name|Field
name|field
decl_stmt|;
name|String
name|pattern
decl_stmt|;
comment|// Iterate through the list of positions
comment|// defined in the @DataField
comment|// and grab the data from the line
name|Collection
name|c
init|=
name|dataFields
operator|.
name|values
argument_list|()
decl_stmt|;
name|Iterator
name|itr
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|dataField
operator|=
operator|(
name|DataField
operator|)
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
name|offset
operator|=
name|dataField
operator|.
name|pos
argument_list|()
expr_stmt|;
name|length
operator|=
name|dataField
operator|.
name|length
argument_list|()
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|offset
argument_list|,
literal|"Position/offset is not defined for the field: "
operator|+
name|dataField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|offset
argument_list|,
literal|"Length is not defined for the field: "
operator|+
name|dataField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|offset
operator|-
literal|1
operator|<=
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Offset/Position of the field "
operator|+
name|dataField
operator|.
name|toString
argument_list|()
operator|+
literal|" cannot be negative"
argument_list|)
throw|;
block|}
name|token
operator|=
name|record
operator|.
name|substring
argument_list|(
name|offset
operator|-
literal|1
argument_list|,
name|offset
operator|+
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataField
operator|.
name|trim
argument_list|()
condition|)
block|{
name|token
operator|=
name|token
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// Check mandatory field
if|if
condition|(
name|dataField
operator|.
name|required
argument_list|()
condition|)
block|{
comment|// Increment counter of mandatory fields
operator|++
name|counterMandatoryFields
expr_stmt|;
comment|// Check if content of the field is empty
comment|// This is not possible for mandatory fields
if|if
condition|(
name|token
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The mandatory field defined at the position "
operator|+
name|pos
operator|+
literal|" is empty for the line: "
operator|+
name|line
argument_list|)
throw|;
block|}
block|}
comment|// Get Field to be setted
name|field
operator|=
name|annotatedFields
operator|.
name|get
argument_list|(
name|offset
argument_list|)
expr_stmt|;
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
literal|"Pos/Offset: {}, Data: {}, Field type: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|offset
block|,
name|token
block|,
name|field
operator|.
name|getType
argument_list|()
block|}
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
name|pattern
operator|=
name|dataField
operator|.
name|pattern
argument_list|()
expr_stmt|;
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
name|getLocale
argument_list|()
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
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|token
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
try|try
block|{
name|value
operator|=
name|format
operator|.
name|parse
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FormatException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ie
operator|.
name|getMessage
argument_list|()
operator|+
literal|", position: "
operator|+
name|offset
operator|+
literal|", line: "
operator|+
name|line
argument_list|,
name|ie
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parsing error detected for field defined at the position/offset: "
operator|+
name|offset
operator|+
literal|", line: "
operator|+
name|line
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|value
operator|=
name|getDefaultValueForPrimitive
argument_list|(
name|field
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|set
argument_list|(
name|modelField
argument_list|,
name|value
argument_list|)
expr_stmt|;
operator|++
name|pos
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Counter mandatory fields: {}"
argument_list|,
name|counterMandatoryFields
argument_list|)
expr_stmt|;
if|if
condition|(
name|pos
operator|<
name|totalFields
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Some fields are missing (optional or mandatory), line: "
operator|+
name|line
argument_list|)
throw|;
block|}
if|if
condition|(
name|counterMandatoryFields
operator|<
name|numberMandatoryFields
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Some mandatory fields are missing, line: "
operator|+
name|line
argument_list|)
throw|;
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
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|results
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|clazz
range|:
name|models
control|)
block|{
if|if
condition|(
name|model
operator|.
name|containsKey
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|obj
init|=
name|model
operator|.
name|get
argument_list|(
name|clazz
operator|.
name|getName
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
literal|"Model object: {}, class: {}"
argument_list|,
name|obj
argument_list|,
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
comment|// Generate Fixed Length table
comment|// containing the positions of the fields
name|generateFixedLengthPositionMap
argument_list|(
name|clazz
argument_list|,
name|obj
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Convert Map<Integer, List> into List<List>
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|sortValues
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|results
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|sortValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// Get list of values
name|List
argument_list|<
name|String
argument_list|>
name|val
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|val
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      *       * Generate a table containing the data formatted and sorted with their position/offset      * The result is placed in the Map<Integer, List> results      */
DECL|method|generateFixedLengthPositionMap (Class clazz, Object obj, Map<Integer, List<String>> results)
specifier|private
name|void
name|generateFixedLengthPositionMap
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Object
name|obj
parameter_list|,
name|Map
argument_list|<
name|Integer
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|results
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|result
init|=
literal|""
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|clazz
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataField
name|datafield
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
name|datafield
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
comment|// Retrieve the format, pattern and precision associated to
comment|// the type
name|Class
argument_list|<
name|?
argument_list|>
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
name|datafield
operator|.
name|pattern
argument_list|()
decl_stmt|;
name|int
name|precision
init|=
name|datafield
operator|.
name|precision
argument_list|()
decl_stmt|;
comment|// Create format
name|Format
argument_list|<
name|?
argument_list|>
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
name|getLocale
argument_list|()
argument_list|,
name|precision
argument_list|)
decl_stmt|;
comment|// Get field value
name|Object
name|value
init|=
name|field
operator|.
name|get
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|result
operator|=
name|formatString
argument_list|(
name|format
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// trim if enabled
if|if
condition|(
name|datafield
operator|.
name|trim
argument_list|()
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// Get length of the field, alignment (LEFT or RIGHT), pad
name|int
name|fieldLength
init|=
name|datafield
operator|.
name|length
argument_list|()
decl_stmt|;
name|String
name|align
init|=
name|datafield
operator|.
name|align
argument_list|()
decl_stmt|;
name|char
name|padCharField
init|=
name|datafield
operator|.
name|paddingChar
argument_list|()
decl_stmt|;
name|char
name|padChar
decl_stmt|;
if|if
condition|(
name|fieldLength
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|temp
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// Check if we must pad
if|if
condition|(
name|result
operator|.
name|length
argument_list|()
operator|<
name|fieldLength
condition|)
block|{
comment|// No padding defined for the field
if|if
condition|(
name|padCharField
operator|==
literal|0
condition|)
block|{
comment|// We use the padding defined for the Record
name|padChar
operator|=
name|paddingChar
expr_stmt|;
block|}
else|else
block|{
name|padChar
operator|=
name|padCharField
expr_stmt|;
block|}
if|if
condition|(
name|align
operator|.
name|contains
argument_list|(
literal|"R"
argument_list|)
condition|)
block|{
name|temp
operator|.
name|append
argument_list|(
name|generatePaddingChars
argument_list|(
name|padChar
argument_list|,
name|fieldLength
argument_list|,
name|result
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|temp
operator|.
name|append
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|align
operator|.
name|contains
argument_list|(
literal|"L"
argument_list|)
condition|)
block|{
name|temp
operator|.
name|append
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|temp
operator|.
name|append
argument_list|(
name|generatePaddingChars
argument_list|(
name|padChar
argument_list|,
name|fieldLength
argument_list|,
name|result
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Alignment for the field: "
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|" must be equal to R for RIGHT or L for LEFT"
argument_list|)
throw|;
block|}
name|result
operator|=
name|temp
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|length
argument_list|()
operator|>
name|fieldLength
condition|)
block|{
comment|// we are bigger than allowed
comment|// is clipped enabled? if so clip the field
if|if
condition|(
name|datafield
operator|.
name|clip
argument_list|()
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fieldLength
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Length for the "
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|" must not be larger than allowed, was: "
operator|+
name|result
operator|.
name|length
argument_list|()
operator|+
literal|", allowed: "
operator|+
name|fieldLength
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Length of the field: "
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|" is a mandatory field and cannot be equal to zero or to be negative, was: "
operator|+
name|fieldLength
argument_list|)
throw|;
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
literal|"Value to be formatted: {}, position: {}, and its formatted value: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|value
block|,
name|datafield
operator|.
name|pos
argument_list|()
block|,
name|result
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
literal|""
expr_stmt|;
block|}
name|Integer
name|key
decl_stmt|;
name|key
operator|=
name|datafield
operator|.
name|pos
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|results
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|results
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|generatePaddingChars (char pad, int lengthField, int lengthString)
specifier|private
name|String
name|generatePaddingChars
parameter_list|(
name|char
name|pad
parameter_list|,
name|int
name|lengthField
parameter_list|,
name|int
name|lengthString
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|lengthField
operator|-
name|lengthString
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toString
argument_list|(
name|pad
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Get parameters defined in @FixedLengthRecord annotation      */
DECL|method|initFixedLengthRecordParameters ()
specifier|private
name|void
name|initFixedLengthRecordParameters
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
comment|// Get annotation @FixedLengthRecord from the class
name|FixedLengthRecord
name|record
init|=
name|cl
operator|.
name|getAnnotation
argument_list|(
name|FixedLengthRecord
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Fixed length record: {}"
argument_list|,
name|record
argument_list|)
expr_stmt|;
comment|// Get carriage return parameter
name|crlf
operator|=
name|record
operator|.
name|crlf
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Carriage return defined for the CSV: {}"
argument_list|,
name|crlf
argument_list|)
expr_stmt|;
comment|// Get hasHeader parameter
name|hasHeader
operator|=
name|record
operator|.
name|hasHeader
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Has Header: {}"
argument_list|,
name|hasHeader
argument_list|)
expr_stmt|;
comment|// Get hasFooter parameter
name|hasFooter
operator|=
name|record
operator|.
name|hasFooter
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Has Footer: {}"
argument_list|,
name|hasFooter
argument_list|)
expr_stmt|;
comment|// Get padding character
name|paddingChar
operator|=
name|record
operator|.
name|paddingChar
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Padding char: {}"
argument_list|,
name|paddingChar
argument_list|)
expr_stmt|;
comment|// Get length of the record
name|recordLength
operator|=
name|record
operator|.
name|length
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Length of the record: {}"
argument_list|,
name|recordLength
argument_list|)
expr_stmt|;
comment|// Get length of the record
name|recordLength
operator|=
name|record
operator|.
name|length
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Length of the record: {}"
argument_list|,
name|recordLength
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Flag indicating if we have a header      */
DECL|method|hasHeader ()
specifier|public
name|boolean
name|hasHeader
parameter_list|()
block|{
return|return
name|hasHeader
return|;
block|}
comment|/**      * Flag indicating if we have a footer      */
DECL|method|hasFooter ()
specifier|public
name|boolean
name|hasFooter
parameter_list|()
block|{
return|return
name|hasFooter
return|;
block|}
comment|/**      * Padding char used to fill the field      */
DECL|method|paddingchar ()
specifier|public
name|char
name|paddingchar
parameter_list|()
block|{
return|return
name|paddingChar
return|;
block|}
DECL|method|recordLength ()
specifier|public
name|int
name|recordLength
parameter_list|()
block|{
return|return
name|recordLength
return|;
block|}
block|}
end_class

end_unit

