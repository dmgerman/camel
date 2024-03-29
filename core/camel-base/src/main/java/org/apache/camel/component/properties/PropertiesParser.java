begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

begin_comment
comment|/**  * A parser to parse properties for a given input  */
end_comment

begin_interface
DECL|interface|PropertiesParser
specifier|public
interface|interface
name|PropertiesParser
block|{
comment|/**      * Parses the string and replaces the property placeholders with values from the given properties.      *      * @param text        the text to be parsed      * @param properties  the properties resolved which values should be looked up      * @param fallback    whether to support using fallback values if a property cannot be found      * @return the parsed text with replaced placeholders      * @throws IllegalArgumentException if uri syntax is not valid or a property is not found      */
DECL|method|parseUri (String text, PropertiesLookup properties, boolean fallback)
name|String
name|parseUri
parameter_list|(
name|String
name|text
parameter_list|,
name|PropertiesLookup
name|properties
parameter_list|,
name|boolean
name|fallback
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
comment|/**      * While parsing the uri using parseUri method each parsed property found invokes this callback.      *<p/>      * This strategy method allows you to hook into the parsing and do custom lookup and return the actual value to use.      *      * @param key        the key      * @param value      the value      * @param properties the properties resolved which values should be looked up      * @return the value to use      */
DECL|method|parseProperty (String key, String value, PropertiesLookup properties)
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|PropertiesLookup
name|properties
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

