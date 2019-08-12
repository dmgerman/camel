begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|Optional
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
name|function
operator|.
name|Predicate
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
name|Component
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * Component for property placeholders and loading properties from sources  * (such as .properties file from classpath or file system)  */
end_comment

begin_interface
DECL|interface|PropertiesComponent
specifier|public
interface|interface
name|PropertiesComponent
extends|extends
name|Component
extends|,
name|StaticService
block|{
comment|/**      * The prefix token.      */
DECL|field|PREFIX_TOKEN
name|String
name|PREFIX_TOKEN
init|=
literal|"{{"
decl_stmt|;
comment|/**      * The suffix token.      */
DECL|field|SUFFIX_TOKEN
name|String
name|SUFFIX_TOKEN
init|=
literal|"}}"
decl_stmt|;
comment|/**      * Has the component been created as a default by {@link org.apache.camel.CamelContext} during starting up Camel.      */
DECL|field|DEFAULT_CREATED
name|String
name|DEFAULT_CREATED
init|=
literal|"PropertiesComponentDefaultCreated"
decl_stmt|;
comment|/**      * Parses the input text and resolve all property placeholders from within the text.      *      * @param uri  input text      * @return text with resolved property placeholders      * @throws IllegalArgumentException is thrown if error during parsing      */
DECL|method|parseUri (String uri)
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Looks up the property with the given key      *      * @param key  the name of the property      * @return the property value if present      */
DECL|method|resolveProperty (String key)
name|Optional
argument_list|<
name|String
argument_list|>
name|resolveProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Loads the properties from the default locations and sources.      *      * @return the properties loaded.      */
DECL|method|loadProperties ()
name|Properties
name|loadProperties
parameter_list|()
function_decl|;
comment|/**      * Loads the properties from the default locations and sources filtering them out according to a predicate.      *</p>      *<pre>{@code      *     PropertiesComponent pc = getPropertiesComponent();      *     Properties props = pc.loadProperties(key -> key.startsWith("camel.component.seda"));      * }</pre>      *      * @param filter the predicate used to filter out properties based on the key.      * @return the properties loaded.      */
DECL|method|loadProperties (Predicate<String> filter)
name|Properties
name|loadProperties
parameter_list|(
name|Predicate
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
function_decl|;
comment|/**      * Gets the configured properties locations.      * This may be empty if the properties component has only been configured with {@link PropertiesSource}.      */
DECL|method|getLocations ()
name|List
argument_list|<
name|String
argument_list|>
name|getLocations
parameter_list|()
function_decl|;
comment|/**      * A list of locations to load properties. You can use comma to separate multiple locations.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocation (String location)
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
function_decl|;
comment|/**      * Adds the list of locations to the current locations, where to load properties.      * You can use comma to separate multiple locations.      */
DECL|method|addLocation (String location)
name|void
name|addLocation
parameter_list|(
name|String
name|location
parameter_list|)
function_decl|;
comment|/**      * Adds a custom {@link PropertiesSource} to use as source for loading and/or looking up property values.      */
DECL|method|addPropertiesSource (PropertiesSource propertiesSource)
name|void
name|addPropertiesSource
parameter_list|(
name|PropertiesSource
name|propertiesSource
parameter_list|)
function_decl|;
comment|/**      * Whether to silently ignore if a location cannot be located, such as a properties file not found.      */
DECL|method|setIgnoreMissingLocation (boolean ignoreMissingLocation)
name|void
name|setIgnoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
function_decl|;
comment|/**      * Sets initial properties which will be added before any property locations are loaded.      */
DECL|method|setInitialProperties (Properties initialProperties)
name|void
name|setInitialProperties
parameter_list|(
name|Properties
name|initialProperties
parameter_list|)
function_decl|;
comment|/**      * Sets a special list of override properties that take precedence      * and will use first, if a property exist.      */
DECL|method|setOverrideProperties (Properties overrideProperties)
name|void
name|setOverrideProperties
parameter_list|(
name|Properties
name|overrideProperties
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

