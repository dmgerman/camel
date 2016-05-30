begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Consul remote service call configuration  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,remote"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"consulConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ConsulConfigurationDefinition
specifier|public
class|class
name|ConsulConfigurationDefinition
extends|extends
name|ServiceCallConfigurationDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|dc
specifier|private
name|String
name|dc
decl_stmt|;
comment|//@XmlAttribute
comment|//private Set<String> tags;
comment|//@XmlAttribute @Metadata(label = "security")
comment|//private SSLContextParameters sslContextParameters;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|aclToken
specifier|private
name|String
name|aclToken
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|connectTimeoutMillis
specifier|private
name|Long
name|connectTimeoutMillis
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|readTimeoutMillis
specifier|private
name|Long
name|readTimeoutMillis
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|writeTimeoutMillis
specifier|private
name|Long
name|writeTimeoutMillis
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|blockSeconds
specifier|private
name|Integer
name|blockSeconds
init|=
literal|10
decl_stmt|;
DECL|method|ConsulConfigurationDefinition ()
specifier|public
name|ConsulConfigurationDefinition
parameter_list|()
block|{     }
DECL|method|ConsulConfigurationDefinition (ServiceCallDefinition parent)
specifier|public
name|ConsulConfigurationDefinition
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Getter/Setter
comment|// -------------------------------------------------------------------------
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
DECL|method|getDc ()
specifier|public
name|String
name|getDc
parameter_list|()
block|{
return|return
name|dc
return|;
block|}
DECL|method|setDc (String dc)
specifier|public
name|void
name|setDc
parameter_list|(
name|String
name|dc
parameter_list|)
block|{
name|this
operator|.
name|dc
operator|=
name|dc
expr_stmt|;
block|}
comment|/*     public Set<String> getTags() {         return tags;     }      public void setTags(Set<String> tags) {         this.tags = tags;     }      public SSLContextParameters getSslContextParameters() {         return sslContextParameters;     }      public void setSslContextParameters(SSLContextParameters sslContextParameters) {         this.sslContextParameters = sslContextParameters;     }     */
DECL|method|getAclToken ()
specifier|public
name|String
name|getAclToken
parameter_list|()
block|{
return|return
name|aclToken
return|;
block|}
DECL|method|setAclToken (String aclToken)
specifier|public
name|void
name|setAclToken
parameter_list|(
name|String
name|aclToken
parameter_list|)
block|{
name|this
operator|.
name|aclToken
operator|=
name|aclToken
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getConnectTimeoutMillis ()
specifier|public
name|Long
name|getConnectTimeoutMillis
parameter_list|()
block|{
return|return
name|connectTimeoutMillis
return|;
block|}
DECL|method|setConnectTimeoutMillis (Long connectTimeoutMillis)
specifier|public
name|void
name|setConnectTimeoutMillis
parameter_list|(
name|Long
name|connectTimeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|connectTimeoutMillis
operator|=
name|connectTimeoutMillis
expr_stmt|;
block|}
DECL|method|getReadTimeoutMillis ()
specifier|public
name|Long
name|getReadTimeoutMillis
parameter_list|()
block|{
return|return
name|readTimeoutMillis
return|;
block|}
DECL|method|setReadTimeoutMillis (Long readTimeoutMillis)
specifier|public
name|void
name|setReadTimeoutMillis
parameter_list|(
name|Long
name|readTimeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|readTimeoutMillis
operator|=
name|readTimeoutMillis
expr_stmt|;
block|}
DECL|method|getWriteTimeoutMillis ()
specifier|public
name|Long
name|getWriteTimeoutMillis
parameter_list|()
block|{
return|return
name|writeTimeoutMillis
return|;
block|}
DECL|method|setWriteTimeoutMillis (Long writeTimeoutMillis)
specifier|public
name|void
name|setWriteTimeoutMillis
parameter_list|(
name|Long
name|writeTimeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|writeTimeoutMillis
operator|=
name|writeTimeoutMillis
expr_stmt|;
block|}
DECL|method|getBlockSeconds ()
specifier|public
name|Integer
name|getBlockSeconds
parameter_list|()
block|{
return|return
name|blockSeconds
return|;
block|}
DECL|method|setBlockSeconds (Integer blockSeconds)
specifier|public
name|void
name|setBlockSeconds
parameter_list|(
name|Integer
name|blockSeconds
parameter_list|)
block|{
name|this
operator|.
name|blockSeconds
operator|=
name|blockSeconds
expr_stmt|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|url (String url)
specifier|public
name|ConsulConfigurationDefinition
name|url
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|dc (String dc)
specifier|public
name|ConsulConfigurationDefinition
name|dc
parameter_list|(
name|String
name|dc
parameter_list|)
block|{
name|setDc
argument_list|(
name|dc
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/*     public ConsulConfigurationDefinition tags(Set<String> tags) {         setTags(tags);         return this;     }      public ConsulConfigurationDefinition sslContextParameters(SSLContextParameters sslContextParameters) {         setSslContextParameters(sslContextParameters);         return this;     }     */
DECL|method|aclToken (String aclToken)
specifier|public
name|ConsulConfigurationDefinition
name|aclToken
parameter_list|(
name|String
name|aclToken
parameter_list|)
block|{
name|setAclToken
argument_list|(
name|aclToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|userName (String userName)
specifier|public
name|ConsulConfigurationDefinition
name|userName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|password (String password)
specifier|public
name|ConsulConfigurationDefinition
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|connectTimeoutMillis (Long connectTimeoutMillis)
specifier|public
name|ConsulConfigurationDefinition
name|connectTimeoutMillis
parameter_list|(
name|Long
name|connectTimeoutMillis
parameter_list|)
block|{
name|setConnectTimeoutMillis
argument_list|(
name|connectTimeoutMillis
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|readTimeoutMillis (Long readTimeoutMillis)
specifier|public
name|ConsulConfigurationDefinition
name|readTimeoutMillis
parameter_list|(
name|Long
name|readTimeoutMillis
parameter_list|)
block|{
name|setReadTimeoutMillis
argument_list|(
name|readTimeoutMillis
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|writeTimeoutMillis (Long writeTimeoutMillis)
specifier|public
name|ConsulConfigurationDefinition
name|writeTimeoutMillis
parameter_list|(
name|Long
name|writeTimeoutMillis
parameter_list|)
block|{
name|setWriteTimeoutMillis
argument_list|(
name|writeTimeoutMillis
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|blockSeconds (Integer blockSeconds)
specifier|public
name|ConsulConfigurationDefinition
name|blockSeconds
parameter_list|(
name|Integer
name|blockSeconds
parameter_list|)
block|{
name|setBlockSeconds
argument_list|(
name|blockSeconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

