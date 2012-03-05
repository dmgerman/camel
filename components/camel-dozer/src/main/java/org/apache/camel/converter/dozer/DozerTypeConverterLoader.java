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

begin_comment
comment|/**  *<code>DozerTypeConverterLoader</code> provides the mechanism for registering  * a Dozer {@link Mapper} as {@link TypeConverter} for a {@link CamelContext}.  *<p/>  * While a mapper can be explicitly supplied as a parameter the  * {@link CamelContext}'s registry will also be searched for {@link Mapper}  * instances. A {@link DozerTypeConverter} is created to wrap each  * {@link Mapper} instance and the mapper is queried for the types it converts.  * The queried types are used to register the {@link TypeConverter} with the  * context via its {@link TypeConverterRegistry}.  */
end_comment

begin_class
DECL|class|DozerTypeConverterLoader
specifier|public
class|class
name|DozerTypeConverterLoader
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
DECL|field|mapper
specifier|private
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
name|init
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a<code>DozerTypeConverter</code> that will wrap the the given      * {@link DozerBeanMapper} as a {@link DozerTypeConverter} and register it      * with the given context. It will also search the context for      *      * @param camelContext the context to register the      *                     {@link DozerTypeConverter} in      * @param mapper       the DozerMapperBean to be wrapped as a type converter.      */
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
name|init
argument_list|(
name|camelContext
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
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
name|lookupByType
argument_list|(
name|DozerBeanMapper
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapper
operator|!=
literal|null
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
name|DozerBeanMapper
name|dozer
range|:
name|mappers
operator|.
name|values
argument_list|()
control|)
block|{
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
name|dozer
argument_list|)
decl_stmt|;
name|registerClassMaps
argument_list|(
name|registry
argument_list|,
name|dozer
argument_list|,
name|all
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerClassMaps (TypeConverterRegistry registry, DozerBeanMapper dozer, List<ClassMap> all)
specifier|private
name|void
name|registerClassMaps
parameter_list|(
name|TypeConverterRegistry
name|registry
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
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Added {} -> {} as type converter to: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|map
operator|.
name|getSrcClassName
argument_list|()
block|,
name|map
operator|.
name|getDestClassName
argument_list|()
block|,
name|registry
block|}
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|addTypeConverter
argument_list|(
name|map
operator|.
name|getSrcClassToMap
argument_list|()
argument_list|,
name|map
operator|.
name|getDestClassToMap
argument_list|()
argument_list|,
name|converter
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addTypeConverter
argument_list|(
name|map
operator|.
name|getDestClassToMap
argument_list|()
argument_list|,
name|map
operator|.
name|getSrcClassToMap
argument_list|()
argument_list|,
name|converter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadMappings (CamelContext camelContext, DozerBeanMapper mapper)
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
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsURL
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|init
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|class|CamelToDozerClassResolverAdapter
specifier|private
specifier|static
specifier|final
class|class
name|CamelToDozerClassResolverAdapter
implements|implements
name|DozerClassLoader
block|{
DECL|field|classResolver
specifier|private
specifier|final
name|ClassResolver
name|classResolver
decl_stmt|;
DECL|method|CamelToDozerClassResolverAdapter (CamelContext camelContext)
specifier|private
name|CamelToDozerClassResolverAdapter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|classResolver
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
expr_stmt|;
block|}
DECL|method|loadClass (String s)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|classResolver
operator|.
name|resolveClass
argument_list|(
name|s
argument_list|)
return|;
block|}
DECL|method|loadResource (String s)
specifier|public
name|URL
name|loadResource
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|URL
name|url
init|=
name|classResolver
operator|.
name|loadResourceAsURL
argument_list|(
name|s
argument_list|)
decl_stmt|;
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
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
block|}
block|}
end_class

end_unit

