begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
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

begin_comment
comment|/**  * Nexus repository  */
end_comment

begin_interface
DECL|interface|NexusRepository
specifier|public
interface|interface
name|NexusRepository
block|{
comment|/**      * Search for new artifacts in the Nexus repository.      *      * @param filter the filter artifacts      * @param latestVersionOnly to only include the latest version of a given Maven<tt>groupId:artifactId</tt>      * @return the found artifacts      */
DECL|method|searchArtifacts (String filter, boolean latestVersionOnly)
name|List
argument_list|<
name|NexusArtifactDto
argument_list|>
name|searchArtifacts
parameter_list|(
name|String
name|filter
parameter_list|,
name|boolean
name|latestVersionOnly
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

