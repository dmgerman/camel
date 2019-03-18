begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.model
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
operator|.
name|model
package|;
end_package

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
name|JsonIgnoreProperties
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
name|JsonInclude
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

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
annotation|@
name|JsonInclude
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_NULL
argument_list|)
DECL|class|AttachmentMeta
specifier|public
class|class
name|AttachmentMeta
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"sys_id"
argument_list|)
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"download_link"
argument_list|)
DECL|field|downloadLink
specifier|private
name|String
name|downloadLink
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"table_name"
argument_list|)
DECL|field|tableName
specifier|private
name|String
name|tableName
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"table_sys_id"
argument_list|)
DECL|field|tableSysId
specifier|private
name|String
name|tableSysId
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"compressed"
argument_list|)
DECL|field|compressed
specifier|private
name|Boolean
name|compressed
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"file_name"
argument_list|)
DECL|field|fileName
specifier|private
name|String
name|fileName
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
DECL|method|getDownloadLink ()
specifier|public
name|String
name|getDownloadLink
parameter_list|()
block|{
return|return
name|downloadLink
return|;
block|}
DECL|method|setDownloadLink (String downloadLink)
specifier|public
name|void
name|setDownloadLink
parameter_list|(
name|String
name|downloadLink
parameter_list|)
block|{
name|this
operator|.
name|downloadLink
operator|=
name|downloadLink
expr_stmt|;
block|}
DECL|method|getTableName ()
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
DECL|method|setTableName (String tableName)
specifier|public
name|void
name|setTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
DECL|method|getTableSysId ()
specifier|public
name|String
name|getTableSysId
parameter_list|()
block|{
return|return
name|tableSysId
return|;
block|}
DECL|method|setTableSysId (String tableSysId)
specifier|public
name|void
name|setTableSysId
parameter_list|(
name|String
name|tableSysId
parameter_list|)
block|{
name|this
operator|.
name|tableSysId
operator|=
name|tableSysId
expr_stmt|;
block|}
DECL|method|getCompressed ()
specifier|public
name|Boolean
name|getCompressed
parameter_list|()
block|{
return|return
name|compressed
return|;
block|}
DECL|method|setCompressed (Boolean compressed)
specifier|public
name|void
name|setCompressed
parameter_list|(
name|Boolean
name|compressed
parameter_list|)
block|{
name|this
operator|.
name|compressed
operator|=
name|compressed
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
block|}
end_class

end_unit

