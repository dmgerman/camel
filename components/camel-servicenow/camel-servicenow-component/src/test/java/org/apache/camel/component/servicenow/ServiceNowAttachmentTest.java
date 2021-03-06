begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Exchange
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
name|Produce
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|servicenow
operator|.
name|model
operator|.
name|AttachmentMeta
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ResourceHelper
operator|.
name|resolveResourceAsInputStream
import|;
end_import

begin_class
DECL|class|ServiceNowAttachmentTest
specifier|public
class|class
name|ServiceNowAttachmentTest
extends|extends
name|ServiceNowTestSupport
block|{
annotation|@
name|Produce
argument_list|(
literal|"direct:servicenow"
argument_list|)
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testAttachment ()
specifier|public
name|void
name|testAttachment
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|AttachmentMeta
argument_list|>
name|attachmentMetaList
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
literal|null
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_ATTACHMENT
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|AttachmentMeta
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_QUERY
argument_list|,
literal|"content_type=application/octet-stream"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_LIMIT
argument_list|,
literal|1
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|attachmentMetaList
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|getExistingResult
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|e
lambda|->
block|{
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_ATTACHMENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_CONTENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
operator|.
name|getHeader
argument_list|()
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|getExistingResult
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|CONTENT_META
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getExistingResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getExistingResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|contentMeta
init|=
name|getExistingResult
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|CONTENT_META
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|contentMeta
operator|.
name|get
argument_list|(
literal|"file_name"
argument_list|)
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|contentMeta
operator|.
name|get
argument_list|(
literal|"table_name"
argument_list|)
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTableName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|contentMeta
operator|.
name|get
argument_list|(
literal|"sys_id"
argument_list|)
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|putResult
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|e
lambda|->
block|{
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_ATTACHMENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_UPLOAD
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|AttachmentMeta
operator|.
name|class
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_FILE_NAME
operator|.
name|getHeader
argument_list|()
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
operator|.
name|getHeader
argument_list|()
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTableName
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_SYS_ID
operator|.
name|getHeader
argument_list|()
argument_list|,
name|attachmentMetaList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTableSysId
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|resolveResourceAsInputStream
argument_list|(
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
literal|"classpath:my-content.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|Exchange
name|getCreatedResult
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|e
lambda|->
block|{
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_ATTACHMENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_CONTENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
operator|.
name|getHeader
argument_list|()
argument_list|,
name|putResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|AttachmentMeta
operator|.
name|class
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|getCreatedResult
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|CONTENT_META
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|getCreatedResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getCreatedResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
name|Exchange
name|deleteResult
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|e
lambda|->
block|{
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_ATTACHMENT
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_DELETE
argument_list|)
expr_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
operator|.
name|getHeader
argument_list|()
argument_list|,
name|putResult
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|AttachmentMeta
operator|.
name|class
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|deleteResult
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|deleteResult
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:servicenow"
argument_list|)
operator|.
name|to
argument_list|(
literal|"servicenow:{{env:SERVICENOW_INSTANCE}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.servicenow?level=INFO&showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:servicenow"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

