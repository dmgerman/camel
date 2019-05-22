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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
comment|/**  * A resolver that can find resources based on package scanning.  */
end_comment

begin_interface
DECL|interface|PackageScanClassResolver
specifier|public
interface|interface
name|PackageScanClassResolver
extends|extends
name|StaticService
block|{
comment|/**      * Gets the ClassLoader instances that should be used when scanning for classes.      *<p/>      * This implementation will return a new unmodifiable set containing the classloaders.      * Use the {@link #addClassLoader(ClassLoader)} method if you want to add new classloaders      * to the class loaders list.      *      * @return the class loaders to use      */
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
comment|/**      * Attempts to discover classes that are annotated with to the annotation.      *      * @param annotation   the annotation that should be present on matching classes      * @param packageNames one or more package names to scan (including subpackages) for classes      * @return the classes found, returns an empty set if none found      */
DECL|method|findAnnotated (Class<? extends Annotation> annotation, String... packageNames)
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findAnnotated
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
function_decl|;
comment|/**      * Attempts to discover classes that are annotated with to the annotation.      *      * @param annotations   the annotations that should be present (any of them) on matching classes      * @param packageNames one or more package names to scan (including subpackages) for classes      * @return the classes found, returns an empty set if none found      */
DECL|method|findAnnotated (Set<Class<? extends Annotation>> annotations, String... packageNames)
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findAnnotated
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
function_decl|;
comment|/**      * Attempts to discover classes that are assignable to the type provided. In      * the case that an interface is provided this method will collect      * implementations. In the case of a non-interface class, subclasses will be      * collected.      *      * @param parent       the class of interface to find subclasses or implementations of      * @param packageNames one or more package names to scan (including subpackages) for classes      * @return the classes found, returns an empty set if none found      */
DECL|method|findImplementations (Class<?> parent, String... packageNames)
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findImplementations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parent
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
function_decl|;
comment|/**      * Attempts to discover classes filter by the provided filter      *      * @param filter  filter to filter desired classes.      * @param packageNames one or more package names to scan (including subpackages) for classes      * @return the classes found, returns an empty set if none found      */
DECL|method|findByFilter (PackageScanFilter filter, String... packageNames)
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findByFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
function_decl|;
comment|/**      * Add a filter that will be applied to all scan operations      *       * @param filter filter to filter desired classes in all scan operations      */
DECL|method|addFilter (PackageScanFilter filter)
name|void
name|addFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
function_decl|;
comment|/**      * Removes the filter      *      * @param filter filter to filter desired classes in all scan operations      */
DECL|method|removeFilter (PackageScanFilter filter)
name|void
name|removeFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

