begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|maven
package|;
end_package

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
name|catalog
operator|.
name|CamelCatalog
import|;
end_import

begin_comment
comment|/**  * Provider which allows downloading artifact using Maven and add content to the {@link CamelCatalog}.  */
end_comment

begin_interface
DECL|interface|MavenArtifactProvider
specifier|public
interface|interface
name|MavenArtifactProvider
block|{
comment|/**      * Configures the directory for the download cache.      *<p/>      * The default folder is<tt>USER_HOME/.groovy/grape</tt>      *      * @param directory the directory.      */
DECL|method|setCacheDirectory (String directory)
name|void
name|setCacheDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
function_decl|;
comment|/**      * To add a 3rd party Maven repository.      *      * @param name the repository name      * @param url  the repository url      */
DECL|method|addMavenRepository (String name, String url)
name|void
name|addMavenRepository
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
function_decl|;
comment|/**      * Downloads the artifact using the Maven coordinates and scans the JAR for Camel components      * which will be added to the CamelCatalog.      *      * @param camelCatalog          The Camel Catalog      * @param groupId               Maven group id      * @param artifactId            Maven artifact id      * @param version               Maven version      * @return the names of the components that was added, or an empty set if none found or they already exists in the catalog      */
DECL|method|addArtifactToCatalog (CamelCatalog camelCatalog, String groupId, String artifactId, String version)
name|Set
argument_list|<
name|String
argument_list|>
name|addArtifactToCatalog
parameter_list|(
name|CamelCatalog
name|camelCatalog
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

