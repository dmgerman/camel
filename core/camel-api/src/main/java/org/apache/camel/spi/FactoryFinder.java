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
name|Optional
import|;
end_import

begin_comment
comment|/**  * Finder to find factories from the resource classpath, usually<b>META-INF/services/org/apache/camel/</b>.  */
end_comment

begin_interface
DECL|interface|FactoryFinder
specifier|public
interface|interface
name|FactoryFinder
block|{
comment|/**      * Gets the resource classpath.      *      * @return the resource classpath.      */
DECL|method|getResourcePath ()
name|String
name|getResourcePath
parameter_list|()
function_decl|;
comment|/**      * Creates a new class instance using the key to lookup      *      * @param key is the key to add to the path to find a text file containing the factory name      * @return a newly created instance (if exists)      */
DECL|method|newInstance (String key)
name|Optional
argument_list|<
name|Object
argument_list|>
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Creates a new class instance using the key to lookup      *      * @param key is the key to add to the path to find a text file containing the factory name      * @param type the class type      * @return a newly created instance (if exists)      */
DECL|method|newInstance (String key, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Finds the given factory class using the key to lookup.      *      * @param key is the key to add to the path to find a text file containing the factory name      * @return the factory class      */
DECL|method|findClass (String key)
name|Optional
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findClass
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Finds the given factory class using the key to lookup.      *      * @param key is the key to add to the path to find a text file containing the factory name      * @param propertyPrefix prefix on key      * @return the factory class      */
DECL|method|findClass (String key, String propertyPrefix)
name|Optional
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findClass
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
function_decl|;
comment|/**      * Finds the given factory class using the key to lookup.      *      * @param key is the key to add to the path to find a text file containing the factory name      * @param propertyPrefix prefix on key      * @param clazz the class which is used for checking compatible      * @return the factory class      */
DECL|method|findClass (String key, String propertyPrefix, Class<?> clazz)
name|Optional
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findClass
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

