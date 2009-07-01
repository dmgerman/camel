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
name|KeyValuePairField
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
name|Message
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
comment|/**  * The BindyKeyValuePairFactory is the class who allows to bind data of type key  * value pair. Such format exist in financial messages FIX. This class allows to  * generate a model associated to message, bind data from a message to the  * POJOs, export data of POJOs to a message and format data into String, Date,  * Double, ... according to the format/pattern defined  */
end_comment

begin_class
DECL|class|BindyKeyValuePairFactory
specifier|public
class|class
name|BindyKeyValuePairFactory
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
name|BindyKeyValuePairFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|keyValuePairFields
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|KeyValuePairField
argument_list|>
name|keyValuePairFields
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Integer
argument_list|,
name|KeyValuePairField
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
DECL|field|keyValuePairSeparator
specifier|private
name|String
name|keyValuePairSeparator
decl_stmt|;
DECL|field|pairSeparator
specifier|private
name|String
name|pairSeparator
decl_stmt|;
DECL|field|messageOrdered
specifier|private
name|boolean
name|messageOrdered
decl_stmt|;
DECL|method|BindyKeyValuePairFactory (PackageScanClassResolver resolver, String... packageNames)
specifier|public
name|BindyKeyValuePairFactory
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
comment|// Initialize what is specific to Key Value Pair model
name|initKeyValuePairModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data This process will scan for classes according to the package      * name provided, check the annotated classes and fields. Next, we retrieve      * the parameters required like : Pair Separator& key value pair separator      *       * @throws Exception      */
DECL|method|initKeyValuePairModel ()
specifier|public
name|void
name|initKeyValuePairModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Find annotated KeyValuePairfields declared in the Model classes
name|initAnnotedFields
argument_list|()
expr_stmt|;
comment|// Initialize key value pair parameter(s)
name|initMessageParameters
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
name|KeyValuePairField
name|keyValuePairField
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|KeyValuePairField
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyValuePairField
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
literal|"Key declared in the class : "
operator|+
name|cl
operator|.
name|getName
argument_list|()
operator|+
literal|", key : "
operator|+
name|keyValuePairField
operator|.
name|tag
argument_list|()
operator|+
literal|", Field : "
operator|+
name|keyValuePairField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|keyValuePairFields
operator|.
name|put
argument_list|(
name|keyValuePairField
operator|.
name|tag
argument_list|()
argument_list|,
name|keyValuePairField
argument_list|)
expr_stmt|;
name|annotedFields
operator|.
name|put
argument_list|(
name|keyValuePairField
operator|.
name|tag
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
literal|"Data : "
operator|+
name|data
argument_list|)
expr_stmt|;
block|}
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
comment|// Separate the key from its value
comment|// e.g 8=FIX 4.1 --> key = 8 and Value = FIX 4.1
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|keyValuePairSeparator
argument_list|,
literal|"Key Value Pair not defined in the @Message annotation"
argument_list|)
expr_stmt|;
name|String
index|[]
name|keyValuePair
init|=
name|data
operator|.
name|get
argument_list|(
name|pos
argument_list|)
operator|.
name|split
argument_list|(
name|this
operator|.
name|getKeyValuePairSeparator
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|tag
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|keyValuePair
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|keyValuePair
index|[
literal|1
index|]
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
literal|"Key : "
operator|+
name|tag
operator|+
literal|", value : "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
name|KeyValuePairField
name|keyValuePairField
init|=
name|keyValuePairFields
operator|.
name|get
argument_list|(
name|tag
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|keyValuePairField
argument_list|,
literal|"No tag defined for the field : "
operator|+
name|tag
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|annotedFields
operator|.
name|get
argument_list|(
name|tag
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
literal|"Tag : "
operator|+
name|tag
operator|+
literal|", Data : "
operator|+
name|value
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
name|String
name|pattern
init|=
name|keyValuePairField
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
name|keyValuePairField
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
name|value
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|KeyValuePairField
argument_list|>
name|keyValuePairFieldsSorted
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|KeyValuePairField
argument_list|>
argument_list|(
name|keyValuePairFields
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|it
init|=
name|keyValuePairFieldsSorted
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
name|pairSeparator
argument_list|,
literal|"The pair separator has not been instantiated or property not defined in the @Message annotation"
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
name|getPairSeparator
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
name|getPairSeparator
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
name|KeyValuePairField
name|keyValuePairField
init|=
name|keyValuePairFieldsSorted
operator|.
name|get
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|keyValuePairField
argument_list|,
literal|"KeyValuePair is null !"
argument_list|)
expr_stmt|;
comment|// Retrieve the field
name|Field
name|field
init|=
name|annotedFields
operator|.
name|get
argument_list|(
name|keyValuePairField
operator|.
name|tag
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
literal|"Tag : "
operator|+
name|keyValuePairField
operator|.
name|tag
argument_list|()
operator|+
literal|", Field type : "
operator|+
name|field
operator|.
name|getType
argument_list|()
operator|+
literal|", class : "
operator|+
name|field
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Retrieve the format associated to the type
name|Format
name|format
decl_stmt|;
name|String
name|pattern
init|=
name|keyValuePairField
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
name|keyValuePairField
operator|.
name|precision
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get object to be formatted
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
if|if
condition|(
name|obj
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
name|obj
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
name|keyValuePairField
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
comment|// Add the content to the TreeMap according to the position defined
name|String
name|value
init|=
name|keyValuePairField
operator|.
name|tag
argument_list|()
operator|+
name|this
operator|.
name|getKeyValuePairSeparator
argument_list|()
operator|+
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
decl_stmt|;
name|positions
operator|.
name|put
argument_list|(
name|keyGenerated
argument_list|,
name|value
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
else|else
block|{
comment|// Convert the content to a String and append it to the
comment|// builder
comment|// Add the tag followed by its key value pair separator
comment|// the data and finish by the pair separator
name|String
name|value
init|=
name|keyValuePairField
operator|.
name|tag
argument_list|()
operator|+
name|this
operator|.
name|getKeyValuePairSeparator
argument_list|()
operator|+
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
operator|+
name|separator
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|value
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
literal|"Value added : "
operator|+
name|keyValuePairField
operator|.
name|tag
argument_list|()
operator|+
name|this
operator|.
name|getKeyValuePairSeparator
argument_list|()
operator|+
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
operator|+
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
operator|+
name|separator
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
comment|/**      * Find the pair separator used to delimit the key value pair fields      */
DECL|method|getPairSeparator ()
specifier|public
name|String
name|getPairSeparator
parameter_list|()
block|{
return|return
name|pairSeparator
return|;
block|}
comment|/**      * Find the key value pair separator used to link the key with its value      */
DECL|method|getKeyValuePairSeparator ()
specifier|public
name|String
name|getKeyValuePairSeparator
parameter_list|()
block|{
return|return
name|keyValuePairSeparator
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
comment|/**      * Get parameters defined in @Message annotation      */
DECL|method|initMessageParameters ()
specifier|private
name|void
name|initMessageParameters
parameter_list|()
block|{
if|if
condition|(
operator|(
name|pairSeparator
operator|==
literal|null
operator|)
operator|||
operator|(
name|keyValuePairSeparator
operator|==
literal|null
operator|)
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
comment|// Get annotation @Message from the class
name|Message
name|message
init|=
name|cl
operator|.
name|getAnnotation
argument_list|(
name|Message
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
name|message
operator|!=
literal|null
condition|)
block|{
comment|// Get Pair Separator parameter
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|message
operator|.
name|pairSeparator
argument_list|()
argument_list|,
literal|"No Pair Separator has been defined in the @Message annotation !"
argument_list|)
expr_stmt|;
name|pairSeparator
operator|=
name|message
operator|.
name|pairSeparator
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
literal|"Pair Separator defined for the message : "
operator|+
name|pairSeparator
argument_list|)
expr_stmt|;
block|}
comment|// Get KeyValuePair Separator parameter
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|message
operator|.
name|keyValuePairSeparator
argument_list|()
argument_list|,
literal|"No Key Value Pair Separator has been defined in the @Message annotation !"
argument_list|)
expr_stmt|;
name|keyValuePairSeparator
operator|=
name|message
operator|.
name|keyValuePairSeparator
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
literal|"Key Value Pair Separator defined for the message : "
operator|+
name|keyValuePairSeparator
argument_list|)
expr_stmt|;
block|}
comment|// Get carriage return parameter
name|crlf
operator|=
name|message
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
literal|"Carriage return defined for the message : "
operator|+
name|crlf
argument_list|)
expr_stmt|;
block|}
comment|// Get isOrderer parameter
name|messageOrdered
operator|=
name|message
operator|.
name|isOrdered
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
literal|"Is the message ordered in output : "
operator|+
name|messageOrdered
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

