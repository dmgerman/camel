begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|LinkedHashSet
import|;
end_import

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
name|Set
import|;
end_import

begin_comment
comment|/**  * A in-memory based {@link ConnectorDataStore}.  */
end_comment

begin_class
DECL|class|MemoryConnectorDataStore
specifier|public
class|class
name|MemoryConnectorDataStore
implements|implements
name|ConnectorDataStore
block|{
DECL|field|connectors
specifier|private
specifier|final
name|Set
argument_list|<
name|ConnectorDto
argument_list|>
name|connectors
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|addConnector (ConnectorDto connector)
specifier|public
name|void
name|addConnector
parameter_list|(
name|ConnectorDto
name|connector
parameter_list|)
block|{
name|connectors
operator|.
name|add
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|searchArtifacts (String filter, boolean latestVersionOnly)
specifier|public
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|searchArtifacts
parameter_list|(
name|String
name|filter
parameter_list|,
name|boolean
name|latestVersionOnly
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

