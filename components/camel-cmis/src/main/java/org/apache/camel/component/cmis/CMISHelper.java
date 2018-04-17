begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|CmisObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|Property
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|PropertyIds
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|data
operator|.
name|PropertyData
import|;
end_import

begin_class
DECL|class|CMISHelper
specifier|public
specifier|final
class|class
name|CMISHelper
block|{
DECL|method|CMISHelper ()
specifier|private
name|CMISHelper
parameter_list|()
block|{     }
DECL|method|filterCMISProperties (Map<String, Object> properties)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filterCMISProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|properties
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"cmis:"
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|objectProperties (CmisObject cmisObject)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objectProperties
parameter_list|(
name|CmisObject
name|cmisObject
parameter_list|)
block|{
name|List
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|propertyList
init|=
name|cmisObject
operator|.
name|getProperties
argument_list|()
decl_stmt|;
return|return
name|propertyDataToMap
argument_list|(
name|propertyList
argument_list|)
return|;
block|}
DECL|method|propertyDataToMap (List<? extends PropertyData<?>> properties)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propertyDataToMap
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|PropertyData
argument_list|<
name|?
argument_list|>
argument_list|>
name|properties
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PropertyData
argument_list|<
name|?
argument_list|>
name|propertyData
range|:
name|properties
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|propertyData
operator|.
name|getId
argument_list|()
argument_list|,
name|propertyData
operator|.
name|getFirstValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|isFolder (CmisObject cmisObject)
specifier|public
specifier|static
name|boolean
name|isFolder
parameter_list|(
name|CmisObject
name|cmisObject
parameter_list|)
block|{
return|return
name|CamelCMISConstants
operator|.
name|CMIS_FOLDER
operator|.
name|equals
argument_list|(
name|getObjectTypeId
argument_list|(
name|cmisObject
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isDocument (CmisObject cmisObject)
specifier|public
specifier|static
name|boolean
name|isDocument
parameter_list|(
name|CmisObject
name|cmisObject
parameter_list|)
block|{
return|return
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
operator|.
name|equals
argument_list|(
name|getObjectTypeId
argument_list|(
name|cmisObject
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getObjectTypeId (CmisObject child)
specifier|public
specifier|static
name|Object
name|getObjectTypeId
parameter_list|(
name|CmisObject
name|child
parameter_list|)
block|{
return|return
name|child
operator|.
name|getPropertyValue
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
return|;
comment|//BASE_TYPE_ID?
block|}
block|}
end_class

end_unit

