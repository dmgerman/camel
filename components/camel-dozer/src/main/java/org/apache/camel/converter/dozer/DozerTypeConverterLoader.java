begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|TypeConverter
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
name|ClassResolver
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
name|TypeConverterRegistry
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
name|camel
operator|.
name|util
operator|.
name|ReflectionHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|DozerBeanMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|Mapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|classmap
operator|.
name|ClassMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|classmap
operator|.
name|MappingFileData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|config
operator|.
name|BeanContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|config
operator|.
name|GlobalSettings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|loader
operator|.
name|api
operator|.
name|BeanMappingBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|loader
operator|.
name|xml
operator|.
name|MappingFileReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|loader
operator|.
name|xml
operator|.
name|XMLParserFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|util
operator|.
name|DozerClassLoader
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

begin_import
import|import static
name|org
operator|.
name|dozer
operator|.
name|classmap
operator|.
name|MappingDirection
operator|.
name|ONE_WAY
import|;
end_import

begin_comment
comment|/**  *<code>DozerTypeConverterLoader</code> provides the mechanism for registering  * a Dozer {@link Mapper} as {@link TypeConverter} for a {@link CamelContext}.  *<p/>  * While a mapper can be explicitly supplied as a parameter the  * {@link CamelContext}'s registry will also be searched for {@link Mapper}  * instances. A {@link DozerTypeConverter} is created to wrap each  * {@link Mapper} instance and the mapper is queried for the types it converts.  * The queried types are used to register the {@link TypeConverter} with the  * context via its {@link TypeConverterRegistry}.  */
end_comment

