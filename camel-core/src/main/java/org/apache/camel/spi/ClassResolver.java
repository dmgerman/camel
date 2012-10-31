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
name|io
operator|.
name|InputStream
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
name|Enumeration
import|;
end_import

begin_comment
comment|/**  * A class resolver for loading classes in a loosly coupled manner to cater for different platforms such  * as standalone, web container, j2ee container and OSGi platforms.  */
end_comment

begin_interface
DECL|interface|ClassResolver
specifier|public
interface|interface
name|ClassResolver
block|{
comment|/**      * Resolves the given class by its name      *      * @param name full qualified name of class      * @return the class if resolved,<tt>null</tt> if not found.      */
DECL|method|resolveClass (String name)
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name full qualified name of class      * @param type the expected type of the class      * @return the class if resolved,<tt>null</tt> if not found.      */
DECL|method|resolveClass (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveClass
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
comment|/**      * Resolves the given class by its name      *      * @param name   full qualified name of class      * @param loader use the provided class loader      * @return the class if resolved,<tt>null</tt> if not found.      */
DECL|method|resolveClass (String name, ClassLoader loader)
name|Class
argument_list|<
name|?
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name   full qualified name of class      * @param type   the expected type of the class      * @param loader use the provided class loader      * @return the class if resolved,<tt>null</tt> if not found.      */
DECL|method|resolveClass (String name, Class<T> type, ClassLoader loader)
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name full qualified name of class      * @return the class if resolved,<tt>null</tt> if not found.      * @throws ClassNotFoundException is thrown if class not found      */
DECL|method|resolveMandatoryClass (String name)
name|Class
argument_list|<
name|?
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name full qualified name of class      * @param type the expected type of the class      * @return the class if resolved,<tt>null</tt> if not found.      * @throws ClassNotFoundException is thrown if class not found      */
DECL|method|resolveMandatoryClass (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveMandatoryClass
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
throws|throws
name|ClassNotFoundException
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name   full qualified name of class      * @param loader use the provided class loader      * @return the class if resolved,<tt>null</tt> if not found.      * @throws ClassNotFoundException is thrown if class not found      */
DECL|method|resolveMandatoryClass (String name, ClassLoader loader)
name|Class
argument_list|<
name|?
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
function_decl|;
comment|/**      * Resolves the given class by its name      *      * @param name   full qualified name of class      * @param type   the expected type of the class      * @param loader use the provided class loader      * @return the class if resolved,<tt>null</tt> if not found.      * @throws ClassNotFoundException is thrown if class not found      */
DECL|method|resolveMandatoryClass (String name, Class<T> type, ClassLoader loader)
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|resolveMandatoryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
throws|throws
name|ClassNotFoundException
function_decl|;
comment|/**      * Loads the given resource as a stream      *      * @param uri the uri of the resource      * @return as a stream      */
DECL|method|loadResourceAsStream (String uri)
name|InputStream
name|loadResourceAsStream
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Loads the given resource as a URL      *      * @param uri the uri of the resource      * @return as a URL      */
DECL|method|loadResourceAsURL (String uri)
name|URL
name|loadResourceAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Loads the given resources as a URL      *      * @param uri the uri of the resource      * @return the URLs found on the classpath      */
DECL|method|loadResourcesAsURL (String uri)
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|loadResourcesAsURL
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

