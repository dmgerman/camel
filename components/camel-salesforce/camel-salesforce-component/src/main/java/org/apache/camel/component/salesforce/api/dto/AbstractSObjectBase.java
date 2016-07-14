begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
DECL|class|AbstractSObjectBase
specifier|public
class|class
name|AbstractSObjectBase
extends|extends
name|AbstractDTOBase
block|{
comment|// WARNING: these fields have case sensitive names,
comment|// the field name MUST match the field name used by Salesforce
comment|// DO NOT change these field names to camel case!!!
DECL|field|attributes
specifier|private
name|Attributes
name|attributes
decl_stmt|;
DECL|field|Id
specifier|private
name|String
name|Id
decl_stmt|;
DECL|field|OwnerId
specifier|private
name|String
name|OwnerId
decl_stmt|;
DECL|field|IsDeleted
specifier|private
name|Boolean
name|IsDeleted
decl_stmt|;
DECL|field|Name
specifier|private
name|String
name|Name
decl_stmt|;
DECL|field|CreatedDate
specifier|private
name|ZonedDateTime
name|CreatedDate
decl_stmt|;
DECL|field|CreatedById
specifier|private
name|String
name|CreatedById
decl_stmt|;
DECL|field|LastModifiedDate
specifier|private
name|ZonedDateTime
name|LastModifiedDate
decl_stmt|;
DECL|field|LastModifiedById
specifier|private
name|String
name|LastModifiedById
decl_stmt|;
DECL|field|SystemModstamp
specifier|private
name|ZonedDateTime
name|SystemModstamp
decl_stmt|;
DECL|field|LastActivityDate
specifier|private
name|ZonedDateTime
name|LastActivityDate
decl_stmt|;
DECL|field|LastViewedDate
specifier|private
name|ZonedDateTime
name|LastViewedDate
decl_stmt|;
DECL|field|LastReferencedDate
specifier|private
name|ZonedDateTime
name|LastReferencedDate
decl_stmt|;
comment|/**      * Utility method to clear all system {@link AbstractSObjectBase} fields.      *<p>Useful when reusing a DTO for a new record, or for update/upsert.</p>      *<p>This method does not clear {@code Name} to allow updating it, so it must be explicitly set to {@code null} if needed.</p>      */
DECL|method|clearBaseFields ()
specifier|public
specifier|final
name|void
name|clearBaseFields
parameter_list|()
block|{
name|attributes
operator|=
literal|null
expr_stmt|;
name|Id
operator|=
literal|null
expr_stmt|;
name|OwnerId
operator|=
literal|null
expr_stmt|;
name|IsDeleted
operator|=
literal|null
expr_stmt|;
name|CreatedDate
operator|=
literal|null
expr_stmt|;
name|CreatedById
operator|=
literal|null
expr_stmt|;
name|LastModifiedDate
operator|=
literal|null
expr_stmt|;
name|LastModifiedById
operator|=
literal|null
expr_stmt|;
name|SystemModstamp
operator|=
literal|null
expr_stmt|;
name|LastActivityDate
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|getAttributes ()
specifier|public
name|Attributes
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
DECL|method|setAttributes (Attributes attributes)
specifier|public
name|void
name|setAttributes
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Id"
argument_list|)
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|Id
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Id"
argument_list|)
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|Id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"OwnerId"
argument_list|)
DECL|method|getOwnerId ()
specifier|public
name|String
name|getOwnerId
parameter_list|()
block|{
return|return
name|OwnerId
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"OwnerId"
argument_list|)
DECL|method|setOwnerId (String ownerId)
specifier|public
name|void
name|setOwnerId
parameter_list|(
name|String
name|ownerId
parameter_list|)
block|{
name|this
operator|.
name|OwnerId
operator|=
name|ownerId
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"IsDeleted"
argument_list|)
DECL|method|isIsDeleted ()
specifier|public
name|Boolean
name|isIsDeleted
parameter_list|()
block|{
return|return
name|IsDeleted
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"IsDeleted"
argument_list|)
DECL|method|setIsDeleted (Boolean isDeleted)
specifier|public
name|void
name|setIsDeleted
parameter_list|(
name|Boolean
name|isDeleted
parameter_list|)
block|{
name|this
operator|.
name|IsDeleted
operator|=
name|isDeleted
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Name"
argument_list|)
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|Name
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Name"
argument_list|)
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|Name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"CreatedDate"
argument_list|)
DECL|method|getCreatedDate ()
specifier|public
name|ZonedDateTime
name|getCreatedDate
parameter_list|()
block|{
return|return
name|CreatedDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"CreatedDate"
argument_list|)
DECL|method|setCreatedDate (ZonedDateTime createdDate)
specifier|public
name|void
name|setCreatedDate
parameter_list|(
name|ZonedDateTime
name|createdDate
parameter_list|)
block|{
name|this
operator|.
name|CreatedDate
operator|=
name|createdDate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"CreatedById"
argument_list|)
DECL|method|getCreatedById ()
specifier|public
name|String
name|getCreatedById
parameter_list|()
block|{
return|return
name|CreatedById
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"CreatedById"
argument_list|)
DECL|method|setCreatedById (String createdById)
specifier|public
name|void
name|setCreatedById
parameter_list|(
name|String
name|createdById
parameter_list|)
block|{
name|this
operator|.
name|CreatedById
operator|=
name|createdById
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastModifiedDate"
argument_list|)
DECL|method|getLastModifiedDate ()
specifier|public
name|ZonedDateTime
name|getLastModifiedDate
parameter_list|()
block|{
return|return
name|LastModifiedDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastModifiedDate"
argument_list|)
DECL|method|setLastModifiedDate (ZonedDateTime lastModifiedDate)
specifier|public
name|void
name|setLastModifiedDate
parameter_list|(
name|ZonedDateTime
name|lastModifiedDate
parameter_list|)
block|{
name|this
operator|.
name|LastModifiedDate
operator|=
name|lastModifiedDate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastModifiedById"
argument_list|)
DECL|method|getLastModifiedById ()
specifier|public
name|String
name|getLastModifiedById
parameter_list|()
block|{
return|return
name|LastModifiedById
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastModifiedById"
argument_list|)
DECL|method|setLastModifiedById (String lastModifiedById)
specifier|public
name|void
name|setLastModifiedById
parameter_list|(
name|String
name|lastModifiedById
parameter_list|)
block|{
name|this
operator|.
name|LastModifiedById
operator|=
name|lastModifiedById
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"SystemModstamp"
argument_list|)
DECL|method|getSystemModstamp ()
specifier|public
name|ZonedDateTime
name|getSystemModstamp
parameter_list|()
block|{
return|return
name|SystemModstamp
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"SystemModstamp"
argument_list|)
DECL|method|setSystemModstamp (ZonedDateTime systemModstamp)
specifier|public
name|void
name|setSystemModstamp
parameter_list|(
name|ZonedDateTime
name|systemModstamp
parameter_list|)
block|{
name|this
operator|.
name|SystemModstamp
operator|=
name|systemModstamp
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastActivityDate"
argument_list|)
DECL|method|getLastActivityDate ()
specifier|public
name|ZonedDateTime
name|getLastActivityDate
parameter_list|()
block|{
return|return
name|LastActivityDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastActivityDate"
argument_list|)
DECL|method|setLastActivityDate (ZonedDateTime lastActivityDate)
specifier|public
name|void
name|setLastActivityDate
parameter_list|(
name|ZonedDateTime
name|lastActivityDate
parameter_list|)
block|{
name|this
operator|.
name|LastActivityDate
operator|=
name|lastActivityDate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastViewedDate"
argument_list|)
DECL|method|getLastViewedDate ()
specifier|public
name|ZonedDateTime
name|getLastViewedDate
parameter_list|()
block|{
return|return
name|LastViewedDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastViewedDate"
argument_list|)
DECL|method|setLastViewedDate (ZonedDateTime lastViewedDate)
specifier|public
name|void
name|setLastViewedDate
parameter_list|(
name|ZonedDateTime
name|lastViewedDate
parameter_list|)
block|{
name|LastViewedDate
operator|=
name|lastViewedDate
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastReferencedDate"
argument_list|)
DECL|method|getLastReferencedDate ()
specifier|public
name|ZonedDateTime
name|getLastReferencedDate
parameter_list|()
block|{
return|return
name|LastReferencedDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"LastReferencedDate"
argument_list|)
DECL|method|setLastReferencedDate (ZonedDateTime lastReferencedDate)
specifier|public
name|void
name|setLastReferencedDate
parameter_list|(
name|ZonedDateTime
name|lastReferencedDate
parameter_list|)
block|{
name|LastReferencedDate
operator|=
name|lastReferencedDate
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

