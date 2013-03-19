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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|spi
operator|.
name|PackageScanFilter
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
comment|/**  * The {@link BindyAbstractFactory} implements what its common to all the formats  * supported by Camel Bindy  */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BindyAbstractFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|annotatedLinkFields
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Field
argument_list|>
argument_list|>
name|annotatedLinkFields
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
DECL|field|models
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|models
decl_stmt|;
DECL|field|modelClassNames
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|modelClassNames
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
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
DECL|field|type
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|type
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
for|for
control|(
name|String
name|str
range|:
name|this
operator|.
name|packageNames
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Package name: {}"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
block|}
name|initModel
argument_list|()
expr_stmt|;
block|}
DECL|method|BindyAbstractFactory (PackageScanClassResolver resolver, Class<?> type)
specifier|public
name|BindyAbstractFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
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
name|type
operator|=
name|type
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
literal|"Class name: {}"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initModel
argument_list|()
expr_stmt|;
block|}
DECL|method|BindyAbstractFactory (PackageScanClassResolver resolver, Class<?> type, PackageScanFilter scanFilter)
specifier|public
name|BindyAbstractFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|PackageScanFilter
name|scanFilter
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
argument_list|,
name|scanFilter
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
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
literal|"Class name: {}"
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|initModel
argument_list|()
expr_stmt|;
block|}
DECL|method|BindyAbstractFactory (PackageScanClassResolver resolver, String[] packageNames, PackageScanFilter scanFilter)
specifier|public
name|BindyAbstractFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|String
index|[]
name|packageNames
parameter_list|,
name|PackageScanFilter
name|scanFilter
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
argument_list|,
name|scanFilter
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
for|for
control|(
name|String
name|str
range|:
name|this
operator|.
name|packageNames
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Package name: {}"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
block|}
name|initModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * method uses to initialize the model representing the classes who will      * bind the data. This process will scan for classes according to the      * package name provided, check the annotated classes and fields.      *      * @throws Exception      */
DECL|method|initModel ()
specifier|public
name|void
name|initModel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Find classes defined as Model
if|if
condition|(
name|packageNames
operator|!=
literal|null
condition|)
block|{
name|initModelClasses
argument_list|(
name|this
operator|.
name|packageNames
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
comment|// use the package name from the type as it may refer to types in the same package
name|String
name|pckName
init|=
name|type
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|initModelClasses
argument_list|(
name|pckName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either packagenames or type should be configured"
argument_list|)
throw|;
block|}
name|modelClassNames
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|models
control|)
block|{
name|modelClassNames
operator|.
name|add
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Find fields annotated in each class of the model      */
DECL|method|initAnnotatedFields ()
specifier|public
specifier|abstract
name|void
name|initAnnotatedFields
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|bind (List<String> data, Map<String, Object> model, int line)
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
parameter_list|,
name|int
name|line
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
name|annotatedLinkFields
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
name|annotatedLinkFields
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
literal|"No @link annotation has been defined for the object to link"
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
comment|/**      * Factory method generating new instances of the model and adding them to a      * HashMap      *      * @return Map is a collection of the objects used to bind data from      *         records, messages      * @throws Exception can be thrown      */
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
comment|/**      * Indicates whether this factory can support a row comprised of the identified classes      * @param classes  the names of the classes in the row      * @return true if the model supports the identified classes      */
DECL|method|supportsModel (Set<String> classes)
specifier|public
name|boolean
name|supportsModel
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|classes
parameter_list|)
block|{
return|return
name|modelClassNames
operator|.
name|containsAll
argument_list|(
name|classes
argument_list|)
return|;
block|}
comment|/**      * Generate a unique key      *      * @param key1 The key of the section number      * @param key2 The key of the position of the field      * @return the key generated      */
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
name|key2Formatted
decl_stmt|;
name|String
name|keyGenerated
decl_stmt|;
comment|// Test added for ticket - camel-2773
if|if
condition|(
operator|(
name|key1
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|key2
operator|!=
literal|null
operator|)
condition|)
block|{
name|key2Formatted
operator|=
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
expr_stmt|;
name|keyGenerated
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|key1
argument_list|)
operator|+
name|key2Formatted
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"@Section and/or @KeyValuePairDataField have not been defined"
argument_list|)
throw|;
block|}
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
name|keyGenerated
argument_list|)
return|;
block|}
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
DECL|method|getDefaultValueForPrimitive (Class<?> clazz)
specifier|public
specifier|static
name|Object
name|getDefaultValueForPrimitive
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
block|{
return|return
name|Byte
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|short
operator|.
name|class
condition|)
block|{
return|return
name|Short
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|int
operator|.
name|class
condition|)
block|{
return|return
name|Integer
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|long
operator|.
name|class
condition|)
block|{
return|return
name|Long
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|float
operator|.
name|class
condition|)
block|{
return|return
name|Float
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|double
operator|.
name|class
condition|)
block|{
return|return
name|Double
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|char
operator|.
name|class
condition|)
block|{
return|return
name|Character
operator|.
name|MIN_VALUE
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
comment|/**      * Format the object into a string according to the format rule defined      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|formatString (Format<?> format, Object value)
specifier|public
name|String
name|formatString
parameter_list|(
name|Format
argument_list|<
name|?
argument_list|>
name|format
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|strValue
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|strValue
operator|=
operator|(
operator|(
name|Format
argument_list|<
name|Object
argument_list|>
operator|)
name|format
operator|)
operator|.
name|format
argument_list|(
name|value
argument_list|)
expr_stmt|;
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
literal|"Formatting error detected for the value: "
operator|+
name|value
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|strValue
return|;
block|}
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
block|}
end_class

end_unit