begin_class
DECL|class|DozerTypeConverterLoader
specifier|public
class|class
name|DozerTypeConverterLoader
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|transient
name|DozerBeanMapperConfiguration
name|configuration
decl_stmt|;
DECL|field|mapper
specifier|private
specifier|transient
name|DozerBeanMapper
name|mapper
decl_stmt|;
comment|/**      * Creates a<code>DozerTypeConverter</code> performing no      * {@link TypeConverter} registration.      */
DECL|method|DozerTypeConverterLoader ()
specifier|public
name|DozerTypeConverterLoader
parameter_list|()
block|{     }
comment|/**      * Creates a<code>DozerTypeConverter</code> that will search the given      * {@link CamelContext} for instances of {@link DozerBeanMapper}. Each      * discovered instance will be wrapped as a {@link DozerTypeConverter} and      * register as a {@link TypeConverter} with the context      *      * @param camelContext the context to register the      *                     {@link DozerTypeConverter} in      */
DECL|method|DozerTypeConverterLoader (CamelContext camelContext)
specifier|public
name|DozerTypeConverterLoader
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
try|try
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|this
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates a<code>DozerTypeConverter</code> using the given      * {@link DozerBeanMapperConfiguration} configuration.      *      * @param camelContext the context to register the      *                     {@link DozerTypeConverter} in      *      * @param configuration dozer mapping bean configuration.      */
DECL|method|DozerTypeConverterLoader (CamelContext camelContext, DozerBeanMapperConfiguration configuration)
specifier|public
name|DozerTypeConverterLoader
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DozerBeanMapperConfiguration
name|configuration
parameter_list|)
block|{
name|GlobalSettings
name|settings
init|=
name|GlobalSettings
operator|.
name|getInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Configuring GlobalSettings to use Camel classloader: {}"
argument_list|,
name|CamelToDozerClassResolverAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|settings
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"classLoaderBeanName"
argument_list|)
decl_stmt|;
name|ReflectionHelper
operator|.
name|setField
argument_list|(
name|field
argument_list|,
name|settings
argument_list|,
name|CamelToDozerClassResolverAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|IllegalStateException
argument_list|(
literal|"Cannot configure Dozer GlobalSettings to use CamelToDozerClassResolverAdapter as classloader due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// must set class loader before we create bean mapper
name|CamelToDozerClassResolverAdapter
name|adapter
init|=
operator|new
name|CamelToDozerClassResolverAdapter
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|BeanContainer
operator|.
name|getInstance
argument_list|()
operator|.
name|setClassLoader
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using DozerBeanMapperConfiguration: {}"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|DozerBeanMapper
name|mapper
init|=
name|createDozerBeanMapper
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|this
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates a<code>DozerTypeConverter</code> that will wrap the the given      * {@link DozerBeanMapper} as a {@link DozerTypeConverter} and register it      * with the given context. It will also search the context for      *      * @param camelContext the context to register the      *                     {@link DozerTypeConverter} in      * @param mapper       the DozerMapperBean to be wrapped as a type converter.      */
annotation|@
name|Deprecated
DECL|method|DozerTypeConverterLoader (CamelContext camelContext, DozerBeanMapper mapper)
specifier|public
name|DozerTypeConverterLoader
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DozerBeanMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|this
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Doses the actual querying and registration of {@link DozerTypeConverter}s      * with the {@link CamelContext}.      *      * @param camelContext the context to register the      *                     {@link DozerTypeConverter} in      * @param mapper       the DozerMapperBean to be wrapped as a type converter.      */
DECL|method|init (CamelContext camelContext, DozerBeanMapper mapper)
specifier|public
name|void
name|init
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DozerBeanMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
if|if
condition|(
name|mapper
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
block|}
name|CamelToDozerClassResolverAdapter
name|adapter
init|=
operator|new
name|CamelToDozerClassResolverAdapter
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|BeanContainer
operator|.
name|getInstance
argument_list|()
operator|.
name|setClassLoader
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DozerBeanMapper
argument_list|>
name|mappers
init|=
name|lookupDozerBeanMappers
argument_list|()
decl_stmt|;
comment|// only add if we do not already have it
if|if
condition|(
name|mapper
operator|!=
literal|null
operator|&&
operator|!
name|mappers
operator|.
name|containsValue
argument_list|(
name|mapper
argument_list|)
condition|)
block|{
name|mappers
operator|.
name|put
argument_list|(
literal|"parameter"
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
block|}
comment|// add any dozer bean mapper configurations
name|Map
argument_list|<
name|String
argument_list|,
name|DozerBeanMapperConfiguration
argument_list|>
name|configurations
init|=
name|lookupDozerBeanMapperConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurations
operator|!=
literal|null
operator|&&
name|configuration
operator|!=
literal|null
condition|)
block|{
comment|// filter out existing configuration, as we do not want to use it twice
name|String
name|key
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DozerBeanMapperConfiguration
argument_list|>
name|entry
range|:
name|configurations
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
name|configuration
condition|)
block|{
name|key
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|configurations
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|configurations
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|configurations
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Loaded "
operator|+
name|configurations
operator|.
name|size
argument_list|()
operator|+
literal|" Dozer configurations from Camel registry."
operator|+
literal|" Dozer is most efficient when there is a single mapper instance. Consider amalgamating instances."
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DozerBeanMapperConfiguration
argument_list|>
name|entry
range|:
name|configurations
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|id
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DozerBeanMapper
name|beanMapper
init|=
name|createDozerBeanMapper
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
comment|// only add if we do not already have it
if|if
condition|(
operator|!
name|mappers
operator|.
name|containsValue
argument_list|(
name|beanMapper
argument_list|)
condition|)
block|{
name|mappers
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|beanMapper
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|mappers
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Loaded "
operator|+
name|mappers
operator|.
name|size
argument_list|()
operator|+
literal|" Dozer mappers from Camel registry."
operator|+
literal|" Dozer is most efficient when there is a single mapper instance. Consider amalgamating instances."
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mappers
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No Dozer mappers found in Camel registry. You should add Dozer mappers as beans to the registry of the type: "
operator|+
name|DozerBeanMapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TypeConverterRegistry
name|registry
init|=
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DozerBeanMapper
argument_list|>
name|entry
range|:
name|mappers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|mapperId
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DozerBeanMapper
name|dozer
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ClassMap
argument_list|>
name|all
init|=
name|loadMappings
argument_list|(
name|camelContext
argument_list|,
name|mapperId
argument_list|,
name|dozer
argument_list|)
decl_stmt|;
name|registerClassMaps
argument_list|(
name|registry
argument_list|,
name|mapperId
argument_list|,
name|dozer
argument_list|,
name|all
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a {@link DozerBeanMapper} from the given configuration.      *      * @param configuration  the dozer bean mapper configuration.      * @return the created mapper      */
DECL|method|createDozerBeanMapper (DozerBeanMapperConfiguration configuration)
specifier|protected
name|DozerBeanMapper
name|createDozerBeanMapper
parameter_list|(
name|DozerBeanMapperConfiguration
name|configuration
parameter_list|)
block|{
name|DozerBeanMapper
name|mapper
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getMappingFiles
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|=
operator|new
name|DozerBeanMapper
argument_list|(
name|configuration
operator|.
name|getMappingFiles
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapper
operator|=
operator|new
name|DozerBeanMapper
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCustomConverters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|.
name|setCustomConverters
argument_list|(
name|configuration
operator|.
name|getCustomConverters
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getEventListeners
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|.
name|setEventListeners
argument_list|(
name|configuration
operator|.
name|getEventListeners
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCustomConvertersWithId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|.
name|setCustomConvertersWithId
argument_list|(
name|configuration
operator|.
name|getCustomConvertersWithId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCustomFieldMapper
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mapper
operator|.
name|setCustomFieldMapper
argument_list|(
name|configuration
operator|.
name|getCustomFieldMapper
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mapper
return|;
block|}
comment|/**      * Lookup the dozer {@link DozerBeanMapper} to be used.      */
DECL|method|lookupDozerBeanMappers ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DozerBeanMapper
argument_list|>
name|lookupDozerBeanMappers
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DozerBeanMapper
argument_list|>
argument_list|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|DozerBeanMapper
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Lookup the dozer {@link DozerBeanMapperConfiguration} to be used.      */
DECL|method|lookupDozerBeanMapperConfigurations ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DozerBeanMapperConfiguration
argument_list|>
name|lookupDozerBeanMapperConfigurations
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DozerBeanMapperConfiguration
argument_list|>
argument_list|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|DozerBeanMapperConfiguration
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
DECL|method|registerClassMaps (TypeConverterRegistry registry, String dozerId, DozerBeanMapper dozer, List<ClassMap> all)
specifier|protected
name|void
name|registerClassMaps
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|String
name|dozerId
parameter_list|,
name|DozerBeanMapper
name|dozer
parameter_list|,
name|List
argument_list|<
name|ClassMap
argument_list|>
name|all
parameter_list|)
block|{
name|DozerTypeConverter
name|converter
init|=
operator|new
name|DozerTypeConverter
argument_list|(
name|dozer
argument_list|)
decl_stmt|;
for|for
control|(
name|ClassMap
name|map
range|:
name|all
control|)
block|{
name|addDozerTypeConverter
argument_list|(
name|registry
argument_list|,
name|converter
argument_list|,
name|dozerId
argument_list|,
name|map
operator|.
name|getSrcClassToMap
argument_list|()
argument_list|,
name|map
operator|.
name|getDestClassToMap
argument_list|()
argument_list|)
expr_stmt|;
comment|// if not one way then add the other way around also
if|if
condition|(
name|map
operator|.
name|getType
argument_list|()
operator|!=
name|ONE_WAY
condition|)
block|{
name|addDozerTypeConverter
argument_list|(
name|registry
argument_list|,
name|converter
argument_list|,
name|dozerId
argument_list|,
name|map
operator|.
name|getDestClassToMap
argument_list|()
argument_list|,
name|map
operator|.
name|getSrcClassToMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addDozerTypeConverter (TypeConverterRegistry registry, DozerTypeConverter converter, String dozerId, Class<?> to, Class<?> from)
specifier|protected
name|void
name|addDozerTypeConverter
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|DozerTypeConverter
name|converter
parameter_list|,
name|String
name|dozerId
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|to
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|from
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|dozerId
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Added Dozer: {} as Camel type converter: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|dozerId
block|,
name|from
block|,
name|to
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Added Dozer as Camel type converter: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|from
block|,
name|to
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|registry
operator|.
name|addTypeConverter
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|converter
argument_list|)
expr_stmt|;
block|}
DECL|method|loadMappings (CamelContext camelContext, String mapperId, DozerBeanMapper mapper)
specifier|private
name|List
argument_list|<
name|ClassMap
argument_list|>
name|loadMappings
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|mapperId
parameter_list|,
name|DozerBeanMapper
name|mapper
parameter_list|)
block|{
name|List
argument_list|<
name|ClassMap
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassMap
argument_list|>
argument_list|()
decl_stmt|;
comment|// load the class map using the class resolver so we can load from classpath in OSGi
name|MappingFileReader
name|reader
init|=
operator|new
name|MappingFileReader
argument_list|(
name|XMLParserFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|mappingFiles
init|=
name|mapper
operator|.
name|getMappingFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|mappingFiles
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
for|for
control|(
name|String
name|name
range|:
name|mappingFiles
control|)
block|{
name|URL
name|url
init|=
name|loadMappingFile
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|MappingFileData
name|data
init|=
name|reader
operator|.
name|read
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|data
operator|.
name|getClassMaps
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Registers Dozer<code>BeanMappingBuilder</code> in current mapper instance.      * This method should be called instead of direct<code>mapper.addMapping()</code> invocation for Camel      * being able to register given type conversion.      *      * @param beanMappingBuilder api-based mapping builder      */
DECL|method|addMapping (BeanMappingBuilder beanMappingBuilder)
specifier|public
name|void
name|addMapping
parameter_list|(
name|BeanMappingBuilder
name|beanMappingBuilder
parameter_list|)
block|{
if|if
condition|(
name|mapper
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No mapper instance provided to "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|". Mapping has not been registered!"
argument_list|)
expr_stmt|;
return|return;
block|}
name|mapper
operator|.
name|addMapping
argument_list|(
name|beanMappingBuilder
argument_list|)
expr_stmt|;
name|MappingFileData
name|mappingFileData
init|=
name|beanMappingBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|TypeConverterRegistry
name|registry
init|=
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ClassMap
argument_list|>
name|classMaps
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassMap
argument_list|>
argument_list|()
decl_stmt|;
name|classMaps
operator|.
name|addAll
argument_list|(
name|mappingFileData
operator|.
name|getClassMaps
argument_list|()
argument_list|)
expr_stmt|;
name|registerClassMaps
argument_list|(
name|registry
argument_list|,
literal|null
argument_list|,
name|mapper
argument_list|,
name|classMaps
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Sets the {@link CamelContext}<b>and also</b> initializes this loader.      *<p/>      * The reason why {@link #init(org.apache.camel.CamelContext, org.dozer.DozerBeanMapper)} is also called      * is because making using Dozer in Spring XML files easier, as no need to use the init-method attribute.      *      * @param camelContext the CamelContext      */
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|camelContext
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
try|try
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|this
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|getMapper ()
specifier|public
name|DozerBeanMapper
name|getMapper
parameter_list|()
block|{
return|return
name|mapper
return|;
block|}
DECL|method|setMapper (DozerBeanMapper mapper)
specifier|public
name|void
name|setMapper
parameter_list|(
name|DozerBeanMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
block|}
DECL|method|loadMappingFile (ClassResolver classResolver, String mappingFile)
specifier|protected
specifier|static
name|URL
name|loadMappingFile
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|mappingFile
parameter_list|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|ResourceHelper
operator|.
name|resolveResourceAsUrl
argument_list|(
name|classResolver
argument_list|,
name|mappingFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
comment|// using the classloader of DozerClassLoader as a fallback
name|url
operator|=
name|DozerClassLoader
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|mappingFile
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
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
name|init
argument_list|(
name|camelContext
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

