begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.analytics.reports
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
operator|.
name|analytics
operator|.
name|reports
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
operator|.
name|AbstractDTOBase
import|;
end_import

begin_comment
comment|/**  * Report instance DTO.  */
end_comment

begin_class
DECL|class|ReportInstance
specifier|public
class|class
name|ReportInstance
extends|extends
name|AbstractDTOBase
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|status
specifier|private
name|ReportStatusEnum
name|status
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|ownerId
specifier|private
name|String
name|ownerId
decl_stmt|;
DECL|field|hasDetailRows
specifier|private
name|Boolean
name|hasDetailRows
decl_stmt|;
DECL|field|completionDate
specifier|private
name|ZonedDateTime
name|completionDate
decl_stmt|;
DECL|field|requestDate
specifier|private
name|ZonedDateTime
name|requestDate
decl_stmt|;
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
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
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getStatus ()
specifier|public
name|ReportStatusEnum
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|setStatus (ReportStatusEnum status)
specifier|public
name|void
name|setStatus
parameter_list|(
name|ReportStatusEnum
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getOwnerId ()
specifier|public
name|String
name|getOwnerId
parameter_list|()
block|{
return|return
name|ownerId
return|;
block|}
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
name|ownerId
operator|=
name|ownerId
expr_stmt|;
block|}
DECL|method|getHasDetailRows ()
specifier|public
name|Boolean
name|getHasDetailRows
parameter_list|()
block|{
return|return
name|hasDetailRows
return|;
block|}
DECL|method|setHasDetailRows (Boolean hasDetailRows)
specifier|public
name|void
name|setHasDetailRows
parameter_list|(
name|Boolean
name|hasDetailRows
parameter_list|)
block|{
name|this
operator|.
name|hasDetailRows
operator|=
name|hasDetailRows
expr_stmt|;
block|}
DECL|method|getCompletionDate ()
specifier|public
name|ZonedDateTime
name|getCompletionDate
parameter_list|()
block|{
return|return
name|completionDate
return|;
block|}
DECL|method|setCompletionDate (ZonedDateTime completionDate)
specifier|public
name|void
name|setCompletionDate
parameter_list|(
name|ZonedDateTime
name|completionDate
parameter_list|)
block|{
name|this
operator|.
name|completionDate
operator|=
name|completionDate
expr_stmt|;
block|}
DECL|method|getRequestDate ()
specifier|public
name|ZonedDateTime
name|getRequestDate
parameter_list|()
block|{
return|return
name|requestDate
return|;
block|}
DECL|method|setRequestDate (ZonedDateTime requestDate)
specifier|public
name|void
name|setRequestDate
parameter_list|(
name|ZonedDateTime
name|requestDate
parameter_list|)
block|{
name|this
operator|.
name|requestDate
operator|=
name|requestDate
expr_stmt|;
block|}
block|}
end_class

end_unit

