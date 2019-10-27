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
name|io
operator|.
name|InputStream
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
name|camel
operator|.
name|StaticService
import|;
end_import

begin_comment
comment|/**  * A resolver that can find resources based on package scanning.  *  * OSGi is not supported as this is intended for standalone Camel runtimes such  * as Camel Main, or Camel Quarkus.  *  * @see PackageScanClassResolver  */
end_comment

begin_interface
DECL|interface|PackageScanResourceResolver
specifier|public
interface|interface
name|PackageScanResourceResolver
extends|extends
name|StaticService
block|{
comment|/**      * Gets the ClassLoader instances that should be used when scanning for resources.      *<p/>      * This implementation will return a new unmodifiable set containing the classloaders.      * Use the {@link #addClassLoader(ClassLoader)} method if you want to add new classloaders      * to the class loaders list.      *      * @return the class loaders to use      */
DECL|method|getClassLoaders ()
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|getClassLoaders
parameter_list|()
function_decl|;
comment|/**      * Adds the class loader to the existing loaders      *      * @param classLoader the loader to add      */
DECL|method|addClassLoader (ClassLoader classLoader)
name|void
name|addClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
function_decl|;
comment|/**      * To specify a set of accepted schemas to use for loading resources as URL connections      * (besides http and https schemas)      */
DECL|method|setAcceptableSchemes (String schemes)
name|void
name|setAcceptableSchemes
parameter_list|(
name|String
name|schemes
parameter_list|)
function_decl|;
comment|/**      * Finds the resources from the given location.      *      * @param locations  the location (support ANT style patterns, eg routes/camel-*.xml)      * @return the found resources, or an empty set if no resources found      * @throws Exception can be thrown during scanning for resources.      */
DECL|method|findResources (String locations)
name|Set
argument_list|<
name|InputStream
argument_list|>
name|findResources
parameter_list|(
name|String
name|locations
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

