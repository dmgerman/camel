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
name|Set
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
name|util
operator|.
name|AnnotationModelLoader
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
comment|/**  * The ModelFactory is the core class of the bindy component and allows to a)  * Generate a model associated to a record (CSV, ...) b) Bind data from a record  * to the POJOs c) Export data of POJOs to a record (CSV, ...) d) Format data  * into String, Date, Double, ... according to the format/pattern defined  */
end_comment

begin_class
DECL|class|BindyCsvFactory
specifier|public
class|class
name|BindyCsvFactory
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
DECL|field|modelsLoader
specifier|private
name|AnnotationModelLoader
name|modelsLoader
decl_stmt|;
DECL|field|models
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|models
decl_stmt|;
DECL|field|mapDataField
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
name|mapDataField
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
DECL|field|mapAnnotedField
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Field
argument_list|>
name|mapAnnotedField
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
DECL|field|mapAnnotedLinkField
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Field
argument_list|>
name|mapAnnotedLinkField
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Field
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
DECL|field|packageName
specifier|private
name|String
name|packageName
decl_stmt|;
DECL|method|BindyCsvFactory (String packageName)
specifier|public
name|BindyCsvFactory
parameter_list|(
name|String
name|packageName
parameter_list|)
throws|throws
name|Exception
block|{
name|modelsLoader
operator|=
operator|new
name|AnnotationModelLoader
argument_list|()
expr_stmt|;
name|this
operator|.
name|packageName
operator|=
name|packageName
expr_stmt|;
name|initModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data This process will scan for classes according to the package      * name provided, check the classes and fields annoted and retrieve the      * separator of the CSV record      *       * @throws Exception      */
DECL|method|initModel ()
specifier|public
name|void
name|initModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Find classes defined as Model
name|initModelClasses
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
comment|// Find annotated fields declared in the Model classes
name|initAnnotedFields
argument_list|()
expr_stmt|;
comment|// Get parameters : separator and skipfirstline from
comment|// @CSVrecord annotation
name|initCsvRecordParameters
argument_list|()
expr_stmt|;
block|}
comment|/**      * Find all the classes defined as model      */
DECL|method|initModelClasses (String packageName)
specifier|private
name|void
name|initModelClasses
parameter_list|(
name|String
name|packageName
parameter_list|)
throws|throws
name|Exception
block|{
name|models
operator|=
name|modelsLoader
operator|.
name|loadModels
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Find fields annoted in each class of the model      */
DECL|method|initAnnotedFields ()
specifier|private
name|void
name|initAnnotedFields
parameter_list|()
throws|throws
name|Exception
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
name|mapDataField
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
name|mapAnnotedField
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
name|mapAnnotedLinkField
operator|.
name|put
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
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
name|mapDataField
operator|.
name|get
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|Field
name|field
init|=
name|mapAnnotedField
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
name|Format
argument_list|<
name|?
argument_list|>
name|format
decl_stmt|;
name|String
name|pattern
init|=
name|dataField
operator|.
name|pattern
argument_list|()
decl_stmt|;
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
name|field
operator|.
name|set
argument_list|(
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
argument_list|,
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
comment|// must use a tree map to get a sorted iterator by the poisition defined by annotations
name|Map
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
name|dataFields
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|DataField
argument_list|>
argument_list|(
name|mapDataField
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|it
init|=
name|dataFields
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
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
name|mapDataField
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
name|mapAnnotedField
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
comment|// Retrieve the format associated to the type
name|Format
name|format
decl_stmt|;
name|String
name|pattern
init|=
name|dataField
operator|.
name|pattern
argument_list|()
decl_stmt|;
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
name|Object
name|obj
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
comment|// Convert the content to a String and append it to the builder
name|builder
operator|.
name|append
argument_list|(
name|format
operator|.
name|format
argument_list|(
name|field
operator|.
name|get
argument_list|(
name|obj
argument_list|)
argument_list|)
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
name|this
operator|.
name|getSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Link objects together (Only 1to1 relation is allowed)      */
DECL|method|link (Map<String, Object> model)
specifier|public
name|void
name|link
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
for|for
control|(
name|String
name|link
range|:
name|mapAnnotedLinkField
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Field
name|field
init|=
name|mapAnnotedLinkField
operator|.
name|get
argument_list|(
name|link
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Retrieve linked object
name|String
name|toClassName
init|=
name|field
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|to
init|=
name|model
operator|.
name|get
argument_list|(
name|toClassName
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|to
argument_list|,
literal|"No @link annotation has been defined for the oject to link"
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
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
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Factory method generating new instances of the model and adding them to a      * HashMap      *       * @return Map is a collection of the objects used to bind data from csv      *         records      * @throws Exception can be thrown      */
DECL|method|factory ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|factory
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mapModel
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
name|Object
name|obj
init|=
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|cl
argument_list|)
decl_stmt|;
comment|// Add instance of the class to the Map Model
name|mapModel
operator|.
name|put
argument_list|(
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
return|return
name|mapModel
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
comment|/**      * Get the parameter skipFirstLine      */
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
comment|/**      * Get paramaters defined in @Csvrecord annotation      */
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
if|if
condition|(
name|record
operator|!=
literal|null
condition|)
block|{
comment|// Get skipFirstLine parameter
name|skipFirstLine
operator|=
name|record
operator|.
name|skipFirstLine
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Skip First Line parameter of the CSV : "
operator|+
name|skipFirstLine
argument_list|)
expr_stmt|;
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
block|}
block|}
block|}
block|}
end_class

end_unit

