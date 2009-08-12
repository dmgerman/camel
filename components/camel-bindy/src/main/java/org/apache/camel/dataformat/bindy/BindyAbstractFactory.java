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
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
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
name|Date
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
name|BigDecimalFormat
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
name|BigIntegerFormat
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
name|ByteFormat
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
name|BytePatternFormat
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
name|CharacterFormat
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
name|DatePatternFormat
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
name|DoubleFormat
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
name|DoublePatternFormat
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
name|FloatFormat
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
name|FloatPatternFormat
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
name|IntegerFormat
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
name|IntegerPatternFormat
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
name|LongFormat
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
name|LongPatternFormat
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
name|ShortFormat
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
name|ShortPatternFormat
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
name|StringFormat
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
comment|/**  * The BindyAbstractFactory implements what its common to all the formats  * supported by camel bindy  */
end_comment

begin_class
DECL|class|BindyAbstractFactory
specifier|public
specifier|abstract
class|class
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
name|BindyAbstractFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|models
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|>
name|models
decl_stmt|;
DECL|field|annotedLinkFields
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|annotedLinkFields
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|linkFields
specifier|protected
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
DECL|field|crlf
specifier|protected
name|String
name|crlf
decl_stmt|;
DECL|field|modelsLoader
specifier|private
name|AnnotationModelLoader
name|modelsLoader
decl_stmt|;
DECL|field|packageNames
specifier|private
name|String
index|[]
name|packageNames
decl_stmt|;
DECL|method|BindyAbstractFactory (PackageScanClassResolver resolver, String... packageNames)
specifier|public
name|BindyAbstractFactory
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
name|this
operator|.
name|modelsLoader
operator|=
operator|new
name|AnnotationModelLoader
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|this
operator|.
name|packageNames
operator|=
name|packageNames
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
literal|"Package(s) name : "
operator|+
name|packageNames
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data. This process will scan for classes according to the      * package name provided, check the annotated classes and fields.      *       * @throws Exception      */
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
name|this
operator|.
name|packageNames
argument_list|)
expr_stmt|;
block|}
comment|/**      * Find all the classes defined as model      */
DECL|method|initModelClasses (String... packageNames)
specifier|private
name|void
name|initModelClasses
parameter_list|(
name|String
modifier|...
name|packageNames
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
name|packageNames
argument_list|)
expr_stmt|;
block|}
comment|/**      * Find fields annoted in each class of the model      */
DECL|method|initAnnotedFields ()
specifier|public
specifier|abstract
name|void
name|initAnnotedFields
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|bind (List<String> data, Map<String, Object> model)
specifier|public
specifier|abstract
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
function_decl|;
DECL|method|unbind (Map<String, Object> model)
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * Link objects together      */
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
comment|// Iterate class by class
for|for
control|(
name|String
name|link
range|:
name|annotedLinkFields
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Field
argument_list|>
name|linkFields
init|=
name|annotedLinkFields
operator|.
name|get
argument_list|(
name|link
argument_list|)
decl_stmt|;
comment|// Iterate through Link fields list
for|for
control|(
name|Field
name|field
range|:
name|linkFields
control|)
block|{
comment|// Change protection for private field
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
block|}
comment|/**      * Factory method generating new instances of the model and adding them to a      * HashMap      *       * @return Map is a collection of the objects used to bind data from      *         records, messages      * @throws Exception      *         can be thrown      */
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
comment|/**      * Generate a unique key      *       * @param key1      *            The key of the section number      * @param key2      *            The key of the position of the field      * @return the key generated      */
DECL|method|generateKey (Integer key1, Integer key2)
specifier|protected
specifier|static
name|Integer
name|generateKey
parameter_list|(
name|Integer
name|key1
parameter_list|,
name|Integer
name|key2
parameter_list|)
block|{
name|String
name|key2Formated
init|=
name|getNumberFormat
argument_list|()
operator|.
name|format
argument_list|(
operator|(
name|long
operator|)
name|key2
argument_list|)
decl_stmt|;
name|String
name|keyGenerated
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|key1
argument_list|)
operator|+
name|key2Formated
decl_stmt|;
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|keyGenerated
argument_list|)
return|;
block|}
comment|/**      *       * @return NumberFormat      */
DECL|method|getNumberFormat ()
specifier|private
specifier|static
name|NumberFormat
name|getNumberFormat
parameter_list|()
block|{
comment|// Get instance of NumberFormat
name|NumberFormat
name|nf
init|=
name|NumberFormat
operator|.
name|getInstance
argument_list|()
decl_stmt|;
comment|// set max number of digits to 3 (thousands)
name|nf
operator|.
name|setMaximumIntegerDigits
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|nf
operator|.
name|setMinimumIntegerDigits
argument_list|(
literal|3
argument_list|)
expr_stmt|;
return|return
name|nf
return|;
block|}
DECL|method|getDefaultValueforPrimitive (Class<?> clazz)
specifier|public
specifier|static
name|Object
name|getDefaultValueforPrimitive
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|clazz
operator|==
name|byte
operator|.
name|class
condition|)
return|return
name|Byte
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|short
operator|.
name|class
condition|)
return|return
name|Short
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|int
operator|.
name|class
condition|)
return|return
name|Integer
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|long
operator|.
name|class
condition|)
return|return
name|Long
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|float
operator|.
name|class
condition|)
return|return
name|Float
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|double
operator|.
name|class
condition|)
return|return
name|Double
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|char
operator|.
name|class
condition|)
return|return
name|Character
operator|.
name|MIN_VALUE
return|;
elseif|else
if|if
condition|(
name|clazz
operator|==
name|boolean
operator|.
name|class
condition|)
return|return
literal|false
return|;
else|else
return|return
literal|null
return|;
block|}
comment|/**      * Find the carriage return set      */
DECL|method|getCarriageReturn ()
specifier|public
name|String
name|getCarriageReturn
parameter_list|()
block|{
return|return
name|crlf
return|;
block|}
block|}
end_class

end_unit

