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

begin_comment
comment|/**  * Represents a service registry which may be implemented via a Spring ApplicationContext,  * via JNDI, a simple Map or the OSGi Service Registry  *  * @version   */
end_comment

begin_interface
DECL|interface|Registry
specifier|public
interface|interface
name|Registry
block|{
comment|/**      * Looks up a service in the registry based purely on name,      * returning the service or<tt>null</tt> if it could not be found.      *      * @param name the name of the service      * @return the service from the registry or<tt>null</tt> if it could not be found      */
DECL|method|lookupByName (String name)
name|Object
name|lookupByName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Looks up a service in the registry, returning the service or<tt>null</tt> if it could not be found.      *      * @param name the name of the service      * @param type the type of the required service      * @return the service from the registry or<tt>null</tt> if it could not be found      */
DECL|method|lookupByNameAndType (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupByNameAndType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Finds services in the registry by their type.      *<p/>      *<b>Note:</b> Not all registry implementations support this feature,      * such as the {@link org.apache.camel.impl.JndiRegistry}.      *      * @param type  the type of the registered services      * @return the types found, with their ids as the key. Returns an empty Map if none found.      */
DECL|method|findByTypeWithName (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|findByTypeWithName
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Finds services in the registry by their type.      *<p/>      *<b>Note:</b> Not all registry implementations support this feature,      * such as the {@link org.apache.camel.impl.JndiRegistry}.      *      * @param type  the type of the registered services      * @return the types found. Returns an empty Set if none found.      */
DECL|method|findByType (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|findByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Looks up a service in the registry based purely on name,      * returning the service or<tt>null</tt> if it could not be found.      *      * @param name the name of the service      * @return the service from the registry or<tt>null</tt> if it could not be found      * @deprecated use {@link #lookupByName(String)}      */
annotation|@
name|Deprecated
DECL|method|lookup (String name)
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Looks up a service in the registry, returning the service or<tt>null</tt> if it could not be found.      *      * @param name the name of the service      * @param type the type of the required service      * @return the service from the registry or<tt>null</tt> if it could not be found      * @deprecated use {@link #lookupByNameAndType(String, Class)}      */
annotation|@
name|Deprecated
DECL|method|lookup (String name, Class<T> type)
argument_list|<
name|T
argument_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Looks up services in the registry by their type.      *<p/>      *<b>Note:</b> Not all registry implementations support this feature,      * such as the {@link org.apache.camel.impl.JndiRegistry}.      *      * @param type  the type of the registered services      * @return the types found, with their id as the key. Returns an empty Map if none found.      * @deprecated use {@link #findByTypeWithName(Class)}      */
annotation|@
name|Deprecated
DECL|method|lookupByType (Class<T> type)
argument_list|<
name|T
argument_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

