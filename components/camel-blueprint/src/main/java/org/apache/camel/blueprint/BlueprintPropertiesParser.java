begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
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
name|Method
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
name|LinkedHashSet
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
name|Properties
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
name|aries
operator|.
name|blueprint
operator|.
name|ExtendedBeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ext
operator|.
name|AbstractPropertyPlaceholder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ext
operator|.
name|PropertyPlaceholder
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
name|component
operator|.
name|properties
operator|.
name|DefaultPropertiesParser
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|component
operator|.
name|properties
operator|.
name|PropertiesParser
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
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ComponentMetadata
import|;
end_import

begin_comment
comment|/**  * Blueprint {@link PropertiesParser} which supports looking up  * property placeholders from the Blueprint Property Placeholder Service.  *<p/>  * This implementation will sit on top of any existing configured  * {@link PropertiesParser} and will delegate to those in case Blueprint could not  * resolve the property.  */
end_comment

begin_class
DECL|class|BlueprintPropertiesParser
specifier|public
class|class
name|BlueprintPropertiesParser
extends|extends
name|DefaultPropertiesParser
block|{
DECL|field|propertiesComponent
specifier|private
specifier|final
name|PropertiesComponent
name|propertiesComponent
decl_stmt|;
DECL|field|container
specifier|private
specifier|final
name|BlueprintContainer
name|container
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|PropertiesParser
name|delegate
decl_stmt|;
DECL|field|placeholders
specifier|private
specifier|final
name|Set
argument_list|<
name|AbstractPropertyPlaceholder
argument_list|>
name|placeholders
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|AbstractPropertyPlaceholder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|method
specifier|private
name|Method
name|method
decl_stmt|;
DECL|method|BlueprintPropertiesParser (PropertiesComponent propertiesComponent, BlueprintContainer container, PropertiesParser delegate)
specifier|public
name|BlueprintPropertiesParser
parameter_list|(
name|PropertiesComponent
name|propertiesComponent
parameter_list|,
name|BlueprintContainer
name|container
parameter_list|,
name|PropertiesParser
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|propertiesComponent
operator|=
name|propertiesComponent
expr_stmt|;
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
comment|/**      * Lookup the ids of the Blueprint property placeholder services in the      * Blueprint container.      *      * @return the ids, will be an empty array if none found.      */
DECL|method|lookupPropertyPlaceholderIds ()
specifier|public
name|String
index|[]
name|lookupPropertyPlaceholderIds
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|componentId
range|:
name|container
operator|.
name|getComponentIds
argument_list|()
control|)
block|{
name|String
name|id
init|=
operator|(
name|String
operator|)
name|componentId
decl_stmt|;
name|ComponentMetadata
name|meta
init|=
name|container
operator|.
name|getComponentMetadata
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|meta
operator|instanceof
name|ExtendedBeanMetadata
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
operator|(
operator|(
name|ExtendedBeanMetadata
operator|)
name|meta
operator|)
operator|.
name|getRuntimeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
operator|&&
name|AbstractPropertyPlaceholder
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ids
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|ids
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Adds the given Blueprint property placeholder service with the given id      *      * @param id id of the Blueprint property placeholder service to add.      */
DECL|method|addPropertyPlaceholder (String id)
specifier|public
name|void
name|addPropertyPlaceholder
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Object
name|component
init|=
name|container
operator|.
name|getComponentInstance
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|instanceof
name|AbstractPropertyPlaceholder
condition|)
block|{
name|AbstractPropertyPlaceholder
name|placeholder
init|=
operator|(
name|AbstractPropertyPlaceholder
operator|)
name|component
decl_stmt|;
name|placeholders
operator|.
name|add
argument_list|(
name|placeholder
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Adding Blueprint PropertyPlaceholder: {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|method
operator|=
name|AbstractPropertyPlaceholder
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getProperty"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot add blueprint property placeholder: "
operator|+
name|id
operator|+
literal|" as the method getProperty is not accessible"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, Properties properties)
specifier|public
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Parsing property key: {} with value: {}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
literal|null
decl_stmt|;
comment|// prefer any override properties
comment|// this logic is special for BlueprintPropertiesParser as we otherwise prefer
comment|// to use the AbstractPropertyPlaceholder from OSGi blueprint config admins
comment|// service to lookup otherwise
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|propertiesComponent
operator|.
name|getOverrideProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|(
name|String
operator|)
name|propertiesComponent
operator|.
name|getOverrideProperties
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|// lookup key in blueprint and return its value
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|key
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractPropertyPlaceholder
name|placeholder
range|:
name|placeholders
control|)
block|{
name|boolean
name|isDefault
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|placeholders
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// okay we have multiple placeholders and we want to return the answer that
comment|// is not the default placeholder if there is multiple keys
if|if
condition|(
name|placeholder
operator|instanceof
name|PropertyPlaceholder
condition|)
block|{
name|Map
name|map
init|=
operator|(
operator|(
name|PropertyPlaceholder
operator|)
name|placeholder
operator|)
operator|.
name|getDefaultProperties
argument_list|()
decl_stmt|;
name|isDefault
operator|=
name|map
operator|!=
literal|null
operator|&&
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Blueprint property key: {} is part of default properties: {}"
argument_list|,
name|key
argument_list|,
name|isDefault
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
name|candidate
init|=
operator|(
name|String
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|placeholder
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|candidate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
operator|||
operator|!
name|isDefault
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Blueprint parsed candidate property key: {} as value: {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|candidate
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Here we just catch the exception and try to use other candidate
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Blueprint parsed property key: {} as value: {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
comment|// if there is a delegate then let it parse the current answer as it may be jasypt which
comment|// need to decrypt values
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|String
name|delegateAnswer
init|=
name|delegate
operator|.
name|parseProperty
argument_list|(
name|key
argument_list|,
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
name|value
argument_list|,
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|delegateAnswer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|delegateAnswer
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"delegate property parser parsed the property key:{} as value: {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot parser the property key: "
operator|+
name|key
operator|+
literal|" with value: "
operator|+
name|value
argument_list|)
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Returning parsed property key: {} as value: {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

