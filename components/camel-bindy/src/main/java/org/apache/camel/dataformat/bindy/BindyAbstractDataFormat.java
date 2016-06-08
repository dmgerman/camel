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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Optional
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
name|function
operator|.
name|Function
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
name|CamelContext
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
name|CamelContextAware
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
name|FormatFactories
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
name|factories
operator|.
name|DefaultFactoryRegistry
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
name|factories
operator|.
name|FactoryRegistry
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
name|factories
operator|.
name|FormatFactoryInterface
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
name|DataFormatName
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
name|Registry
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
name|support
operator|.
name|ServiceSupport
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

begin_class
DECL|class|BindyAbstractDataFormat
specifier|public
specifier|abstract
class|class
name|BindyAbstractDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BindyAbstractDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
DECL|field|modelFactory
specifier|private
name|BindyAbstractFactory
name|modelFactory
decl_stmt|;
DECL|field|classType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|classType
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|BindyAbstractDataFormat ()
specifier|public
name|BindyAbstractDataFormat
parameter_list|()
block|{     }
DECL|method|BindyAbstractDataFormat (Class<?> classType)
specifier|protected
name|BindyAbstractDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
block|}
DECL|method|getClassType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClassType
parameter_list|()
block|{
return|return
name|classType
return|;
block|}
DECL|method|setClassType (Class<?> classType)
specifier|public
name|void
name|setClassType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
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
DECL|method|getFactory ()
specifier|public
name|BindyAbstractFactory
name|getFactory
parameter_list|()
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
name|FormatFactory
name|formatFactory
init|=
name|createFormatFactory
argument_list|()
decl_stmt|;
name|registerAdditionalConverter
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
name|modelFactory
operator|=
name|createModelFactory
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
name|modelFactory
operator|.
name|setLocale
argument_list|(
name|locale
argument_list|)
expr_stmt|;
block|}
return|return
name|modelFactory
return|;
block|}
DECL|method|registerAdditionalConverter (FormatFactory formatFactory)
specifier|private
name|void
name|registerAdditionalConverter
parameter_list|(
name|FormatFactory
name|formatFactory
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
name|Function
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|FormatFactories
argument_list|>
name|g
init|=
name|aClass
lambda|->
name|aClass
operator|.
name|getAnnotation
argument_list|(
name|FormatFactories
operator|.
name|class
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|FormatFactories
argument_list|,
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|FormatFactoryInterface
argument_list|>
argument_list|>
argument_list|>
name|h
init|=
name|formatFactories
lambda|->
name|Arrays
operator|.
name|asList
argument_list|(
name|formatFactories
operator|.
name|value
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|FormatFactoryInterface
argument_list|>
argument_list|>
name|array
init|=
name|Optional
operator|.
name|ofNullable
argument_list|(
name|classType
argument_list|)
operator|.
name|map
argument_list|(
name|g
argument_list|)
operator|.
name|map
argument_list|(
name|h
argument_list|)
operator|.
name|orElse
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|FormatFactoryInterface
argument_list|>
name|l
range|:
name|array
control|)
block|{
name|formatFactory
operator|.
name|getFactoryRegistry
argument_list|()
operator|.
name|register
argument_list|(
name|l
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createFormatFactory ()
specifier|private
name|FormatFactory
name|createFormatFactory
parameter_list|()
block|{
name|FormatFactory
name|formatFactory
init|=
operator|new
name|FormatFactory
argument_list|()
decl_stmt|;
name|FactoryRegistry
name|factoryRegistry
init|=
name|createFactoryRegistry
argument_list|()
decl_stmt|;
name|formatFactory
operator|.
name|setFactoryRegistry
argument_list|(
name|factoryRegistry
argument_list|)
expr_stmt|;
return|return
name|formatFactory
return|;
block|}
DECL|method|createFactoryRegistry ()
specifier|private
name|FactoryRegistry
name|createFactoryRegistry
parameter_list|()
block|{
return|return
name|tryToGetFactoryRegistry
argument_list|()
return|;
block|}
DECL|method|tryToGetFactoryRegistry ()
specifier|private
name|FactoryRegistry
name|tryToGetFactoryRegistry
parameter_list|()
block|{
name|Function
argument_list|<
name|CamelContext
argument_list|,
name|Registry
argument_list|>
name|f
init|=
name|CamelContext
operator|::
name|getRegistry
decl_stmt|;
name|Function
argument_list|<
name|Registry
argument_list|,
name|Set
argument_list|<
name|FactoryRegistry
argument_list|>
argument_list|>
name|g
init|=
name|r
lambda|->
name|r
operator|.
name|findByType
argument_list|(
name|FactoryRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Set
argument_list|<
name|FactoryRegistry
argument_list|>
argument_list|,
name|FactoryRegistry
argument_list|>
name|h
init|=
name|factoryRegistries
lambda|->
block|{
if|if
condition|(
name|factoryRegistries
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Number of registered {}: {}"
argument_list|,
name|FactoryRegistry
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|factoryRegistries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factoryRegistries
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|factoryRegistries
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultFactoryRegistry
argument_list|()
return|;
block|}
block|}
decl_stmt|;
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|camelContext
argument_list|)
operator|.
name|map
argument_list|(
name|f
argument_list|)
operator|.
name|map
argument_list|(
name|g
argument_list|)
operator|.
name|map
argument_list|(
name|h
argument_list|)
operator|.
name|orElse
argument_list|(
operator|new
name|DefaultFactoryRegistry
argument_list|()
argument_list|)
return|;
block|}
DECL|method|setModelFactory (BindyAbstractFactory modelFactory)
specifier|public
name|void
name|setModelFactory
parameter_list|(
name|BindyAbstractFactory
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
DECL|method|createLinkedFieldsModel (Object model)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createLinkedFieldsModel
parameter_list|(
name|Object
name|model
parameter_list|)
throws|throws
name|IllegalAccessException
block|{
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
argument_list|<>
argument_list|()
decl_stmt|;
name|createLinkedFieldsModel
argument_list|(
name|model
argument_list|,
name|row
argument_list|)
expr_stmt|;
return|return
name|row
return|;
block|}
DECL|method|createLinkedFieldsModel (Object model, Map<String, Object> row)
specifier|protected
name|void
name|createLinkedFieldsModel
parameter_list|(
name|Object
name|model
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
parameter_list|)
throws|throws
name|IllegalAccessException
block|{
for|for
control|(
name|Field
name|field
range|:
name|model
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
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
name|boolean
name|accessible
init|=
name|field
operator|.
name|isAccessible
argument_list|()
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
operator|!
name|row
operator|.
name|containsKey
argument_list|(
name|field
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
name|field
operator|.
name|getType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|field
operator|.
name|get
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setAccessible
argument_list|(
name|accessible
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createModelFactory (FormatFactory formatFactory)
specifier|protected
specifier|abstract
name|BindyAbstractFactory
name|createModelFactory
parameter_list|(
name|FormatFactory
name|formatFactory
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|extractUnmarshalResult (List<Map<String, Object>> models)
specifier|protected
name|Object
name|extractUnmarshalResult
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|getClassType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// we expect to findForFormattingOptions this type in the models, and grab only that type
name|List
argument_list|<
name|Object
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|models
control|)
block|{
name|Object
name|data
init|=
name|entry
operator|.
name|get
argument_list|(
name|getClassType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if there is only 1 then dont return a list
if|if
condition|(
name|answer
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
else|else
block|{
return|return
name|models
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
block|}
end_class

end_unit

