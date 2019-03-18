begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|StorageCredentials
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_class
DECL|class|AbstractConfiguration
specifier|public
specifier|abstract
class|class
name|AbstractConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
DECL|field|credentials
specifier|private
name|StorageCredentials
name|credentials
decl_stmt|;
DECL|field|accountName
specifier|private
name|String
name|accountName
decl_stmt|;
DECL|method|getAccountName ()
specifier|public
name|String
name|getAccountName
parameter_list|()
block|{
return|return
name|accountName
return|;
block|}
comment|/**      * Set the Azure account name      */
DECL|method|setAccountName (String accountName)
specifier|public
name|void
name|setAccountName
parameter_list|(
name|String
name|accountName
parameter_list|)
block|{
name|this
operator|.
name|accountName
operator|=
name|accountName
expr_stmt|;
block|}
DECL|method|getCredentials ()
specifier|public
name|StorageCredentials
name|getCredentials
parameter_list|()
block|{
return|return
name|credentials
return|;
block|}
comment|/**      * Set the storage credentials, required in most cases      */
DECL|method|setCredentials (StorageCredentials credentials)
specifier|public
name|void
name|setCredentials
parameter_list|(
name|StorageCredentials
name|credentials
parameter_list|)
block|{
name|this
operator|.
name|credentials
operator|=
name|credentials
expr_stmt|;
block|}
block|}
end_class

end_unit

