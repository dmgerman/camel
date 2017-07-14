begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.ha.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|ha
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|ConfigMap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|ConfigMapBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Central lock for testing leader election.  */
end_comment

begin_class
DECL|class|ConfigMapLockSimulator
specifier|public
class|class
name|ConfigMapLockSimulator
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConfigMapLockSimulator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configMapName
specifier|private
name|String
name|configMapName
decl_stmt|;
DECL|field|currentMap
specifier|private
name|ConfigMap
name|currentMap
decl_stmt|;
DECL|field|versionCounter
specifier|private
name|long
name|versionCounter
init|=
literal|1000000
decl_stmt|;
DECL|method|ConfigMapLockSimulator (String configMapName)
specifier|public
name|ConfigMapLockSimulator
parameter_list|(
name|String
name|configMapName
parameter_list|)
block|{
name|this
operator|.
name|configMapName
operator|=
name|configMapName
expr_stmt|;
block|}
DECL|method|getConfigMapName ()
specifier|public
name|String
name|getConfigMapName
parameter_list|()
block|{
return|return
name|configMapName
return|;
block|}
DECL|method|setConfigMap (ConfigMap map, boolean insert)
specifier|public
specifier|synchronized
name|boolean
name|setConfigMap
parameter_list|(
name|ConfigMap
name|map
parameter_list|,
name|boolean
name|insert
parameter_list|)
block|{
comment|// Insert
if|if
condition|(
name|insert
operator|&&
name|currentMap
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Current map should have been null"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// Update
if|if
condition|(
operator|!
name|insert
operator|&&
name|currentMap
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Current map should not have been null"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|version
init|=
name|map
operator|.
name|getMetadata
argument_list|()
operator|!=
literal|null
condition|?
name|map
operator|.
name|getMetadata
argument_list|()
operator|.
name|getResourceVersion
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|long
name|versionLong
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionLong
operator|!=
name|versionCounter
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Current resource version is {} while the update is related to version {}"
argument_list|,
name|versionCounter
argument_list|,
name|versionLong
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|this
operator|.
name|currentMap
operator|=
operator|new
name|ConfigMapBuilder
argument_list|(
name|map
argument_list|)
operator|.
name|editOrNewMetadata
argument_list|()
operator|.
name|withResourceVersion
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
operator|++
name|versionCounter
argument_list|)
argument_list|)
operator|.
name|endMetadata
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getConfigMap ()
specifier|public
specifier|synchronized
name|ConfigMap
name|getConfigMap
parameter_list|()
block|{
if|if
condition|(
name|currentMap
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ConfigMapBuilder
argument_list|(
name|currentMap
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

