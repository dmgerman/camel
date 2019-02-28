begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
import|;
end_import

begin_interface
DECL|interface|PropertiesComponent
specifier|public
interface|interface
name|PropertiesComponent
extends|extends
name|Component
block|{
comment|/**      * The default prefix token.      */
DECL|field|DEFAULT_PREFIX_TOKEN
name|String
name|DEFAULT_PREFIX_TOKEN
init|=
literal|"{{"
decl_stmt|;
comment|/**      * The default suffix token.      */
DECL|field|DEFAULT_SUFFIX_TOKEN
name|String
name|DEFAULT_SUFFIX_TOKEN
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
DECL|method|getPrefixToken ()
name|String
name|getPrefixToken
parameter_list|()
function_decl|;
DECL|method|getSuffixToken ()
name|String
name|getSuffixToken
parameter_list|()
function_decl|;
DECL|method|parseUri (String uri)
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|parseUri (String uri, String... uris)
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
modifier|...
name|uris
parameter_list|)
throws|throws
name|Exception
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
comment|/**      * Adds the list of locations to the current locations, where to load properties.      * You can use comma to separate multiple locations.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|addLocation (String location)
name|void
name|addLocation
parameter_list|(
name|String
name|location
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
block|}
end_interface

end_unit

